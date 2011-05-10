package net.frontlinesms.payment.safaricom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMessage;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;

public class MpesaPersonalService extends MpesaPaymentService {
	private static final String PERSONAL_REGEX_PATTERN = "[A-Z0-9]+ Confirmed.\n" +
					"You have received Ksh[,|[0-9]]+ ([A-Za-z ]+) 2547[0-9]{8} on " +
					"(([1-2]?[1-9]|3[0-1])/([1-9]|1[0-2])/(1[1-2]))\\s(at)\\s([1]?\\d:[0-5]\\d)\\s(AM|PM)" +
					"\nNew M-PESA balance Ksh[,|[0-9]]+";

	@Override
	Account getAccount(FrontlineMessage message) {
		Client client = clientDao.getClientByPhoneNumber(getPhoneNumber(message));
		List<Account> accountsByClientId = new ArrayList<Account>(client.getAccounts());
		if(!accountsByClientId.isEmpty()){
			return accountsByClientId.get(0);
		}
		throw new RuntimeException("Account Not Found");
		
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
	boolean isMessageTextValid(String messageText) {
		return messageText.matches(PERSONAL_REGEX_PATTERN);
	}
	
	public String toString(){
		return "MPesa Safaricom - Kenya: Personal Payment Service";
	}
}
