package org.creditsms.plugins.paymentview.userhomepropeties.incomingpayments;

import net.frontlinesms.resources.UserHomeFilePropertySet;

public class AutoReplyProperties extends UserHomeFilePropertySet {
	private static final String DEFAULT_VALUE = "";
	private static final String AUTO_REPLY_ON = "auto-reply-on";
	private static final String MESSAGE = "message";
	private static final AutoReplyProperties INSTANCE = new AutoReplyProperties();
	
	private AutoReplyProperties() {
		super("paymentview-incomingpayments");
	}

	public static AutoReplyProperties getInstance() {
		return INSTANCE;
	}
	
	public synchronized void setAutoReplyOn(boolean status) {
		this.setProperty(AUTO_REPLY_ON, Boolean.toString(status));
		this.saveToDisk();
	}
	
	public synchronized boolean isAutoReplyOn() {
		return this.getPropertyAsBoolean(AUTO_REPLY_ON, false);
	}
	
	public synchronized void setMessage(String message) {
		this.setProperty(MESSAGE, message);
		this.saveToDisk();
	}
	
	public synchronized String getMessage() {
		return this.getProperty(MESSAGE, DEFAULT_VALUE);
	}
	
	public synchronized void toggleAutoReply(){
		setAutoReplyOn(!this.isAutoReplyOn());
	}
}
