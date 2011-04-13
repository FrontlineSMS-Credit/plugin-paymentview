package org.creditsms.plugins.paymentview.data.repository.hibernate;

import org.creditsms.plugins.paymentview.data.domain.OtherClientDetails;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

public class OtherClientDetailsIntergrationTest extends HibernateTestCase  {
	@Autowired                     
	HibernateOtherClientDetailsDao hibernateOtherClientDetailsDao;
	@Autowired
	HibernateClientDao hibernateClientDao;
	
	public void testSetup() {
		assertNotNull(hibernateOtherClientDetailsDao);
		assertNotNull(hibernateClientDao);
	}
	
	public void testSave() throws DuplicateKeyException {
		assertEmptyDatabase();
		OtherClientDetails ocd = createOtherClientdetails("Kariobangi South");
		hibernateOtherClientDetailsDao.saveOtherClientDetails(ocd);
		assertEquals(1, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
	
	public void testGetOtherClientDetailsById() throws DuplicateKeyException{
		assertEmptyDatabase();
		createAndSaveOtherClientDetails("Kariobangi", null, 1);
		long gId = getOtherClientDetails().getId();
		assertEquals("Kariobangi",hibernateOtherClientDetailsDao.getOtherClientDetailsById(gId).getLocation());
	}
	
	public void testGetOtherClientDetailsByClientId() throws DuplicateKeyException{
		assertEmptyDatabase();
		Client c1 = createAndSaveClient("0721000000", "Waweru Nguru", 1);
		createAndSaveOtherClientDetails("Kamkunji", c1, 1);
		long clientId = getOtherClientDetails().getClient().getId();
		assertEquals("Kamkunji",hibernateOtherClientDetailsDao.getOtherDetailsByClientId(clientId).get(0).getLocation());
	}
	
	public void testDeleteAccount() throws DuplicateKeyException{
		assertEmptyDatabase();
		createAndSaveOtherClientDetails("Mkuru Kwa Jenga", null, 1);
		hibernateOtherClientDetailsDao.deleteOtherClientDetails(getOtherClientDetails());
		assertEquals(0, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
	
	private void createAndSaveOtherClientDetails(String location, Client client, int expectedPaymentCount) throws DuplicateKeyException{
		hibernateOtherClientDetailsDao.saveOtherClientDetails(createOtherClientdetails(location, client));
		assertEquals(1, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
	
	private OtherClientDetails getOtherClientDetails(){
		assertEquals(1, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
		return(this.hibernateOtherClientDetailsDao.getAllOtherDetails().get(0));
	}
	
	
	private Client createAndSaveClient(String phnNumber, String firstName, int expectedAccountCount) throws DuplicateKeyException{
		Client c = new Client();
		c.setPhoneNumber(phnNumber);
		c.setFirstName(firstName);
		hibernateClientDao.saveClient(c);
		assertEquals(expectedAccountCount, hibernateClientDao.getAllClients().size());
		return c;
	}
	
	private OtherClientDetails createOtherClientdetails(String location){
		OtherClientDetails ocd = new OtherClientDetails();
		ocd.setLocation(location);
		return ocd;
	}
	
	private OtherClientDetails createOtherClientdetails(String location, Client client){
		OtherClientDetails ocd = new OtherClientDetails();
		ocd.setLocation(location);
		if(client!=null) ocd.setClient(client);
		return ocd;
	}
	
	void assertEmptyDatabase(){
		assertEquals(0, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
}
