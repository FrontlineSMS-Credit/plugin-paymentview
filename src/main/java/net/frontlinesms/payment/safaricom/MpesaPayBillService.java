package net.frontlinesms.payment.safaricom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.frontlinesms.data.domain.FrontlineMessage;

import org.creditsms.plugins.paymentview.data.domain.Account;

public class MpesaPayBillService extends MpesaPaymentService {
	@Override
	Account getAccount(FrontlineMessage message) {
		String accNumber = getFirstMatch(message, "Account Number [0-9]+");
		return accountDao.getAccountByAccountNumber(accNumber
				.substring("Account Number ".length()));
	}

	@Override
	Date getTimePaid(FrontlineMessage message) {
		String longtext = message.getTextContent().replace("\\s", " ");
		String section1 = longtext.split("on ")[1];
        String datetimesection = section1.split("Ksh[0-9,]+ received from")[0];
        String datetime = datetimesection.replace(" at ", " ");
        
		Date date = null;
		try {
			date = new SimpleDateFormat("d/M/yy hh:mm a").parse(datetime);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;

	}
}
