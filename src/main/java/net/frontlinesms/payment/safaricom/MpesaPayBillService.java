package net.frontlinesms.payment.safaricom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.frontlinesms.data.domain.FrontlineMessage;

import org.creditsms.plugins.paymentview.data.domain.Account;

public class MpesaPayBillService extends MpesaPaymentService {
	private static final String PAYBILL_REGEX_PATTERN = "[A-Z0-9]+ Confirmed.\n"
			+ "on (([1-2]?[1-9]|[1-2]0|3[0-1])/([1-9]|1[0-2])/(1[1-3])) at ([1]?\\d:[0-5]\\d) (AM|PM)\n"
			+ "Ksh[,|\\d]+ received from ([A-Za-z ]+) 2547[\\d]{8}.\n"
			+ "Account Number (\\d+)\n"
			+ "New Utility balance is Ksh[,|\\d]+\n"
			+ "Time: ([0-2]\\d|[3][0-1])/(0[1-9]|1[0-2])/(20[1][1-2]) (([2][0-3]|[0-1]\\d):([0-5]\\d):([0-5]\\d))";

	
	@Override
	Account getAccount(FrontlineMessage message) {
		String accNumber = getFirstMatch(message, ACCOUNT_NUMBER_PATTERN);
		return accountDao.getAccountByAccountNumber(accNumber
				.substring("Account Number ".length()));
	}

	@Override
	String getPaymentBy(FrontlineMessage message) {
		try {
			String nameAndPhone = getFirstMatch(message, RECEIVED_FROM +" "+PAID_BY_PATTERN + " " + PHONE_PATTERN);
			String nameWKsh = nameAndPhone.replace(RECEIVED_FROM, "");
			String names = getFirstMatch(nameWKsh, PAID_BY_PATTERN).trim();
			return names;
		} catch (ArrayIndexOutOfBoundsException ex) {
			throw new IllegalArgumentException(ex);
		}
	}

	@Override
	Date getTimePaid(FrontlineMessage message) {
		String longtext = message.getTextContent().replace("\\s", " ");
		String section1 = longtext.split("on ")[1];
		String datetimesection = section1.split(AMOUNT_PATTERN+ " " + RECEIVED_FROM)[0];
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
		if(!messageText.matches(PAYBILL_REGEX_PATTERN)) {
			return false;
		} else {
			// TODO Check if the date is valid.
			return true;
		}
	}

	@Override
	public String toString() {
		return "Mpesa Kenya: Paybill Service";
	}

}
