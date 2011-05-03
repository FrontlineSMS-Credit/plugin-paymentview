package org.creditsms.plugins.paymentview.utils;

public enum PaymentType {
	INCOMING("Incoming"), 
	OUTGOING("Outgoing");
	
	private String paymenttype;
	
	PaymentType(String paymenttype){
		this.paymenttype = paymenttype;
	}
	
	public String toString() {
		return paymenttype;
	}
}
