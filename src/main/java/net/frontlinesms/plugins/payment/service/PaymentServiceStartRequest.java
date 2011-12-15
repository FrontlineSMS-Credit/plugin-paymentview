package net.frontlinesms.plugins.payment.service;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.events.FrontlineEventNotification;

public class PaymentServiceStartRequest implements FrontlineEventNotification {
	private final PersistableSettings settings;
	
	public PaymentServiceStartRequest(PersistableSettings settings) {
		this.settings = settings;
	}
	
	public PersistableSettings getSettings() {
		return settings;
	}
}
