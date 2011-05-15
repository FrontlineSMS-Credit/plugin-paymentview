package net.frontlinesms.payment;

import net.frontlinesms.events.EventObserver;

public interface PaymentServiceAccount extends EventObserver, PaymentService{
	public enum PaymentAccountType{
		PAYBILL("Paybill"),
		PERSONAL("Personal");
		
		private final String type;
		
		PaymentAccountType(String type){ this.type = type; }
		public String getType() { return this.type; }
	}
	
	String getName();
	void setName(String name);
	
	String getOperator();
	void setOperator(String operator);
	
	String getPin();
	void setPin(String pin);
	
	String getGeography();
	void setGeography(String geography);
	
	PaymentAccountType getPaymentAccountType();
	void setPaymentAccountType(PaymentAccountType paymentAccountType);
}