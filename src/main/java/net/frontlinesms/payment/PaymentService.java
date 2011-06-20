package net.frontlinesms.payment;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment.Status;

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
	
//	void makePayment(Account account, BigDecimal amount) throws PaymentServiceException;
	void makePayment(Client client, BigDecimal amount) throws PaymentServiceException;
	void checkBalance() throws PaymentServiceException;
}
