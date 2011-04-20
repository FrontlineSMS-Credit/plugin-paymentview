package org.creditsms.plugins.paymentview.data.repository.hibernate;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import java.math.BigDecimal;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

/**
 * 
 * @author Roy
 *
 */
public class TargetIntergrationTest extends HibernateTestCase{
	@Autowired    
	HibernateServiceItemDao hibernateServiceItemDao;
	@Autowired    
	HibernateTargetDao hibernateTargetDao;
	@Autowired    
	HibernateAccountDao hibernateAccountDao;
	
	public void testSetup() {
		assertNotNull(hibernateServiceItemDao);
		assertNotNull(hibernateTargetDao);
		assertNotNull(hibernateAccountDao);
	}
	
	public void testAddTarget() throws DuplicateKeyException{
		assertDatabaseEmpty();
		Target t = createTarget(getAccountNumber(11), saveAndGetServiceItem("Solar Panel","32000",1), "24/04/2011", "24/07/2011");
		hibernateTargetDao.saveTarget(t);
		assertEquals(1, hibernateTargetDao.getTargetCount());
	}
	
	public void testDeleteTarget() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber(11), saveAndGetServiceItem("Solar Panel","32000",1), "24/04/2011", "24/07/2011", 1);
		hibernateTargetDao.deleteTarget(getTarget());
		assertEquals(0, hibernateTargetDao.getTargetCount());
	}
	
	public void testTargetById() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber(11), saveAndGetServiceItem("Water Pump","12000",1), "24/04/2011", "24/07/2011", 1);
		List<Target> tgtLst = this.hibernateTargetDao.getAllTarget();
		Target t2 = hibernateTargetDao.getTargetById(tgtLst.get(0).getId());
		assertEquals(new BigDecimal("12000"), t2.getServiceItem().getAmount());
	}

	public void testTargetByAccount() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber(13), saveAndGetServiceItem("Solar Lamps","400",1), "24/04/2011", "24/07/2011", 1);
		Target t2 = hibernateTargetDao.getTargetByAccount(13);
		assertEquals(new BigDecimal("400"), t2.getServiceItem().getAmount());
	}
	
	public void testTargetByTargetItemName() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber(13), saveAndGetServiceItem("15,000 Shillings Loan","15000",1), "24/04/2011", "24/07/2011", 1);
		Target t2 = hibernateTargetDao.getTargetByName("15,000 Shillings Loan").get(0);
		assertEquals(new BigDecimal("15000"), t2.getServiceItem().getAmount());
	}
	
	private Target getTarget(){
		return this.hibernateTargetDao.getAllTarget().get(0);
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
	
	private void createAndSaveTarget(Account ac, ServiceItem si, String startDateStr, String endDateStr, int expectedCount) throws DuplicateKeyException{
		Target t1 =  createTarget(ac, si, startDateStr, endDateStr);
		hibernateTargetDao.saveTarget(t1);
		assertEquals(expectedCount, hibernateTargetDao.getTargetCount());
	}
	
	private Target createTarget(Account ac, ServiceItem si, String startDateStr, String endDateStr){
		Target tgt = new Target();
		tgt.setServiceItem(si);
		tgt.setAccount(ac);
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try { 
			Date startDate = df.parse(startDateStr);  
			Date endDate = df.parse(endDateStr);
			tgt.setStartDate(startDate);
			tgt.setEndDate(endDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return tgt;
	}
	
	private Account getAccountNumber(long accNum) throws DuplicateKeyException{
		Account acc = new Account();
		acc.setAccountNumber(accNum);
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
