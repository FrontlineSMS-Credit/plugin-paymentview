package net.frontlinesms.plugins.payment.event;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.payment.service.PaymentService;

public class PaymentServiceStoppedNotification implements FrontlineEventNotification {
private PaymentService paymentService;
	
	public PaymentServiceStoppedNotification(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}
}
