package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author Alex, Roy
 *
 */
public class ClientIntegrationTest extends HibernateTestCase {
	@Autowired
	HibernateClientDao hibernateClientDao;
	@Autowired                     
	HibernateCustomFieldDao hibernateCustomFieldDao;
	@Autowired                     
	HibernateCustomValueDao hibernateCustomValueDao;

	public void testSetup() {
		assertNotNull(hibernateClientDao);
		assertNotNull(hibernateCustomFieldDao);
		assertNotNull(hibernateCustomValueDao);
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
		} catch (Exception e) {
			passedTest = true;
		}
		assertTrue(passedTest);
	}
	
	public void testGetClientsByFilter() throws DuplicateKeyException{
		assertDatabaseEmpty();
		//create client - create custom field, create custom value
		Client c3 = createAndSaveClientWithPhoneNumber("+25472012345");
		Client c4 = createAndSaveClientWithPhoneNumber("+25472987654");
		Client c5 = createAndSaveClientWithPhoneNumber("+25472987655");
		CustomField cf1 = createAndSaveCustomField("Village",1);
		createAndSaveCustomValue("Nunguni",c3,1,cf1);
		createAndSaveCustomValue("Nunguni",c4,2,cf1);
		CustomField cf2 = createAndSaveCustomField("Address",2);
		createAndSaveCustomValue("Nunguni",c3,3,cf2);
		createAndSaveCustomValue("Nairobi",c4,4,cf2);
		CustomField cf3 = createAndSaveUnusedCustomField("PostalAddress",3);
		createAndSaveCustomValue("Nunguni",c5,5 ,cf3);
		
		List<Client> clientList = hibernateClientDao.getClientsByFilter("Nunguni", 0, 10);
		assertEquals(2, clientList.size());
	}



	private Client createAndSaveClientWithPhoneNumber(String phoneNumber) throws DuplicateKeyException{
		Client client = createClientWithPhoneNumber(phoneNumber);
		hibernateClientDao.saveClient(client);
		return client;
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
	
	
	private CustomField createAndSaveCustomField(String strName, int expectedCustomFieldCount) throws DuplicateKeyException{
        CustomField cf = createCustomField(strName);
        cf.setUsed(Boolean.TRUE);
        hibernateCustomFieldDao.saveCustomField(cf);
        assertEquals(expectedCustomFieldCount, hibernateCustomFieldDao.getCustomFieldCount());
        return cf;
	}
	
	private CustomField createAndSaveUnusedCustomField(String strName, int expectedCustomFieldCount) throws DuplicateKeyException{
        CustomField cf = createCustomField(strName);
        cf.setUsed(Boolean.FALSE);
        hibernateCustomFieldDao.saveCustomField(cf);
        assertEquals(expectedCustomFieldCount, hibernateCustomFieldDao.getCustomFieldCount());
        return cf;
	}
	
	private CustomField createCustomField(String strName){
		CustomField cf = new CustomField();
		cf.setReadableName(strName);
		return cf;
	}
	
	
	private void createAndSaveCustomValue(String cfName, Client client, int expectedPaymentCount, CustomField cf) throws DuplicateKeyException{
		hibernateCustomValueDao.saveCustomValue(createOtherClientdetails(cfName, client, cf));
		assertEquals(expectedPaymentCount, hibernateCustomValueDao.getAllCustomValues().size());
	}
	
	private CustomValue createOtherClientdetails(String strVal, Client client, CustomField cf){
		CustomValue ocd = new CustomValue();
		ocd.setStrValue(strVal);
		ocd.setCustomField(cf);
		if(client!=null) ocd.setClient(client);
		return ocd;
	}

	private void assertDatabaseEmpty() {
		assertEquals(0, hibernateClientDao.getClientCount());
	}
}
