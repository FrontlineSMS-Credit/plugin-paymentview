package org.creditsms.plugins.paymentview.analytics;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
		assertEquals(new BigDecimal("96.774200"), this.targetAnalytics.getPercentageToGo(targetId));
	}
	
	public void testGetAmountSaved(){
		assertEquals(new BigDecimal("9000"), this.targetAnalytics.getAmountSaved(targetId));
	}
	
	public void testGetLastAmountPaid(){
		assertEquals(new BigDecimal("2000"), this.targetAnalytics.getLastAmountPaid(targetId));
	}
	
	public void testGetDaysRemaining(){
		assertEquals(3, this.targetAnalytics.getDaysRemaining(targetId));	
	}
	
	public void testTargetStatus() {
		assertEquals(true, this.targetAnalytics.isStatusGood(targetId));
	}
	
	public void testGetLastDatePaid(){
		
		assertEquals(this.todaysDate, this.targetAnalytics.getLastDatePaid(targetId));	
	}

	private void setUpTestData() throws DuplicateKeyException{
		Account acc = getAccountNumber("104");
		ServiceItem si = saveServiceItem("Solar Cooker","9300", 1);
		targetId = createTarget(acc, si, "01/05/2011", "15/05/2011").getId();
		createIncomingPayment("0723000000","4500","Mr. Renyenjes", acc);
		createIncomingPayment("0723000000","2500","Mr. Renyenjes", acc);
		createIncomingPayment("0723000000","2000","Mr. Renyenjes", acc);
	}

	private Target createTarget(Account ac, ServiceItem si, String startDateStr, String endDateStr) throws DuplicateKeyException {
		Target tgt = new Target();
		tgt.setServiceItem(si);
		tgt.setAccount(ac);
	
		Date startDate = getDate(startDateStr);  
		Date endDate = getDate(endDateStr);
		tgt.setStartDate(startDate);
		tgt.setEndDate(endDate);
		this.hibernateTargetDao.saveTarget(tgt);

		return tgt;
	}

	private Account getAccountNumber(String accNum) throws DuplicateKeyException{
		Account acc = new Account();
		acc.setAccountNumber(accNum);
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
			String by, Account account) {
		
		IncomingPayment ip = new IncomingPayment();
		ip.setPhoneNumber(phoneNumber);
		ip.setAmountPaid(new BigDecimal(amount));
		ip.setPaymentBy(by);
		ip.setAccount(account);
		Date todaysDatesv = new Date(); 
		this.todaysDate = todaysDatesv;
		ip.setTimePaid(todaysDatesv);
		this.hibernateIncomingPaymentDao.saveIncomingPayment(ip);
		return ip;
	}
	
	private Date getDate(String strDate){
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try { 
			Date theDate = df.parse(strDate); 
			return theDate;
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
