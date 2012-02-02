package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.Order;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Client.Field;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Subqueries;

@SuppressWarnings("unchecked")
public class HibernateClientDao extends BaseHibernateDao<Client> implements
		ClientDao {
	protected HibernateClientDao() {
		super(Client.class);
	}

	public void deleteClient(Client client) {
		super.delete(client);
	}

	public List<Client> getAllClients() {
		return this.getHibernateTemplate().loadAll(Client.class);
	}

	public List<Client> getAllClients(int startIndex, int limit) {
		return super.getAll(startIndex, limit);
	}

	public Client getClientById(long id) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("id", id));
		return super.getUnique(criteria);
	}

	public Client getClientByPhoneNumber(String phoneNumber) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("phoneNumber", phoneNumber));
		return super.getUnique(criteria);
	}
	
	public int getClientCount() {
		return getAllActiveClients().size();
	}

	public List<Client> getAllClientsSorted(
			Field sortBy, Order order) {
		DetachedCriteria criteria = super.getSortCriterion(sortBy, order);
		return super.getList(criteria);
	}
	
	public List<Client> getAllActiveClientsSorted(
			Field sortBy, Order order) {
		DetachedCriteria criteria = super.getSortCriterion(sortBy, order);
		criteria.add(Restrictions.eq(Client.Field.ACTIVE.getFieldName(),
				Boolean.TRUE));
		return super.getList(criteria);
	}
	
	public List<Client> getAllActiveClientsSorted(int startIndex, int limit,
			Field sortBy, Order order) {
		DetachedCriteria criteria = super.getSortCriterion(sortBy, order);
		criteria.add(Restrictions.eq(Client.Field.ACTIVE.getFieldName(),
				Boolean.TRUE));
		return super.getList(criteria);
	}
	
	public List<Client> getClientsByFilter(String filter) {
		DetachedCriteria subCriteria = DetachedCriteria.forClass(CustomValue.class);
		subCriteria.add(Restrictions.ilike("strValue", filter.trim(),MatchMode.ANYWHERE));
		DetachedCriteria customFieldSubCriteria = subCriteria.createCriteria("customField");
		customFieldSubCriteria.add(Restrictions.eq(CustomField.Field.USED.getFieldName(),Boolean.TRUE));
		subCriteria.setProjection(Projections.distinct(Projections.property("client")));

		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions
						.disjunction()
						.add(Restrictions.ilike("firstName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("otherName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("phoneNumber", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Subqueries.propertyIn("id", subCriteria)));
		criteria.add(Restrictions.eq(Client.Field.ACTIVE.getFieldName(),Boolean.TRUE));
		return super.getList(criteria);
	}
	
	public List<Client> getAllClientsByNameFilter(String filter) {
		DetachedCriteria subCriteria = DetachedCriteria.forClass(CustomValue.class);
		subCriteria.add(Restrictions.ilike("strValue", filter.trim(),MatchMode.ANYWHERE));
		DetachedCriteria customFieldSubCriteria = subCriteria.createCriteria("customField");
		customFieldSubCriteria.add(Restrictions.eq(CustomField.Field.USED.getFieldName(),Boolean.TRUE));
		subCriteria.setProjection(Projections.distinct(Projections.property("client")));

		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions
						.disjunction()
						.add(Restrictions.ilike("firstName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("otherName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Subqueries.propertyIn("id", subCriteria)));
		return super.getList(criteria);
	}
	
	public List<Client> getAllClientsByFilter(String filter, int startIndex,int limit) {
		DetachedCriteria subCriteria = DetachedCriteria.forClass(CustomValue.class);
		subCriteria.add(Restrictions.ilike("strValue", filter.trim(),MatchMode.ANYWHERE));
		DetachedCriteria customFieldSubCriteria = subCriteria.createCriteria("customField");
		customFieldSubCriteria.add(Restrictions.eq(CustomField.Field.USED.getFieldName(),Boolean.TRUE));
		subCriteria.setProjection(Projections.distinct(Projections.property("client")));

		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions
						.disjunction()
						.add(Restrictions.ilike("firstName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("otherName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("phoneNumber", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Subqueries.propertyIn("id", subCriteria)));
		return super.getList(criteria, startIndex, limit);
	}
	
	public List<Client> getClientsByNameFilter(String filter) {
		DetachedCriteria subCriteria = DetachedCriteria.forClass(CustomValue.class);
		subCriteria.add(Restrictions.ilike("strValue", filter.trim(),MatchMode.ANYWHERE));
		DetachedCriteria customFieldSubCriteria = subCriteria.createCriteria("customField");
		customFieldSubCriteria.add(Restrictions.eq(CustomField.Field.USED.getFieldName(),Boolean.TRUE));
		subCriteria.setProjection(Projections.distinct(Projections.property("client")));

		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions
						.disjunction()
						.add(Restrictions.ilike("firstName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("otherName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Subqueries.propertyIn("id", subCriteria)));
		criteria.add(Restrictions.eq(Client.Field.ACTIVE.getFieldName(),Boolean.TRUE));
		return super.getList(criteria);
	}
	
	public List<Client> getClientsByFilter(String filter, int startIndex,int limit) {
		DetachedCriteria subCriteria = DetachedCriteria.forClass(CustomValue.class);
		subCriteria.add(Restrictions.ilike("strValue", filter.trim(),MatchMode.ANYWHERE));
		DetachedCriteria customFieldSubCriteria = subCriteria.createCriteria("customField");
		customFieldSubCriteria.add(Restrictions.eq(CustomField.Field.USED.getFieldName(),Boolean.TRUE));
		subCriteria.setProjection(Projections.distinct(Projections.property("client")));

		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions
						.disjunction()
						.add(Restrictions.ilike("firstName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("otherName", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("phoneNumber", filter.trim(),
								MatchMode.ANYWHERE))
						.add(Subqueries.propertyIn("id", subCriteria)));
		criteria.add(Restrictions.eq(Client.Field.ACTIVE.getFieldName(),Boolean.TRUE));
		return super.getList(criteria, startIndex, limit);
	}
	
	public List<Client> getAllActiveClients() {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(Client.Field.ACTIVE.getFieldName(),
				Boolean.TRUE));
		return super.getList(criteria);
	}
	
	public List<Client> getAllActiveClients(int startIndex,
			int limit) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(Client.Field.ACTIVE.getFieldName(),
				Boolean.TRUE));
		return super.getList(criteria, startIndex, limit);
	}

	public void saveClient(Client client) {
		super.saveWithoutDuplicateHandling(client);
	}

	public void updateClient(Client client) {
		super.updateWithoutDuplicateHandling(client);
	}
}
