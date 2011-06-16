package net.frontlinesms.payment.safaricom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

public class MpesaPersonalService extends MpesaPaymentService {
	
//> REGEX PATTERN CONSTANTS
	private static final String PERSONAL_INCOMING_PAYMENT_REGEX_PATTERN = "[A-Z0-9]+ Confirmed.\n" +
			"You have received Ksh[,|\\d]+ ([A-Za-z ]+) 2547[\\d]{8} on " +
			"(([1-2]?[1-9]|[1-2]0|3[0-1])/([1-9]|1[0-2])/(1[0-2])) (at) ([1]?\\d:[0-5]\\d) (AM|PM)\n" +
			"New M-PESA balance Ksh[,|\\d]+";
	protected static final String PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN = 
		"[A-Z\\d]+ Confirmed.\n"+
		"Ksh[,|\\d]+ sent to ([A-Za-z ]+) 2547[\\d]{8} on "+
		"(([1-2]?[1-9]|[1-2]0|3[0-1])/([1-9]|1[0-2])/(1[0-2])) at ([1]?\\d:[0-5]\\d) ([A|P]M)\n"+
		"New M-PESA balance Ksh([,|\\d]+)";
	
//>BEGIN - OUTGOING PAYMENT REGION	
	@Override
	public void processMessage(final FrontlineMessage message) {
		super.processMessage(message);
		
		if (isValidOutgoingPaymentConfirmation(message)) {
			processOutgoingPayment(message);
		}
	}
	
	private void processOutgoingPayment(final FrontlineMessage message) {
		//The Payment should be fetched from the DB;
		//So as to be updated as confirmed
		new FrontlineUiUpateJob() {
			public void run() {
				try {
					final OutgoingPayment payment = new OutgoingPayment();
					payment.setAccount(getAccount(message));
					payment.setPhoneNumber(getPhoneNumber(message));
					payment.setAmountPaid(getAmount(message));
					payment.setConfirmationCode(getConfirmationCode(message));
//					payment.setPaymentTo(getPaymentTo(message));
					payment.setTimePaid(getTimePaid(message));
					payment.setStatus(OutgoingPayment.Status.CONFIRMED);
						
					//TODO -> should retrieve the saved unconfirmed outgoingpayment
					outgoingPaymentDao.saveOutgoingPayment(payment);
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
	
	private String getPaymentTo(FrontlineMessage message) {
		try {
	        String nameAndPhone = getFirstMatch(message, "Ksh[,|[0-9]]+ sent to ([A-Za-z ]+) 2547[0-9]{8}");
	        String nameWKshSentTo = nameAndPhone.split(AMOUNT_PATTERN+SENT_TO)[1];
	        String names = getFirstMatch(nameWKshSentTo,PAID_BY_PATTERN).trim();
	        return names;
		} catch(ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentException(ex);
		}
	}
	
	private boolean isValidOutgoingPaymentConfirmation(FrontlineMessage message) {
		return message.getTextContent().matches(PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN);
	}
//>END - OUTGOING PAYMENT REGION
	
	@Override
	Account getAccount(FrontlineMessage message) {
		Client client = clientDao.getClientByPhoneNumber(getPhoneNumber(message));
		if (client != null) {
			List<Account> accountsByClientId = accountDao.getAccountsByClientId(client.getId());
			if(!accountsByClientId.isEmpty()){
				return accountsByClientId.get(0);
			}
		}
		return null;
	}
	
	@Override
	String getPaymentBy(FrontlineMessage message) {
		try {
	        String nameAndPhone = getFirstMatch(message, "Ksh[,|[0-9]]+ ([A-Za-z ]+) 2547[0-9]{8}");
	        String nameWKsh = nameAndPhone.split(AMOUNT_PATTERN)[1];
	        String names = getFirstMatch(nameWKsh,PAID_BY_PATTERN).trim();
	        return names;
		} catch(ArrayIndexOutOfBoundsException ex) {
		        throw new IllegalArgumentException(ex);
		}
	}	

	@Override
	Date getTimePaid(FrontlineMessage message) {
        String section1 = message.getTextContent().split(" on ")[1];
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
		return message.matches(PERSONAL_INCOMING_PAYMENT_REGEX_PATTERN);
	}

	@Override
	public String toString() {
		return "Mpesa Kenya: Personal Service";
	}
}
