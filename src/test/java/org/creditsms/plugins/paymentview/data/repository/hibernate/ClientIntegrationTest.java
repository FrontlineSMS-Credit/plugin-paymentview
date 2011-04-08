package org.creditsms.plugins.paymentview.data.repository.hibernate;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.junit.HibernateTestCase;

public class ClientIntegrationTest extends HibernateTestCase {
	@Autowired
	HibernateClientDao hibernateClientDao;

	public void testSetup() {
		assertNotNull(hibernateClientDao);
	}
	
	public void testSave() {
		assertDatabaseEmpty();
		Client c = createClientWithPhoneNumber("123");
		hibernateClientDao.saveUpdateClient(c);
		assertEquals(1, hibernateClientDao.getClientCount());
	}
	
	public void testGetClientById() {
		assertDatabaseEmpty();
		Client c = createClientWithPhoneNumber("123");
		hibernateClientDao.saveUpdateClient(c);
		Client fetchedClient = hibernateClientDao.getClientById(c.getId());
		assertNotNull(fetchedClient);
		assertEquals(c.getId(), fetchedClient.getId());
	}
	
	public void testPhoneNumberNotNull() {
		assertDatabaseEmpty();
		Client nullNumber = new Client();
		try {
			this.hibernateClientDao.saveUpdateClient(nullNumber);
			fail("Should not be able to save client with null phone number");
		} catch(RuntimeException ex) {
			// expected
		}
	}

	public void testPhoneNumberUnique() {
		assertDatabaseEmpty();
		Client c1 = createClientWithPhoneNumber("+25472012345");
		Client c2 = createClientWithPhoneNumber("+25472987654");
		Client c1again = createClientWithPhoneNumber("+25472012345");
		
		hibernateClientDao.saveUpdateClient(c1);
		hibernateClientDao.saveUpdateClient(c2);
		
		try {
			hibernateClientDao.saveUpdateClient(c1again);
			fail("Should not be able to save contact with duplicate phone number.");
		} catch(RuntimeException ex) {
			// expected
		}
	}
	
	private Client createClientWithPhoneNumber(String string) {
		Client c = new Client();
		c.setPhoneNumber(string);
		return c;
	}

	private void assertDatabaseEmpty() {
		assertEquals(0, hibernateClientDao.getClientCount());
	}
}
