package org.creditsms.plugins.paymentview.analytics;

import java.util.Calendar;

public class PaymentDateSettings {
	public Calendar setEndOfDayFormat(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 24);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, -1);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	public Calendar setStartOfDay(Calendar cal) {
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
}
