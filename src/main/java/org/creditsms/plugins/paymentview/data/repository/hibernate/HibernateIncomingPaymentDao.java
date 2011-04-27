package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernateIncomingPaymentDao extends
		BaseHibernateDao<IncomingPayment> implements IncomingPaymentDao {

	protected HibernateIncomingPaymentDao() {
		super(IncomingPayment.class);
	}

	public void deleteIncomingPayment(IncomingPayment incomingPayment) {
		super.delete(incomingPayment);
	}

	public List<IncomingPayment> getAllIncomingPayments() {
		return super.getAll();
	}

	public List<IncomingPayment> getAllIncomingPayments(int startingIndex,
			int limit) {
		return super.getAll(startingIndex, limit);
	}

	public List<IncomingPayment> getIncomingPaymentByClientId(long clientId) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		DetachedCriteria clientCriteria = accountCriteria
				.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", clientId));
		return super.getList(criteria);
	}

	public IncomingPayment getIncomingPaymentById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountIdByTimeRange(
			long accountId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumber(
			String accountNumber) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add(Restrictions.eq("accountNumber", accountNumber));
		return super.getList(criteria);
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumberByTimeRange(
			long accountId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByClientIdByDateRange(
			long clientId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByPayer(String payer) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("paymentBy", payer));
		return super.getList(criteria);
	}

	public List<IncomingPayment> getIncomingPaymentsByPhoneNo(String phoneNo) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("phoneNumber", phoneNo));
		return super.getList(criteria);
	}

	public List<IncomingPayment> getIncomingPaymentsByTimeRange(Date startTime,
			Date endtime) {
		// TODO Auto-generated method stub
		return null;
	}

	public void saveIncomingPayment(IncomingPayment incomingPayment)
			throws DuplicateKeyException {
		super.save(incomingPayment);
	}

	public void updateIncomingPayment(IncomingPayment incomingPayment)
			throws DuplicateKeyException {
		super.update(incomingPayment);
	}

	public int getIncomingPaymentsCount() {
		return super.countAll();
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumber(
			long accountId) {
		// TODO Auto-generated method stub
		return null;
	}
}
