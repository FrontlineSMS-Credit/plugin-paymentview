package net.frontlinesms.plugins.payment.event;

import net.frontlinesms.events.FrontlineEventNotification;

public class BalanceFraudNotification implements FrontlineEventNotification{
	private final String message;
	public BalanceFraudNotification(String message){this.message=message;}
	public String getMessage(){return message;}
}