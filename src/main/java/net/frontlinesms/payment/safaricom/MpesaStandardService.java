package net.frontlinesms.payment.safaricom;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMessage;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;

public class MpesaStandardService extends MpesaPaymentService {
	@Override
	Account getAccount(FrontlineMessage message) {
		Client client = clientDao.getClientByPhoneNumber(getPhoneNumber(message));

		List<Account> accountsByClientId = accountDao.getAccountsByClientId(client.getId());
		if(!accountsByClientId.isEmpty()){
			return accountsByClientId.get(0);
		}
		return null;
	}

	@Override
	Date getTimePaid(FrontlineMessage message) {
        /*  "BH45UU225 Confirmed.\n"
			+ "on 5/4/11 at 2:45 PM\n"
			+ "Ksh950 received from BORIS BECKER 254723908002.\n"
			+ "Account Number 0700000021\n"
			+ "New Utility balance is Ksh50,802\n"
			+ "Time: 05/04/2011 14:45:34"
         */
        String section1 = message.getTextContent().split(" on ")[1];
        String datetimesection = section1.split("New M-PESA balance")[0];
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
