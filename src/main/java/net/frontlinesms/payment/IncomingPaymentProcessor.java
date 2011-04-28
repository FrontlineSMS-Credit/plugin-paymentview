package net.frontlinesms.payment;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

public interface IncomingPaymentProcessor {
	void process(IncomingPayment payment);
}
