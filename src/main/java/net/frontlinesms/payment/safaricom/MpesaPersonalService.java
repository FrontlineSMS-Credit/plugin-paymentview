package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.payment.PaymentJob;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

public class MpesaPersonalService extends MpesaPaymentService {
	
	//> REGEX PATTERN CONSTANTS
	private static final String STR_PERSONAL_INCOMING_PAYMENT_REGEX_PATTERN = "[A-Z0-9]+ Confirmed.\n" +
			"You have received Ksh[,|.|\\d]+ from\n([A-Za-z ]+) 2547[\\d]{8}\non " +
			"(([1-2]?[1-9]|[1-2]0|3[0-1])/([1-9]|1[0-2])/(1[0-2])) (at) ([1]?\\d:[0-5]\\d) (AM|PM)\n" +
			"New M-PESA balance is Ksh[,|.|\\d]+";
	private static final Pattern PERSONAL_INCOMING_PAYMENT_REGEX_PATTERN = Pattern.compile(STR_PERSONAL_INCOMING_PAYMENT_REGEX_PATTERN);
	
	private static final String STR_MPESA_PAYMENT_FAILURE_PATTERN = "";
	private static final Pattern MPESA_PAYMENT_FAILURE_PATTERN = Pattern.compile(STR_MPESA_PAYMENT_FAILURE_PATTERN);
	
	private static final String STR_PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN = 
		"[A-Z0-9]+ Confirmed. " +
		"Ksh[,|.|\\d]+ sent to ([A-Za-z ]+) \\+2547[\\d]{8} on " +
		"(([1-2]?[1-9]|[1-2]0|3[0-1])/([1-9]|1[0-2])/(1[0-2])) at ([1]?\\d:[0-5]\\d) ([A|P]M) New M-PESA balance is Ksh([,|.|\\d]+)";
	
	private static final Pattern PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN = Pattern.compile(STR_PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN);
	private static final String STR_BALANCE_REGEX_PATTERN = "[A-Z0-9]+ Confirmed.\n"
		+ "Your M-PESA balance was Ksh([,|.|\\d]+)\n"
		+ "on (([1-2]?[1-9]|[1-2]0|3[0-1])/([1-9]|1[0-2])/(1[0-2])) at (([1]?\\d:[0-5]\\d) ([A|P]M))";
	
	private static final Pattern BALANCE_REGEX_PATTERN = Pattern.compile(STR_BALANCE_REGEX_PATTERN);
	
//>BEGIN - OUTGOING PAYMENT REGION	
	protected boolean isValidBalanceMessage(FrontlineMessage message){
		return BALANCE_REGEX_PATTERN.matcher(message.getTextContent()).matches();
	}
	
	@Override
	protected void processMessage(final FrontlineMessage message) {
		if (isValidOutgoingPaymentConfirmation(message)) {
			processOutgoingPayment(message);
		}else if (isFailedMpesaPayment(message)){
			//TODO: Implement me!!
		}else {
			super.processMessage(message);
		}
	}
	
	private boolean isFailedMpesaPayment(final FrontlineMessage message) {
		return MPESA_PAYMENT_FAILURE_PATTERN.matcher(message.getTextContent()).matches();
	}
	
	private void processOutgoingPayment(final FrontlineMessage message) {
		new PaymentJob() {
			public void run() {
				try {				
					// Retrieve the corresponding outgoing payment with status UNCONFIRMED
					List<OutgoingPayment> outgoingPayments = 
						outgoingPaymentDao.getByPhoneNumberAndAmountPaid(getPhoneNumber(message),
							new BigDecimal(getAmount(message).toString()), OutgoingPayment.Status.UNCONFIRMED);

					if (!outgoingPayments.isEmpty()){				
						final OutgoingPayment outgoingPayment = outgoingPayments.get(0);
						outgoingPayment.setConfirmationCode(getConfirmationCode(message));
						outgoingPayment.setTimeConfirmed(getTimePaid(message, true).getTime());
						outgoingPayment.setStatus(OutgoingPayment.Status.CONFIRMED);
						performOutgoingPaymentFraudCheck(message, outgoingPayment);
						
						outgoingPaymentDao.updateOutgoingPayment(outgoingPayment);
						
						logMessageDao.saveLogMessage(
							new LogMessage(LogMessage.LogLevel.INFO,
								   	"Outgoing Confirmation Payment",
								   	message.getTextContent()));
					} else {
						logMessageDao.saveLogMessage(
								new LogMessage(LogMessage.LogLevel.WARNING,
									   	"Outgoing Confirmation Payment: No unconfirmed outgoing payment for the following confirmation message",
									   	message.getTextContent()));
					}
				} catch (IllegalArgumentException ex) {
					logMessageDao.saveLogMessage(
							new LogMessage(LogMessage.LogLevel.ERROR,
								   	"Outgoing Confirmation Payment: Message failed to parse; likely incorrect format",
								   	message.getTextContent()));
					throw new RuntimeException(ex);
				} catch (Exception ex) {
					logMessageDao.saveLogMessage(
							new LogMessage(LogMessage.LogLevel.ERROR,
								   	"Outgoing Confirmation Payment: Unexpected exception parsing outgoing payment SMS",
								   	message.getTextContent()));
					throw new RuntimeException(ex);
				}
			}
		}.execute();
	}
	
	synchronized void performOutgoingPaymentFraudCheck(final FrontlineMessage message,
			final OutgoingPayment outgoingPayment) {
		BigDecimal tempBalanceAmount = balance.getBalanceAmount();
		
		//check is: Let Previous Balance be p, Current Balance be c and Amount sent be a
		//c == p - a
		//It might be wise that
		BigDecimal currentBalance = getBalance(message);
		BigDecimal expectedBalance = tempBalanceAmount.subtract(outgoingPayment.getAmountPaid());
		
		informUserOnFraud(currentBalance, expectedBalance, !expectedBalance.equals(currentBalance), message.getTextContent());
		
		balance.setBalanceAmount(currentBalance);
		balance.setConfirmationCode(outgoingPayment.getConfirmationCode());
		balance.setDateTime(new Date(outgoingPayment.getTimeConfirmed()));
		balance.setBalanceUpdateMethod("Outgoing Payment");
		
		balance.updateBalance();
	}
	
	private boolean isValidOutgoingPaymentConfirmation(FrontlineMessage message) {
		return PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN.matcher(message.getTextContent()).matches();
	}
//>END - OUTGOING PAYMENT REGION
	
	/*
	 * This function returns the active non-generic account, or the generic account if no accounts are active.
	 */
	@Override
	Account getAccount(FrontlineMessage message) {
		Client client = clientDao.getClientByPhoneNumber(getPhoneNumber(message));
		if (client != null) {
			List<Account> activeNonGenericAccountsByClientId = accountDao.getActiveNonGenericAccountsByClientId(client.getId());
			if(!activeNonGenericAccountsByClientId.isEmpty()){
				return activeNonGenericAccountsByClientId.get(0);
			} else {
				return accountDao.getGenericAccountsByClientId(client.getId());
			}
		}
		return null;
	}
	
	@Override
	String getPaymentBy(FrontlineMessage message) {
		try {
	        String nameAndPhone = getFirstMatch(message, "Ksh[,|.|\\d]+ from\n([A-Za-z ]+) 2547[0-9]{8}");
	        String nameWKsh = nameAndPhone.split((AMOUNT_PATTERN + " from\n"))[1];
	        String names = getFirstMatch(nameWKsh,PAID_BY_PATTERN).trim();
	        return names;
		} catch(ArrayIndexOutOfBoundsException ex) {
		        throw new IllegalArgumentException(ex);
		}
	}	
	
	@Override
	Date getTimePaid(FrontlineMessage message) {
		return getTimePaid(message, false);
	}

	Date getTimePaid(FrontlineMessage message, boolean isOutgoingPayment) {
		String section1 = "";
		if (isOutgoingPayment) {
			section1 = message.getTextContent().split(" on ")[1];
		}else{
			section1 = message.getTextContent().split("\non ")[1];
		}
        String datetimesection = section1.split("New M-PESA balance")[0];
        String datetime = datetimesection.replace(" at ", " ");
        
        Date date = null;
        try {
			date = new SimpleDateFormat(DATETIME_PATTERN).parse(datetime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	@Override
	boolean isMessageTextValid(String message) {
		return PERSONAL_INCOMING_PAYMENT_REGEX_PATTERN.matcher(message).matches();
	}

	@Override
	public String toString() {
		return "Mpesa Kenya: Personal Service";
	}
}
