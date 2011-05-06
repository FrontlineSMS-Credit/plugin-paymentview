package org.creditsms.plugins.paymentview.events;
import java.util.EventListener;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

public interface IncomingPaymentListener extends EventListener{
	public void incomingPaymentReceived(IncomingPayment i);
}
