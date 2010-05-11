package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.PaymentService;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernatePaymentServiceDao extends BaseHibernateDao<PaymentService> implements PaymentServiceDao {

	/** Create a new instance for this DAO*/
	public HibernatePaymentServiceDao() {
		super(PaymentService.class);
	}

	/** @see PaymentServiceDao#deletePaymentService(PaymentService) */
	public void deletePaymentService(PaymentService service, boolean destroyTransactions) {
	    
	    if(destroyTransactions){
    	    // Delete transactions associated with this payment service
    	    String query = "DELETE FROM PaymentServiceTransaction WHERE paymentService = ?";
    	    
    	    super.getHibernateTemplate().bulkUpdate(query, service);
	    }
	    
		super.delete(service);
	}

	/** @see PaymentServiceDao#getAllPaymentServices() */
	public List<PaymentService> getAllPaymentServices() {
		return super.getAll();
	}

	/** @see PaymentServiceDao#getAllPaymentServices(int, int) */
	public List<PaymentService> getAllPaymentServices(int startIndex, int limit) {
		return super.getAll(startIndex, limit);
	}

	public PaymentService getPaymentServiceByName(String name) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(PaymentService.Field.NAME.getFieldName(), name));
		return super.getUnique(criteria);
	}
	
	/** @see PaymentServiceDao#getPaymentServiceByShortCode(String) */
	public PaymentService getPaymentServiceByShortCode(String shortCode){
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(PaymentService.Field.SHORT_CODE.getFieldName(), shortCode));
		return super.getUnique(criteria);
	}

	/** @see PaymentServiceDao#getPaymentServicesCount() */
	public int getPaymentServicesCount() {
		return super.countAll();
	}

	/** @see PaymentServiceDao#savePaymentService(PaymentService)  */
	public void savePaymentService(PaymentService service) throws DuplicateKeyException {
		super.save(service);
	}

	/** @see PaymentServiceDao#updatePaymentService(PaymentService) */
	public void updatePaymentService(PaymentService service) throws DuplicateKeyException {
		super.update(service);

	}

}
