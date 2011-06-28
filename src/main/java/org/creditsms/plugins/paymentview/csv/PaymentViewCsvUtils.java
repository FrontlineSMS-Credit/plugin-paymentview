package org.creditsms.plugins.paymentview.csv;

import net.frontlinesms.csv.CsvUtils;

public class PaymentViewCsvUtils extends CsvUtils {

	public static final String MARKER_CLIENT_ACCOUNTS = "${accounts}";
	public static final String MARKER_CLIENT_FIRST_NAME = "${first_name}";
	public static final String MARKER_CLIENT_OTHER_NAME = "${other_name}";
	public static final String MARKER_CLIENT_PHONE = "${phone_number}";
	public static final String MARKER_INCOMING_ACCOUNT = "${account}";
	public static final String MARKER_INCOMING_AMOUNT_PAID = "${amount_paid}";
	public static final String MARKER_INCOMING_DATE_PAID = "${date_paid}";
	public static final String MARKER_INCOMING_PAYMENT_BY = "${payment_by}";
	public static final String MARKER_INCOMING_PHONE_NUMBER = "${phone_number}";
	public static final String MARKER_INCOMING_TIME_PAID = "${time_paid}";
	public static final String MARKER_OUTGOING_CONFIRMATION = "${confirmation}";
	public static final String MARKER_OUTGOING_NOTES = "${notes}";
	public static final String MARKER_PAYMENT_BY = "${payment_by}";
	public static final String MARKER_CLIENT_NAME = "${client_name}";
}
