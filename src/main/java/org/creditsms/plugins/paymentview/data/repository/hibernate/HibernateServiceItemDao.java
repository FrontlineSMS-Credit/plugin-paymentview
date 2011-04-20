package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * @author Roy
 **/
public class HibernateServiceItemDao extends BaseHibernateDao<ServiceItem>  implements ServiceItemDao{
	
	protected HibernateServiceItemDao(){
		super(ServiceItem.class);
	}

	public List<ServiceItem> getAllServiceItem() {
		return super.getAll();
	}

	public ServiceItem getServiceItemById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public List<ServiceItem> getAllServiceItem(int startIndex, int limit) {
		return super.getAll(startIndex, limit);
	}

	public List<ServiceItem> getServiceItemByName(String serviceItemName) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("targetName", serviceItemName));
		return super.getList(criteria);
	}

	public List<ServiceItem> getServiceItemByName(String serviceItemName,
			int startIndex, int limit) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("targetName", serviceItemName));
		return super.getList(criteria, startIndex, limit);
	}

	public int getServiceItemCount() {
		return super.countAll();
	}

	public void deleteServiceItem(ServiceItem serviceItem) {
		super.delete(serviceItem);
	}

	public void saveServiceItem(ServiceItem serviceItem)
			throws DuplicateKeyException {
		super.save(serviceItem);
	}

	public void updateServiceItem(ServiceItem serviceItem)
			throws DuplicateKeyException {
		super.update(serviceItem);
	}
}
