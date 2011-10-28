package org.creditsms.plugins.paymentview.userhomepropeties.analytics;

import net.frontlinesms.resources.UserHomeFilePropertySet;

public class CreateAlertProperties extends UserHomeFilePropertySet {
	private static final String ANALYTICS_ALERT_ON = "analytics-alert-on";
	private static final String MESSAGE = "message";
	private static final String DEFAULT_VALUE = "";
	
	private static final String COMPLETESTGT_CHECKBOX_COMPONENT = "completesTgt";
	private static final String MISSEDDEADLINE_CHECKBOX_COMPONENT = "missesDeadline";
	private static final String TWOWEEKSNOPAY_CHECKBOX_COMPONENT = "twksWithoutPay";
	private static final String AMONTHWITHNOPAY_CHECKBOX_COMPONENT = "a_mnthWithoutPay";
	private static final String MEETSHALFTGT_CHECKBOX_COMPONENT = "meetsHalfTgt";
	
	private static final CreateAlertProperties INSTANCE = new CreateAlertProperties();
	
	private CreateAlertProperties() {
		super("paymentview-analytics.alert");
	}

	public static CreateAlertProperties getInstance() {
		return INSTANCE;
	}
	
	public synchronized void setAlertOn(boolean status) {
		this.setProperty(ANALYTICS_ALERT_ON, Boolean.toString(status));
		this.saveToDisk();
	}
	
	public synchronized void setMessage(String message) {
		this.setProperty(MESSAGE, message);
		this.saveToDisk();
	}
	
	public synchronized String getMessage() {
		return this.getProperty(MESSAGE, DEFAULT_VALUE);
	}

	public synchronized boolean isAlertOn() {
		return this.getPropertyAsBoolean(ANALYTICS_ALERT_ON, false);
	}
	
	public synchronized void toggleAlert(){
		setAlertOn(!this.isAlertOn());
	}
	
	public synchronized void setCompletesTgt(Boolean selected) {
		this.setPropertyAsBoolean(COMPLETESTGT_CHECKBOX_COMPONENT, selected);
		this.saveToDisk();
	}
	
	public synchronized boolean getCompletesTgt() {
		return this.getPropertyAsBoolean(COMPLETESTGT_CHECKBOX_COMPONENT,false);
	}
	
	public synchronized void setMissesDeadline(Boolean selected) {
		this.setPropertyAsBoolean(MISSEDDEADLINE_CHECKBOX_COMPONENT, selected);
		this.saveToDisk();
	}
	
	public synchronized boolean getMissesDeadline() {
		return this.getPropertyAsBoolean(MISSEDDEADLINE_CHECKBOX_COMPONENT,false);
	}
	
	public synchronized void setTwksWithoutPay(Boolean selected) {
		this.setPropertyAsBoolean(TWOWEEKSNOPAY_CHECKBOX_COMPONENT, selected);
		this.saveToDisk();
	}
	
	public synchronized boolean getTwksWithoutPay() {
		return this.getPropertyAsBoolean(TWOWEEKSNOPAY_CHECKBOX_COMPONENT,false);
	}
	
	public synchronized void setA_mnthWithoutPay(Boolean selected) {
		this.setPropertyAsBoolean(AMONTHWITHNOPAY_CHECKBOX_COMPONENT, selected);
		this.saveToDisk();
	}
	
	public synchronized boolean getA_mnthWithoutPay() {
		return this.getPropertyAsBoolean(AMONTHWITHNOPAY_CHECKBOX_COMPONENT,false);
	}
	
	public synchronized void setMeetsHalfTgt(Boolean selected) {
		this.setPropertyAsBoolean(MEETSHALFTGT_CHECKBOX_COMPONENT, selected);
		this.saveToDisk();
	}
	
	public synchronized boolean getMeetsHalfTgt() {
		return this.getPropertyAsBoolean(MEETSHALFTGT_CHECKBOX_COMPONENT,false);
	}
}
