package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment.Status;
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
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		OutgoingPayment op = createOutgoingPayment(c,"8000", new Date(), Status.CREATED);
		hibernateOutgoingPaymentDao.saveOutgoingPayment(op);
		assertEquals(1, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
		assertEquals(Status.CREATED, hibernateOutgoingPaymentDao.getOutgoingPaymentsByClientId(c.getId()).get(0).getStatus());
	}
	
	public void testUpdatingOutgoingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		OutgoingPayment op = createOutgoingPayment(c,"8000", new Date(), Status.CREATED);
		hibernateOutgoingPaymentDao.saveOutgoingPayment(op);
		
		op.setStatus(Status.UNCONFIRMED);
		hibernateOutgoingPaymentDao.updateOutgoingPayment(op);
		assertEquals(1, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
		assertEquals(Status.UNCONFIRMED, hibernateOutgoingPaymentDao.getOutgoingPaymentsByClientId(c.getId()).get(0).getStatus());
	}
	
	public void testDeletingOutgoingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		createAndSaveOutgoingPayment(c, "8000", new Date(), null, Status.UNCONFIRMED,1);
		hibernateOutgoingPaymentDao.deleteOutgoingPayment(getOutgoingPayment());
		assertEquals(0, hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
	}
	
	public void testGettingOutgoingPaymentById() throws DuplicateKeyException{
		assertEmptyDatabases();
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		createAndSaveOutgoingPayment(c, "18000", new Date(), null, Status.CONFIRMED,1);
		long opId = getOutgoingPayment().getId();
		assertEquals(new BigDecimal("18000"), hibernateOutgoingPaymentDao.getOutgoingPaymentById(opId).getAmountPaid());
	}
	
	public void testGettingOutgoingPaymentByPhoneNumber() throws DuplicateKeyException{
		assertEmptyDatabases();
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		createAndSaveOutgoingPayment(c, "23000000", new Date(), null, Status.CONFIRMED,1);
		String phoneNumber = getOutgoingPayment().getClient().getPhoneNumber();
	    assertEquals(new BigDecimal("23000000"), hibernateOutgoingPaymentDao.getOutgoingPaymentsByPhoneNo(phoneNumber).get(0).getAmountPaid());
	}

	public void testGettingOutgoingPaymentsByAccountNumber() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		Account ac = createAndSaveAccount("983", 1, null);
		createAndSaveOutgoingPayment(c, "900000", new Date(), ac, Status.CONFIRMED,1);
		String accNumber = getOutgoingPayment().getAccount().getAccountNumber();
		assertEquals(new BigDecimal("900000"), hibernateOutgoingPaymentDao.
				getOutgoingPaymentsByAccountNumber(accNumber).get(0).getAmountPaid());
	}
	
	public void testGettingOutgoingPaymentByuserId() throws DuplicateKeyException{
		assertEmptyDatabases();
		
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		Account ac = createAndSaveAccount("981", 1, c);
		createAndSaveOutgoingPayment(c, "700000", new Date(), ac, Status.CONFIRMED,1);
		long clientId = getOutgoingPayment().getClient().getId();
		assertEquals(new BigDecimal("700000"), hibernateOutgoingPaymentDao.getOutgoingPaymentsByClientId(clientId).get(0).getAmountPaid());
	}
	
	public void testGettingOutgoingPaymentsByPhoneNumberAndAmountPaid() throws DuplicateKeyException{
		assertEmptyDatabases();
		Client c = createAndSaveClient("0719000000", "Anne Njoki", 1);
		Account ac = createAndSaveAccount("981", 1, c);
		Date d1 = new Date();
		Date d2 = new Date();
		createAndSaveOutgoingPayment(c, "700000", d1, ac, Status.UNCONFIRMED,1);
		createAndSaveOutgoingPayment(c, "700000", d2, ac, Status.UNCONFIRMED,2);
		assertEquals(2,this.hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
		assertEquals((Long) d2.getTime(), this.hibernateOutgoingPaymentDao.getByPhoneNumberAndAmountPaid(c.getPhoneNumber(),
				new BigDecimal("700000"), Status.UNCONFIRMED).get(0).getTimePaid());
	}
	
	private OutgoingPayment getOutgoingPayment(){
		List<OutgoingPayment> oPaymentsLst = hibernateOutgoingPaymentDao.getAllOutgoingPayments();
		return(this.hibernateOutgoingPaymentDao.getOutgoingPaymentById(oPaymentsLst.get(0).getId()));
	}
	
	private void createAndSaveOutgoingPayment(Client client, String amount, Date timePaid, Account account, Status status
			, int expectedOutgoingPaymentCount) throws DuplicateKeyException{
		this.hibernateOutgoingPaymentDao.saveOutgoingPayment(createOutgoingPayment(client, amount, timePaid, account, status));
		assertEquals(expectedOutgoingPaymentCount, this.hibernateOutgoingPaymentDao.getAllOutgoingPayments().size());
	}
	
	private OutgoingPayment createOutgoingPayment(Client client, String amount, Date timePaid, Status status){
		OutgoingPayment op = new OutgoingPayment();
		op.setClient(client);
		op.setTimePaid(timePaid);
		op.setAmountPaid(new BigDecimal(amount));
		op.setStatus(status);
		return op;
	}
	
	private OutgoingPayment createOutgoingPayment(Client client, String amount, Date timePaid, Account account, Status status){
		OutgoingPayment op = new OutgoingPayment();
		op.setClient(client);
		op.setTimePaid(timePaid);
		op.setAmountPaid(new BigDecimal(amount));
		op.setStatus(status);
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
