/**
 * 
 */
package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.domain.QuickDialCode;
import org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author Emmanuel Kala
 *
 */
public class HibernateQuickDialCodeDao extends BaseHibernateDao<QuickDialCode> implements  QuickDialCodeDao {
    
    public HibernateQuickDialCodeDao() {
        super(QuickDialCode.class);
    }
    
    /**
     * @see org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao#deleteQuickDialCode(org.creditsms.plugins.paymentview.data.domain.QuickDialCode)
     */
    public void deleteQuickDialCode(QuickDialCode quickDialCode) {
        super.delete(quickDialCode);
    }

    /**
     * @see org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao#getAllQuickDialCodes()
     */
    public List<QuickDialCode> getAllQuickDialCodes() {
        return super.getAll();
    }

    /**
     * @see org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao#getQuickDialCodeCount()
     */
    public int getQuickDialCodeCount() {
        return super.countAll();
    }

    /**
     * @see org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao#getQuickDialCodes(int, int)
     */
    public List<QuickDialCode> getQuickDialCodes(int startIndex, int limit) {
        return super.getAll(startIndex, limit);
    }

    /**
     * @see org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao#getQuickDialCodesByNetworkOperator(org.creditsms.plugins.paymentview.data.domain.NetworkOperator)
     */
    public List<QuickDialCode> getQuickDialCodesByNetworkOperator(NetworkOperator operator) {
        DetachedCriteria criteria = super.getCriterion();
        //criteria.add(Restrictions.eq(, value))
        return null;
    }

    /**
     *  @see org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao#saveQuickDialCode(org.creditsms.plugins.paymentview.data.domain.QuickDialCode)
     */
    public void saveQuickDialCode(QuickDialCode quickDialCode) throws DuplicateKeyException {
        super.save(quickDialCode);
    }

    /**
     * @see org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao#updateQuickDialCode(org.creditsms.plugins.paymentview.data.domain.QuickDialCode)
     */
    public void updateQuickDialCode(QuickDialCode quickDialCode) throws DuplicateKeyException {
        super.update(quickDialCode);
    }

}
