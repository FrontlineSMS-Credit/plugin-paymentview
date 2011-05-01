package org.creditsms.plugins.paymentview.utils;

import java.util.Collection;
import java.util.Iterator;

import org.creditsms.plugins.paymentview.data.domain.Account;

public class PaymentViewUtils extends net.frontlinesms.FrontlineUtils {

	public static String accountsAsString(Collection<Account> accounts,
			String groupsDelimiter) {
		String str_accounts = "";
		for (Iterator<Account> ai = accounts.iterator(); ai.hasNext();) {
			str_accounts += ai.next().getAccountNumber();
			if (ai.hasNext()) {
				str_accounts += groupsDelimiter;
			}
		}
		return str_accounts;
	}

}
