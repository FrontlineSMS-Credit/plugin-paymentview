package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
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

	public List<CustomField> getCustomFieldsByName(String name) {
		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions.disjunction().add(
						Restrictions.ilike(
								CustomField.Field.NAME.getFieldName(),
								name.trim(), MatchMode.ANYWHERE)));
		return super.getList(criteria);
	}

	public List<CustomField> getCustomFieldsByName(String name, int startIndex,
			int limit) {
		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions.disjunction().add(
						Restrictions.ilike(
								CustomField.Field.NAME.getFieldName(),
								name.trim(), MatchMode.ANYWHERE)));
		return super.getList(criteria, startIndex, limit);
	}

	public List<CustomField> getCustomFieldsByReadableName(String strName) {
		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions.disjunction().add(
						Restrictions.ilike(
								CustomField.Field.READABLE_NAME.getFieldName(),
								strName.trim(), MatchMode.ANYWHERE)));
		return super.getList(criteria);
	}

	public List<CustomField> getCustomFieldsByReadableName(String strName,
			int startIndex, int limit) {
		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions.disjunction().add(
						Restrictions.ilike(
								CustomField.Field.READABLE_NAME.getFieldName(),
								strName.trim(), MatchMode.ANYWHERE)));
		return super.getList(criteria, startIndex, limit);
	}

	public List<CustomField> getAllActiveUnusedCustomFields() {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(CustomField.Field.ACTIVE.getFieldName(),
				Boolean.TRUE));
		criteria.add(Restrictions.eq(CustomField.Field.USED.getFieldName(),
				Boolean.FALSE));
		return super.getList(criteria);
	}

	public List<CustomField> getAllActiveUsedCustomFields() {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(CustomField.Field.ACTIVE.getFieldName(),
				Boolean.TRUE));
		criteria.add(Restrictions.eq(CustomField.Field.USED.getFieldName(),
				Boolean.TRUE));
		return super.getList(criteria);
	}

	public List<CustomField> getAllActiveCustomFields() {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(CustomField.Field.ACTIVE.getFieldName(),
				Boolean.TRUE));
		return super.getList(criteria);
	}

	public void saveCustomField(CustomField customField)
			throws DuplicateKeyException {
		super.save(customField);
	}

	public void updateCustomField(CustomField customField)
			throws DuplicateKeyException {
		super.update(customField);

	}
}
