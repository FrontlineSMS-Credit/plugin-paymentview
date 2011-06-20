package net.frontlinesms.payment;

@SuppressWarnings("serial")
public class PaymentServiceException extends Exception {
	public PaymentServiceException(Exception cause) {
		super(cause);
	}

	public PaymentServiceException(String message) {
		super(message);
	}
}
