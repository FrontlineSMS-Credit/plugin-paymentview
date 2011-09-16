package org.creditsms.plugins.paymentview.data.repository.hibernate;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Roy
 *
 */
public class TargetIntergrationTest extends HibernateTestCase{
	@Autowired    
	HibernateServiceItemDao hibernateServiceItemDao;
	HibernateTargetServiceItemDao hibernateTargetServiceItemDao;
	@Autowired    
	HibernateTargetDao hibernateTargetDao;
	@Autowired    
	HibernateAccountDao hibernateAccountDao;
	
	public void testSetup() {
		assertNotNull(hibernateServiceItemDao);
		assertNotNull(hibernateTargetDao);
		assertNotNull(hibernateAccountDao);
		assertNotNull(hibernateTargetServiceItemDao);
	}
	
	public void testAddTarget() throws DuplicateKeyException{
		assertDatabaseEmpty();
		Target t = createTarget(getAccountNumber("11"), saveAndGetTargetServiceItem(1), "24/04/2011", "24/07/2011");
		hibernateTargetDao.saveTarget(t);
		assertEquals(1, hibernateTargetDao.getTargetCount());
	}
	
	public void testDeleteTarget() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber("11"), saveAndGetTargetServiceItem(1), "24/04/2011", "24/07/2011", 1);
		hibernateTargetDao.deleteTarget(getTarget());
		assertEquals(0, hibernateTargetDao.getTargetCount());
	}
	
	public void testTargetById() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber("11"), saveAndGetTargetServiceItem(1), "24/04/2011", "24/07/2011", 1);
		List<Target> tgtLst = this.hibernateTargetDao.getAllTargets();
		Target t2 = hibernateTargetDao.getTargetById(tgtLst.get(0).getId());
		assertEquals(new BigDecimal("12000"), t2.getTotalTargetCost());
	}

	public void testTargetByAccount() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber("13"), saveAndGetTargetServiceItem(1), "24/04/2011", "24/07/2011", 1);
		Target t2 = hibernateTargetDao.getTargetByAccount("13");
		assertEquals(new BigDecimal("400"), t2.getTotalTargetCost());
	}
	
	private Target getTarget(){
		return this.hibernateTargetDao.getAllTargets().get(0);
	}
	
	private TargetServiceItem saveAndGetTargetServiceItem(int expectedCount) throws DuplicateKeyException{
		TargetServiceItem tsi = new TargetServiceItem();
		getTargetServiceItem(saveAndGetServiceItem("Solar Panel","32000",1));
		hibernateTargetServiceItemDao.saveTargetServiceItem(tsi);
		assertEquals(1, hibernateTargetServiceItemDao.getAllTargetServiceItems().size());
		return tsi;
	}
	
	private ServiceItem saveAndGetServiceItem(String serviceItemName, String amount, int expectedCount) throws DuplicateKeyException{
		ServiceItem si = getServiceItem(serviceItemName, amount);
		hibernateServiceItemDao.saveServiceItem(si);
		assertEquals(1, hibernateServiceItemDao.getServiceItemCount());
		return si;
	}
	
	private TargetServiceItem getTargetServiceItem(ServiceItem si){
		TargetServiceItem tsi = new TargetServiceItem();
		tsi.setAmount(si.getAmount());
		tsi.setServiceItem(si);
		tsi.setServiceItemQty(1);
		return tsi;
	}
	
	private ServiceItem getServiceItem(String serviceItemName, String amount){
		ServiceItem si = new ServiceItem();
		si.setTargetName(serviceItemName);
		si.setAmount(new BigDecimal(amount));
		return si;
	}
	
	private void createAndSaveTarget(Account ac, TargetServiceItem tsi, String startDateStr, String endDateStr, int expectedCount) throws DuplicateKeyException{
		Target t1 =  createTarget(ac, tsi, startDateStr, endDateStr);
		hibernateTargetDao.saveTarget(t1);
		assertEquals(expectedCount, hibernateTargetDao.getTargetCount());
	}
	
	private Target createTarget(Account ac, TargetServiceItem tsi, String startDateStr, String endDateStr){
		Target tgt = new Target();
		tgt.setAccount(ac);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try { 
			Date startDate = df.parse(startDateStr);  
			Date endDate = df.parse(endDateStr);
			tgt.setStartDate(startDate);
			tgt.setEndDate(endDate);
			tgt.setCompletedDate(null);
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
		assertEquals(0, hibernateServiceItemDao.getServiceItemCount());
		assertEquals(0, hibernateTargetDao.getTargetCount());
		assertEquals(0, hibernateAccountDao.getAllAcounts().size());
	}
}
