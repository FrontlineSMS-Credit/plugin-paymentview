package net.frontlinesms.payment;

import java.math.BigDecimal;

import net.frontlinesms.data.ConfigurableService;
import net.frontlinesms.data.domain.PersistableSettings;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

public interface PaymentService extends ConfigurableService {
	void makePayment(Client client, OutgoingPayment outgoingPayment) throws PaymentServiceException;
	/** Trigger a balance check with the central service.  This action is asynchronous. */
	void checkBalance() throws PaymentServiceException;
	/** @return the current recorded balance of the service. */
	BigDecimal getBalanceAmount();
	
	void startService() throws PaymentServiceException;
	void stopService();
	
	/**
	 * Initialise the service using the supplied properties.
	 * @param settings
	 */
	public void initSettings(PersistableSettings settings);
	
	/** @return <code>true</code> if the service can support outgoing payments and they are enabled. */ 
	boolean isOutgoingPaymentEnabled();
}
