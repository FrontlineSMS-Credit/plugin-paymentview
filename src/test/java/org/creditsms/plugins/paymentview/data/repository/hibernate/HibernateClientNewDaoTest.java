package org.creditsms.plugins.paymentview.data.repository.hibernate;

import org.creditsms.plugins.paymentview.data.domain.ClientNew;
import org.creditsms.plugins.paymentview.data.repository.ClientNewDao;
import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.junit.HibernateTestCase;

public class HibernateClientNewDaoTest extends HibernateTestCase {
	@Autowired
	ClientNewDao hibernateClientNewDao;
	
	public void testSetup() {
		assertNotNull(hibernateClientNewDao);
	}
	
	public void testSave() {
		assertEquals(0, hibernateClientNewDao.getClientCount());
		ClientNew c = new ClientNew();
		hibernateClientNewDao.saveUpdateClient(c);
		System.out.print(hibernateClientNewDao.getClientCount());
	}
}
