package org.creditsms.plugins.paymentview.data.repository;

import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

/**
 * @author Roy
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
			long accountId);

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
	public List<IncomingPayment> getIncomingPaymentsByTimeRange(Date startTime,
			Date endtime);

	/**
	 * saves an IncomingPayment payment to the system
	 * */
	public void saveIncomingPayment(IncomingPayment incomingPayment)
			throws DuplicateKeyException;

	/**
	 * updates an IncomingPayment payment to the system
	 * */
	public void updateIncomingPayment(IncomingPayment incomingPayment)
			throws DuplicateKeyException;
}
