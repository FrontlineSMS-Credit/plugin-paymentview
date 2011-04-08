package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;
import java.util.Calendar;
import java.util.Date;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

/**
 * @author Roy
 * */
public interface OutgoingPaymentDao {
	/**
	 * get and return an OutgoingPayment with a specific id 
	 * */
	public OutgoingPayment getOutgoingPaymentById(long outgoingPaymentId);
	
	/**
	 * returns all the OutgoingPayments in the system
	 * */
	public List<OutgoingPayment> getAllOutgoingPayments();
	
	/**
	 * returns OutgoingPayment(s) within a given date range
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByDateRange(Calendar startDate, Calendar endDate);
	
	/**
	 * returns OutgoingPayment(s) by time range
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByTimeRange(Date startTime, Date endtime);
	
	/**
	 * returns OutgoingPayment(s) by client and by date
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByClientIdByDateRange(long clientId, Calendar startDate, Calendar endDate);
	
	/**
	 * returns OutgoingPayment(s) by clientId
	 * */
	public List<OutgoingPayment> getOutgoingPaymentByClientId(long clientId);
	
	/**
	 * returns OutgoingPayment(s) by accountId
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByAccountId(long accountId);
	
	/**
	 * returns OutgoingPayment(s) by accountId by date ranges
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByAccountIdByDateRange(long accountId, Calendar startDate, Calendar endDate);
	
	/**
	 * returns OutgoingPayment(s) by accountId by time ranges
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByAccountIdByTimeRange(long accountId, Date startDate, Date endDate);

	/**
	 * returns OutgoingPayment(s) by payer 
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByPayer(String payer);

	/**
	 * returns OutgoingPayment(s) by phone number
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByPhoneNo(long phoneNo);

	/**
	 * saves or updates an OutgoingPayment payment to the system
	 * */
	public void saveOrUpdateOutgoingPayment(OutgoingPayment outgoingPayment);
	
	/**
	 * removes an OutgoingPayment payment to the system
	 * */
	public void deleteOutgoingPayment(OutgoingPayment outgoingPayment);
}
