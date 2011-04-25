package org.creditsms.plugins.paymentview.data.repository;

import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

/**
 * @author Roy
 * */
public interface OutgoingPaymentDao {
	/**
	 * removes an OutgoingPayment payment to the system
	 * */
	public void deleteOutgoingPayment(OutgoingPayment outgoingPayment);

	/**
	 * returns all the OutgoingPayments in the system
	 * */
	public List<OutgoingPayment> getAllOutgoingPayments();
	
	public List<OutgoingPayment> getAllOutgoingPayments(int startIndex,
			int limit);
	
	/**
	 * @return
	 */
	public int getOutgoingPaymentsCount();

	/**
	 * returns OutgoingPayment(s) by clientId
	 * */
	public List<OutgoingPayment> getOutgoingPaymentByClientId(long clientId);

	/**
	 * get and return an OutgoingPayment with a specific id
	 * */
	public OutgoingPayment getOutgoingPaymentById(long outgoingPaymentId);

	/**
	 * returns OutgoingPayment(s) by accountId
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByAccountNumber(
			String accountNumber);

	/**
	 * returns OutgoingPayment(s) by accountId by time ranges
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByAccountNumberByTimeRange(
			long accountId, Date startDate, Date endDate);

	/**
	 * returns OutgoingPayment(s) by phone number
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByPhoneNo(String phoneNo);

	/**
	 * returns OutgoingPayment(s) by time range
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByTimeRange(Date startTime,
			Date endtime);

	/**
	 * saves an OutgoingPayment payment to the system
	 * */
	public void saveOutgoingPayment(OutgoingPayment outgoingPayment)
			throws DuplicateKeyException;

	/**
	 * updates an OutgoingPayment payment to the system
	 * */
	public void updateOutgoingPayment(OutgoingPayment outgoingPayment)
			throws DuplicateKeyException;

	

}
