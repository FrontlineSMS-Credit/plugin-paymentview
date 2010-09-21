package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.PaymentViewError;
import org.creditsms.plugins.paymentview.data.domain.PaymentViewError.ErrorType;
import org.creditsms.plugins.paymentview.data.domain.PaymentViewError.Field;
import org.creditsms.plugins.paymentview.data.repository.PaymentViewErrorDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * Hibernate implementation for {@link PaymentViewErrorDao}
 * @author Emmanuel Kala
 *
 */
public class HibernatePaymentViewErrorDao extends BaseHibernateDao<PaymentViewError> implements PaymentViewErrorDao {

    /** Creates a constructor for this class */
    public HibernatePaymentViewErrorDao() {
        super(PaymentViewError.class);
    }
    
    /** @see PaymentViewErrorDao#delete(PaymentViewError) */
    public void delete(PaymentViewError error) {
        super.delete(error);
    }

    /** @see PaymentViewErrorDao#deleteAll() */
    public void deleteAll() {
       super.getHibernateTemplate().bulkUpdate("DELETE FROM PaymentViewError");
    }

    /** @see PaymentViewErrorDao#getAllErrors() */
    public List<PaymentViewError> getAllErrors() {
        return super.getAll();
    }

    /** @see PaymentViewErrorDao#getAllErrors(int, int) */
    public List<PaymentViewError> getAllErrors(int startIndex, int limit) {
        return super.getAll(startIndex, limit);
    }

    /** @see PaymentViewErrorDao#getAllErrors(ErrorType) */
    public List<PaymentViewError> getAllErrors(ErrorType type) {
        DetachedCriteria criteria = super.getCriterion();
        
        criteria.add(Restrictions.eq(PaymentViewError.Field.TYPE.getFieldName(), type));
        
        return super.getList(criteria);
    }

    /** @see PaymentViewErrorDao#getAllErrors(ErrorType, int, int) */
    public List<PaymentViewError> getAllErrors(ErrorType type, int startIndex, int limit) {
        DetachedCriteria criteria = super.getCriterion();
        criteria.add(Restrictions.eq(Field.TYPE.getFieldName(), type));
        
        return super.getList(criteria, startIndex, limit);
    }

    /** @see PaymentViewErrorDao#getErrorCount() */
    public int getErrorCount() {
        return super.countAll();
    }

    /** @see PaymentViewErrorDao#getErrorCount(ErrorType) */
    public int getErrorCount(ErrorType type) {
        DetachedCriteria criteria = super.getCriterion();        
        criteria.add(Restrictions.eq(PaymentViewError.Field.TYPE.getFieldName(), type));
        
        return super.getCount(criteria);
    }

    /** @see PaymentViewErrorDao#save(PaymentViewError) */
    public void save(PaymentViewError error) {
        super.saveWithoutDuplicateHandling(error);
    }

    /** @see PaymentViewErrorDao#update(PaymentViewError) */
    public void update(PaymentViewError error) {
        super.saveWithoutDuplicateHandling(error);
    }

}
