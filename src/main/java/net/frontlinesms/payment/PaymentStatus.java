package net.frontlinesms.payment;

public enum PaymentStatus {
	// TODO i18n all messages here
	SENDING("Sending payment(s) ..."),
	COMPLETE("Payment Process completed."),
	RECEIVING("Processing Incoming Payment ..."),
	PROCESSED("New Incoming Payment has been received."),
	CHECK_BALANCE("Checking Balance ..."),
	CHECK_COMPLETE("Check Balance Complete."),
	CONFIGURE_STARTED("Configuring Modem ..."),
	CONFIGURE_COMPLETE("Modem Configuration Complete."),
	PAYMENTSERVICE_OFF("Payment Service Not Setup."),
	PAYMENTSERVICE_ON("Payment Service is Set Up."),
	
	ERROR("Error occurred.");
	
	private final String statusMessage;

	PaymentStatus(String statusMessage) {
		this.statusMessage = statusMessage;
	}
	
	@Override
	public String toString() {
		return statusMessage; // FIXME replace this with getI18nKey() method
	}
}
