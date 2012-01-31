package net.frontlinesms.plugins.payment.service;

import java.math.BigDecimal;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.plugins.payment.service.ui.PaymentServiceUiActionHandler;
import net.frontlinesms.serviceconfig.ConfigurableService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

public interface PaymentService extends ConfigurableService {
	void makePayment(OutgoingPayment payment) throws PaymentServiceException;
	/** Trigger a balance check with the central service.  This action is asynchronous. */
	void checkBalance() throws PaymentServiceException;
	/** @return the current recorded balance of the service. */
	BigDecimal getBalanceAmount();
	
	void init(PaymentViewPluginController pluginController) throws PaymentServiceException;
	
	void startService() throws PaymentServiceException;
	void stopService();
	
	/** @return <code>true</code> if the service can support outgoing payments and they are enabled. */ 
	boolean isOutgoingPaymentEnabled();
	
	/** @return <code>true</code> if the service can support check balance and they are enabled. */ 
	boolean isCheckBalanceEnabled();
	
	/** @return action handler for additional service-specific user-triggered actions, or <code>null</code> if there are no additional actions. */
	PaymentServiceUiActionHandler getServiceActionUiHandler(UiGeneratorController ui);
	
	/** @return <code>true</code> if the service should be restarted were the new settings applied; <code>false</code> otherwise. */
	boolean isRestartRequired(PersistableSettings newSettings);
}
