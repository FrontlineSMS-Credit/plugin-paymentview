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
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Roy
 *
 */
public class TargetIntergrationTest extends HibernateTestCase{
	@Autowired    
	HibernateTargetDao hibernateTargetDao;
	@Autowired    
	HibernateAccountDao hibernateAccountDao;
	
	public void testSetup() {
		assertNotNull(hibernateTargetDao);
		assertNotNull(hibernateAccountDao);
	}
	
	public void testAddTarget() throws DuplicateKeyException{
		assertDatabaseEmpty();
		Target t = createTarget(getAccountNumber("11"), "24/04/2011", "24/07/2011");
		hibernateTargetDao.saveTarget(t);
		assertEquals(1, hibernateTargetDao.getTargetCount());
	}
	
	public void testDeleteTarget() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber("11"), "24/04/2011", "24/07/2011", 1);
		hibernateTargetDao.deleteTarget(getTarget());
		assertEquals(0, hibernateTargetDao.getTargetCount());
	}
	
	public void testTargetById() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber("11"), "24/04/2011", "24/07/2011", 1);
		List<Target> tgtLst = this.hibernateTargetDao.getAllTargets();
		Target t2 = hibernateTargetDao.getTargetById(tgtLst.get(0).getId());
		assertEquals(new BigDecimal("4500"), t2.getTotalTargetCost());
	}

	public void testTargetByAccount() throws DuplicateKeyException{
		assertDatabaseEmpty();
		createAndSaveTarget(getAccountNumber("13"), "24/04/2011", "24/07/2011", 1);
		List<Target> lstt2 = hibernateTargetDao.getTargetsByAccount("13");
		Target t2 = lstt2.get(0);
		assertEquals(new BigDecimal("4500"), t2.getTotalTargetCost());
	}
	
	private Target getTarget(){
		return this.hibernateTargetDao.getAllTargets().get(0);
	}
	
	
	private void createAndSaveTarget(Account ac, String startDateStr, String endDateStr, int expectedCount) throws DuplicateKeyException{
		Target t1 =  createTarget(ac, startDateStr, endDateStr);
		hibernateTargetDao.saveTarget(t1);
		assertEquals(expectedCount, hibernateTargetDao.getTargetCount());
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
	}
}
