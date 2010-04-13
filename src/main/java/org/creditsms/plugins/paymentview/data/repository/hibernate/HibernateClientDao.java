package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;

public class HibernateClientDao extends BaseHibernateDao<Client> implements ClientDao {

	public HibernateClientDao(){
		super(Client.class);
	}
	
	public void deleteClient(Client client) throws DuplicateKeyException {
		// TODO Auto-generated method stub

	}

	public List<Client> getAllClients() {
		// TODO Auto-generated method stub
		return null;
	}

	public List<Client> getAllClients(int startIndex, int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	public Client getClientByName(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	public Client getClientByPhoneNumber(String phoneNumber) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getClientCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void saveClient(Client client) throws DuplicateKeyException {
		// TODO Auto-generated method stub

	}

}
