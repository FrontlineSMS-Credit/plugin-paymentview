package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.dao.support.DataAccessUtils;

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
		DetachedCriteria accountCriteria = criteria.createCriteria(IncomingPayment.Field.ACCOUNT.getFieldName());
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
			String accountNumber, long startDate, long endDate) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.between("timePaid", startDate, endDate));
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add(Restrictions.eq("accountNumber", accountNumber));
		return super.getList(criteria);
	}

	public Long getLastIncomingPaymentDateByAccountNumber(
			String accountNumber) {
		DetachedCriteria criteria = super.getCriterion();
        ProjectionList ipProj = Projections.projectionList();
        ipProj.add(Projections.max("timePaid"));
        criteria.setProjection(ipProj);
        return DataAccessUtils.longResult(this.getHibernateTemplate().findByCriteria(criteria));
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

	public void saveIncomingPayment(IncomingPayment incomingPayment) {
		super.saveWithoutDuplicateHandling(incomingPayment);
	}

	public void updateIncomingPayment(IncomingPayment incomingPayment)  {
		super.updateWithoutDuplicateHandling(incomingPayment);
	}

	public int getIncomingPaymentsCount() {
		return super.countAll();
	}

	public List<IncomingPayment> getIncomingPaymentsByAccountNumber(
			long accountId) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<IncomingPayment> getIncomingPaymentsByDateRange(Date startTime,
			Date endtime) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<IncomingPayment> getIncomingPaymentsByDate(Date date) {
		// TODO Auto-generated method stub
		return null;
	}
}
