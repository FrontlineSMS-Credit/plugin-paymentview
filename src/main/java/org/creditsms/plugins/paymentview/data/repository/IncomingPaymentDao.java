package org.creditsms.plugins.paymentview.data.repository;

import java.util.Date;
import java.util.List;

import net.frontlinesms.data.domain.PersistableSettings;

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
	public List<IncomingPayment> getActiveIncomingPayments();
	
	/**
	 * gets Outgoing payments by Confirmation code
	 * @param confirmationCode
	 */
	public IncomingPayment getByConfirmationCode(String confirmationCode);

	/**
	 * returns all the active incomingPayments count
	 * */
	public int getActiveIncomingPaymentsCount();

	/**
	 * returns all the active incomingPayments in the system
	 * 
	 * @param startingIndex
	 * @param limit
	 * @return
	 */
	public List<IncomingPayment> getActiveIncomingPayments(int startingIndex, int limit);
	
	/**
	 * returns IncomingPayment(s) by clientId
	 * */
	public List<IncomingPayment> getActiveIncomingPaymentByClientId(long clientId);

	/**
	 * get and return an IncomingPayment with a specific id
	 * */
	public IncomingPayment getActiveIncomingPaymentById(long incomingPaymentId);

	/**
	 * returns IncomingPayment(s) by targetId
	 * */
	public List<IncomingPayment> getActiveIncomingPaymentsByTarget(
			long targetId);
	
	/**
	 * returns IncomingPayment(s) by targetId and dates
	 * */
	public List<IncomingPayment> getActiveIncomingPaymentsByTargetAndDates(
			long targetId, Date startDate, Date endDate);

	/**
	 * returns IncomingPayment(s) by targetId and dates
	 * */
	public List<IncomingPayment> getIncomingPaymentsByTargetAndDates(
			long targetId, Date startDate, Date endDate);
	
	/**
	 * returns IncomingPayment(s) by accountId
	 * */
	public List<IncomingPayment> getActiveIncomingPaymentsByAccountNumber(
			String accountNo);

	/**
	 * returns the last IncomingPayment(s) by accountId
	 * */
	public List<IncomingPayment> getActiveIncomingPaymentsByAccountNumberOrderByTimepaid(
			String accountNumber);
	
	public List<IncomingPayment> getActiveIncomingPaymentsByAccountNumberBtwnDateOrderByTimepaid(
			String accountNumber, Date startDate, Date endDate);
	
	/**
	 * returns IncomingPayment(s) by accountId by time ranges
	 * */
	public List<IncomingPayment> getIncomingPaymentsByAccountNumberByTimeRange(
			long accountId, Date startDate, Date endDate);

	/**
	 * returns IncomingPayment(s) by payer
	 * */
	public List<IncomingPayment> getActiveIncomingPaymentsByPayer(String payer);

	/**
	 * returns IncomingPayment(s) by phone number
	 * */
	public List<IncomingPayment> getActiveIncomingPaymentsByPhoneNo(String phoneNo);

	/**
	 * returns IncomingPayment(s) by time range
	 * */
	public List<IncomingPayment> getIncomingPaymentsByDateRange(Date startTime,
			Date endtime, int startingIndex, int limit);
	
	/**
	 * returns IncomingPayment(s) from start date
	 * */
	public List<IncomingPayment> getIncomingPaymentsByStartDate(Date startTime, int startingIndex, int limit);
	
	/**
	 * returns IncomingPayment(s) until end date
	 * */
	public List<IncomingPayment> getIncomingPaymentsByEndDate(Date endTime, int startingIndex, int limit);
	
	/**
	 * returns IncomingPayment(s) by time range
	 * */
	public List<IncomingPayment> getIncomingPaymentsByDateRange(Date startTime,
			Date endtime);
	
	/**
	 * returns IncomingPayment(s) from start date
	 * */
	public List<IncomingPayment> getIncomingPaymentsByStartDate(Date startTime);
	
	/**
	 * returns IncomingPayment(s) until end date
	 * */
	public List<IncomingPayment> getIncomingPaymentsByEndDate(Date endTime);
	
	/**
	 * @param date
	 * @return
	 */
	public List<IncomingPayment> getIncomingPaymentsByDate(Date date);
	
	/**
	 * returns IncomingPayment(s) by payment service settings
	 * */
	public List<IncomingPayment> getByPaymentServiceSettings(PersistableSettings paymentServiceSettings);

	/**
	 * saves an IncomingPayment payment to the system
	 * */
	public void saveIncomingPayment(IncomingPayment incomingPayment);

	/**
	 * updates an IncomingPayment payment to the system
	 * */
	public void updateIncomingPayment(IncomingPayment incomingPayment);
}
