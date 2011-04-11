package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.junit.HibernateTestCase;

public class OutgoingPaymentIntergrationTest extends HibernateTestCase{
	@Autowired                     
	HibernateClientDao hibernateClientDao;
	@Autowired
	HibernateAccountDao hibernateAccountDao;
	@Autowired
	HibernateOutgoingPaymentDao hibernateOutgoingPaymentDao;
	
	public void testSetup() {
		assertNotNull(hibernateClientDao);
		assertNotNull(hibernateAccountDao);
		assertNotNull(hibernateOutgoingPaymentDao);
	}
	
	private void assertEmptyDatabases(){
		assertEquals(0, hibernateClientDao.getAllClients().size());
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
		assertEquals(0, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
	}

	public void testSavingOutgoingPayment(){
		assertEmptyDatabases();

		OutgoingPayment op = new OutgoingPayment();
		op.setPhoneNumber("0725000000");
		op.setAmountPaid(new BigDecimal("8000"));
		
		hibernateOutgoingPaymentDao.saveOrUpdateOutgoingPayment(op);
		assertEquals(1, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
	}
	
	public void testDeletingOutgoingPayment(){
		assertEmptyDatabases();
		
		OutgoingPayment op = new OutgoingPayment();
		op.setPhoneNumber("0725000000");
		op.setAmountPaid(new BigDecimal("8000"));
		hibernateOutgoingPaymentDao.saveOrUpdateOutgoingPayment(op);
		assertEquals(1, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
		
		hibernateOutgoingPaymentDao.deleteOutgoingPayment(op);
		assertEquals(0, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
	}
	
	public void testGettingOutgoingPaymentById(){
		assertEmptyDatabases();
		
		OutgoingPayment op = new OutgoingPayment();
		op.setPhoneNumber("0726000000");
		op.setAmountPaid(new BigDecimal("18000"));
		hibernateOutgoingPaymentDao.saveOrUpdateOutgoingPayment(op);
		assertEquals(1, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
		
		long opId = hibernateOutgoingPaymentDao.getAllOutgoingPayments().get(hibernateOutgoingPaymentDao.getAllOutgoingPayments().size()-1).getId();
		assertEquals(new BigDecimal("18000"), hibernateOutgoingPaymentDao.getOutgoingPaymentById(opId).getAmountPaid());
	}
	
	public void testGettingOutgoingPaymentByPhoneNumber(){
		assertEmptyDatabases();
		
		OutgoingPayment op = new OutgoingPayment();
		op.setPhoneNumber("0734000000");
		op.setAmountPaid(new BigDecimal("23000000"));
		hibernateOutgoingPaymentDao.saveOrUpdateOutgoingPayment(op);
		assertEquals(1,hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
		
		String phoneNumber = hibernateOutgoingPaymentDao.getAllOutgoingPayments().get(hibernateOutgoingPaymentDao.getAllOutgoingPayments().size()-1).getPhoneNumber();
	    assertEquals(new BigDecimal("23000000"), hibernateOutgoingPaymentDao.getOutgoingPaymentsByPhoneNo(phoneNumber).get(hibernateOutgoingPaymentDao.getOutgoingPaymentsByPhoneNo(phoneNumber).size()-1).getAmountPaid());
	
	}

	public void testGettingOutgoingPaymentsByAccountNumber(){
		assertEmptyDatabases();
		
		Account ac = new Account();
		ac.setAccountNumber(983);
		hibernateAccountDao.saveUpdateAccount(ac);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
		
		OutgoingPayment op = new OutgoingPayment();
		op.setAccount(ac);
		op.setPhoneNumber("0739000000");
		op.setAmountPaid(new BigDecimal("900000"));
		hibernateOutgoingPaymentDao.saveOrUpdateOutgoingPayment(op);
		assertEquals(1, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
		
		long accNumber = hibernateOutgoingPaymentDao.getAllOutgoingPayments().get(hibernateOutgoingPaymentDao.getAllOutgoingPayments().size()-1).getAccount().getAccountNumber();
		assertEquals(new BigDecimal("900000"), hibernateOutgoingPaymentDao.getOutgoingPaymentsByAccountNumber(accNumber).get(hibernateOutgoingPaymentDao.getOutgoingPaymentsByAccountNumber(accNumber).size()-1).getAmountPaid());
	}
	
	public void testGettingOutgoingPaymentByuserId(){
		assertEmptyDatabases();
		
		Client c = new Client();
		c.setFirstName("Anne Njoki");
		c.setPhoneNumber("0719000000");
		hibernateClientDao.saveUpdateClient(c);
		assertEquals(1, hibernateClientDao.getAllClients().size());
		
		Account ac = new Account();
		ac.setClient(c);
		ac.setAccountNumber(981);
		hibernateAccountDao.saveUpdateAccount(ac);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
		
		OutgoingPayment op = new OutgoingPayment();
		op.setAccount(ac);
		op.setPhoneNumber("0739000000");
		op.setAmountPaid(new BigDecimal("700000"));
		hibernateOutgoingPaymentDao.saveOrUpdateOutgoingPayment(op);
		assertEquals(1, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
		
		long clientId = hibernateOutgoingPaymentDao.getAllOutgoingPayments().get(hibernateOutgoingPaymentDao.getAllOutgoingPayments().size()-1).getAccount().getClient().getId();
		assertEquals(new BigDecimal("700000"), hibernateOutgoingPaymentDao.getOutgoingPaymentByClientId(clientId).get(hibernateOutgoingPaymentDao.getOutgoingPaymentByClientId(clientId).size()-1).getAmountPaid());
	}
}
