package org.creditsms.plugins.paymentview.data.repository.hibernate;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

public class IncomingPaymentIntergrationTest extends HibernateTestCase{
	@Autowired                     
	HibernateClientDao hibernateClientDao;
	@Autowired
	HibernateAccountDao hibernateAccountDao;
	@Autowired
	HibernateIncomingPaymentDao hibernateIncomingPaymentDao;
	
	public void testSetup() {
		assertNotNull(hibernateClientDao);
		assertNotNull(hibernateAccountDao);
		assertNotNull(hibernateIncomingPaymentDao);
	}
	
	public void testSavingIncomingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();

		IncomingPayment ip = createIncomingPayment("0725000000", new BigDecimal("2000"), "bob");
		hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		assertEquals(1, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
	}

	public void testDeletingIncomingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		IncomingPayment ip = createIncomingPayment("0726000000", new BigDecimal("2100"), "+3454356435");
		hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		assertEquals(1, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
		
		hibernateIncomingPaymentDao.deleteIncomingPayment(ip);
		assertEquals(0, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
	}
	
	public void testGettingIncomingPaymentById() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber("0722000000");
		ip.setAmountPaid(new BigDecimal("2300"));
		hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		long ipId = this.hibernateIncomingPaymentDao.getAllIncomingPayments().get(0).getId();
		
		assertEquals(new BigDecimal("2300"), hibernateIncomingPaymentDao.getIncomingPaymentById(ipId).getAmountPaid());
	}
	
	public void testGettingIncomingPaymentByPhoneNumber() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber("0721000000");
		ip.setAmountPaid(new BigDecimal("2600"));
		hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		String phoneNumber = hibernateIncomingPaymentDao.getAllIncomingPayments().get(hibernateIncomingPaymentDao.getAllIncomingPayments().size()-1).getPhoneNumber();

		assertEquals(new BigDecimal("2600"), hibernateIncomingPaymentDao.getIncomingPaymentsByPhoneNo(phoneNumber).get(hibernateIncomingPaymentDao.getIncomingPaymentsByPhoneNo(phoneNumber).size()-1).getAmountPaid());
	}
	
	public void testGettingIncomingPaymentByPayer() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber("0723000000");
		ip.setPaymentBy("Mr. Renyenjes");
		ip.setAmountPaid(new BigDecimal("2850"));
		hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		String paidBy = hibernateIncomingPaymentDao.getAllIncomingPayments().get(hibernateIncomingPaymentDao.getAllIncomingPayments().size()-1).getPaymentBy();
		
		assertEquals(new BigDecimal("2850"), hibernateIncomingPaymentDao.getIncomingPaymentsByPayer(paidBy).get(hibernateIncomingPaymentDao.getIncomingPaymentsByPayer(paidBy).size()-1).getAmountPaid());
	}
	
	public void testGettingIncomingPaymentByAccountNumber() throws DuplicateKeyException{
		Account ac = new Account();
		
		ac.setAccountNumber(4201);
		hibernateAccountDao.saveUpdateAccount(ac);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
		
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber("0733000000");
		ip.setAmountPaid(new BigDecimal("12000000"));
		ip.setAccount(ac);
		ip.setPaymentBy("Mr Sang");
		hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		assertEquals(1, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
		
		long accountNumber = hibernateAccountDao.getAllAcounts().get(hibernateAccountDao.getAllAcounts().size()-1).getAccountNumber();
		assertEquals(new BigDecimal("12000000"),hibernateIncomingPaymentDao.getIncomingPaymentsByAccountNumber(accountNumber).get(hibernateIncomingPaymentDao.getIncomingPaymentsByAccountNumber(accountNumber).size()-1).getAmountPaid());
		
	}
	
	public void testGettingIncomingPaymentsByUserId() throws DuplicateKeyException{
		
		Client c = new Client();
		c.setPhoneNumber("0734000000");
		c.setFirstName("Hamisi");
		hibernateClientDao.saveUpdateClient(c);
		assertEquals(1, hibernateClientDao.getAllClients().size());
		
		Account ac = new Account();
		ac.setAccountNumber(000201);
		ac.setClient(c);
		hibernateAccountDao.saveUpdateAccount(ac);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
		
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber("0733000000");
		ip.setAmountPaid(new BigDecimal("24500"));
		ip.setPaymentBy("Mr Sang");
		ip.setAccount(ac);
		hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		assertEquals(1, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
		
		long clientId = hibernateAccountDao.getAllAcounts().get(hibernateAccountDao.getAllAcounts().size()-1).getClient().getId();
		
		assertEquals(new BigDecimal("24500"),hibernateIncomingPaymentDao.getIncomingPaymentByClientId(clientId).get(hibernateIncomingPaymentDao.getIncomingPaymentByClientId(clientId).size()-1).getAmountPaid());
		
	}
	
	private void assertEmptyDatabases(){
		assertEquals(0, hibernateClientDao.getAllClients().size());
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
		assertEquals(0, hibernateIncomingPaymentDao.getAllIncomingPayments().size());
	}

	private IncomingPayment createIncomingPayment(String phoneNumber, BigDecimal amount,
			String by) {
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber(phoneNumber);
		ip.setAmountPaid(amount);
		ip.setPaymentBy(by);
		return ip;
	}	
}
