package org.creditsms.plugins.paymentview;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.springframework.context.ApplicationContext;

public class DemoData {
	private ApplicationContext ctx;
	private ClientDao clientDao;
	private CustomFieldDao customFieldDao;
	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private AccountDao accountDao;
	private ServiceItemDao serviceItemDao;
	private TargetDao targetDao;

	private DemoData(ApplicationContext ctx) {
		this.ctx = ctx;
	}
	
//> DUMMY DATA CREATION
	
	/*private Client createDummyClient(String name, String phoneNumber,
		String[] accountNumbers) {
		String[] names = name.split(" ");
		
		Client c = new Client(names[0], names[1], phoneNumber);
		getClientDao().saveClient(c);
		Account a = null;
		try {
			for (String accountNumber : accountNumbers) { 
				a = getAccountDao().getAccountByAccountNumber(accountNumber);
				if(a == null) {
					a = new Account(accountNumber);
					a.setClient(c);
					a.setActiveAccount(true);
					getAccountDao().saveAccount(a);
				}
				c.addAccount(a);
				a.setClient(getClientDao().getClientById(c.getId()));
				getAccountDao().updateAccount(a);
			}
			getClientDao().updateClient(c);
		} catch (Throwable e) {
			System.out.println(c);
			System.out.println(a);
			e.printStackTrace();
		}
		return c;
	}*/
	
	private Client createDummyClient(String name, String phoneNumber) throws DuplicateKeyException {
			String[] names = name.split(" ");
			Client c = new Client(names[0], names[1], phoneNumber);
			getClientDao().saveClient(c);
			
			Account account = new Account(createAccountNumber(),c,false,true);
			getAccountDao().saveAccount(account);
			return c;
	}
	
	public String createAccountNumber(){
		int accountNumberGenerated = getAccountDao().getAccountCount()+1;
		String accountNumberGeneratedStr = String.format("%05d", accountNumberGenerated);
		while (getAccountDao().getAccountByAccountNumber(accountNumberGeneratedStr) != null){
			accountNumberGeneratedStr = String.format("%05d", ++ accountNumberGenerated);
		}
		return accountNumberGeneratedStr;
	}
	
	private void createDummyIncomingPayment(String confirmationCode, String paymentBy,
			String phoneNumber, String timePaid, BigDecimal amountPaid,
			String accountNumber, Target target) {
		IncomingPayment i = new IncomingPayment();
		i.setAmountPaid(amountPaid);
		Account myAcc = getAccountDao().getAccountByAccountNumber(accountNumber);
		i.setAccount(myAcc);
		i.setConfirmationCode(confirmationCode);
		i.setPaymentBy(paymentBy);
		i.setPhoneNumber(phoneNumber);
		i.setTarget(target);
		i.setTimePaid(new Date(Long.parseLong(timePaid)));
		i.setActive(true);
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
		Client myClient = getClientDao().getClientByPhoneNumber(phoneNumber);
		o.setClient(myClient);
	
		o.setTimePaid(new Date(Long.parseLong(timePaid)));
		try {
			//getOutgoingPaymentDao().saveOutgoingPayment(o);
		} catch (Exception e) {
			System.out.println(o);
			System.out.println(myAcc);
			e.printStackTrace();
		}
	
	}
	
	/*private void createDummyServiceItem_Targets(String targetName, String amount, String[][] serviceItems){
		ServiceItem s = new ServiceItem();
		s.setAmount(new BigDecimal(amount));
		s.setTargetName(targetName);
		 	
		getServiceItemDao().saveServiceItem(s);
		
		for(String[] serviceItem : serviceItems){
			createDummyTargets(s, serviceItem[0], serviceItem[1], serviceItem[2]);
		}
	}*/
	
	private void createDummyServiceItem_Targets(String targetName, String amount){
		ServiceItem s = new ServiceItem();
		s.setAmount(new BigDecimal(amount));
		s.setTargetName(targetName);
		 	
		getServiceItemDao().saveServiceItem(s);
	}
	
	/**
	 * @throws DuplicateKeyException 
	 * 
	 */
	private void createDummyData() throws DuplicateKeyException{
		// Create dummy clients
		
		createDummyClient("Kim Wangare", "+254701103438");
		createDummyClient("John Kamau", "+254720547355");
		createDummyClient("Wekalao Matanda", "+254725452345");
		createDummyClient("Ismael Koli", "+254720445345");
		createDummyClient("Alice Wayua", "+254720459345");
		createDummyClient("Laura Agutu", "+254720546415");
		createDummyClient("Wangunyu Maigua", "+254724504645");
		createDummyClient("Stephen Kalungi", "+254725044545");
		createDummyClient("Mitawi Kisio", "+254724630345");
		createDummyClient("Angela Koki", "+254724634495");
		createDummyClient("Edith Khalai", "+254726745415");
		createDummyClient("Batista Omondi", "+254726712345");
		createDummyClient("Harry Mwabare", "+254725702345");
		createDummyClient("Wambui Waweru", "+254720144545");
		createDummyClient("Lavender Akoth", "+254725565345");
		createDummyClient("Sammy Kitonyi", "+254724412345");

		createDummyServiceItem_Targets("Solar Panel", "120000");
		createDummyServiceItem_Targets("Solar Generator", "320000");
		createDummyServiceItem_Targets("Bore Hole", "10000");
		
		//createDummyTargets()
		
		ServiceItem si = new ServiceItem();
		si = getServiceItemDao().getServiceItemsByName("Solar Panel").get(0);
		
		Calendar calendar1 = Calendar.getInstance();
		calendar1.set(Calendar.HOUR_OF_DAY, 0);  
		calendar1.set(Calendar.MINUTE, 0);  
		calendar1.set(Calendar.SECOND, 0);  
		calendar1.set(Calendar.MILLISECOND, 0);
		Date startDate = calendar1.getTime();
		
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.DATE, 14);
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		calendar.set(Calendar.MILLISECOND, 0); 
		Date endDate = calendar.getTime();

		Client clnt = getClientDao().getClientByPhoneNumber("+254701103438");
		String accountNumber = createAccountNumber();
		Target tgt = createDummyTargets(si,createDummyAccount(clnt, accountNumber), startDate, endDate);
		
		createDummyIncomingPayment("BSD454494", "Alice Wangare", "+254701103438", "1300560000000", new BigDecimal("4500.00"), accountNumber, tgt);
		createDummyIncomingPayment("BSD45D594", "Alice Wangare", "+254701103438", "1300560000100", new BigDecimal("1300.00"), accountNumber, tgt);
		createDummyIncomingPayment("BSDSFJ94F", "Alice Wangare", "+254701103438", "1300560000200", new BigDecimal("1200.00"), accountNumber, tgt);
		createDummyIncomingPayment("BSD4SDF94", "Alice Wangare", "+254701103438", "1300560200300", new BigDecimal("7400.00"), accountNumber, tgt);

		Client clnt1 = getClientDao().getClientByPhoneNumber("+254720547355");
		accountNumber = createAccountNumber();
		Target tgt1 = createDummyTargets(si,createDummyAccount(clnt1, accountNumber), startDate, endDate);
		
		createDummyIncomingPayment("BSD4SDFGF", "John Kamau", "+254720547355", "1300560000000", new BigDecimal("14500.00"), accountNumber, tgt1);
		createDummyIncomingPayment("BS3534DDF", "John Kamau", "+254720547355", "1300560000100", new BigDecimal("11300.00"), accountNumber, tgt1);
		createDummyIncomingPayment("BSGSD78SF", "John Kamau", "+254720547355", "1300560000200", new BigDecimal("11200.00"), accountNumber, tgt1);
		createDummyIncomingPayment("BSSDFD5DF", "John Kamau", "+254720547355", "1300560200300", new BigDecimal("37400.00"), accountNumber, tgt1);
		
		Client clnt2 = getClientDao().getClientByPhoneNumber("+254725452345");
		accountNumber = createAccountNumber();
		Target tgt2 = createDummyTargets(si,createDummyAccount(clnt2, accountNumber), startDate, endDate);
		
	}
	
	private Account createDummyAccount(Client client, String accountNumber) {
	
		Account ac = null;
		try {
			ac = new Account(accountNumber,false);
			ac.setClient(client);
			ac.setActiveAccount(true);
			getAccountDao().saveAccount(ac);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return ac;
	}
	
	private Target createDummyTargets(ServiceItem targetItem, Account accnt, Date startDate, Date endDate){
		Target t = new Target();
		t.setAccount(accnt);
		try { 
			t.setStartDate(startDate);
			t.setEndDate(endDate);
			t.setCompletedDate(null);
			t.setServiceItem(targetItem);
			getTargetDao().saveTarget(t);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return t;
	}
	
	public AccountDao getAccountDao() {
		if (accountDao == null){
			accountDao = (AccountDao) ctx.getBean("accountDao");
		}
		return accountDao;
	}
	
	public ClientDao getClientDao() {
		if (clientDao == null){
			clientDao = (ClientDao) ctx.getBean("clientDao");
		}
		return clientDao;
	}
	
	public CustomFieldDao getCustomFieldDao() {
		if (customFieldDao == null){
			customFieldDao = (CustomFieldDao) ctx.getBean("customFieldDao");
		}
		return customFieldDao;
	}
	
	public IncomingPaymentDao getIncomingPaymentDao() {
		if (incomingPaymentDao == null){
			incomingPaymentDao = (IncomingPaymentDao) ctx.getBean("incomingPaymentDao");
		}
		return incomingPaymentDao;
	}
	
	public ServiceItemDao getServiceItemDao() {
		if (serviceItemDao == null){
			serviceItemDao = (ServiceItemDao) ctx.getBean("serviceItemDao");
		}
		return serviceItemDao;
	}
	
	public OutgoingPaymentDao getOutgoingPaymentDao() {
		if (outgoingPaymentDao == null){
			outgoingPaymentDao = (OutgoingPaymentDao) ctx.getBean("outgoingPaymentDao");
		}
		return outgoingPaymentDao;
	}
	
	private TargetDao getTargetDao() {
		if (targetDao == null){
			targetDao = (TargetDao) ctx.getBean("targetDao");
		}
		return targetDao;
	}

	
//> STATIC METHODS
	public static void createDemoData(ApplicationContext applicationContext) throws DuplicateKeyException{
		DemoData d = new DemoData(applicationContext);
		d.createDummyData();
	}
}

/*
 * 
 createDummyIncomingPayment("Phanice Nafula", "+25472014545",
		"1300560020400", new BigDecimal("500.00"), "43493444");
createDummyIncomingPayment("Charlene Nyambura", "+254720762345",
		"1300560000500", new BigDecimal("7030.60"), "23432674");
createDummyIncomingPayment("Tangus Koech", "+254721012326",
		"1300560200600", new BigDecimal("3000.00"), "42400255");
createDummyIncomingPayment("Peter Kamau", "+254724555345",
		"1300560200700", new BigDecimal("100.00"), "24373423");
createDummyIncomingPayment("Isiah Mwiki", "+25472012345",
		"1300560000800", new BigDecimal("5000.00"), "108949744");
createDummyIncomingPayment("Kimani Karao", "+254720347345",
		"1300560000900", new BigDecimal("1000.00"), "00425425");
createDummyIncomingPayment("Lowuya Lamini", "+254720455345",
		"1300560001000", new BigDecimal("1200.00"), "434147425");
		
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
		
*/