package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class HibernateOutgoingPaymentDao extends HibernateDaoSupport implements OutgoingPaymentDao {

	public OutgoingPayment getOutgoingPaymentById(long outgoingPaymentId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getAllOutgoingPayments() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByAccountId(long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByAccountIdByDateRange(
			long accountId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByAccountIdByTimeRange(
			long accountId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByPayer(String payer) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByPhoneNo(long phoneNo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveOrUpdateOutgoingPayment(OutgoingPayment outgoingPayment) {
		// TODO Auto-generated method stub
		
	}

	public void deleteOutgoingPayment(OutgoingPayment outgoingPayment) {
		// TODO Auto-generated method stub
		
	}

}
