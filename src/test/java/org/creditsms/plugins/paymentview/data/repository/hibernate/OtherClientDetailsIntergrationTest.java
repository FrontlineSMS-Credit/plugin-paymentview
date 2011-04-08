package org.creditsms.plugins.paymentview.data.repository.hibernate;

import org.creditsms.plugins.paymentview.data.domain.OtherClientDetails;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.springframework.beans.factory.annotation.Autowired;

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
	
	public void testSave() {
		assertEmptyDatabase();
		OtherClientDetails ocd = new OtherClientDetails();
		hibernateOtherClientDetailsDao.saveUpdateOtherClientDetails(ocd);
		assertEquals(1, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
	
	public void testGetOtherClientDetailsById(){
		assertEmptyDatabase();
		
		OtherClientDetails ocd = new OtherClientDetails();
		ocd.setLocation("Kariobangi");
		hibernateOtherClientDetailsDao.saveUpdateOtherClientDetails(ocd);
		
		long gId = hibernateOtherClientDetailsDao.getAllOtherDetails().get(hibernateOtherClientDetailsDao.getAllOtherDetails().size()-1).getId();
		
		assertEquals("Kariobangi",hibernateOtherClientDetailsDao.getOtherClientDetailsById(gId).getLocation());
	}
	
	public void testGetOtherClientDetailsByClientId(){
		assertEmptyDatabase();
		Client c1 = createClientWithPhoneNumber("0721000000");
		hibernateClientDao.saveUpdateClient(c1);
		
		OtherClientDetails ocd1 = createOtherClientdetails("Kamkunji");
		ocd1.setClient(c1);
		hibernateOtherClientDetailsDao.saveUpdateOtherClientDetails(ocd1);
		
		long clientId = hibernateClientDao.getAllClients().get(hibernateClientDao.getAllClients().size()-1).getId();

		assertEquals("Kamkunji",hibernateOtherClientDetailsDao.getOtherDetailsByClientId(clientId).get(hibernateOtherClientDetailsDao.getOtherDetailsByClientId(clientId).size()-1).getLocation());

	}
	
	public void testDeleteAccount(){
		assertEmptyDatabase();
		OtherClientDetails ocd = new OtherClientDetails();
		hibernateOtherClientDetailsDao.saveUpdateOtherClientDetails(ocd);
		assertEquals(1, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
		
		hibernateOtherClientDetailsDao.deleteOtherClientDetails(ocd);
		assertEquals(0, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
	
	private Client createClientWithPhoneNumber(String phoneNumber){
		Client c = new Client();
		
		c.setPhoneNumber(phoneNumber);
		return c;
	}
	
	private OtherClientDetails createOtherClientdetails(String location){
		OtherClientDetails ocd = new OtherClientDetails();

		ocd.setLocation(location);
		return ocd;
	}
	
	void assertEmptyDatabase(){
		assertEquals(0, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
}
