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
	public void deleteClient(Client client) throws DuplicateKeyException {
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

	public Client getClientByPhoneNumber(String phoneNumber) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq(Client.Field.PHONE_NUMBER.getFieldName(), phoneNumber));
		return super.getUnique(criteria);
	}

	/** @see ClientDao#getClientCount() */
	public int getClientCount() {
		return super.countAll();
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
