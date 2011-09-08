package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;

public enum FormatterMarkerType {
	CLIENT_NAME("CLIENT_NAME", PaymentViewCsvUtils.CLIENT_NAME),
	RECEPIENT_NAME("RECEPIENT_NAME", PaymentViewCsvUtils.RECEPIENT_NAME),
	AMOUNT_PAID("AMOUNT_PAID", PaymentViewCsvUtils.AMOUNT_PAID),
	DATE_PAID("DATE_PAID", PaymentViewCsvUtils.DATE_PAID),
	AMOUNT_REMAINING("AMOUNT_REMAINING", PaymentViewCsvUtils.AMOUNT_REMAINING),
	TARGET_ENDDATE("TARGET_ENDDATE", PaymentViewCsvUtils.TARGET_ENDDATE),
	DAYS_REMAINING("DAYS_REMAINING", PaymentViewCsvUtils.DAYS_REMAINING),
	MONTHLY_SAVINGS("MONTHLY_SAVINGS", PaymentViewCsvUtils.MONTHLY_SAVINGS), 
	MONTHLY_DUE("MONTHLY_DUE", PaymentViewCsvUtils.MONTHLY_DUE),
	END_MONTH_INTERVAL("MONTHLY_DUEDATE", PaymentViewCsvUtils.END_MONTH_INTERVAL);
	
	private String name;
	private String marker;
	
	FormatterMarkerType(String name, String marker){
		this.name = name;
		this.marker = marker;
	}
	
	@Override
	public String toString() {
		return name;
	}
	
	public String getMarker(){
		return marker;
	}
}
