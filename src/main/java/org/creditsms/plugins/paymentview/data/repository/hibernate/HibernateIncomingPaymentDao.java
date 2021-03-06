package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.Order;
import net.frontlinesms.data.domain.PersistableSettings;
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
	
	public List<IncomingPayment> getActiveIncomingPayments() {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		return super.getList(criteria);
	}

	public List<IncomingPayment> getActiveIncomingPayments(int startingIndex, int limit) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", Boolean.TRUE));
		List<IncomingPayment> payments = getList(criteria);
		return payments.subList(startingIndex, Math.min(startingIndex+limit, payments.size()));
	}

	public List<IncomingPayment> getActiveIncomingPaymentByClientId(long clientId) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		DetachedCriteria accountCriteria = criteria.createCriteria(IncomingPayment.Field.ACCOUNT.getFieldName());
		DetachedCriteria clientCriteria = accountCriteria.createCriteria("client");
		clientCriteria.add(Restrictions.eq("id", clientId));
		return super.getList(criteria);
	}

	public IncomingPayment getActiveIncomingPaymentById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountIdByTimeRange(
			long accountId, Date startDate, Date endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getActiveIncomingPaymentsByTarget(long targetId) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		DetachedCriteria accountCriteria = criteria.createCriteria("target");
		criteria.add(Restrictions.eq("active", true));
		accountCriteria.add(Restrictions.eq("id", targetId));
		return super.getList(criteria);
	}
	
	public List<IncomingPayment> getActiveIncomingPaymentsByTargetAndDates(long targetId, Date startDate,
			Date endDate) {
		
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		DetachedCriteria accountCriteria = criteria.createCriteria("target");
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.between("timePaid", startDate.getTime(), endDate.getTime()));
		accountCriteria.add(Restrictions.eq("id", targetId));
		return super.getList(criteria);
	}
	
	public List<IncomingPayment> getIncomingPaymentsByTargetAndDates(
			long targetId, Date startDate, Date endDate) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		DetachedCriteria accountCriteria = criteria.createCriteria("target");
		criteria.add(Restrictions.between("timePaid", startDate.getTime(), endDate.getTime()));
		accountCriteria.add(Restrictions.eq("id", targetId));
		return super.getList(criteria);
	}
	
	public List<IncomingPayment> getActiveIncomingPaymentsByAccountNumber(String accountNumber) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("active", true));
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add(Restrictions.eq("accountNumber", accountNumber));
		
		return super.getList(criteria);
	}

	public List<IncomingPayment> getActiveIncomingPaymentsByAccountNumberOrderByTimepaid(
			String accountNumber) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add(Restrictions.eq("accountNumber", accountNumber));
		return super.getList(criteria);    
	}
	
	public List<IncomingPayment> getActiveIncomingPaymentsByAccountNumberBtwnDateOrderByTimepaid(
			String accountNumber, Date startDate, Date endDate) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.between("timePaid", startDate.getTime(), endDate.getTime()));
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

	public List<IncomingPayment> getActiveIncomingPaymentsByPayer(String payer) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.eq("paymentBy", payer));
		return super.getList(criteria);
	}

	public List<IncomingPayment> getActiveIncomingPaymentsByPhoneNo(String phoneNo) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.eq("phoneNumber", phoneNo));
		return super.getList(criteria);
	}

	public void saveIncomingPayment(IncomingPayment incomingPayment) {
		super.saveWithoutDuplicateHandling(incomingPayment);
	}

	public void updateIncomingPayment(IncomingPayment incomingPayment)  {
		super.updateWithoutDuplicateHandling(incomingPayment);
	}
	
	public int getActiveIncomingPaymentsCount() {
		return getActiveIncomingPayments().size();
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumber(
			long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByDateRange(Date startDate,
			Date endDate, int startingIndex, int limit) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.between("timePaid", startDate.getTime(), endDate.getTime()));
		List<IncomingPayment> payments = getList(criteria);
		return payments.subList(startingIndex, Math.min(startingIndex+limit, payments.size()));
	}
	
	public List<IncomingPayment> getIncomingPaymentsByDateRange(Date startDate,
			Date endDate) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.between("timePaid", startDate.getTime(), endDate.getTime()));
		return super.getList(criteria);
	}
	
	public List<IncomingPayment> getIncomingPaymentsByStartDate(Date startDate, int startingIndex, int limit) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.ge("timePaid", startDate.getTime()));
		List<IncomingPayment> payments = getList(criteria);
		return payments.subList(startingIndex, Math.min(startingIndex+limit, payments.size()));
	}
	
	public List<IncomingPayment> getIncomingPaymentsByStartDate(Date startDate) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.ge("timePaid", startDate.getTime()));
		return super.getList(criteria);
	}
	
	public List<IncomingPayment> getIncomingPaymentsByEndDate(Date endDate, int startingIndex, int limit) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.le("timePaid", endDate.getTime()));
		List<IncomingPayment> payments = getList(criteria);
		return payments.subList(startingIndex, Math.min(startingIndex+limit, payments.size()));
	}
	
	public List<IncomingPayment> getIncomingPaymentsByEndDate(Date endDate) {
		DetachedCriteria criteria = super.getSortCriterion(IncomingPayment.Field.TIME_PAID, Order.DESCENDING);
		criteria.add(Restrictions.eq("active", true));
		criteria.add(Restrictions.le("timePaid", endDate.getTime()));
		return super.getList(criteria);
	}
	
	public List<IncomingPayment> getIncomingPaymentsByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}

	public IncomingPayment getByConfirmationCode(String confirmationCode) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions
			.eq(IncomingPayment.Field.CONFIRMATION_CODE.getFieldName(), confirmationCode));
		return super.getUnique(criteria);
	}
	
	public List<IncomingPayment> getByPaymentServiceSettings(PersistableSettings paymentServiceSettings){
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria paymentServiceSettingsCriteria = criteria.createCriteria("serviceSettings");
		paymentServiceSettingsCriteria.add(Restrictions.eq("id", paymentServiceSettings.getId()));
		return super.getList(criteria);
	}
}
