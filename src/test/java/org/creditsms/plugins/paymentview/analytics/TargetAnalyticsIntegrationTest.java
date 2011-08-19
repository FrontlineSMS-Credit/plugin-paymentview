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
		assertEquals(new BigDecimal("216.61"), this.targetAnalytics.getMonthlyAmountSaved());
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
		assertEquals(TargetAnalytics.Status.ON_TRACK, this.targetAnalytics.getStatus(targetId));
	}
	
	public void testGetLastDatePaid(){
		assertEquals(this.todaysDate, this.targetAnalytics.getLastDatePaid(targetId));	
	}
	
	private Calendar setStartOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	private Calendar setEndOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 24);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, -1);  
		cal.set(Calendar.MILLISECOND, 0);
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

		Account acc = getAccountNumber("104");
		ServiceItem si = saveServiceItem("Solar Cooker","9300", 1);
		Target tgt = createTarget(acc, si, startDate, endDate);
		targetId = tgt.getId();
		createIncomingPayment("0723000000","4500","Mr. Renyenjes", acc, tgt);
		createIncomingPayment("0723000000","2500","Mr. Renyenjes", acc, tgt);
		createIncomingPayment("0723000000","2000","Mr. Renyenjes", acc, tgt);
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

	private Account getAccountNumber(String accNum) throws DuplicateKeyException{
		Account acc = new Account();
		acc.setAccountNumber(accNum);
		acc.setActiveAccount(true);
		this.hibernateAccountDao.saveAccount(acc);
		assertEquals(1, this.hibernateAccountDao.getAllAcounts().size());
		return this.hibernateAccountDao.getAllAcounts().get(0);
	}
	
	private ServiceItem saveServiceItem(String serviceItemName, String amount, int expectedCount) throws DuplicateKeyException{
		ServiceItem si = getServiceItem(serviceItemName, amount);
		this.hibernateServiceItemDao.saveServiceItem(si);
		assertEquals(1, this.hibernateServiceItemDao.getServiceItemCount());
		
		return si;
	}
	
	private ServiceItem getServiceItem(String serviceItemName, String amount){
		ServiceItem si = new ServiceItem();
		si.setTargetName(serviceItemName);
		si.setAmount(new BigDecimal(amount));
		return si;
	}
	
	private IncomingPayment createIncomingPayment(String phoneNumber, String amount,
			String by, Account account, Target tgt) {
		
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber(phoneNumber);
		ip.setAmountPaid(new BigDecimal(amount));
		ip.setPaymentBy(by);
		ip.setAccount(account);
		ip.setTarget(tgt);
		ip.setActive(true);
		Date todaysDatesv = new Date(); 
		this.todaysDate = todaysDatesv;
		ip.setTimePaid(todaysDatesv);
		this.hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		return ip;
	}

}
