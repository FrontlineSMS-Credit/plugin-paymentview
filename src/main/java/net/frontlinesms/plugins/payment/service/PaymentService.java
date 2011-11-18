package net.frontlinesms.plugins.payment.service;

import java.math.BigDecimal;

import net.frontlinesms.serviceconfig.ConfigurableService;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

public interface PaymentService extends ConfigurableService {
	void makePayment(OutgoingPayment payment) throws PaymentServiceException;
	/** Trigger a balance check with the central service.  This action is asynchronous. */
	void checkBalance() throws PaymentServiceException;
	/** @return the current recorded balance of the service. */
	BigDecimal getBalanceAmount();
	
	void startService() throws PaymentServiceException;
	void stopService();
	
	/** @return <code>true</code> if the service can support outgoing payments and they are enabled. */ 
	boolean isOutgoingPaymentEnabled();
}
