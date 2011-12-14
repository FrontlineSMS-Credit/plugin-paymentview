package net.frontlinesms.plugins.payment.service;

import net.frontlinesms.events.FrontlineEventNotification;

public class PaymentServiceStartRequest implements FrontlineEventNotification {
	private final long settingsId;
	
	public PaymentServiceStartRequest(long settingsId) {
		this.settingsId = settingsId;
	}
	
	public long getSettingsId() {
		return settingsId;
	}
}
