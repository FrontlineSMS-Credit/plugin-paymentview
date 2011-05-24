package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernateAccountDao extends BaseHibernateDao<Account> implements
		AccountDao {

	protected HibernateAccountDao() {
		super(Account.class);
	}

	public void deleteAccount(Account account) {
		super.delete(account);
	}

	public Account getAccountByAccountNumber(String accNumber) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("accountNumber", accNumber));
		return super.getUnique(criteria);
	}

	public Account getAccountById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public List<Account> getAccountsByClientId(long id) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria clientCriteria = criteria.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", id));
		return super.getList(criteria);
	}
	

	public List<Account> getInactiveAccountsByClientId(long clientId) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("activeAccount", false));
		DetachedCriteria clientCriteria = criteria.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", clientId));
		return super.getList(criteria);
	}

	public List<Account> getAllAcounts() {
		return super.getAll();
	}

	public int getAcountsCount() {
		return super.countAll();
	}
	
	public void saveAccount(Account account) {
		super.saveWithoutDuplicateHandling(account);
	}

	public void updateAccount(Account account) {
		super.updateWithoutDuplicateHandling(account);
	}

	public int getAccountCount() {
		return super.getAll().size();
	}
}
