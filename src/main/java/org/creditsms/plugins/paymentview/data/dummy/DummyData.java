package org.creditsms.plugins.paymentview.data.dummy;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.TreeSet;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;

public class DummyData {
	public static final DummyData INSTANCE = new DummyData();
	
	private final DummyClientDao clientDao = new DummyClientDao();
	private final DummyAccountDao accountDao = new DummyAccountDao();
	private final DummyNetworkOperatorDao networkOperatorDao = new DummyNetworkOperatorDao();
	
	private DummyData() {
		// Create dummy clients
		createDummyClient("Alice Wangare", "+25472457645", new long[]{43425425, 23242423});
		createDummyClient("John Kamau", "+254720547355", new long[]{43425425, 23242423});
		createDummyClient("Wekalao Matanda", "+254725452345", new long[]{43425425, 23242423});
		createDummyClient("Ismael Koli", "+254720445345", new long[]{43425425, 23242423});
		createDummyClient("Alice Wayua", "+25472045345", new long[]{43425425, 23242423});
		createDummyClient("Laura Agutu", "+25472054645", new long[]{43425425, 23242423});
		createDummyClient("Wangunyu Maigua", "+25472454645", new long[]{43425425, 23242423});
		createDummyClient("Stephen Kalungi", "+25472544545", new long[]{43425425, 23242423});
		createDummyClient("Mitawi Kisio", "+25472463345", new long[]{43425425, 23242423});
		createDummyClient("Angela Koki", "+25472463445", new long[]{43425425, 23242423});
		createDummyClient("Edith Khalai", "+25472674545", new long[]{43425425, 23242423});
		createDummyClient("Juma Omondi", "+254726712345",  new long[]{43425425, 23242423});
		createDummyClient("Harry Mwabare", "+25472572345",  new long[]{43425425, 23242423});
		createDummyClient("Wambui Waweru", "+254720144545",  new long[]{43425425, 23242423});
		createDummyClient("Lavender Akoth", "+254725565345",  new long[]{43425425, 23242423});		
		createDummyClient("Sammy Kitonyi", "+254724412345",  new long[]{43425425, 23242423});
		createDummyClient("Isiah Muchene", "+254723312235",  new long[]{43425425, 23242423});
		createDummyClient("Onesmus Mukewa", "+25473612345",  new long[]{43425425, 23242423});
		createDummyClient("Roy Owino", "+25474512345",  new long[]{43425425, 23242423});
		createDummyClient("Justin Mwakidedi", "+25475412345",  new long[]{43425425, 23242423});
		createDummyClient("Mario Mwangi", "+25472542345",  new long[]{43425425, 23242423});
		createDummyClient("Peter Kamau", "+254724555345",  new long[]{43425425, 23242423});		
		createDummyClient("Phanice Nafula", "+254724552345",  new long[]{43425425, 23242423});
		createDummyClient("Isiah Mwiki", "+25472012345",  new long[]{43425425, 23242423});
		createDummyClient("Kimani Karao", "+254720347345",  new long[]{43425425, 23242423});
		createDummyClient("Lowuya Lamini", "+254720455345",  new long[]{43425425, 23242423});
		createDummyClient("Charlene Nyambura", "+25472762345",  new long[]{43425425, 23242423});
		createDummyClient("Tangus Koech", "+25472012326", new long[]{43425425, 23242423});
		createDummyClient("Justus Matanda", "+25472014545",  new long[]{43425425, 23242423});
	}

	private Client createDummyClient(String name, String phoneNumber, long[] accountNumbers) {
		Client c = new Client();
		String[] names = name.split(" ");
		c.setFirstName(names[0]); 
		c.setOtherName(names[1]);
		c.setPhoneNumber(phoneNumber);
		for(long accountNumber:accountNumbers){
			Account a = new Account(accountNumber);
			accountDao.saveUpdateAccount(a);
			c.addAccount(a);
		}
		try {
			clientDao.saveClient(c);
		} catch (DuplicateKeyException e) {
			throw new RuntimeException(e);
		}
		return c;
	}
	
	private NetworkOperator createDummyNetworkOperator(String name) {
		NetworkOperator n = new NetworkOperator();
		n.setOperatorName(name);
		try {
			networkOperatorDao.saveNetworkOperator(n); 
		} catch (DuplicateKeyException e) {
			throw new RuntimeException(e);
		}
		return n;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	private class DummyAccountDao implements AccountDao {
		private TreeSet<Account> accounts = new TreeSet<Account>(new Comparator<Account>() {
			public int compare(Account a1, Account a2) { 
				return (int) (a1.getAccountId() - a2.getAccountId());
			}
		});

		public List<Account> getAllAcounts() {
			return new ArrayList<Account>(accounts);
		}

		public List<Account> getAccountsByClientId(long clientId) {
			// TODO Auto-generated method stub
			return null;
		}

		public Account getAccountById(long accountId) {
			// TODO Auto-generated method stub
			return null;
		}

		public void deleteAccount(Account account) {
			// TODO Auto-generated method stub
			
		}

		public void saveUpdateAccount(Account account) {
			// TODO Auto-generated method stub
			
		}		
	}
	
	private class DummyNetworkOperatorDao implements NetworkOperatorDao {
		private TreeSet<NetworkOperator> incomingPayments = new TreeSet<NetworkOperator>(new Comparator<NetworkOperator>() {
			public int compare(NetworkOperator n1, NetworkOperator n2) {
				return (int) (n1.getId() - n1.getId()); 
			}
		});

		public List<NetworkOperator> getAllNetworkOperators() {
			// TODO Auto-generated method stub 
			return null;
		}

		public List<NetworkOperator> getAllNetworkOperators(int startIndex,
				int limit) {
			// TODO Auto-generated method stub
			return null;
		}

		public NetworkOperator getNetworkOperatorByName(String name) {
			// TODO Auto-generated method stub
			return null;
		}

		public int getNetworkOperatorCount() {
			// TODO Auto-generated method stub
			return 0;
		}

		public void saveNetworkOperator(NetworkOperator operator)
				throws DuplicateKeyException {
			// TODO Auto-generated method stub
			
		}

		public void updateNetworkOperator(NetworkOperator operator)
				throws DuplicateKeyException {
			// TODO Auto-generated method stub
			
		}

		public void deleteNetworkOperator(NetworkOperator operator) {
			// TODO Auto-generated method stub
			
		}
	}
	
	private class DummyIncomingPaymentDao implements IncomingPaymentDao {
		private TreeSet<IncomingPayment> incomingPayments = new TreeSet<IncomingPayment>(new Comparator<IncomingPayment>() {
			public int compare(IncomingPayment i1, IncomingPayment i2) {
				return (int) (i1.getIncomingPaymentId() - i2.getIncomingPaymentId()); 
			}
		});

		public IncomingPayment getIncomingPaymentById(long incomingPaymentId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getAllIncomingPayments() {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByDateRange(
				Calendar startDate, Calendar endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByTimeRange(
				Date startTime, Date endtime) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByClientIdByDateRange(
				long clientId, Calendar startDate, Calendar endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentByClientId(long clientId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByAccountId(
				long accountId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByAccountIdByDateRange(
				long accountId, Calendar startDate, Calendar endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByAccountIdByTimeRange(
				long accountId, Date startDate, Date endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByPayer(String payer) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByPhoneNo(long phoneNo) {
			// TODO Auto-generated method stub
			return null;
		}

		public void saveOrUpdateIncomingPayment(IncomingPayment incomingPayment) {
			// TODO Auto-generated method stub
			
		}

		public void deleteIncomingPayment(IncomingPayment incomingPayment) {
			// TODO Auto-generated method stub
			
		}
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

		

		public void deleteClient(Client client) {
			clients.remove(client);
		}


		public Client getClientById(long clientId) {
			// TODO Auto-generated method stub
			return null;
		}


		public List<Client> getAllClients(int startIndex, int limit) {
			// TODO Auto-generated method stub
			return null;
		}


		public List<Client> getClientsByName(String clientName) {
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

		
	}
}
