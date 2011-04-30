package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

public class HibernateCustomValueDao extends BaseHibernateDao<CustomValue>
		implements CustomValueDao {

	protected HibernateCustomValueDao() {
		super(CustomValue.class);
	}

	public void deleteCustomValue(CustomValue customValue) {
		super.delete(customValue);
	}

	public List<CustomValue> getAllCustomValues() {
		return super.getAll();
	}
	
	public int getCustomValuesCount() {
		return super.countAll();
	}

	public CustomValue getCustomValueById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(CustomValue.Field.ID.getFieldName(), id));
		return super.getUnique(criteria);
	}

	public List<CustomValue> getCustomValuesByClientId(long id) {
		DetachedCriteria criteria = super.getCriterion();
		DetachedCriteria clientCriteria = criteria.createCriteria("client");
		clientCriteria.add(Restrictions.eq(Client.Field.ID.getFieldName(), id));
		return super.getList(criteria);
	}
		
	public List<CustomValue> getCustomValuesByCustomFieldByValue(
			long customfieldId, String strValue) {
		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions.disjunction().add(
						Restrictions.ilike(CustomValue.Field.STR_VALUE.getFieldName(), strValue.trim(),
								MatchMode.ANYWHERE)));
		DetachedCriteria customFieldCriteria = criteria
				.createCriteria("customField");
		customFieldCriteria.add(Restrictions.eq("id", customfieldId));
		return super.getList(criteria);
	}

	public void saveCustomValue(CustomValue customValue)
			throws DuplicateKeyException {
		super.save(customValue);
	}

	public void updateCustomValue(CustomValue customValue)
			throws DuplicateKeyException {
		super.update(customValue);
	}
}
