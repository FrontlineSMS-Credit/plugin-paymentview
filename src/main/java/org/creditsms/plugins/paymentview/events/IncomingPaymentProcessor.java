package org.creditsms.plugins.paymentview.events;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

public interface IncomingPaymentProcessor {
	void process(IncomingPayment payment);
}
