package net.frontlinesms.payment.event;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.PaymentService;

public class PaymentServiceStartedNotification implements FrontlineEventNotification {

	private PaymentService paymentService;
	
	public PaymentServiceStartedNotification(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}
}
