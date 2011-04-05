package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDAO;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

@SuppressWarnings("unchecked")
public class IncomingPaymentDAOImpl extends HibernateDaoSupport implements IncomingPaymentDAO {

	public IncomingPayment getIncomingPaymentById(long incomingPaymentId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getAllIncomingPayments() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountId(long accountId) {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByPhoneNo(long phoneNo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveOrUpdateIncomingPayment(IncomingPayment incomingPayment) {
		// TODO Auto-generated method stub
		
	}

	public void deleteIncomingPayment(IncomingPayment incomingPayment) {
		// TODO Auto-generated method stub
		
	}

}
