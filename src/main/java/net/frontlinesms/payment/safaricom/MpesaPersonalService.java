package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
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
		"[A-Z\\d]+ Confirmed. " +
		"Ksh[,|.|\\d]+ sent to ([A-Za-z ]+) \\+2547[\\d]{8} on " +
		"(([1-2]?[1-9]|[1-2]0|3[0-1])/([1-9]|1[0-2])/(1[0-2])) at ([1]?\\d:[0-5]\\d) ([A|P]M) New M-PESA balance is Ksh([,|.|\\d]+)";
	
	private static final Pattern PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN = Pattern.compile(STR_PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN);
	private static final String STR_BALANCE_REGEX_PATTERN = "[A-Z0-9]+ Confirmed.\n"
		+ "Your M-PESA balance was Ksh[,|.|\\d]+\n"
		+ "on (([1-2]?[1-9]|[1-2]0|3[0-1])/([1-9]|1[0-2])/(1[0-2])) at ([1]?\\d:[0-5]\\d) ([A|P]M)";
	
	private static final Pattern BALANCE_REGEX_PATTERN = Pattern.compile(STR_BALANCE_REGEX_PATTERN);
	
//>BEGIN - OUTGOING PAYMENT REGION	
	protected boolean isValidBalanceMessage(FrontlineMessage message){
		return BALANCE_REGEX_PATTERN.matcher(message.getTextContent()).matches();
	}
	
	@Override
	protected void processMessage(final FrontlineMessage message) {
		super.processMessage(message);
		
		if (isValidOutgoingPaymentConfirmation(message)) {
			processOutgoingPayment(message);
		}else if (isFailedMpesaPayment(message)){
			
		}
	}
	
	private boolean isFailedMpesaPayment(final FrontlineMessage message) {
		return MPESA_PAYMENT_FAILURE_PATTERN.matcher(message.getTextContent()).matches();
	}

	private void processOutgoingPayment(final FrontlineMessage message) {
		new FrontlineUiUpateJob() {
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
						
						//Update outgoing payment
						outgoingPaymentDao.updateOutgoingPayment(outgoingPayment);
					} else {
						pvLog.warn("No unconfirmed outgoing payment for the following confirmation message: " + message.getTextContent());
					}
				} catch (IllegalArgumentException ex) {
					log.warn("Message failed to parse; likely incorrect format", ex);
					throw new RuntimeException(ex);
				} catch (Exception ex) {
					log.error("Unexpected exception parsing outgoing payment SMS.", ex);
					throw new RuntimeException(ex);
				}
			}
		}.execute();
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
