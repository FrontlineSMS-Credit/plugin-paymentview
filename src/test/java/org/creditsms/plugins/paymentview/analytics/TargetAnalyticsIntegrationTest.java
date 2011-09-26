package org.creditsms.plugins.paymentview.analytics;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.hibernate.HibernateAccountDao;
import org.creditsms.plugins.paymentview.data.repository.hibernate.HibernateIncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.hibernate.HibernateServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.hibernate.HibernateTargetDao;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Roy
 */
public class TargetAnalyticsIntegrationTest extends HibernateTestCase {
	@Autowired    
	HibernateServiceItemDao hibernateServiceItemDao;
	@Autowired    
	HibernateTargetDao hibernateTargetDao;
	@Autowired    
	HibernateAccountDao hibernateAccountDao;
	@Autowired    
	HibernateIncomingPaymentDao hibernateIncomingPaymentDao;

	private TargetAnalytics targetAnalytics;
	
	private long targetId;
	private Date todaysDate; 
	private Date endOfIntervalDate;
	private long targetId_clientA;
	private long targetId_clientB;
	private long targetId_clientC;
	
	@Override
	protected void onSetUp() throws Exception {
		super.onSetUp();
		setUpDaos();
	}
	
	private void setUpDaos() throws DuplicateKeyException {
		this.targetAnalytics = new TargetAnalytics();
		
		this.targetAnalytics.setIncomingPaymentDao(hibernateIncomingPaymentDao);
		this.targetAnalytics.setTargetDao(hibernateTargetDao);
		setUpTestData();
	}
	
	public void testSetup() {
		assertNotNull(hibernateAccountDao);
		assertNotNull(hibernateIncomingPaymentDao);
		assertNotNull(hibernateServiceItemDao);
		assertNotNull(hibernateTargetDao);
	}
	
	public void testGetPercentageToGo(){
		assertEquals(new BigDecimal("97"), this.targetAnalytics.getPercentageToGo(targetId));
	}
	
	public void testComputeAnalyticsIntervalDatesAndSavings(){
		this.targetAnalytics.computeAnalyticsIntervalDatesAndSavings(targetId);
		assertEquals(endOfIntervalDate, this.targetAnalytics.getEndMonthInterval());
		assertEquals(new BigDecimal("516.67"), this.targetAnalytics.getMonthlyTarget());
		assertEquals(18, this.targetAnalytics.getInstalments());
		assertEquals(new BigDecimal("300.06"), this.targetAnalytics.getMonthlyAmountDue());
		assertEquals(new BigDecimal("9000.00"), this.targetAnalytics.getMonthlyAmountSaved());
	}
	
	public void testGetAmountSaved(){
		assertEquals(new BigDecimal("9000.00"), this.targetAnalytics.getAmountSaved(targetId).setScale(2, BigDecimal.ROUND_HALF_UP));
	}
	
	public void testGetLastAmountPaid(){
		assertEquals(new BigDecimal("2000"), this.targetAnalytics.getLastAmountPaid(targetId));
	}
	
	public void testGetDaysRemaining(){
		assertEquals(Long.valueOf(214), this.targetAnalytics.getDaysRemaining(targetId));	
	}
	
	public void testTargetStatus() {
		assertEquals(TargetAnalytics.Status.PAYING, this.targetAnalytics.getStatus(targetId));
	}
	
	public void testGetLastDatePaid(){
		assertEquals(this.todaysDate, this.targetAnalytics.getLastDatePaid(targetId));	
	}
	
	
	/*
	 * Test on client who had consistently saved previous month
	 */
	public void testConsistentClient(){
		this.targetAnalytics.computeAnalyticsIntervalDatesAndSavings(targetId_clientA);
		assertEquals(new BigDecimal("0.00"), this.targetAnalytics.getMonthlyAmountSaved());	
		assertEquals(new BigDecimal("5000"), this.targetAnalytics.getAmountSaved(targetId_clientA));
		assertEquals(new BigDecimal("5000.00"), this.targetAnalytics.getMonthlyAmountDue());
	}
	
	/*
	 * Test on client who had saved more than expected previous month
	 */
	public void testClientPayingMore(){
		this.targetAnalytics.computeAnalyticsIntervalDatesAndSavings(targetId_clientB);
		assertEquals(new BigDecimal("0.00"), this.targetAnalytics.getMonthlyAmountSaved());	
		assertEquals(new BigDecimal("7000"), this.targetAnalytics.getAmountSaved(targetId_clientB));
		assertEquals(new BigDecimal("3000.00"), this.targetAnalytics.getMonthlyAmountDue());
	}
	
	/*
	 * Test on client who had saved less than expected previous month
	 */
	public void testConsistentClientPayingLess(){
		this.targetAnalytics.computeAnalyticsIntervalDatesAndSavings(targetId_clientC);
		assertEquals(new BigDecimal("0.00"), this.targetAnalytics.getMonthlyAmountSaved());	
		assertEquals(new BigDecimal("2000"), this.targetAnalytics.getAmountSaved(targetId_clientC));
		assertEquals(new BigDecimal("8000.00"), this.targetAnalytics.getMonthlyAmountDue());
	}
	
	
	
	private Calendar setStartOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	private Calendar setEndOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 23);  
		cal.set(Calendar.MINUTE, 59);  
		cal.set(Calendar.MILLISECOND, 0);
		cal.set(Calendar.SECOND, 59);  
		return cal;
	}

	private void setUpTestData() throws DuplicateKeyException{
		Calendar calStartDat = Calendar.getInstance();
		calStartDat.add(Calendar.MONTH, -11);  
		calStartDat.add(Calendar.DATE, 1);
		calStartDat = setStartOfDay(calStartDat);
		Date startDate = calStartDat.getTime();
		
		Calendar calEndDate = Calendar.getInstance();
		calEndDate.add(Calendar.MONTH, 7);  
		calEndDate = setEndOfDay(calEndDate);
		Date endDate = calEndDate.getTime();
		endOfIntervalDate = endDate;
		
		Account acc = getAccountNumber("104",1);
		ServiceItem si = saveServiceItem("Solar Cooker","9300", 1);
		Target tgt = createTarget(acc, si, startDate, endDate);
		targetId = tgt.getId();
		createIncomingPayment("0723000000","4500","Mr. Renyenjes", acc, tgt,new Date());
		createIncomingPayment("0723000000","2500","Mr. Renyenjes", acc, tgt,new Date());
		IncomingPayment ip = createIncomingPayment("0723000000","2000","Mr. Renyenjes", acc, tgt,new Date());
		this.todaysDate = new Date(ip.getTimePaid());
		
		
		/*
		 * Create client who is consistently saving
		 */
		calStartDat = Calendar.getInstance();
		calStartDat.add(Calendar.MONTH, -1);  
		calStartDat.add(Calendar.DATE, -10);
		calStartDat = setStartOfDay(calStartDat);
		startDate = calStartDat.getTime();
		
		calEndDate = Calendar.getInstance();
		calEndDate.add(Calendar.MONTH, 3);  
		calEndDate.add(Calendar.DATE, -10);
		calEndDate = setEndOfDay(calEndDate);
		endDate = calEndDate.getTime();
		
		Account accA = getAccountNumber("105",2);
		ServiceItem siSolarPanel = saveServiceItem("Solar Panel","20000", 2);
		Target tgtA = createTarget(accA, siSolarPanel, startDate, endDate);
		targetId_clientA = tgtA.getId();
		Calendar calDate_clientA = Calendar.getInstance();
		calDate_clientA.add(Calendar.MONTH, -1);
		createIncomingPayment("0723000001","5000","Mr Good Client", accA, tgtA,calDate_clientA.getTime());
		
		
		/*
		 * Create client who has saved more than expected the previous month
		 */
		Account accB = getAccountNumber("106",3);
		Target tgtB = createTarget(accB, siSolarPanel, startDate, endDate);
		targetId_clientB = tgtB.getId();
		Calendar calDate_clientB = Calendar.getInstance();
		calDate_clientB.add(Calendar.MONTH, -1);
		createIncomingPayment("0723000001","7000","Mr Good Client", accB, tgtB,calDate_clientB.getTime());
		
		
		/*
		 * Create client who has saved less than expected the previous month
		 */		
		Account accC = getAccountNumber("107",4);
		Target tgtC = createTarget(accC, siSolarPanel, startDate, endDate);
		targetId_clientC = tgtC.getId();
		Calendar calDate_clientC = Calendar.getInstance();
		calDate_clientC.add(Calendar.MONTH, -1);
		createIncomingPayment("0723000001","2000","Mr Good Client", accC, tgtC,calDate_clientC.getTime());
	}

	private Target createTarget(Account ac, ServiceItem si, Date startDate, Date endDate) throws DuplicateKeyException {
		Target tgt = new Target();
		tgt.setServiceItem(si);
		tgt.setAccount(ac);

		tgt.setStartDate(startDate);
		tgt.setEndDate(endDate);
		this.hibernateTargetDao.saveTarget(tgt);

		return tgt;
	}

	private Account getAccountNumber(String accNum, int expectedAccCount) throws DuplicateKeyException{
		Account acc = new Account();
		acc.setAccountNumber(accNum);
		acc.setActiveAccount(true);
		this.hibernateAccountDao.saveAccount(acc);
		assertEquals(expectedAccCount, this.hibernateAccountDao.getAllAcounts().size());
		return acc;
	}
	
	private ServiceItem saveServiceItem(String serviceItemName, String amount, int expectedCount) throws DuplicateKeyException{
		ServiceItem si = getServiceItem(serviceItemName, amount);
		this.hibernateServiceItemDao.saveServiceItem(si);
		assertEquals(expectedCount, this.hibernateServiceItemDao.getServiceItemCount());
		
		return si;
	}
	
	private ServiceItem getServiceItem(String serviceItemName, String amount){
		ServiceItem si = new ServiceItem();
		si.setTargetName(serviceItemName);
		si.setAmount(new BigDecimal(amount));
		return si;
	}
	
	private IncomingPayment createIncomingPayment(String phoneNumber, String amount,
			String by, Account account, Target tgt, Date date) {
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {}
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber(phoneNumber);
		ip.setAmountPaid(new BigDecimal(amount));
		ip.setPaymentBy(by);
		ip.setAccount(account);
		ip.setTarget(tgt);
		ip.setActive(true);

		
		ip.setTimePaid(date);
		this.hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		return ip;
	}
}
