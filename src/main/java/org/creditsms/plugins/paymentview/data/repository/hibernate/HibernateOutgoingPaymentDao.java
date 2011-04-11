package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class HibernateOutgoingPaymentDao extends BaseHibernateDao<OutgoingPayment> implements OutgoingPaymentDao {

	protected HibernateOutgoingPaymentDao(){
		super(OutgoingPayment.class);
	}

	public OutgoingPayment getOutgoingPaymentById(long outgoingPaymentId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(OutgoingPayment.class)
		.add(Restrictions.eq("id", outgoingPaymentId));
			
		List<OutgoingPayment> opLst = criteria.list();

		if (opLst.size() == 0) {
			return null;
		}
		return opLst.get(0);
	}

	public List<OutgoingPayment> getAllOutgoingPayments() {
		return this.getHibernateTemplate().loadAll(OutgoingPayment.class);
	}

	public List<OutgoingPayment> getOutgoingPaymentsByDateRange(
			Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByTimeRange(Date startTime,
			Date endtime) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByClientIdByDateRange(
			long clientId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentByClientId(long clientId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(OutgoingPayment.class);
			
		Criteria accountCriteria = criteria.createCriteria("account");
		Criteria clientCriteria = accountCriteria.createCriteria("client");
		clientCriteria.add( Restrictions.eq("id", clientId ));
				
		List<OutgoingPayment> opLst= criteria.list();

		if (opLst.size() == 0) {
			return null;
		}
		return opLst ;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByAccountNumber(
			long accNumber) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(OutgoingPayment.class);
			
		Criteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add( Restrictions.eq("accountNumber", accNumber ));
				
		List<OutgoingPayment> opLst= criteria.list();

		if (opLst.size() == 0) {
			return null;
		}
		return opLst ;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByAccountNumberByDateRange(
			long accountId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByAccountNumberByTimeRange(
			long accountId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByPhoneNo(String phoneNo) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(OutgoingPayment.class)
		.add(Restrictions.eq("phoneNumber", phoneNo));
			
		List<OutgoingPayment> opLst = criteria.list();

		if (opLst.size() == 0) {
			return null;
		}
		return opLst;
	}

	public void saveOrUpdateOutgoingPayment(OutgoingPayment outgoingPayment) {
		this.getHibernateTemplate().saveOrUpdate(outgoingPayment);
	}

	public void deleteOutgoingPayment(OutgoingPayment outgoingPayment) {
		this.getHibernateTemplate().delete(outgoingPayment);
	}
}
