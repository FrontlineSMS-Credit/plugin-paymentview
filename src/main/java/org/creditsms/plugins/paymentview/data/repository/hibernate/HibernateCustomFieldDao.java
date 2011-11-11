package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

/**
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 * @author Roy
 */
public class HibernateCustomFieldDao extends BaseHibernateDao<CustomField>
		implements CustomFieldDao {
	protected HibernateCustomFieldDao() {
		super(CustomField.class);
	}

	public void deleteCustomField(CustomField customField) {
		super.delete(customField);
	}

	public List<CustomField> getAllCustomFields() {
		return super.getAll();
	}

	public List<CustomField> getAllCustomFields(int startIndex, int limit) {
		return super.getAll(startIndex, limit);
	}

	public CustomField getCustomFieldById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public int getCustomFieldCount() {
		return super.countAll();
	}

	public List<CustomField> getCustomFieldsByReadableName(String name) {
		return super.getList(getReadableNameCriteria(name));
	}

	public List<CustomField> getCustomFieldsByReadableName(String name, int startIndex, int limit) {
		return super.getList(getReadableNameCriteria(name), startIndex, limit);
	}
	
	private DetachedCriteria getReadableNameCriteria(String name) {
		DetachedCriteria criteria = super.getCriterion();
		String fieldName = CustomField.Field.READABLE_NAME.getFieldName();
		Criterion ilike = Restrictions.ilike(fieldName, name.trim(), MatchMode.ANYWHERE);
		criteria.add(Restrictions.disjunction().add(ilike));
		return criteria;
	}

	public List<CustomField> getAllActiveUnusedCustomFields() {
		return getCustomFields(true, false);
	}

	public List<CustomField> getAllActiveUsedCustomFields() {
		return getCustomFields(true, true);
	}

	public List<CustomField> getAllActiveCustomFields() {
		return getCustomFields(true, null);
	}
	
	private List<CustomField> getCustomFields(Boolean active, Boolean used) {
		DetachedCriteria criteria = super.getCriterion();
		if(active != null) {
			criteria.add(Restrictions.eq(CustomField.Field.ACTIVE.getFieldName(), active));
		}
		if(used != null) {
			criteria.add(Restrictions.eq(CustomField.Field.USED.getFieldName(), used));
		}
		return super.getList(criteria);
	}

	public void saveCustomField(CustomField customField) throws DuplicateKeyException {
		super.save(customField);
	}

	public void updateCustomField(CustomField customField) throws DuplicateKeyException {
		super.update(customField);
	}
}
