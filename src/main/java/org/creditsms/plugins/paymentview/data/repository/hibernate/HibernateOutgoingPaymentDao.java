package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.Order;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernateOutgoingPaymentDao extends
		BaseHibernateDao<OutgoingPayment> implements OutgoingPaymentDao {

	protected HibernateOutgoingPaymentDao() {
		super(OutgoingPayment.class);
	}

	public void deleteOutgoingPayment(OutgoingPayment outgoingPayment) {
		super.delete(outgoingPayment);
	}

	public List<OutgoingPayment> getAllOutgoingPayments() {
		return super.getAll();
	}
	
	public List<OutgoingPayment> getAllOutgoingPayments(int startIndex,
			int limit) {
		return super.getAll(startIndex, limit);
	}
	
	public int getOutgoingPaymentsCount() {
		return super.countAll();
	}

	public List<OutgoingPayment> getOutgoingPaymentsByClientId(long clientId) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		DetachedCriteria clientCriteria = accountCriteria
				.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", clientId));
		return super.getList(criteria);
	}

	public OutgoingPayment getOutgoingPaymentById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public List<OutgoingPayment> getOutgoingPaymentsByAccountNumber(
			String accNumber) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add(Restrictions.eq("accountNumber", accNumber));
		return super.getList(criteria);
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

	public List<OutgoingPayment> getOutgoingPaymentsByClientIdByDateRange(
			long clientId, Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByDateRange(
			Calendar startDate, Calendar endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<OutgoingPayment> getOutgoingPaymentsByPhoneNo(String phoneNo) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("phoneNumber", phoneNo));
		return super.getList(criteria);
	}

	public List<OutgoingPayment> getOutgoingPaymentsByTimeRange(Date startTime,
			Date endtime) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<OutgoingPayment> getOutgoingPaymentsByPhoneNumberAndAmountPaid(String phoneNo, BigDecimal amountPaid, OutgoingPayment.Status status){
		DetachedCriteria criteria = super.getSortCriterion(OutgoingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("phoneNumber", phoneNo));
		criteria.add(Restrictions.eq("amountPaid", amountPaid));
		criteria.add(Restrictions.eq("status", status));
		
		return super.getList(criteria);
	}


	public void saveOutgoingPayment(OutgoingPayment outgoingPayment){
		super.saveWithoutDuplicateHandling(outgoingPayment);
	}

	public void updateOutgoingPayment(OutgoingPayment outgoingPayment)
			throws DuplicateKeyException {
		super.update(outgoingPayment);

	}	
}
