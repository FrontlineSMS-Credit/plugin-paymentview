package net.frontlinesms.payment;

import net.frontlinesms.events.FrontlineEventNotification;

public class PaymentServiceStoppedNotification implements FrontlineEventNotification {
private PaymentService paymentService;
	
	public PaymentServiceStoppedNotification(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}
}
