package org.creditsms.plugins.paymentview;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.data.repository.TargetServiceItemDao;
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
	private TargetServiceItemDao targetServiceItemDao;

	private DemoData(ApplicationContext ctx) {
		this.ctx = ctx;
	}
		
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

	private Calendar setStartOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	private Calendar setEndOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 24);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, -1);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
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

		Calendar calStartDat = Calendar.getInstance();
		calStartDat.add(Calendar.MONTH, -11);  
		calStartDat.add(Calendar.DATE, 1);
		calStartDat = setStartOfDay(calStartDat);
		Date startDate = calStartDat.getTime();
		
		Calendar calEndDate = Calendar.getInstance();
		calEndDate.add(Calendar.MONTH, 7);  
		calEndDate = setEndOfDay(calEndDate);
		Date endDate = calEndDate.getTime();

		Client clnt = getClientDao().getClientByPhoneNumber("+254701103438");
		String accountNumber = createAccountNumber();
		Target tgt = createDummyTargets(new BigDecimal("120000"),createDummyAccount(clnt, accountNumber), startDate, endDate, "Solar Panel");
		
		createDummyIncomingPayment("BSD454494", "Alice Wangare", "+254701103438", "1300560000000", new BigDecimal("4500.00"), accountNumber, tgt);
		createDummyIncomingPayment("BSD45D594", "Alice Wangare", "+254701103438", "1300560000100", new BigDecimal("1300.00"), accountNumber, tgt);
		createDummyIncomingPayment("BSDSFJ94F", "Alice Wangare", "+254701103438", "1300560000200", new BigDecimal("1200.00"), accountNumber, tgt);
		createDummyIncomingPayment("BSD4SDF94", "Alice Wangare", "+254701103438", "1300560200300", new BigDecimal("7400.00"), accountNumber, tgt);

		Client clnt1 = getClientDao().getClientByPhoneNumber("+254720547355");
		accountNumber = createAccountNumber();
		Target tgt1 = createDummyTargets(new BigDecimal("87000"),createDummyAccount(clnt1, accountNumber), startDate, endDate, "Water Pump");
		
		createDummyIncomingPayment("BSD4SDFGF", "John Kamau", "+254720547355", "1300560000000", new BigDecimal("14500.00"), accountNumber, tgt1);
		createDummyIncomingPayment("BS3534DDF", "John Kamau", "+254720547355", "1300560000100", new BigDecimal("11300.00"), accountNumber, tgt1);
		createDummyIncomingPayment("BSGSD78SF", "John Kamau", "+254720547355", "1300560000200", new BigDecimal("11200.00"), accountNumber, tgt1);
		createDummyIncomingPayment("BSSDFD5DF", "John Kamau", "+254720547355", "1300560200300", new BigDecimal("37400.00"), accountNumber, tgt1);
	
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
	
	private Target createDummyTargets(BigDecimal totalTargetCost, 
			Account accnt, Date startDate, Date endDate, String itemName){
		Target t = new Target();
		t.setAccount(accnt);
		try { 
			t.setStartDate(startDate);
			t.setEndDate(endDate);
			t.setCompletedDate(null);
			t.setTotalTargetCost(totalTargetCost);
			getTargetDao().saveTarget(t);
			
			createTargetServiceItem(t, itemName, totalTargetCost);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return t;
	}
	
	private void createTargetServiceItem(Target t, String targetName, 
			BigDecimal amount) throws DuplicateKeyException{
		
		TargetServiceItem tsi = new TargetServiceItem();
		tsi.setAmount(amount);
		tsi.setServiceItem(createServiceItem(targetName, amount));
		tsi.setServiceItemQty(1);
		tsi.setTarget(t);	
		getTargetServiceItemDao().saveTargetServiceItem(tsi);
	}
	
	private ServiceItem createServiceItem(String targetName, BigDecimal amount){
		ServiceItem s = new ServiceItem();
		s.setAmount(amount);
		s.setTargetName(targetName);
		getServiceItemDao().saveServiceItem(s);
		return serviceItemDao.getServiceItemsByName(targetName).get(0);
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

	private TargetServiceItemDao getTargetServiceItemDao() {
		if(targetServiceItemDao == null){
			targetServiceItemDao = (TargetServiceItemDao) ctx.getBean("targetServiceItemDao");
		}
		return targetServiceItemDao;
	}
//> STATIC METHODS
	public static void createDemoData(ApplicationContext applicationContext) throws DuplicateKeyException{
		DemoData d = new DemoData(applicationContext);
		d.createDummyData();
	}
}