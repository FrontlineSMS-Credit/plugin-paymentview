package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Roy
 */
public class IncomingPaymentIntergrationTest extends HibernateTestCase {
	@Autowired                     
	HibernateClientDao hibernateClientDao;
	@Autowired
	HibernateAccountDao hibernateAccountDao;
	@Autowired
	HibernateIncomingPaymentDao hibernateIncomingPaymentDao;
	@Autowired    
	HibernateTargetDao hibernateTargetDao;
	
	public void testSetup() {
		assertNotNull(hibernateClientDao);
		assertNotNull(hibernateAccountDao);
		assertNotNull(hibernateIncomingPaymentDao);
		assertNotNull(hibernateTargetDao);
	}
	
	public void testSavingIncomingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();

		IncomingPayment ip = createIncomingPayment("0725000000", "2000", "bob");
		hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		assertEquals(1, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
		assertEquals("00001", ip.getAccount().getAccountNumber());
	}

	public void testDeletingIncomingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		createAndSaveIncomingPayment("0726000000", "2100", "+3454356435", 1);
		hibernateIncomingPaymentDao.deleteIncomingPayment(getIncomingPayment());
		assertEquals(0, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
	}

	
	public void testGettingActiveIncoming() throws DuplicateKeyException{
		assertEmptyDatabases();
		final String accountNumber1 = "0001";
		Account ac1 = createAndSaveAccount(accountNumber1, 1, null);
		createAndSaveIncomingPayment("0722000000", "2300", "Mr Kikalo",ac1, 1);
		final String accountNumber2 = "0002";
		Account ac2 = createAndSaveAccount(accountNumber2, 2, null);
		createAndSaveIncomingPayment("0721000000","2600","Ian Mbogua",ac2, 2);
		final String accountNumber3 = "0003";
		Account ac3 = createAndSaveAccount(accountNumber3, 3, null);
		createAndSaveInactiveIncomingPayment("0733000000", "15000000", "Mr. Sang",ac3,3);
		assertEquals(2, hibernateIncomingPaymentDao.getActiveIncomingPayments().size());
	}
	
	public void testGettingIncomingPaymentById() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		createAndSaveIncomingPayment("0722000000", "2300", "Mr Kikalo", 1);
		long ipId = this.hibernateIncomingPaymentDao.getAllIncomingPayments().get(0).getId();
		assertEquals(new BigDecimal("2300"), hibernateIncomingPaymentDao.getActiveIncomingPaymentById(ipId).getAmountPaid());
	}
	
	public void testGettingIncomingPaymentByPhoneNumber() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		createAndSaveIncomingPayment("0721000000","2600","Ian Mbogua", 1);
		String phoneNumber = hibernateIncomingPaymentDao.getActiveIncomingPayments().get(0).getPhoneNumber();
		assertEquals(new BigDecimal("2600"), hibernateIncomingPaymentDao.getActiveIncomingPaymentsByPhoneNo(phoneNumber).get(0).getAmountPaid());
	}
	
	public void testGettingIncomingPaymentByPayer() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		createAndSaveIncomingPayment("0723000000","2850","Mr. Renyenjes",1);
		String paidBy = hibernateIncomingPaymentDao.getActiveIncomingPayments().get(hibernateIncomingPaymentDao.getActiveIncomingPayments().size()-1).getPaymentBy();
		assertEquals(new BigDecimal("2850"), hibernateIncomingPaymentDao.getActiveIncomingPaymentsByPayer(paidBy).get(hibernateIncomingPaymentDao.getActiveIncomingPaymentsByPayer(paidBy).size()-1).getAmountPaid());
	}
	
	public void testGettingIncomingPaymentByAccountNumber() throws DuplicateKeyException{
		final String accountNumber = "4201";
		Account ac = createAndSaveAccount(accountNumber, 1, null);
		createAndSaveIncomingPayment("0733000000", "12000000", "Mr. Sang", ac, 1);
		List<IncomingPayment> actualIncomingPayments = hibernateIncomingPaymentDao.getActiveIncomingPaymentsByAccountNumber(accountNumber);
		assertEquals(1, actualIncomingPayments.size());
		
		BigDecimal actualAmountPaid = actualIncomingPayments.get(0).getAmountPaid();
		assertEquals(new BigDecimal("12000000"), actualAmountPaid);
		assertEquals(accountNumber, actualIncomingPayments.get(0).getAccount().getAccountNumber());
	}
	
	public void testGettingActiveIncomingPaymentByAccountNumber() throws DuplicateKeyException{
		final String accountNumber = "4201";
		Account ac = createAndSaveAccount(accountNumber, 1, null);
		createAndSaveIncomingPayment("0733000000", "12000000", "Mr. Sang", ac, 1);
		createAndSaveInactiveIncomingPayment("0733000000", "15000000", "Mr. Sang", ac, 2);
		
		List<IncomingPayment> actualIncomingPayments = hibernateIncomingPaymentDao.getActiveIncomingPaymentsByAccountNumber(accountNumber);
		assertEquals(1, actualIncomingPayments.size());
		
		BigDecimal actualAmountPaid = actualIncomingPayments.get(0).getAmountPaid();
		assertEquals(new BigDecimal("12000000"), actualAmountPaid);
		assertEquals(accountNumber, actualIncomingPayments.get(0).getAccount().getAccountNumber());
	}
	
	public void testGettingIncomingPaymentsByUserId() throws DuplicateKeyException{
		Client c = createAndSaveClient("0734000000", "Hamisi",1);
		Account ac = createAndSaveAccount("000201", 1, c);
		
		createAndSaveIncomingPayment("0733000000", "24500", "Mr. Sang", ac, 1);
		createAndSaveInactiveIncomingPayment("0733000000", "15000000", "Mr. Sang", ac, 2);
		long clientId = hibernateAccountDao.getAllAcounts().get(0).getClient().getId();
		assertEquals(new BigDecimal("24500"), hibernateIncomingPaymentDao.getActiveIncomingPaymentByClientId(clientId).get(0).getAmountPaid());
	}
	
	private Target createTarget(Account ac, String startDateStr, String endDateStr){
		Target tgt = new Target();
		tgt.setAccount(ac);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try { 
			Date startDate = df.parse(startDateStr);  
			Date endDate = df.parse(endDateStr);
			tgt.setStartDate(startDate);
			tgt.setEndDate(endDate);
			tgt.setCompletedDate(null);
			tgt.setTotalTargetCost(new BigDecimal("4500"));
			hibernateTargetDao.saveTarget(tgt);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tgt;
	}
	
	private void assertEmptyDatabases(){
		assertEquals(0, hibernateClientDao.getAllClients().size());
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
		assertEquals(0, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
		assertEquals(0, hibernateTargetDao.getAllActiveTargets().size());
	}
	
	private void createAndSaveIncomingPayment(String phoneNumber, String amount, String by, Account account, int expectedPaymentCount) throws DuplicateKeyException {
		this.hibernateIncomingPaymentDao.saveIncomingPayment(createIncomingPayment(phoneNumber, amount, by, account));
		assertEquals(expectedPaymentCount, this.hibernateIncomingPaymentDao.getActiveIncomingPayments().size());
	}
	
	private void createAndSaveInactiveIncomingPayment(String phoneNumber, String amount, String by, Account account, int expectedPaymentCount) throws DuplicateKeyException {
		IncomingPayment inactiveIncomingPayment = createIncomingPayment(phoneNumber, amount, by, account);
		inactiveIncomingPayment.setActive(false);
		this.hibernateIncomingPaymentDao.saveIncomingPayment(inactiveIncomingPayment);
		assertEquals(expectedPaymentCount, this.hibernateIncomingPaymentDao.getAllIncomingPayments().size());
	}

	private void createAndSaveIncomingPayment(String phoneNumber, String amount,
			String by, int expectedPaymentCount) throws DuplicateKeyException {
		createAndSaveIncomingPayment(phoneNumber, amount, by, null, expectedPaymentCount);
	}

	private IncomingPayment createIncomingPayment(String phoneNumber, String amount,
			String by) throws DuplicateKeyException {
		return createIncomingPayment(phoneNumber, amount, by, null);
	}
	
	private IncomingPayment createIncomingPayment(String phoneNumber, String amount,
			String by, Account account) throws DuplicateKeyException {
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber(phoneNumber);
		ip.setAmountPaid(new BigDecimal(amount));
		ip.setPaymentBy(by);
		ip.setActive(true);
		ip.setTarget(createTarget(account,"24/04/2011", "24/07/2011"));
		if(account != null) {
			ip.setAccount(account);
		} else {
			Client client= createAndSaveClient(phoneNumber,by,1);
			Account acc = createAndSaveAccount("00001",1,client);
			ip.setAccount(acc);
		}
		return ip;
	}
	
	private IncomingPayment getIncomingPayment(){
		return this.hibernateIncomingPaymentDao.getActiveIncomingPayments().get(0);
	}
	
	private Account createAndSaveAccount(String accountNumber, int expectedAccountCount, Client client) throws DuplicateKeyException {
		Account ac = new Account();
		ac.setAccountNumber(accountNumber);
		ac.setActiveAccount(true);
		if(client != null) ac.setClient(client);
		hibernateAccountDao.saveAccount(ac);
		assertEquals(expectedAccountCount, hibernateAccountDao.getAllAcounts().size());
		return ac;
	}	
	
	private Client createAndSaveClient(String phnNumber, String firstName, int expectedClientCount) throws DuplicateKeyException{
		Client c = new Client();
		c.setPhoneNumber(phnNumber);
		c.setFirstName(firstName);
		hibernateClientDao.saveClient(c);
		assertEquals(expectedClientCount, hibernateClientDao.getAllClients().size());
		return c;
	}
	
	public void testGetAll() throws DuplicateKeyException {
		// given
		createIncomingPayments();
		
		// when
		List<IncomingPayment> allIncomingPayments = hibernateIncomingPaymentDao.
		getAllIncomingPayments();
		
		// then
		assertEquals(55, allIncomingPayments.size());
	}
	
	public void testGettingActiveIncomingPayments() throws DuplicateKeyException{
		// given
		createIncomingPayments();
		
		// when
		List<IncomingPayment> firstPageOfIncomingPayments = hibernateIncomingPaymentDao.
		getActiveIncomingPayments(0, 50);
		
		//then
		assertEquals(50, firstPageOfIncomingPayments.size());
	}
	
	public void testGettingActiveIncomingPaymentsOtherPages() throws DuplicateKeyException{
		// given 
		createIncomingPayments();
		
		// when
		List<IncomingPayment> secondPageOfIncomingPayments = hibernateIncomingPaymentDao.
		getActiveIncomingPayments(50, 50);

		//then
		assertEquals(5, secondPageOfIncomingPayments.size());
	}
	
	private void createIncomingPayments() throws DuplicateKeyException {
		Client c = createAndSaveClient("0734000000", "John Doe",1);
		Account ac = createAndSaveAccount("000201", 1, c);
		
		for(int ip = 0; ip < 55; ip++) {
			createAndSaveIncomingPayment("0733000000", "24500", "John Doe", ac, ip+1);
		}
	}
}
