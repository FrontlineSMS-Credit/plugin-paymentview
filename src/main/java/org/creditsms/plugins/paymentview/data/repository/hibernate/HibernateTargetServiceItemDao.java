package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.data.repository.TargetServiceItemDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author Roy
 * 
 */
public class HibernateTargetServiceItemDao extends BaseHibernateDao<TargetServiceItem> implements
TargetServiceItemDao {

	protected HibernateTargetServiceItemDao() {
		super(TargetServiceItem.class);
	}
	
	public List<TargetServiceItem> getAllTargetServiceItemByTarget(long targetId) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria targetCriteria = criteria.createCriteria("target");
		targetCriteria.add(Restrictions.eq("id", targetId));
		return super.getList(criteria);
	}

	public List<TargetServiceItem> getAllTargetServiceItemByTargetFiltered(
			long targetId, int startIndex, int limit) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria targetCriteria = criteria.createCriteria("target");
		targetCriteria.add(Restrictions.eq("id", targetId));
		return super.getList(criteria, startIndex, limit);
	}

	public void deleteTargetServiceItem(TargetServiceItem targetServiceItem) {
		super.delete(targetServiceItem);
	}

	public void saveTargetServiceItem(TargetServiceItem targetServiceItem)
			throws DuplicateKeyException {
		super.save(targetServiceItem);
	}

	public void updateTargetServiceItem(TargetServiceItem targetServiceItem)
			throws DuplicateKeyException {
		super.update(targetServiceItem);
	}

	public TargetServiceItem getTargetServiceItemById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public List<TargetServiceItem> getAllTargetServiceItems() {
		return super.getAll();
	}
}
