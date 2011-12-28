package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author Roy
 */
public class TargetServiceItemIntergrationTest extends HibernateTestCase {
	@Autowired    
	HibernateServiceItemDao hibernateServiceItemDao;
	@Autowired  
	HibernateTargetServiceItemDao hibernateTargetServiceItemDao;
	@Autowired    
	HibernateTargetDao hibernateTargetDao;
	@Autowired    
	HibernateAccountDao hibernateAccountDao;
	
	public void testSetup() {
		assertNotNull(hibernateTargetDao);
		assertNotNull(hibernateAccountDao);
		assertNotNull(hibernateServiceItemDao);
		assertNotNull(hibernateTargetServiceItemDao);
	}
	
	public void testAddTargetServiceItem() throws DuplicateKeyException {
		assertDatabaseEmpty();
		Target target = createTarget();
		TargetServiceItem tsi = getTargetServiceItem(target);
		hibernateTargetServiceItemDao.saveTargetServiceItem(tsi);
		assertEquals(1, hibernateTargetServiceItemDao.getAllTargetServiceItems().size());
	}
	
	public void testDeleteTargetServiceItem() throws DuplicateKeyException{
		assertDatabaseEmpty();
		saveTargetServiceItem(createTarget());
		TargetServiceItem tsi = hibernateTargetServiceItemDao.getAllTargetServiceItems().get(0);
		hibernateTargetServiceItemDao.deleteTargetServiceItem(tsi);
		assertEquals(0, hibernateTargetServiceItemDao.getAllTargetServiceItems().size());
	}
	
	public void testUpdateTargetServiceItem() throws DuplicateKeyException{
		assertDatabaseEmpty();
		saveTargetServiceItem(createTarget());
		TargetServiceItem tsi = hibernateTargetServiceItemDao.getAllTargetServiceItems().get(0);
		assertEquals(1, hibernateTargetServiceItemDao.getAllTargetServiceItems().get(0).getServiceItemQty());	
		tsi.setServiceItemQty(13);
		hibernateTargetServiceItemDao.updateTargetServiceItem(tsi);
		assertEquals(13, hibernateTargetServiceItemDao.getAllTargetServiceItems().get(0).getServiceItemQty());	
	}
	
	public void testGetTargetServiceItemByTargetId() throws DuplicateKeyException{
		assertDatabaseEmpty();
		saveTargetServiceItem(createTarget());
		TargetServiceItem tsi = hibernateTargetServiceItemDao.getAllTargetServiceItems().get(0);
		Target tgt = tsi.getTarget();
		assertEquals("Solar Panel", hibernateTargetServiceItemDao.
				getAllTargetServiceItemByTarget(tgt.getId()).
				get(0).getServiceItem().getTargetName());
	}
	
	private Target createTarget() throws DuplicateKeyException{
		assertDatabaseEmpty();
		Target t = createTarget(getAccountNumber("11"), "24/04/2011", "24/07/2011");
		hibernateTargetDao.saveTarget(t);
		assertEquals(1, hibernateTargetDao.getTargetCount());
		return t;
	}
	
	private void saveTargetServiceItem(Target target) throws DuplicateKeyException{
		TargetServiceItem tsi = new TargetServiceItem();
		ServiceItem si = saveAndGetServiceItem("Solar Panel","32000",1);
		tsi.setAmount(si.getAmount());
		tsi.setServiceItem(si);
		tsi.setTarget(target);
		tsi.setServiceItemQty(1);
		hibernateTargetServiceItemDao.saveTargetServiceItem(tsi);
		assertEquals(1, hibernateTargetServiceItemDao.getAllTargetServiceItems().size());
	}
	
	private TargetServiceItem getTargetServiceItem(Target target) throws DuplicateKeyException{
		TargetServiceItem tsi = new TargetServiceItem();
		ServiceItem si = saveAndGetServiceItem("Solar Panel","32000",1);
		tsi.setAmount(si.getAmount());
		tsi.setServiceItem(si);
		tsi.setTarget(target);
		tsi.setServiceItemQty(1);
		return tsi;
	}
	
	private ServiceItem saveAndGetServiceItem(String serviceItemName, String amount, int expectedCount) throws DuplicateKeyException{
		ServiceItem si = getServiceItem(serviceItemName, amount);
		hibernateServiceItemDao.saveServiceItem(si);
		assertEquals(1, hibernateServiceItemDao.getServiceItemCount());
		return si;
	}
	
	private ServiceItem getServiceItem(String serviceItemName, String amount){
		ServiceItem si = new ServiceItem();
		si.setTargetName(serviceItemName);
		si.setAmount(new BigDecimal(amount));
		return si;
	}

	private Target createTarget(Account ac, String startDateStr, String endDateStr){
		Target tgt = new Target();
		tgt.setAccount(ac);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try { 
			Date startDate = df.parse(startDateStr);  
			Date endDate = df.parse(endDateStr);
			tgt.setStartDate(startDate);
			tgt.setEndDate(endDate);
			tgt.setCompletedDate(null);
			tgt.setTotalTargetCost(new BigDecimal("4500"));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tgt;
	}
	
	private Account getAccountNumber(String accNum) throws DuplicateKeyException{
		Account acc = new Account();
		acc.setAccountNumber(accNum);
		acc.setActiveAccount(true);
		hibernateAccountDao.saveAccount(acc);
		assertEquals(1, hibernateAccountDao.getAllAcounts().size());
		return hibernateAccountDao.getAllAcounts().get(0);
	}
	
	private void assertDatabaseEmpty() {
		assertEquals(0, hibernateTargetDao.getTargetCount());
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
		assertEquals(0, hibernateServiceItemDao.getAllServiceItems().size());
		assertEquals(0, hibernateTargetServiceItemDao.getAllTargetServiceItems().size());
	}
}
