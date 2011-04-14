package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernateOutgoingPaymentDao extends BaseHibernateDao<OutgoingPayment> implements OutgoingPaymentDao {

	protected HibernateOutgoingPaymentDao(){
		super(OutgoingPayment.class);
	}

	public OutgoingPayment getOutgoingPaymentById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public List<OutgoingPayment> getAllOutgoingPayments() {
		return super.getAll();
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

	public List<OutgoingPayment> getOutgoingPaymentsByPhoneNo(String phoneNo) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("phoneNumber", phoneNo));
		return super.getList(criteria);
	}
	
	public List<OutgoingPayment> getOutgoingPaymentByClientId(long clientId) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		DetachedCriteria clientCriteria = accountCriteria.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", clientId));
		return super.getList(criteria);
	}

	public List<OutgoingPayment> getOutgoingPaymentsByAccountNumber(
			long accNumber) {
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

	public void saveOutgoingPayment(OutgoingPayment outgoingPayment)throws DuplicateKeyException {
		super.save(outgoingPayment);
	}
	
	public void deleteOutgoingPayment(OutgoingPayment outgoingPayment) {
		super.delete(outgoingPayment);
	}

	public void updateOutgoingPayment(OutgoingPayment outgoingPayment)
			throws DuplicateKeyException {
		super.update(outgoingPayment);
		
	}
}