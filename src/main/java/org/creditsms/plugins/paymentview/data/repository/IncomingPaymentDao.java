package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

/**
 * @author Roy
 * */
public interface IncomingPaymentDao {
	/**
	 * get and return an IncomingPayment with a specific id 
	 * */
	public IncomingPayment getIncomingPaymentById(long incomingPaymentId);
	
	/**
	 * returns all the incomingPayments in the system
	 * */
	public List<IncomingPayment> getAllIncomingPayments();
	
	/**
	 * returns IncomingPayment(s) within a given date range
	 * */
	public List<IncomingPayment> getIncomingPaymentsByDateRange(Calendar startDate, Calendar endDate);
	
	/**
	 * returns IncomingPayment(s) by time range
	 * */
	public List<IncomingPayment> getIncomingPaymentsByTimeRange(Date startTime, Date endtime);
	
	/**
	 * returns IncomingPayment(s) by client and by date
	 * */
	public List<IncomingPayment> getIncomingPaymentsByClientIdByDateRange(long clientId, Calendar startDate, Calendar endDate);
	
	/**
	 * returns IncomingPayment(s) by clientId
	 * */
	public List<IncomingPayment> getIncomingPaymentByClientId(long clientId);
	
	/**
	 * returns IncomingPayment(s) by accountId
	 * */
	public List<IncomingPayment> getIncomingPaymentsByAccountId(long accountId);
	
	/**
	 * returns IncomingPayment(s) by accountId by date ranges
	 * */
	public List<IncomingPayment> getIncomingPaymentsByAccountIdByDateRange(long accountId, Calendar startDate, Calendar endDate);
	
	/**
	 * returns IncomingPayment(s) by accountId by time ranges
	 * */
	public List<IncomingPayment> getIncomingPaymentsByAccountIdByTimeRange(long accountId, Date startDate, Date endDate);

	/**
	 * returns IncomingPayment(s) by payer 
	 * */
	public List<IncomingPayment> getIncomingPaymentsByPayer(String payer);

	/**
	 * returns IncomingPayment(s) by phone number
	 * */
	public List<IncomingPayment> getIncomingPaymentsByPhoneNo(long phoneNo);

	/**
	 * saves or updates an IncomingPayment payment to the system
	 * */
	public void saveOrUpdateIncomingPayment(IncomingPayment incomingPayment);
	
	/**
	 * removes an IncomingPayment payment to the system
	 * */
	public void deleteIncomingPayment(IncomingPayment incomingPayment);
}
