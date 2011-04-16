package org.creditsms.plugins.paymentview.utils;

import java.util.Collection;

import org.creditsms.plugins.paymentview.data.domain.Account;

public class PaymentViewUtils extends net.frontlinesms.FrontlineUtils{

	public static String accountsAsString(Collection<Account> accounts,
			String groupsDelimiter) {
		String str_accounts = ""; 
		for (Account g : accounts) {
			str_accounts += g.getAccountNumber() + groupsDelimiter; 
		}
		if (str_accounts.endsWith(groupsDelimiter)) {
			str_accounts = str_accounts.substring(0, str_accounts.length() - groupsDelimiter.length());
		}
		return str_accounts;
	}
	
}
