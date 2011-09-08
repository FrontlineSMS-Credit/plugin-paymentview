package net.frontlinesms.payment;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.domain.PaymentServiceSettings;

public interface PaymentService {
	String getPin();
	void setPin(String pin);
	
	public static enum PaymentServiceType {
		SAFARICOMMPESA("Safaricom Mpesa","MpesaPersonalService");

		private String displayName;
		private String type;

		private PaymentServiceType(String displayName, String type) {
			this.displayName = displayName;
			this.type = type;
		}

		@Override
		public String toString() {
			return displayName;
		}

		public String getType() {
			return type;
		}
	}
	
	void makePayment(Client client, OutgoingPayment outgoingPayment) throws PaymentServiceException;
	void checkBalance() throws PaymentServiceException;
	void configureModem() throws PaymentServiceException;
	void stop();
	
	/** @return the settings attached to this {@link PaymentService} instance. */
	public PaymentServiceSettings getSettings();

	/**
	 * Initialise the service using the supplied properties.
	 * @param settings
	 */
	public void setSettings(PaymentServiceSettings settings);
}
