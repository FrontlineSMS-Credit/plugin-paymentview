package org.creditsms.plugins.paymentview;

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
import org.springframework.context.ApplicationContext;

public class DemoData {
	private ApplicationContext ctx;

	private DemoData(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	
//> DUMMY DATA CREATION
	
	private Client createDummyClient(String name, String phoneNumber,
		String[] accountNumbers) {
		String[] names = name.split(" ");
		
		Client c = new Client(names[0], names[1], phoneNumber);
		
		for (String accountNumber : accountNumbers) { 
			Account a = getAccountDao().getAccountByAccountNumber(accountNumber);
			if(a == null) {
				try {
					a = new Account(accountNumber);
					getAccountDao().saveAccount(a);
				} catch (DuplicateKeyException e) {
					System.out.println(a);
					e.printStackTrace();
					a = null;
				}
			}
			if(a != null) {
				c.addAccount(a);	
			}
		}
		
		try {
			getClientDao().saveClient(c);
		} catch (DuplicateKeyException e) {
			System.out.println(c);
			e.printStackTrace();
		}
		
		return c;
	}
	
	private void createDummyIncomingPayment(String paymentBy,
			String phoneNumber, String timePaid, BigDecimal amountPaid,
			String accountNumber) {
		IncomingPayment i = new IncomingPayment();
		i.setAmountPaid(amountPaid);
		Account myAcc = getAccountDao().getAccountByAccountNumber(accountNumber);
		i.setAccount(myAcc);
		i.setPaymentBy(paymentBy);
		i.setPhoneNumber(phoneNumber);
		i.setTimePaid(new Date(Long.parseLong(timePaid)));
		try {
			getIncomingPaymentDao().saveIncomingPayment(i);
		} catch (Exception e) {// DuplicateKeyException
			System.out.println(i);
			System.out.println(myAcc);
			e.printStackTrace();
		}
	
	}
	
	private void createDummyOutgoingPayment(String paymentTo,
			String phoneNumber, String timePaid, BigDecimal amountPaid,
			String accountNumber) {
		OutgoingPayment o = new OutgoingPayment();
		o.setAmountPaid(amountPaid);
		Account myAcc = getAccountDao().getAccountByAccountNumber(accountNumber);
		o.setAccount(myAcc);
		o.setPhoneNumber(phoneNumber);
	
		o.setTimePaid(new Date(Long.parseLong(timePaid)));
		try {
			getOutgoingPaymentDao().saveOutgoingPayment(o);
		} catch (Exception e) {
			System.out.println(o);
			System.out.println(myAcc);
			e.printStackTrace();
		}
	
	}
	
	private void createDummyData() {
		// Create dummy clients
		createDummyClient("Alice Wangare", "+25472457645", new String[] {
				"4343625", "247362623" });
		createDummyClient("John Kamau", "+254720547355", new String[] { "82666633",
				"23233423" });
		createDummyClient("Wekalao Matanda", "+254725452345", new String[] {
				"4323425", "234327443" });
		createDummyClient("Ismael Koli", "+254720445345", new String[] {
				"434147425", "2439423" });
		createDummyClient("Alice Wayua", "+25472045345", new String[] { "81672323",
				"25467623" });
		createDummyClient("Laura Agutu", "+25472054645", new String[] { "88882672",
				"233854323" });
		createDummyClient("Wangunyu Maigua", "+25472454645", new String[] {
				"454264435", "265373423" });
		createDummyClient("Stephen Kalungi", "+25472544545", new String[] {
				"432957346", "23643523" });
		createDummyClient("Mitawi Kisio", "+25472463345", new String[] {
				"430002454", "2334423" });
		createDummyClient("Angela Koki", "+25472463445", new String[] { "9923623",
				"232255000" });
		createDummyClient("Edith Khalai", "+25472674545", new String[] {
				"433599009", "23355753" });
		createDummyClient("Juma Omondi", "+254726712345", new String[] {
				"432592853", "57437263" });
		createDummyClient("Harry Mwabare", "+25472572345", new String[] {
				"459732535", "37334423" });
		createDummyClient("Wambui Waweru", "+254720144545", new String[] {
				"443600833", "33736423" });
		createDummyClient("Lavender Akoth", "+254725565345", new String[] {
				"434584239", "36734723" });
		createDummyClient("Sammy Kitonyi", "+254724412345", new String[] {
				"343763254", "37356723" });
		createDummyClient("Isiah Muchene", "+254723312235", new String[] {
				"463537445", "59536723" });
		createDummyClient("Onesmus Mukewa", "+25473612345", new String[] {
				"50962324", "25798563" });
		createDummyClient("Roy Owino", "+25474512345", new String[] { "88851243",
				"232259896" });
		createDummyClient("Justin Mwakidedi", "+25475412345", new String[] {
				"232492474", "25857623" });
		createDummyClient("Mario Mwangi", "+25472542345", new String[] {
				"434322934", "23278523" });
		createDummyClient("Peter Kamau", "+254724555345", new String[] {
				"343972222", "24373423" });
		createDummyClient("Phanice Nafula", "+254724552345", new String[] {
				"872243234", "27434523" });
		createDummyClient("Isiah Mwiki", "+25472012345", new String[] { "77175292",
				"108949744" });
		createDummyClient("Kimani Karao", "+254720347345", new String[] {
				"00425425", "22456483" });
		createDummyClient("Lowuya Lamini", "+254720455345", new String[] {
				"00023225", "232363547" });
		createDummyClient("Charlene Nyambura", "+25472762345", new String[] {
				"43330025", "23432674" });
		createDummyClient("Tangus Koech", "+25472012326", new String[] {
				"42400255", "24343423" });
		createDummyClient("Justus Matanda", "+25472014545", new String[] {
				"42547455", "43493444" });
	
		createDummyIncomingPayment("Isiah Muchene", "+254723312235",
				"1300560000000", new BigDecimal("4513.20"), "59536723");
		createDummyIncomingPayment("Ian Mukewa", "+25472762345",
				"1300560000100", new BigDecimal("300.00"), "23432674");
		createDummyIncomingPayment("Justin Mwakidedi", "+25475412345",
				"1300560000200", new BigDecimal("420.00"), "25857623");
		createDummyIncomingPayment("John Muigai", "+25472012326",
				"1300560200300", new BigDecimal("400.00"), "24343423");
		createDummyIncomingPayment("Phanice Nafula", "+25472014545",
				"1300560020400", new BigDecimal("500.00"), "43493444");
		createDummyIncomingPayment("Charlene Nyambura", "+25472014545",
				"1300560000500", new BigDecimal("7030.60"), "23432674");
		createDummyIncomingPayment("Tangus Koech", "+25472014545",
				"1300560200600", new BigDecimal("3000.00"), "42400255");
		createDummyIncomingPayment("Peter Kamau", "+254724555345",
				"1300560200700", new BigDecimal("100.00"), "24373423");
		createDummyIncomingPayment("Isiah Mwiki", "+25472012345",
				"1300560000800", new BigDecimal("5000.00"), "108949744");
		createDummyIncomingPayment("Kimani Karao", "+254720347345",
				"1300560000900", new BigDecimal("1000.00"), "00425425");
		createDummyIncomingPayment("Lowuya Lamini", "+254720455345",
				"1300560001000", new BigDecimal("1200.00"), "434147425");
	
		createDummyIncomingPayment("Angela Koki", "+254720999345",
				"1300564344000", new BigDecimal("4000.95"), "23278523");
		createDummyIncomingPayment("Isiah Mwiki", "+254720785345",
				"1300232333300", new BigDecimal("8800.00"), "233854323");
		createDummyIncomingPayment("Roy Owino", "+254720487545",
				"1302304299006", new BigDecimal("10000.00"), "25857623");
		createDummyIncomingPayment("Wambui Waweru", "+254720113445",
				"1302304299996", new BigDecimal("1780.00"), "232363547");
		createDummyIncomingPayment("Lavendar Akoth", "+254724666645",
				"1302304299996", new BigDecimal("2000.00"), "232255000");
	
		createDummyOutgoingPayment("Isiah Muchene", "+254723312233",
				"1302560000896", new BigDecimal("3000.00"), "59536723");
		createDummyOutgoingPayment("Ian Mukewa", "+254727623453",
				"1300000000896", new BigDecimal("4000.00"), "23432674");
		createDummyOutgoingPayment("Justin Mwakidedi", "+254754123445",
				"1302300049896", new BigDecimal("100.00"), "25857623");
		createDummyOutgoingPayment("John Muigai", "+254720123426",
				"1302000034896", new BigDecimal("500.00"), "24343423");
		createDummyOutgoingPayment("Phanice Nafula", "+254720145345",
				"1302000500896", new BigDecimal("4200.00"), "43493444");
		createDummyOutgoingPayment("Charlene Nyambura", "+254720145345",
				"1302000433896", new BigDecimal("9320.60"), "23432674");
		createDummyOutgoingPayment("Tangus Koech", "+254720145445",
				"1302000004296", new BigDecimal("3000.40"), "42400255");
		createDummyOutgoingPayment("Peter Kamau", "+254724555434",
				"1302000233596", new BigDecimal("3005.60"), "24373423");
		createDummyOutgoingPayment("Isiah Mwiki", "+254720123435",
				"1302567149436", new BigDecimal("5400.66"), "108949744");
		createDummyOutgoingPayment("Kimani Karao", "+254720347344",
				"1302002333496", new BigDecimal("590.00"), "00425425");
		createDummyOutgoingPayment("Lowuya Lamini", "+254720455344",
				"1300000382896", new BigDecimal("600.00"), "265373423");
		createDummyOutgoingPayment("Angela Koki", "+254720999334",
				"1302000233896", new BigDecimal("1300.00"), "872243234");
		createDummyOutgoingPayment("Isiah Mwiki", "+254720785334",
				"1302038720896", new BigDecimal("400.00"), "232363547");
		createDummyOutgoingPayment("Roy Owino", "+254720487354",
				"1302003230896", new BigDecimal("1350.00"), "23355753");
		createDummyOutgoingPayment("Wambui Waweru", "+254720113344",
				"1302002323896", new BigDecimal("6600.00"), "4323425");
		createDummyOutgoingPayment("Lavendar Akoth", "+254724666364",
				"1302000003896", new BigDecimal("7000.34"), "234327443");
	}
	
	public AccountDao getAccountDao() {
		return (AccountDao) ctx.getBean("accountDao");
	}
	
	public ClientDao getClientDao() {
		return (ClientDao) ctx.getBean("clientDao");
	}
	
	public CustomFieldDao getCustomFieldDao() {
		return (CustomFieldDao) ctx.getBean("customFieldDao");
	}
	
	public IncomingPaymentDao getIncomingPaymentDao() {
		return (IncomingPaymentDao) ctx.getBean("incomingPaymentDao");
	}
	
	public OutgoingPaymentDao getOutgoingPaymentDao() {
		return (OutgoingPaymentDao) ctx.getBean("outgoingPaymentDao");
	}

	
//> STATIC METHODS
	public static void createDemoData(ApplicationContext applicationContext) {
		DemoData d = new DemoData(applicationContext);
		d.createDummyData();
	}
}
