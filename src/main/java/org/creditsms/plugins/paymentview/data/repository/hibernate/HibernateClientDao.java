package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;

public class HibernateClientDao extends BaseHibernateDao<Client> implements ClientDao {

	public HibernateClientDao(){
		super(Client.class);
	}
	
	/** @see ClientDao#deleteClient(Client) */
	public void deleteClient(Client client) {
		// Delete all the transactions associated with this client
		super.getHibernateTemplate().bulkUpdate("DELETE FROM PaymentServiceTransaction WHERE client=?", client);
		
		// Delete the client
		super.delete(client);
	}
	
	/** @see ClientDao#getAllClients() */
	public List<Client> getAllClients() {
		return super.getAll();
	}

	/** @see ClientDao#getAllClients(int, int) */
	public List<Client> getAllClients(int startIndex, int limit) {
		return super.getAll(startIndex, limit);
	}

	/** @see ClientDao#getClientByName(String) */
	public Client getClientByName(String name) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(Client.Field.NAME.getFieldName(), name));
		return super.getUnique(criteria);
	}

	/** @see ClientDao#filterClientsByName(String) */
	public List<Client> filterClientsByName(String name) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.ilike(Client.Field.NAME.getFieldName(), "%"+name+"%"));
		return super.getList(criteria);
	}

	/** @see ClientDao#filterClientsByName(String, int, int) */
	public List<Client> filterClientsByName(String name, int startIndex, int limit) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.ilike(Client.Field.NAME.getFieldName(), "%"+name+"%"));
		return super.getList(criteria, startIndex, limit);
	}

	public Client getClientByPhoneNumber(String phoneNumber) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(Client.Field.PHONE_NUMBER.getFieldName(), phoneNumber));
		return super.getUnique(criteria);
	}

	/** @see ClientDao#getClientCount() */
	public int getClientCount() {
		return super.countAll();
	}

	/** @see ClientDao#getFilteredClientCount(String) */
	public int getFilteredClientCount(String name) {
		String queryString = "SELECT COUNT(name) FROM Client WHERE name LIKE(?)";
		return super.getHibernateTemplate().find(queryString, "%" + name + "%").size();
	}

	/** @see ClientDao#saveClient(Client) */
	public void saveClient(Client client) throws DuplicateKeyException {
		 super.save(client);
	}
	
	/** @see ClientDao#updateClient(Client) */
	public void updateClient(Client client) throws DuplicateKeyException{
		super.update(client);
	}

}
