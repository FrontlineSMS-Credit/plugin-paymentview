package org.creditsms.plugins.paymentview.data.repository;

import java.util.Date;
import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

/**
 * @author Roy
 * @author Ian
 * */
public interface IncomingPaymentDao {
	/**
	 * removes an IncomingPayment payment to the system
	 * */
	public void deleteIncomingPayment(IncomingPayment incomingPayment);

	/**
	 * returns all the incomingPayments in the system
	 * */
	public List<IncomingPayment> getAllIncomingPayments();
	
	/**
	 * returns all the incomingPayments in the system
	 * */
	public int getIncomingPaymentsCount();

	/**
	 * returns all the incomingPayments in the system
	 * 
	 * @param startingIndex
	 * @param limit
	 * @return
	 */
	public List<IncomingPayment> getAllIncomingPayments(int startingIndex,
			int limit);

	/**
	 * returns IncomingPayment(s) by clientId
	 * */
	public List<IncomingPayment> getIncomingPaymentByClientId(long clientId);

	/**
	 * get and return an IncomingPayment with a specific id
	 * */
	public IncomingPayment getIncomingPaymentById(long incomingPaymentId);

	/**
	 * returns IncomingPayment(s) by accountId
	 * */
	public List<IncomingPayment> getIncomingPaymentsByAccountNumber(
			String accountNumber, long startDate, long endDate);

	/**
	 * returns the last IncomingPayment(s) by accountId
	 * */
	public Long getLastIncomingPaymentDateByAccountNumber(
			String accountNumber);
	
	/**
	 * returns IncomingPayment(s) by accountId by time ranges
	 * */
	public List<IncomingPayment> getIncomingPaymentsByAccountNumberByTimeRange(
			long accountId, Date startDate, Date endDate);

	/**
	 * returns IncomingPayment(s) by payer
	 * */
	public List<IncomingPayment> getIncomingPaymentsByPayer(String payer);

	/**
	 * returns IncomingPayment(s) by phone number
	 * */
	public List<IncomingPayment> getIncomingPaymentsByPhoneNo(String phoneNo);

	/**
	 * returns IncomingPayment(s) by time range
	 * */
	public List<IncomingPayment> getIncomingPaymentsByDateRange(Date startTime,
			Date endtime);
	
	/**
	 * @param date
	 * @return
	 */
	public List<IncomingPayment> getIncomingPaymentsByDate(Date date);

	/**
	 * saves an IncomingPayment payment to the system
	 * */
	public void saveIncomingPayment(IncomingPayment incomingPayment);

	/**
	 * updates an IncomingPayment payment to the system
	 * */
	public void updateIncomingPayment(IncomingPayment incomingPayment);
}
