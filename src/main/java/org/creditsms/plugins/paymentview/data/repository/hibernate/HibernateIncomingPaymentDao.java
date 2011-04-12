package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;

public class HibernateIncomingPaymentDao extends BaseHibernateDao<IncomingPayment> implements IncomingPaymentDao {

	protected HibernateIncomingPaymentDao(){
		super(IncomingPayment.class);
	}
	
	public IncomingPayment getIncomingPaymentById(long incomingPaymentId) {
		return null;
	}

	public List<IncomingPayment> getAllIncomingPayments() {
		return super.getAll();
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

	public List<IncomingPayment> getIncomingPaymentsByPhoneNo(String phoneNo) {
		// TODO Auto-generated method stub
		return null;
	}

	public void deleteIncomingPayment(IncomingPayment incomingPayment) {
		super.delete(incomingPayment);
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumber(
			long accountNumber) {// FIXME
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumberByDateRange(
			long accountId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getAllIncomingPayments(int startingIndex,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumberByTimeRange(
			long accountId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveIncomingPayment(IncomingPayment incomingPayment)
			throws DuplicateKeyException {
		super.save(incomingPayment);
	}

	public void updateIncomingPayment(IncomingPayment incomingPayment)
			throws DuplicateKeyException {
		// TODO Auto-generated method stub
		
	}
}
