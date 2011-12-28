package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.math.BigDecimal;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Roy
 */
public class ServiceItemIntergrationTest extends HibernateTestCase {
	@Autowired    
	HibernateServiceItemDao hibernateServiceItemDao;
	
	public void testSetup() {
		assertNotNull(hibernateServiceItemDao);
	}
	
	public void testAddServiceItem() throws DuplicateKeyException{
		assertDatabaseEmpty();
		ServiceItem si = getServiceItem("Solar Panel", "18000");
		hibernateServiceItemDao.saveServiceItem(si);
		assertEquals(1, hibernateServiceItemDao.getServiceItemCount());
	}
	
	public void testGetServiceItemByName() throws DuplicateKeyException{
		assertDatabaseEmpty();
		saveServiceItem("Green house installation","128000", 1);
		List<ServiceItem> siLst = this.hibernateServiceItemDao.getServiceItemsByName("Green house installation");
		assertEquals(1, siLst.size());
	}
	
	public void testGetServiceItemById() throws DuplicateKeyException{
		assertDatabaseEmpty();
		saveServiceItem("Green house installation","128000", 1);
		List<ServiceItem> siLst = this.hibernateServiceItemDao.getAllServiceItems();
		ServiceItem si2 = hibernateServiceItemDao.getServiceItemById(siLst.get(0).getId());
		assertEquals(new BigDecimal("128000"), si2.getAmount());
	}
	
	public void testdeleteServiceItem() throws DuplicateKeyException{
		assertDatabaseEmpty();
		saveServiceItem("Solar Cooker","12000", 1);
		hibernateServiceItemDao.deleteServiceItem(getServiceItem());
		assertEquals(0, hibernateServiceItemDao.getServiceItemCount());
	}
	
	private ServiceItem getServiceItem(){
		return this.hibernateServiceItemDao.getAllServiceItems().get(0);
	}
	
	private void saveServiceItem(String serviceItemName, String amount, int expectedCount) throws DuplicateKeyException{
		ServiceItem si = getServiceItem(serviceItemName, amount);
		hibernateServiceItemDao.saveServiceItem(si);
		assertEquals(1, hibernateServiceItemDao.getServiceItemCount());
	}
	
	private ServiceItem getServiceItem(String serviceItemName, String amount){
		ServiceItem si = new ServiceItem();
		si.setTargetName(serviceItemName);
		si.setAmount(new BigDecimal(amount));
		return si;
	}
	
	private void assertDatabaseEmpty() {
		assertEquals(0, hibernateServiceItemDao.getServiceItemCount());
	}
}
