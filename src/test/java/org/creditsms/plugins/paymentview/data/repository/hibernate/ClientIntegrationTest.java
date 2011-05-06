package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author Alex, Roy
 *
 */
public class ClientIntegrationTest extends HibernateTestCase {
	@Autowired
	HibernateClientDao hibernateClientDao;

	public void testSetup() {
		assertNotNull(hibernateClientDao);
	}
	
	public void testSave() throws DuplicateKeyException {
		assertDatabaseEmpty();
		Client c = createClientWithPhoneNumber("123");
		hibernateClientDao.saveClient(c);
		assertEquals(1, hibernateClientDao.getClientCount());
	}
	
	public void testGetClientById() throws DuplicateKeyException {
		assertDatabaseEmpty();
		createAndSaveClientWithPhoneNumber("0756098765");
		Client fetchedClient = getClient();
		assertNotNull(fetchedClient);
		assertEquals("0756098765", fetchedClient.getPhoneNumber());
	}
	
	public void testPhoneNumberNotNull() throws DuplicateKeyException {
		assertDatabaseEmpty();
		Client nullNumber = new Client();
		try {
			this.hibernateClientDao.saveClient(nullNumber);
			fail("Should not be able to save client with null phone number");
		} catch(RuntimeException ex) {
		}
	}

	public void testPhoneNumberUnique() throws DuplicateKeyException {
		boolean passedTest;
		assertDatabaseEmpty();
		Client c1 = createClientWithPhoneNumber("+25472012345");
		Client c2 = createClientWithPhoneNumber("+25472987654");
		Client c1again = createClientWithPhoneNumber("+25472012345");
		
		hibernateClientDao.saveClient(c1);
		hibernateClientDao.saveClient(c2);
		
		try {
			hibernateClientDao.saveClient(c1again);
			fail("Should not be able to save contact with duplicate phone number.");
			passedTest = false;
		} catch (DuplicateKeyException e) {
			passedTest = true;
		}
		assertTrue(passedTest);
	}
	
	private void createAndSaveClientWithPhoneNumber(String phoneNumber) throws DuplicateKeyException{
		hibernateClientDao.saveClient(createClientWithPhoneNumber(phoneNumber));
	}
	
	private Client getClient(){
		List<Client> clientLst = this.hibernateClientDao.getAllClients();
		return this.hibernateClientDao.getClientById(clientLst.get(0).getId());
	}
	
	private Client createClientWithPhoneNumber(String phoneNumber) {
		Client c = new Client();
		c.setPhoneNumber(phoneNumber);
		return c;
	}

	private void assertDatabaseEmpty() {
		assertEquals(0, hibernateClientDao.getClientCount());
	}
}
