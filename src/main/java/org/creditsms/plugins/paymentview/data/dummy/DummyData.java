package org.creditsms.plugins.paymentview.data.dummy;

import java.math.BigDecimal;
import java.util.Date;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

public class DummyData {
	private AccountDao accountDao;
	private ClientDao clientDao;
	private CustomFieldDao customFieldDao;
	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao;

	public DummyData(AccountDao accountDao, ClientDao clientDao,
			CustomFieldDao customFieldDao,
			IncomingPaymentDao incomingPaymentDao,
			OutgoingPaymentDao outgoingPaymentDao) {
		this.accountDao = accountDao;
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.incomingPaymentDao = incomingPaymentDao;
		this.outgoingPaymentDao = outgoingPaymentDao;

		if (clientDao.getAllClients().size() != 0) {
			createDummyData();
		}
	}

	private Client createDummyClient(String name, String phoneNumber,
			long[] accountNumbers) {
		String[] names = name.split(" ");
		
		Client c = new Client(names[0], names[1], phoneNumber);
		
		for (long accountNumber : accountNumbers) {
			Account a = new Account(accountNumber);
			try {
				accountDao.saveAccount(a);
			} catch (DuplicateKeyException e) {
				throw new RuntimeException(e);
			}
			c.addAccount(a);
		}
		
		try {
			clientDao.saveClient(c);
		} catch (DuplicateKeyException e) {
			throw new RuntimeException(e);
		}
		return c;
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

	private void createDummyOutgoingPayment(String paymentTo,
			String phoneNumber, String timePaid, BigDecimal amountPaid,
			int accountId) {
		OutgoingPayment o = new OutgoingPayment();
		o.setAmountPaid(amountPaid);
		Account myAcc = accountDao.getAccountByAccountNumber(accountId);
		o.setAccount(myAcc);
		o.setPhoneNumber(phoneNumber);

		o.setTimePaid(new Date(Long.parseLong(timePaid)));
		try {
			outgoingPaymentDao.saveOutgoingPayment(o);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	private void createDummyData() {
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

		createDummyIncomingPayment("Isiah Muchene", "+254723312235",
				"1300560000000", new BigDecimal("4513.20"), 59536723);
		createDummyIncomingPayment("Ian Mukewa", "+25472762345",
				"1300560000100", new BigDecimal("300.00"), 23432674);
		createDummyIncomingPayment("Justin Mwakidedi", "+25475412345",
				"1300560000200", new BigDecimal("420.00"), 25857623);
		createDummyIncomingPayment("John Muigai", "+25472012326",
				"1300560200300", new BigDecimal("400.00"), 24343423);
		createDummyIncomingPayment("Phanice Nafula", "+25472014545",
				"1300560020400", new BigDecimal("500.00"), 43493444);
		createDummyIncomingPayment("Charlene Nyambura", "+25472014545",
				"1300560000500", new BigDecimal("7030.60"), 23432674);
		createDummyIncomingPayment("Tangus Koech", "+25472014545",
				"1300560200600", new BigDecimal("3000.00"), 42400255);
		createDummyIncomingPayment("Peter Kamau", "+254724555345",
				"1300560200700", new BigDecimal("100.00"), 24373423);
		createDummyIncomingPayment("Isiah Mwiki", "+25472012345",
				"1300560000800", new BigDecimal("5000.00"), 108949744);
		createDummyIncomingPayment("Kimani Karao", "+254720347345",
				"1300560000900", new BigDecimal("1000.00"), 00425425);
		createDummyIncomingPayment("Lowuya Lamini", "+254720455345",
				"1300560001000", new BigDecimal("1200.00"), 434147425);

		createDummyIncomingPayment("Angela Koki", "+254720999345",
				"1300564344000", new BigDecimal("4000.95"), 23278523);
		createDummyIncomingPayment("Isiah Mwiki", "+254720785345",
				"1300232333300", new BigDecimal("8800.00"), 233854323);
		createDummyIncomingPayment("Roy Owino", "+254720487545",
				"1302304299006", new BigDecimal("10000.00"), 25857623);
		createDummyIncomingPayment("Wambui Waweru", "+254720113445",
				"1302304299996", new BigDecimal("1780.00"), 232363547);
		createDummyIncomingPayment("Lavendar Akoth", "+254724666645",
				"1302304299996", new BigDecimal("2000.00"), 232255000);

		createDummyOutgoingPayment("Isiah Muchene", "+254723312233",
				"1302560000896", new BigDecimal("3000.00"), 59536723);
		createDummyOutgoingPayment("Ian Mukewa", "+254727623453",
				"1300000000896", new BigDecimal("4000.00"), 23432674);
		createDummyOutgoingPayment("Justin Mwakidedi", "+254754123445",
				"1302300049896", new BigDecimal("100.00"), 25857623);
		createDummyOutgoingPayment("John Muigai", "+254720123426",
				"1302000034896", new BigDecimal("500.00"), 24343423);
		createDummyOutgoingPayment("Phanice Nafula", "+254720145345",
				"1302000500896", new BigDecimal("4200.00"), 43493444);
		createDummyOutgoingPayment("Charlene Nyambura", "+254720145345",
				"1302000433896", new BigDecimal("9320.60"), 23432674);
		createDummyOutgoingPayment("Tangus Koech", "+254720145445",
				"1302000004296", new BigDecimal("3000.40"), 42400255);
		createDummyOutgoingPayment("Peter Kamau", "+254724555434",
				"1302000233596", new BigDecimal("3005.60"), 24373423);
		createDummyOutgoingPayment("Isiah Mwiki", "+254720123435",
				"1302567149436", new BigDecimal("5400.66"), 108949744);
		createDummyOutgoingPayment("Kimani Karao", "+254720347344",
				"1302002333496", new BigDecimal("590.00"), 00425425);
		createDummyOutgoingPayment("Lowuya Lamini", "+254720455344",
				"1300000382896", new BigDecimal("600.00"), 265373423);
		createDummyOutgoingPayment("Angela Koki", "+254720999334",
				"1302000233896", new BigDecimal("1300.00"), 872243234);
		createDummyOutgoingPayment("Isiah Mwiki", "+254720785334",
				"1302038720896", new BigDecimal("400.00"), 232363547);
		createDummyOutgoingPayment("Roy Owino", "+254720487354",
				"1302003230896", new BigDecimal("1350.00"), 23355753);
		createDummyOutgoingPayment("Wambui Waweru", "+254720113344",
				"1302002323896", new BigDecimal("6600.00"), 4323425);
		createDummyOutgoingPayment("Lavendar Akoth", "+254724666364",
				"1302000003896", new BigDecimal("7000.34"), 234327443);
	}

	public AccountDao getAccountDao() {
		return this.accountDao;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	public CustomFieldDao getCustomFieldDao() {
		return this.customFieldDao;
	}

	public IncomingPaymentDao getIncomingPaymentDao() {
		return this.incomingPaymentDao;
	}

	public OutgoingPaymentDao getOutgoingPaymentDao() {
		return this.outgoingPaymentDao;
	}
}
