package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.PaymentService;

public interface PaymentServiceDao {
	/**
	 * Gets the list of all payment services in the system
	 * @return
	 */
	public List<PaymentService> getAllPaymentServices();
	
	/**
	 * Gets the list of payment services from a particular start index with a maximum number of payment services
	 * in the set
	 * @param startIndex
	 * @param limit
	 * @return
	 */
	public List<PaymentService> getAllPaymentServices(int startIndex, int limit);
	
	/**
	 * Gets the payment service with the specified name or <code>null</code> if none exists
	 * @param name Name of the payment service to be retrieved
	 * @return  {@link PaymentService} with the specified name or <code>null</code> if none exists
	 */
	public PaymentService getPaymentServiceByName(String name);
	
	/**
	 * Gets the payment service that uses the specifed short code or <code>null/</code> if none exists
	 * @param shortCode short code for the payment service
	 * @return {@link PaymentService} with the specified name or <code>null</code> if none exists
	 */
	public PaymentService getPaymentServiceByShortCode(String shortCode);
	
	/**
	 * Gets the number of payment services in the system
	 * @return
	 */
	public int getPaymentServicesCount();
	
	/**
	 * Saves a new payment service
	 * @param service payment service to be created
	 * @throws DuplicateKeyException if a payment service with the specified name already exists
	 */
	public void savePaymentService(PaymentService service) throws DuplicateKeyException;
	
	/**
	 * Updates the details of a payment service
	 * @param service payment service to be updated
	 * @throws DuplicateKeyException if a payment service with the specified name already exists
	 */
	public void updatePaymentService(PaymentService service) throws DuplicateKeyException;
	
	/**
	 * Deletes a payment service from the system
	 * @param service payment service to be deleted
	 * @param destroyTransactions where the associated transactions are also to be deleted
	 */
	public void deletePaymentService(PaymentService service, boolean destroyTransactions);
}
