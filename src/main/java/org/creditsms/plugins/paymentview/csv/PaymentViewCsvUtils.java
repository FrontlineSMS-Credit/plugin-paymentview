package org.creditsms.plugins.paymentview.csv;

import net.frontlinesms.csv.CsvUtils;

public class PaymentViewCsvUtils extends CsvUtils {

	public static final String MARKER_CLIENT_ACCOUNTS = "${accounts}";
	public static final String MARKER_CLIENT_FIRST_NAME = "${first_name}";
	public static final String MARKER_CLIENT_OTHER_NAME = "${other_name}";
	public static final String MARKER_CLIENT_PHONE = "${phone_number}";
	public static final String MARKER_AMOUNT_PAID = "${amount_paid}";
	public static final String MARKER_INCOMING_ACCOUNT = "${account}";
	public static final String MARKER_INCOMING_DATE_PAID = "${date_paid}";
	public static final String MARKER_INCOMING_PAYMENT_BY = "${payment_by}";
	public static final String MARKER_PHONE_NUMBER = "${phone_number}";
	public static final String MARKER_TIME_PAID = "${time_paid}";
	public static final String MARKER_OUTGOING_STATUS = "${status}";
	public static final String MARKER_PAYMENT_ID = "${payment_id}";
	public static final String MARKER_INCOMING_CONFIRMATION_CODE = "${confirmation}";
	public static final String MARKER_OUTGOING_CONFIRMATION_CODE = "${confirmation}";
	public static final String MARKER_OUTGOING_AMOUNT_TO_PAY = "${amount_to_pay}";
	public static final String MARKER_NOTES = "${notes}";
	public static final String MARKER_NAME = "${payment_by}";
	public static final String MARKER_CLIENT_NAME = "${client_name}";

	public static final String CLIENT_NAME = "${client_name}";
	public static final String AMOUNT_PAID = "${amount_paid}";
	public static final String AMOUNT_REMAINING = "${amount_remaining}";
	public static final String DATE_PAID = "${date_paid}";
	public static final String DAYS_REMAINING = "${days_remaining}";
	public static final String MONTHLY_DUE = "${current_amount_due}";
	public static final String END_MONTH_INTERVAL = "${current_due_date}";
	public static final String MONTHLY_SAVINGS = "${amount_paid_this_month}";
	public static final String RECEPIENT_NAME = "${recipient_name}";
	public static final String TARGET_ENDDATE = "${target_enddate}";
	
	
}
