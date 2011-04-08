package org.creditsms.plugins.paymentview.data.dummy;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.TreeSet;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;

public class DummyData {
	public static final DummyData INSTANCE = new DummyData();
	
	private final DummyClientDao clientDao = new DummyClientDao();
	
	private DummyData() {
		// Create dummy clients
		createDummyClient("Alice Libata", "+25472012345");
		createDummyClient("John Kamau", "+25472012345");
		createDummyClient("Ian Smith", "+25472012345");
		createDummyClient("Kim Koli", "+25472012345");
		createDummyClient("Alice Nafula", "+25472012345");
		createDummyClient("Jim Jerry", "+25472012345");
		createDummyClient("Bart Simpson", "+25472012345");
		createDummyClient("Homer Lynn", "+25472012345");
		createDummyClient("Jim Carrey", "+25472012345");
		createDummyClient("Hosbon Diner", "+25472012345");
		createDummyClient("Edith Rachael", "+25472012345");
	}

	private Client createDummyClient(String name, String phoneNumber) {
		Client c = new Client();
		c.setFirstName(name);
		c.setPhoneNumber(phoneNumber);
		try {
			clientDao.saveClient(c);
		} catch (DuplicateKeyException e) {
			throw new RuntimeException(e);
		}
		return c;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	private class DummyClientDao implements ClientDao {
		private TreeSet<Client> clients = new TreeSet<Client>(new Comparator<Client>() {
			public int compare(Client c1, Client c2) {
				return (int) (c1.getId() - c2.getId());
			}
		});
		/** Counter used for simulating Hibernate database IDs */
		private long clientIdCounter;

		
		public List<Client> getAllClients() {
			return new ArrayList<Client>(clients);
		}

		public List<Client> getAllClients(int startIndex, int limit) {
			return null;
		}

		public List<Client> filterClientsByName(String name) {
			return null;
		}

		public List<Client> filterClientsByName(String name, int startIndex,
				int limit) { 
			return null;
		}

		public List<Client> getClientByName(String name) {
			return null;
		}

		public Client getClientByPhoneNumber(String phoneNumber) {
			return null;
		}

		public int getClientCount() {
			return this.clients.size();
		}

		public int getFilteredClientCount(String name) {
			return 0;
		}

		public void saveClient(Client client) throws DuplicateKeyException {
			boolean isNew = clients.add(client);
			if(isNew) {
				assignDatabaseId(client);
			}
		}

		private void assignDatabaseId(Client client) {
			client.setId(++clientIdCounter);
		}

		public void updateClient(Client client) throws DuplicateKeyException {
			// do nothing ;)
		}

		public void deleteClient(Client client) {
			clients.remove(client);
		}

		public Client getClientById(long clientId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<Client> getClientsByName(String clientName, int startIndex,
				int limit) {
			// TODO Auto-generated method stub
			return null;
		}

		public Client getClientByPhoneNumber(long phoneNumber) {
			// TODO Auto-generated method stub
			return null;
		}

		public void saveUpdateClient(Client client) {
			// TODO Auto-generated method stub
			
		}

		public List<Client> getClientsByName(String clientName) {
			// TODO Auto-generated method stub
			return null;
		}		
	}
}
