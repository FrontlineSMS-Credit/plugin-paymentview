package org.creditsms.plugins.paymentview.data.repository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.PersistableSettings;

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
	public List<OutgoingPayment> getOutgoingPaymentsByClientId(long clientId);

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
	 * returns OutgoingPayment(s) by date range
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByDateRange(Date startDate,	Date endDate,int startIndex,int limit);
	
	/**
	 * returns OutgoingPayment(s) from start date
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByStartDate(Date startTime, int startingIndex, int limit);
	
	/**
	 * returns OutgoingPayment(s) until end date
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByEndDate(Date endTime, int startingIndex, int limit);
	
	/**
	 * returns OutgoingPayment(s) by time range
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByDateRange(Date startTime,
			Date endtime);

	/**
	 * returns OutgoingPayment(s) from start date
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByStartDate(Date startTime);
	
	/**
	 * returns OutgoingPayment(s) until end date
	 * */
	public List<OutgoingPayment> getOutgoingPaymentsByEndDate(Date endTime);
	
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
	 * returns OutgoingPayment(s) by phone number and amount paid (Time paid desc)
	 * */
	public List<OutgoingPayment> getByPhoneNumberAndAmountPaid(String phoneNo,
			BigDecimal amountPaid, OutgoingPayment.Status status);
	
	/**
	 * returns OutgoingPayment(s) by amount paid and status (Time paid desc)
	 * */
	public List<OutgoingPayment> getByAmountPaidAndStatus(BigDecimal amountPaid,
			OutgoingPayment.Status status);
	
	/**
	 * returns OutgoingPayment(s) by amount paid and client inactive (Time paid desc)
	 * */
	public List<OutgoingPayment> getOutgoingPaymentByAmountPayBillNameAndAccountNo(String payBillName, String accountNum, BigDecimal amountPaid, OutgoingPayment.Status status);
	
	/**
	 * returns OutgoingPayment(s) by payment service settings
	 * */
	public List<OutgoingPayment> getByPaymentServiceSettings(PersistableSettings paymentServiceSettings);
	
	/**
	 * saves an OutgoingPayment payment to the system
	 * */
	public void saveOutgoingPayment(OutgoingPayment outgoingPayment);

	/**
	 * updates an OutgoingPayment payment to the system
	 * */
	public void updateOutgoingPayment(OutgoingPayment outgoingPayment)
			throws DuplicateKeyException;



}
