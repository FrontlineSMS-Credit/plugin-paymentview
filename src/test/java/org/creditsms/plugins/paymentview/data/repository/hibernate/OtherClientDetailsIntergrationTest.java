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
		asserEmptyDatabase();
		OtherClientDetails ocd = new OtherClientDetails();
		hibernateOtherClientDetailsDao.saveUpdateOtherClientDetails(ocd);
		assertEquals(1, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
	
	public void testDeleteAccount(){
		asserEmptyDatabase();
		OtherClientDetails ocd = new OtherClientDetails();
		hibernateOtherClientDetailsDao.saveUpdateOtherClientDetails(ocd);
		assertEquals(1, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
		
		hibernateOtherClientDetailsDao.deleteOtherClientDetails(ocd);
		assertEquals(0, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
	
	void asserEmptyDatabase(){
		assertEquals(0, hibernateOtherClientDetailsDao.getAllOtherDetails().size());
	}
}
