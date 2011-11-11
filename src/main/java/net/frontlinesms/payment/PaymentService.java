package net.frontlinesms.payment;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.domain.PaymentServiceSettings;
import org.creditsms.plugins.paymentview.userhomepropeties.payment.balance.Balance;

public interface PaymentService {
	void makePayment(Client client, OutgoingPayment outgoingPayment) throws PaymentServiceException;
	/** Trigger a balance check with the central service.  This action is asynchronous. */
	void checkBalance() throws PaymentServiceException;
	/** @return the current recorded balance of the service. */
	Balance getBalance();
	
	void startService() throws PaymentServiceException;
	void stopService();
	
	/** @return the settings attached to this {@link PaymentService} instance. */
	public PaymentServiceSettings getSettings();
	/**
	 * Initialise the service using the supplied properties.
	 * @param settings
	 */
	public void initSettings(PaymentServiceSettings settings);
	
	/** @return <code>true</code> if the service can support outgoing payments and they are enabled. */ 
	boolean isOutgoingPaymentEnabled();
}
