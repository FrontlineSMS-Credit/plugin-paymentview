package org.creditsms.plugins.paymentview.data.dummy;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

public class DummyData {
	public static final DummyData INSTANCE = new DummyData();

	public static final long NO_ID_SET = 0;

	private final DummyClientDao clientDao = new DummyClientDao();
	private final DummyAccountDao accountDao = new DummyAccountDao();
	private final DummyIncomingPaymentDao incomingPaymentDao = new DummyIncomingPaymentDao();
	private final DummyNetworkOperatorDao networkOperatorDao = new DummyNetworkOperatorDao();
	private final OutgoingPaymentDao outgoingPaymentDao = new DummyOutgoingPaymentDao();

	private DummyData() {
		// Create dummy clients
		createDummyClient("Alice Wangare", "+25472457645", new long[] {
				4343625, 247362623 });
		createDummyClient("John Kamau", "+254720547355", new long[] { 82666633,
				23233423 });
		createDummyClient("Wekalao Matanda", "+254725452345", new long[] {
				4323425, 234327443 });
		createDummyClient("Ismael Koli", "+254720445345", new long[] {
				434147425, 2439423 });
		createDummyClient("Alice Wayua", "+25472045345", new long[] { 81672323,
				25467623 });
		createDummyClient("Laura Agutu", "+25472054645", new long[] { 88882672,
				233854323 });
		createDummyClient("Wangunyu Maigua", "+25472454645", new long[] {
				454264435, 265373423 });
		createDummyClient("Stephen Kalungi", "+25472544545", new long[] {
				432957346, 23643523 });
		createDummyClient("Mitawi Kisio", "+25472463345", new long[] {
				430002454, 2334423 });
		createDummyClient("Angela Koki", "+25472463445", new long[] { 9923623,
				232255000 });
		createDummyClient("Edith Khalai", "+25472674545", new long[] {
				433599009, 23355753 });
		createDummyClient("Juma Omondi", "+254726712345", new long[] {
				432592853, 57437263 });
		createDummyClient("Harry Mwabare", "+25472572345", new long[] {
				459732535, 37334423 });
		createDummyClient("Wambui Waweru", "+254720144545", new long[] {
				443600833, 33736423 });
		createDummyClient("Lavender Akoth", "+254725565345", new long[] {
				434584239, 36734723 });
		createDummyClient("Sammy Kitonyi", "+254724412345", new long[] {
				343763254, 37356723 });
		createDummyClient("Isiah Muchene", "+254723312235", new long[] {
				463537445, 59536723 });
		createDummyClient("Onesmus Mukewa", "+25473612345", new long[] {
				50962324, 25798563 });
		createDummyClient("Roy Owino", "+25474512345", new long[] { 88851243,
				232259896 });
		createDummyClient("Justin Mwakidedi", "+25475412345", new long[] {
				232492474, 25857623 });
		createDummyClient("Mario Mwangi", "+25472542345", new long[] {
				434322934, 23278523 });
		createDummyClient("Peter Kamau", "+254724555345", new long[] {
				343972222, 24373423 });
		createDummyClient("Phanice Nafula", "+254724552345", new long[] {
				872243234, 27434523 });
		createDummyClient("Isiah Mwiki", "+25472012345", new long[] { 77175292,
				108949744 });
		createDummyClient("Kimani Karao", "+254720347345", new long[] {
				00425425, 22456483 });
		createDummyClient("Lowuya Lamini", "+254720455345", new long[] {
				00023225, 232363547 });
		createDummyClient("Charlene Nyambura", "+25472762345", new long[] {
				43330025, 23432674 });
		createDummyClient("Tangus Koech", "+25472012326", new long[] {
				42400255, 24343423 });
		createDummyClient("Justus Matanda", "+25472014545", new long[] {
				42547455, 43493444 });

	createDummyIncomingPayment("Isiah Muchene", "+254723312235", "1300560000000", new BigDecimal("43513.60"), 59536723);
		createDummyIncomingPayment("Ian Mukewa", "+25472762345", "1300560000100", new BigDecimal("313.60"), 23432674);
		createDummyIncomingPayment("Justin Mwakidedi", "+25475412345", "1300560000200", new BigDecimal("433.00"), 25857623);
		createDummyIncomingPayment("John Muigai", "+25472012326", "1300560200300", new BigDecimal("44232.24"), 24343423);
		createDummyIncomingPayment("Phanice Nafula", "+25472014545", "1300560020400", new BigDecimal("44343.23"), 43493444);
		createDummyIncomingPayment("Charlene Nyambura", "+25472014545","1300560000500", new BigDecimal("345323.63"),
				23432674);
		createDummyIncomingPayment("Tangus Koech", "+25472014545", "1300560200600", new BigDecimal("34243.44"), 42400255);
		createDummyIncomingPayment("Peter Kamau", "+254724555345", "1300560200700", new BigDecimal("353366.66"), 24373423);
		createDummyIncomingPayment("Isiah Mwiki", "+25472012345", "1300560000800", new BigDecimal("5456555.66"),
				108949744);
		createDummyIncomingPayment("Kimani Karao", "+254720347345", "1300560000900", new BigDecimal("535635.45"), 00425425);
		createDummyIncomingPayment("Lowuya Lamini", "+254720455345", "1300560001000", new BigDecimal("636336.33"),
				434147425);

		createDummyIncomingPayment("Angela Koki", "+254720999345", "1300564344000", new BigDecimal("636336.33"),
				23278523);
		createDummyIncomingPayment("Isiah Mwiki", "+254720785345", "1300232333300", new BigDecimal("636336.33"),
				233854323);
		createDummyIncomingPayment("Roy Owino", "+254720487545",   "1302304299006", new BigDecimal("636336.33"),
				25857623);
		createDummyIncomingPayment("Wambui Waweru", "+254720113445", "1302304299996", new BigDecimal("636336.33"),
				232363547);
		createDummyIncomingPayment("Lavendar Akoth", "+254724666645","1302304299996", new BigDecimal("636336.33"),
				232255000);

		createDummyOutgoingPayment("Isiah Muchene", 	"+254723312233", "1302560000896", new BigDecimal("43513.60"), 	59536723);
		createDummyOutgoingPayment("Ian Mukewa", 		"+254727623453", "1300000000896", new BigDecimal("313.60"), 	23432674);
		createDummyOutgoingPayment("Justin Mwakidedi", 	"+254754123445", "1302300049896", new BigDecimal("433.00"), 	25857623);
		createDummyOutgoingPayment("John Muigai", 		"+254720123426", "1302000034896", new BigDecimal("44232.24"), 	24343423);
		createDummyOutgoingPayment("Phanice Nafula", 	"+254720145345", "1302000500896", new BigDecimal("44343.23"), 	43493444);
		createDummyOutgoingPayment("Charlene Nyambura", "+254720145345", "1302000433896", new BigDecimal("345323.64"),	23432674);
		createDummyOutgoingPayment("Tangus Koech", 		"+254720145445", "1302000004296", new BigDecimal("34243.46"), 	42400255);
		createDummyOutgoingPayment("Peter Kamau", 		"+254724555434", "1302000233596", new BigDecimal("353366.66"), 	24373423);
		createDummyOutgoingPayment("Isiah Mwiki", 		"+254720123435", "1302567149436", new BigDecimal("5456555.66"),	108949744);
		createDummyOutgoingPayment("Kimani Karao", 		"+254720347344", "1302002333496", new BigDecimal("535635.45"), 	00425425);
		createDummyOutgoingPayment("Lowuya Lamini", 	"+254720455344", "1300000382896", new BigDecimal("636336.34"),	265373423);
		createDummyOutgoingPayment("Angela Koki", 		"+254720999334", "1302000233896", new BigDecimal("636336.34"),	872243234);
		createDummyOutgoingPayment("Isiah Mwiki", 		"+254720785334", "1302038720896", new BigDecimal("636336.34"),	232363547);
		createDummyOutgoingPayment("Roy Owino", 		"+254720487354", "1302003230896", new BigDecimal("636336.34"),	23355753);
		createDummyOutgoingPayment("Wambui Waweru", 	"+254720113344", "1302002323896", new BigDecimal("636336.34"),	4323425);
		createDummyOutgoingPayment("Lavendar Akoth", 	"+254724666364", "1302000003896", new BigDecimal("636336.34"),	234327443);
	}

	private void createDummyOutgoingPayment(String paymentTo,
			String phoneNumber, String timePaid, BigDecimal amountPaid,
			int accountId) {
		OutgoingPayment o = new OutgoingPayment();
		o.setAmountPaid(amountPaid);
		Account myAcc = accountDao.getAccountByAccountNumber(accountId);
		o.setAccount(myAcc);
		o.setAmountPaid(amountPaid);
		o.setPhoneNumber(phoneNumber);
		o.setTimePaid(new Date(Long.parseLong(timePaid)));
		try {
			outgoingPaymentDao.saveOutgoingPayment(o);
		} catch (Exception e) {// DuplicateKeyExceptioncreateDummyOutgoingPayment
			throw new RuntimeException(e);
		}

	}

	private void createDummyIncomingPayment(String paymentBy,
			String phoneNumber, String timePaid, BigDecimal amountPaid,
			long accountId) {
		IncomingPayment i = new IncomingPayment();
		i.setAmountPaid(amountPaid);
		Account myAcc = accountDao.getAccountByAccountNumber(accountId);
		i.setAccount(myAcc);
		i.setPaymentBy(paymentBy);
		i.setPhoneNumber(phoneNumber);
		i.setTimePaid(new Date(Long.parseLong(timePaid)));
		try {
			incomingPaymentDao.saveIncomingPayment(i);
		} catch (Exception e) {// DuplicateKeyException
			throw new RuntimeException(e);
		}

	}

	private Client createDummyClient(String name, String phoneNumber,
			long[] accountNumbers) {
		Client c = new Client();
		String[] names = name.split(" ");
		c.setFirstName(names[0]);
		c.setOtherName(names[1]);
		c.setPhoneNumber(phoneNumber);
		for (long accountNumber : accountNumbers) {
			Account a = new Account(accountNumber);
			accountDao.saveUpdateAccount(a);
			c.addAccount(a);
		}
		try {
			clientDao.saveUpdateClient(c);
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
		private TreeSet<Account> accounts = new TreeSet<Account>(
				new Comparator<Account>() {
					public int compare(Account a1, Account a2) {
						return (int) (a1.getAccountId() - a2.getAccountId());
					}
				});
		private int accountIdCounter = 0;

		public List<Account> getAllAcounts() {
			return new ArrayList<Account>(accounts);
		}

		public List<Account> getAccountsByClientId(long clientId) {
			// TODO Auto-generated method stub
			return null;
		}

		public Account getAccountByAccountNumber(long accountNumber) {
			Account theAcc = null;
			for (Account acc : accounts) {
				if (accountNumber == acc.getAccountNumber()) {
					theAcc = acc;
				}
			}
			return theAcc;
		}

		public void deleteAccount(Account account) {
			accounts.remove(account);
		}

		public void saveUpdateAccount(Account account) {
			boolean isNew = accounts.add(account);
			if (isNew) {
				assignDatabaseId(account);
			}
		}

		private void assignDatabaseId(Account account) {
			account.setAccountId(++accountIdCounter);
		}

		public Account getAccountById(long accountId) {
			// TODO Auto-generated method stub
			return null;
		}

		public void saveAccount(Account account) throws DuplicateKeyException {
			// TODO Auto-generated method stub
			
		}

		public void updateAccount(Account account) throws DuplicateKeyException {
			// TODO Auto-generated method stub
			
		}
	}

	private class DummyNetworkOperatorDao implements NetworkOperatorDao {
		private TreeSet<NetworkOperator> incomingPayments = new TreeSet<NetworkOperator>(
				new Comparator<NetworkOperator>() {
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

	private class DummyOutgoingPaymentDao implements OutgoingPaymentDao {
		private TreeSet<OutgoingPayment> outgoingPayments = new TreeSet<OutgoingPayment>(
				new Comparator<OutgoingPayment>() {
					public int compare(OutgoingPayment i1, OutgoingPayment i2) {
						return (int) (i1.getId() - i2.getId());
					}
				});
		private int incomingPaymentIdCounter = 0;

		public OutgoingPayment getOutgoingPaymentById(long incomingPaymentId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<OutgoingPayment> getAllOutgoingPayments() {
			return new ArrayList<OutgoingPayment>(outgoingPayments);
		}

		public List<OutgoingPayment> getOutgoingPaymentsByDateRange(
				Calendar startDate, Calendar endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<OutgoingPayment> getOutgoingPaymentsByTimeRange(
				Date startTime, Date endtime) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<OutgoingPayment> getOutgoingPaymentsByClientIdByDateRange(
				long clientId, Calendar startDate, Calendar endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<OutgoingPayment> getOutgoingPaymentByClientId(long clientId) {
			// TODO Auto-generated method stub
			return null;
		}

		public void saveOrUpdateOutgoingPayment(OutgoingPayment incomingPayment) {
			boolean isNew = outgoingPayments.add(incomingPayment);
			if (isNew) {
				assignDatabaseId(incomingPayment);
			}
		}

		private void assignDatabaseId(OutgoingPayment incomingPayment) {
			incomingPayment.setId(++incomingPaymentIdCounter);
		}

		public void deleteOutgoingPayment(OutgoingPayment incomingPayment) {
			// TODO Auto-generated method stub

		}

		public List<OutgoingPayment> getOutgoingPaymentsByAccountNumber(
				long accountId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<OutgoingPayment> getOutgoingPaymentsByAccountNumberByDateRange(
				long accountId, Calendar startDate, Calendar endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<OutgoingPayment> getOutgoingPaymentsByAccountNumberByTimeRange(
				long accountId, Date startDate, Date endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<OutgoingPayment> getOutgoingPaymentsByPhoneNo(String phoneNo) {
			// TODO Auto-generated method stub
			return null;
		}

		public void saveOutgoingPayment(OutgoingPayment outgoingPayment)
				throws DuplicateKeyException {
			// TODO Auto-generated method stub
			
		}

		public void updateOutgoingPayment(OutgoingPayment outgoingPayment)
				throws DuplicateKeyException {
			// TODO Auto-generated method stub
			
		}
	}

	private class DummyIncomingPaymentDao implements IncomingPaymentDao {
		private TreeSet<IncomingPayment> incomingPayments = new TreeSet<IncomingPayment>(
				new Comparator<IncomingPayment>() {
					public int compare(IncomingPayment i1, IncomingPayment i2) {
						return (int) (i1.getId() - i2.getId());
					}
				});
		private int incomingPaymentIdCounter = 0;

		public List<IncomingPayment> getAllIncomingPayments(int startingIndex,
				int limit) {
			List<IncomingPayment> inps = new ArrayList<IncomingPayment>(
					incomingPayments);
			return inps.subList(startingIndex, limit);
		}

		public IncomingPayment getIncomingPaymentById(long incomingPaymentId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getAllIncomingPayments() {
			return new ArrayList<IncomingPayment>(incomingPayments);
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

		public List<IncomingPayment> getIncomingPaymentsByPhoneNo(String phoneNo) {
			// TODO Auto-generated method stub
			return null;
		}

		public void saveIncomingPayment(IncomingPayment incomingPayment) {
			boolean isNew = incomingPayments.add(incomingPayment);
			if (isNew) {
				assignDatabaseId(incomingPayment);
			}
		}

		private void assignDatabaseId(IncomingPayment incomingPayment) {
			incomingPayment.setId(++incomingPaymentIdCounter);
		}

		public void deleteIncomingPayment(IncomingPayment incomingPayment) {
			// TODO Auto-generated method stub

		}

		public List<IncomingPayment> getIncomingPaymentsByAccountNumber(
				long accountId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByAccountNumberByDateRange(
				long accountId, Calendar startDate, Calendar endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<IncomingPayment> getIncomingPaymentsByAccountNumberByTimeRange(
				long accountId, Date startDate, Date endDate) {
			// TODO Auto-generated method stub
			return null;
		}

		public void updateIncomingPayment(IncomingPayment incomingPayment)
				throws DuplicateKeyException {
			// TODO Auto-generated method stub

		}
	}

	private class DummyClientDao implements ClientDao {
		private Map<Long, Client> clients = new HashMap<Long, Client>();
		/** Counter used for simulating Hibernate database IDs */
		private long clientIdCounter;

		public List<Client> getAllClients() {
			return new ArrayList<Client>(clients.values());
		}

		public List<Client> getAllClients(int startIndex, int limit) {
			return new ArrayList<Client>(clients.values()).subList(startIndex,
					limit);
		}

		public int getClientCount() {
			return this.clients.size();
		}

		public void saveUpdateClient(Client client)
				throws DuplicateKeyException {
			if (client.getId() == NO_ID_SET) {
				assignDatabaseId(client);
			}
			clients.put(client.getId(), client);
		}

		private void assignDatabaseId(Client client) {
			client.setId(++clientIdCounter);
		}

		public void deleteClient(Client client) {
			clients.remove(client.getId());
		}

		public Client getClientById(long clientId) {
			// TODO Auto-generated method stub
			return null;
		}

		public List<Client> getClientsByName(String clientName) {
			List<Client> filteredClients = new ArrayList<Client>();
			for (Client c : clients.values()) {
				if (clientName.toLowerCase().equals(
						c.getFirstName().toLowerCase())) {
					filteredClients.add(c);
				}
			}
			return filteredClients;
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

		public Client getClientByAccount(Account account) {
			for (Client client : this.clients.values()) {
				if (client.getAccounts().contains(account)) {
					return client;
				}
			}
			return null;
		}

		public void saveClient(Client client) throws DuplicateKeyException {
			// TODO Auto-generated method stub
			
		}

		public void updateClient(Client client) throws DuplicateKeyException {
			// TODO Auto-generated method stub
			
		}

	}

	public IncomingPaymentDao getIncomingPaymentDao() {
		return this.incomingPaymentDao;
	}

	public OutgoingPaymentDao getOutgoingPaymentDao() {
		return this.outgoingPaymentDao;
	}

	public AccountDao getAccountDao() {
		return this.accountDao;
	}
}
