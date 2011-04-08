package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.hibernate.Session;
import org.hibernate.Criteria;

@SuppressWarnings("unchecked")
public class HibernateClientDao extends BaseHibernateDao<Client> implements ClientDao {
	protected HibernateClientDao() {
		super(Client.class);
	}

	public List<Client> getAllClients() {
		return this.getHibernateTemplate().loadAll(Client.class);
	}

	public List<Client> getAllClients(int startIndex, int limit) {
		List<Client> clientList = null;
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(Client.class);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(limit);
		clientList = criteria.list();
		
		return clientList;
	}

	public List<Client> getClientsByName(String clientName) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Client.class)
		.add(Restrictions.ilike("firstName", clientName.trim(), MatchMode.ANYWHERE))
		.add(Restrictions.ilike("otherName", clientName.trim(), MatchMode.ANYWHERE));
			
		List<Client> clientList = criteria.list();

		if (clientList.size() == 0) {
			return null;
		}
		return clientList;
	}

	public List<Client> getClientByName(String clientName, int startIndex,
			int limit) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Client.class)
		.add(Restrictions.disjunction()
		.add(Restrictions.ilike("firstName", clientName.trim(), MatchMode.ANYWHERE))
		.add(Restrictions.ilike("otherName", clientName.trim(), MatchMode.ANYWHERE)));
			
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(limit);
		
		List<Client> clientList = criteria.list();

		if (clientList.size() == 0) {
			return null;
		}
		return clientList;
	}

	public Client getClientByPhoneNumber(long phoneNumber) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Client.class)
		.add(Restrictions.eq("phoneNumber", phoneNumber));
			
		List<Client> clientList = criteria.list();

		if (clientList.size() == 0) {
			return null;
		}
		return clientList.get(0);
	}

	@SuppressWarnings("rawtypes")
	public int getClientCount() {
		Session session = this.getSession();
		int rowCount = 0;
		Criteria criteria = session.createCriteria(Client.class);

		criteria.setProjection(Projections.rowCount());
		List result  = criteria.list();
		if(!result.isEmpty()){
			rowCount = (Integer) result.get(0);
		}else{
		}
		return rowCount;
	}

	public void deleteClient(Client client) {
		this.getHibernateTemplate().delete(client);
		this.getHibernateTemplate().flush();
	}

	public void saveUpdateClient(Client client) {
		this.getHibernateTemplate().saveOrUpdate(client);
	}

	public Client getClientById(long clientId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(Client.class)
		.add(Restrictions.eq("id", clientId));
			
		List<Client> clientList = criteria.list();

		if (clientList.size() == 0) {
			return null;
		}
		return clientList.get(0);
	}

}
