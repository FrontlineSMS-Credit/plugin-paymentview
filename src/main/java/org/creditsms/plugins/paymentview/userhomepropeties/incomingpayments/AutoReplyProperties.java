package org.creditsms.plugins.paymentview.userhomepropeties.incomingpayments;

import net.frontlinesms.resources.UserHomeFilePropertySet;

public class AutoReplyProperties extends UserHomeFilePropertySet {
	private static final String MESSAGE = "message";
	private static final AutoReplyProperties INSTANCE = new AutoReplyProperties();
	
	private AutoReplyProperties() {
		super("paymentview-incomingpayments");
	}

	public static AutoReplyProperties getInstance() {
		return INSTANCE;
	}
	
	public void setMessage(String message) {
		this.setProperty(MESSAGE, message);
		this.saveToDisk();
	}
	
	public String getMessage() {
		return this.getProperty(MESSAGE);
	}
}
