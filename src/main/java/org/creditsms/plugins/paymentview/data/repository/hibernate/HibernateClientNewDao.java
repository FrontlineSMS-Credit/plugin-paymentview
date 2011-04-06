package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import org.creditsms.plugins.paymentview.data.domain.ClientNew;
import org.creditsms.plugins.paymentview.data.repository.ClientNewDao;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;
import org.hibernate.criterion.Projections;
import org.hibernate.Session;
import org.hibernate.Criteria;

@SuppressWarnings("unchecked")
public class HibernateClientNewDao extends BaseHibernateDao<ClientNew> implements ClientNewDao {
	protected HibernateClientNewDao() {
		super(ClientNew.class);
	}

	public List<ClientNew> getAllClients() {
		return this.getHibernateTemplate().loadAll(ClientNew.class);
	}

	public List<ClientNew> getAllClients(int startIndex, int limit) {
		List<ClientNew> clientList = null;
		Session session = this.getSession();
		Criteria criteria = session.createCriteria(ClientNew.class);
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(limit);
		clientList = criteria.list();
		
		return clientList;
	}

	public List<ClientNew> getClientByName(String clientName) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(ClientNew.class)
		.add(Restrictions.ilike("firstName", clientName.trim(), MatchMode.ANYWHERE))
		.add(Restrictions.ilike("otherName", clientName.trim(), MatchMode.ANYWHERE));
			
		List<ClientNew> clientList = criteria.list();

		if (clientList.size() == 0) {
			return null;
		}
		return clientList;
	}

	public List<ClientNew> getClientByName(String clientName, int startIndex,
			int limit) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(ClientNew.class)
		.add(Restrictions.disjunction()
		.add(Restrictions.ilike("firstName", clientName.trim(), MatchMode.ANYWHERE))
		.add(Restrictions.ilike("otherName", clientName.trim(), MatchMode.ANYWHERE)));
			
		criteria.setFirstResult(startIndex);
		criteria.setMaxResults(limit);
		
		List<ClientNew> clientList = criteria.list();

		if (clientList.size() == 0) {
			return null;
		}
		return clientList;
	}

	public ClientNew getClientByPhoneNumber(long phoneNumber) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(ClientNew.class)
		.add(Restrictions.eq("phoneNumber", phoneNumber));
			
		List<ClientNew> clientList = criteria.list();

		if (clientList.size() == 0) {
			return null;
		}
		return clientList.get(0);
	}

	@SuppressWarnings("rawtypes")
	public int getClientCount() {
		Session session = this.getSession();
		int rowCount = 0;
		Criteria criteria = session.createCriteria(ClientNew.class);

		criteria.setProjection(Projections.rowCount());
		List result  = criteria.list();
		if(!result.isEmpty()){
			rowCount = (Integer) result.get(0);
		}else{
		}
		return rowCount;
	}

	public void deleteClient(ClientNew client) {
		this.getHibernateTemplate().delete(client);
		this.getHibernateTemplate().flush();
	}

	public void saveUpdateClient(ClientNew client) {
		this.getHibernateTemplate().saveOrUpdate(client);
		this.getHibernateTemplate().flush();
		this.getHibernateTemplate().refresh(client);
	}

	public ClientNew getClientById(long clientId) {
		Session session = getSession();
		Criteria criteria = session.createCriteria(ClientNew.class)
		.add(Restrictions.eq("clientId", clientId));
			
		List<ClientNew> clientList = criteria.list();

		if (clientList.size() == 0) {
			return null;
		}
		return clientList.get(0);
	}

}
