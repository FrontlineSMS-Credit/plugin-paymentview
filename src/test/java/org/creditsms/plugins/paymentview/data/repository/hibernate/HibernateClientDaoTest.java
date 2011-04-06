package org.creditsms.plugins.paymentview.data.repository.hibernate;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.junit.HibernateTestCase;

public class HibernateClientDaoTest extends HibernateTestCase {
	@Autowired
	ClientDao hibernateClientDao;
	
	public void testSetup() {
		assertNotNull(hibernateClientDao);
	}
	
	public void testSave() {
		assertEquals(0, hibernateClientDao.getClientCount());
		Client c = new Client();
		hibernateClientDao.saveUpdateClient(c);
		System.out.print(hibernateClientDao.getClientCount());
	}
}
