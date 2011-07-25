package net.frontlinesms.payment;

import net.frontlinesms.events.FrontlineEventNotification;

public class PaymentServiceStartedNotification implements FrontlineEventNotification {

	private PaymentService paymentService;
	
	public PaymentServiceStartedNotification(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}
}
