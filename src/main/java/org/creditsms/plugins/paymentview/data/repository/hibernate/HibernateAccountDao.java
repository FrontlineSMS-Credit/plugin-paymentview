package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class HibernateAccountDao extends BaseHibernateDao<Account>  implements AccountDao{

	protected HibernateAccountDao() {
		super(Account.class);
	}
	
	public List<Account> getAllAcounts() {
		return this.getHibernateTemplate().loadAll(Account.class);
	}

	public List<Account> getAccountsByClientId(long clientId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Account.class);
			
		Criteria clientCriteria = criteria.createCriteria("client");
		clientCriteria.add( Restrictions.eq("id", clientId ));
				
		List<Account> accountLst= criteria.list();

		if (accountLst.size() == 0) {
			return null;
		}
		return accountLst ;
	}

	public Account getAccountById(long id) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Account.class)
		.add(Restrictions.eq("id", id));
			
		List<Account> accLst = criteria.list();

		if (accLst.size() == 0) {
			return null;
		}
		return accLst.get(0);
	}

	public void deleteAccount(Account account) {
		this.getHibernateTemplate().delete(account);
	}

	public void saveUpdateAccount(Account account) {
		this.getHibernateTemplate().saveOrUpdate(account);
	}

}
