package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author Roy
 *
 */
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

	public void testSavingOutgoingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		OutgoingPayment op = createOutgoingPayment("0725000000","8000", new Date());
		hibernateOutgoingPaymentDao.saveOutgoingPayment(op);
		assertEquals(1, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
	}
	
	public void testDeletingOutgoingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		createAndSaveOutgoingPayment("0725000000", "8000", new Date(), null);
		hibernateOutgoingPaymentDao.deleteOutgoingPayment(getOutgoingPayment());
		assertEquals(0, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
	}
	
	public void testGettingOutgoingPaymentById() throws DuplicateKeyException{
		assertEmptyDatabases();
		createAndSaveOutgoingPayment("0726000000", "18000", new Date(), null);
		long opId = getOutgoingPayment().getId();
		assertEquals(new BigDecimal("18000"), hibernateOutgoingPaymentDao.getOutgoingPaymentById(opId).getAmountPaid());
	}
	
	public void testGettingOutgoingPaymentByPhoneNumber() throws DuplicateKeyException{
		assertEmptyDatabases();
		createAndSaveOutgoingPayment("0734000000", "23000000", new Date(), null);
		String phoneNumber = getOutgoingPayment().getPhoneNumber();
	    assertEquals(new BigDecimal("23000000"), hibernateOutgoingPaymentDao.getOutgoingPaymentsByPhoneNo(phoneNumber).get(0).getAmountPaid());
	}

	public void testGettingOutgoingPaymentsByAccountNumber() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		Account ac = createAndSaveAccount("983", 1, null);
		createAndSaveOutgoingPayment("0739000000", "900000", new Date(), ac);
		String accNumber = getOutgoingPayment().getAccount().getAccountNumber();
		assertEquals(new BigDecimal("900000"), hibernateOutgoingPaymentDao.
				getOutgoingPaymentsByAccountNumber(accNumber).get(0).getAmountPaid());
	}
	
	public void testGettingOutgoingPaymentByuserId() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		Account ac = createAndSaveAccount("981", 1, c);
		createAndSaveOutgoingPayment("0739000000", "700000", new Date(), ac);
		long clientId = getOutgoingPayment().getAccount().getClient().getId();
		assertEquals(new BigDecimal("700000"), hibernateOutgoingPaymentDao.getOutgoingPaymentsByClientId(clientId).get(0).getAmountPaid());
	}
	
	private OutgoingPayment getOutgoingPayment(){
		List<OutgoingPayment> oPaymentsLst = hibernateOutgoingPaymentDao.getAllOutgoingPayments();
		return(this.hibernateOutgoingPaymentDao.getOutgoingPaymentById(oPaymentsLst.get(0).getId()));
	}
	
	private void createAndSaveOutgoingPayment(String phoneNumber, String amount, Date timePaid, Account account) throws DuplicateKeyException{
		this.hibernateOutgoingPaymentDao.saveOutgoingPayment(createOutgoingPayment(phoneNumber, amount, timePaid, account));
		assertEquals(1, this.hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
	}
	
	private OutgoingPayment createOutgoingPayment(String phoneNumber, String amount, Date timePaid){
		OutgoingPayment op = new OutgoingPayment();
		op.setPhoneNumber(phoneNumber);
		op.setTimePaid(timePaid);
		op.setAmountPaid(new BigDecimal(amount));
		return op;
	}
	
	private OutgoingPayment createOutgoingPayment(String phoneNumber, String amount, Date timePaid, Account account){
		OutgoingPayment op = new OutgoingPayment();
		op.setPhoneNumber(phoneNumber);
		op.setTimePaid(timePaid);
		op.setAmountPaid(new BigDecimal(amount));
		if(account != null) op.setAccount(account);
		return op;
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
	
	private Client createAndSaveClient(String phnNumber, String firstName, int expectedAccountCount) throws DuplicateKeyException{
		Client c = new Client();
		c.setPhoneNumber(phnNumber);
		c.setFirstName(firstName);
		hibernateClientDao.saveClient(c);
		assertEquals(expectedAccountCount, hibernateClientDao.getAllClients().size());
		return c;
	}
}
