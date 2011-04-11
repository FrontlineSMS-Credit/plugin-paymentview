package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class HibernateIncomingPaymentDao extends BaseHibernateDao<IncomingPayment> implements IncomingPaymentDao {

	protected HibernateIncomingPaymentDao(){
		super(IncomingPayment.class);
	}
	
	public IncomingPayment getIncomingPaymentById(long incomingPaymentId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(IncomingPayment.class)
		.add(Restrictions.eq("id", incomingPaymentId));
			
		List<IncomingPayment> ipLst = criteria.list();

		if (ipLst.size() == 0) {
			return null;
		}
		return ipLst.get(0);
	}

	public List<IncomingPayment> getAllIncomingPayments() {
		return this.getHibernateTemplate().loadAll(IncomingPayment.class);
	}

	public List<IncomingPayment> getIncomingPaymentsByDateRange(
			Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByTimeRange(Date startTime,
			Date endtime) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByClientIdByDateRange(
			long clientId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentByClientId(long clientId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(IncomingPayment.class);
			
		Criteria accountCriteria = criteria.createCriteria("account");
		Criteria clientCriteria = accountCriteria.createCriteria("client");
		clientCriteria.add( Restrictions.eq("id", clientId ));
				
		List<IncomingPayment> ipLst= criteria.list();

		if (ipLst.size() == 0) {
			return null;
		}
		return ipLst ;
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountIdByDateRange(
			long accountId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountIdByTimeRange(
			long accountId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByPayer(String payer) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(IncomingPayment.class)
		.add(Restrictions.ilike("paymentBy", payer.trim(), MatchMode.ANYWHERE));
			
		List<IncomingPayment> ipList = criteria.list();

		if (ipList.size() == 0) {
			return null;
		}
		return ipList;
	}

	public List<IncomingPayment> getIncomingPaymentsByPhoneNo(String phoneNo) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(IncomingPayment.class)
		.add(Restrictions.eq("phoneNumber", phoneNo));
			
		List<IncomingPayment> ipLst = criteria.list();

		if (ipLst.size() == 0) {
			return null;
		}
		return ipLst;
	}

	public void saveOrUpdateIncomingPayment(IncomingPayment incomingPayment) {
		this.getHibernateTemplate().saveOrUpdate(incomingPayment);
	}

	public void deleteIncomingPayment(IncomingPayment incomingPayment) {
		this.getHibernateTemplate().delete(incomingPayment);
		
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumber(
			long accountNumber) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(IncomingPayment.class);
			
		Criteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add( Restrictions.eq("accountNumber", accountNumber ));
				
		List<IncomingPayment> ipLst= criteria.list();

		if (ipLst.size() == 0) {
			return null;
		}
		return ipLst ;
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumberByDateRange(
			long accountId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumberByTimeRange(
			long accountId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

}
