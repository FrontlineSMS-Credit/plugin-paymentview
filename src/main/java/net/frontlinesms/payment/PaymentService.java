package net.frontlinesms.payment;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.Account;

public interface PaymentService {
	void setIncomingPaymentProcessor(IncomingPaymentProcessor incomingPaymentProcessor);
	void makePayment(Account account, BigDecimal amount);
	void checkBalance();
}
