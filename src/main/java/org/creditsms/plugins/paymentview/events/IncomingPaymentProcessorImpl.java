package org.creditsms.plugins.paymentview.events;

import javax.swing.event.EventListenerList;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

public class IncomingPaymentProcessorImpl implements IncomingPaymentProcessor {
	private EventListenerList incomingPaymentListeners = new EventListenerList();
	
	public IncomingPaymentProcessorImpl (){
	}
	
	public void addIncomingMessageListener(IncomingPaymentListener incomingMessageListener) {
		this.incomingPaymentListeners.add(IncomingPaymentListener.class, incomingMessageListener);
	}
	
	public void removeIncomingMessageListener(IncomingPaymentListener incomingMessageListener) {
		this.incomingPaymentListeners.remove(IncomingPaymentListener.class, incomingMessageListener);
	}
	
	public void process(IncomingPayment payment) {
		for(IncomingPaymentListener listener : this.incomingPaymentListeners.getListeners(IncomingPaymentListener.class)) {
			listener.incomingPaymentReceived(payment);
		}
	}

	public void removeAllIncomingMessageListeners() {
		IncomingPaymentListener[] listeners = incomingPaymentListeners.getListeners(IncomingPaymentListener.class);
		for (IncomingPaymentListener listener : listeners){
			incomingPaymentListeners.remove(IncomingPaymentListener.class, listener);
		}
	}
}
