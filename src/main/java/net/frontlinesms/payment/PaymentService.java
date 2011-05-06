package net.frontlinesms.payment;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.events.IncomingPaymentProcessor;

public interface PaymentService {
	void setIncomingPaymentProcessor(IncomingPaymentProcessor incomingPaymentProcessor) throws PaymentServiceException;
	void makePayment(Account account, BigDecimal amount) throws PaymentServiceException;
	void checkBalance() throws PaymentServiceException;
}
