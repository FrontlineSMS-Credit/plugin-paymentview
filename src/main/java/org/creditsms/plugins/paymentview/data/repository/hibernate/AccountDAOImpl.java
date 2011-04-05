package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.repository.AccountDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class AccountDAOImpl extends HibernateDaoSupport implements AccountDAO{

	public List<Account> getAllAcounts() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Account> getAccountsByClientId(long clientId) {
		// TODO Auto-generated method stub
		return null;
	}

	public Account getAccountByAccountId(long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteAccount(Account account) {
		// TODO Auto-generated method stub
		
	}

	public void saveUpdateAccount(Account account) {
		// TODO Auto-generated method stub
		
	}

}
