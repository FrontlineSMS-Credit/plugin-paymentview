package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

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

	public Client getClientByPhoneNumber(long phoneNumber) {
		DetachedCriteria criteria = super.getCriterion();
		criteria.add(Restrictions.eq("phoneNumber", phoneNumber));
		return super.getUnique(criteria);
	}

	public int getClientCount() {
		return super.getAll().size();
	}

	public List<Client> getClientsByName(String clientName) {
		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions
						.disjunction()
						.add(Restrictions.ilike("firstName", clientName.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("otherName", clientName.trim(),
								MatchMode.ANYWHERE)));
		return super.getList(criteria);
	}

	public List<Client> getClientsByName(String clientName, int startIndex,
			int limit) {
		DetachedCriteria criteria = super.getCriterion().add(
				Restrictions
						.disjunction()
						.add(Restrictions.ilike("firstName", clientName.trim(),
								MatchMode.ANYWHERE))
						.add(Restrictions.ilike("otherName", clientName.trim(),
								MatchMode.ANYWHERE)));
		return super.getList(criteria, startIndex, limit);
	}

	public void saveClient(Client client) throws DuplicateKeyException {
		super.save(client);
	}

	public void updateClient(Client client) throws DuplicateKeyException {
		super.update(client);
	}
}
