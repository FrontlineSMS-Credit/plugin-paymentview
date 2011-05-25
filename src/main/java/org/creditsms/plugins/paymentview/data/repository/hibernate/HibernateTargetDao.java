package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.sql.Time;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

/**
 * 
 * @author Roy
 * 
 */
public class HibernateTargetDao extends BaseHibernateDao<Target> implements
		TargetDao {

	protected HibernateTargetDao() {
		super(Target.class);
	}

	public void deleteTarget(Target target) {
		super.delete(target);
	}

	public List<Target> getAllTargets() {
		return super.getAll();
	}

	public List<Target> getAllTargets(int startIndex, int limit) {
		return super.getAll(startIndex, limit);
	}

	public Target getTargetByAccount(String string) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add(Restrictions.eq("accountNumber", string));
		return super.getUnique(criteria);
	}
	
	public Target getActiveTargetByAccount(String string) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria accountCriteria = criteria.createCriteria("account");
		accountCriteria.add(Restrictions.eq("accountNumber", string));
		criteria.add(Restrictions.eq("completedDate", null));
		return super.getUnique(criteria);
	}

	public Target getTargetByEndDateBetweenDates(long targetItemId,
			Time startDate, Time endDate) {
		// TODO Auto-generated method stub
		return null;
	}

	public Target getTargetById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public List<Target> getTargetsByName(String targetName) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria targetItemCriteria = criteria
				.createCriteria("serviceItem");
		targetItemCriteria.add(Restrictions.eq("targetName", targetName));
		return super.getList(criteria);
	}

	public List<Target> getTargetsByName(String targetName, int startIndex,
			int limit) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria targetItemCriteria = criteria
				.createCriteria("serviceItem");
		targetItemCriteria.add(Restrictions.eq("targetName", targetName));
		return super.getList(criteria, startIndex, limit);
	}

	public List<Target> getTargetsByServiceItem(long targetItemId) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria targetItemCriteria = criteria
				.createCriteria("serviceItem");
		targetItemCriteria.add(Restrictions.eq("id", targetItemId));
		return super.getList(criteria);
	}

	public int getTargetCount() {
		return super.countAll();
	}

	public void saveTarget(Target target) {
		super.saveWithoutDuplicateHandling(target);
	}

	public void updateTarget(Target target) throws DuplicateKeyException {
		super.update(target);
	}

}
