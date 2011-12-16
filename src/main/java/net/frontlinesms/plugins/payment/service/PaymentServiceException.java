package net.frontlinesms.plugins.payment.service;

@SuppressWarnings("serial")
public class PaymentServiceException extends Exception {
	public PaymentServiceException(Exception cause) {
		super(cause);
	}

	public PaymentServiceException(String message) {
		super(message);
	}

	public PaymentServiceException(String message, Exception cause) {
		super(message, cause);
	}
}
