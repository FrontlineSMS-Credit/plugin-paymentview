package org.creditsms.plugins.paymentview.data.importexport;

import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_ACCOUNT;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_AMOUNT_PAID;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_FIRST_NAME;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_NOTES;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_OTHER_NAME;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_PHONE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_TIME_PAID;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.PAYMENT_BY;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.csv.Utf8FileWriter;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

public class PaymentViewCsvExporter extends net.frontlinesms.csv.CsvExporter {
	/**
	 * @param exportFile
	 * @param incomingPayments
	 * @param messageFormat
	 * @throws IOException
	 */
	public static void exportClients(File exportFile,
			Collection<Client> clients, AccountDao accountDao,CustomFieldDao customFieldDao,
			CustomValueDao customValueDao, CsvRowFormat clientFormat)
			throws IOException {
		LOG.trace("ENTER");
		LOG.debug("Client format [" + clientFormat + "]");
		LOG.debug("Filename [" + exportFile.getAbsolutePath() + "]");

		Utf8FileWriter out = null;

		try {
			out = new Utf8FileWriter(exportFile);
			List<CustomField> usedCustomFields = customFieldDao
					.getAllActiveUsedCustomFields();
			List<String> items = new ArrayList<String>(usedCustomFields.size());

			items.add(PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME);
			items.add(InternationalisationUtils
					.getI18nString(COMMON_FIRST_NAME));
			items.add(PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME);
			items.add(InternationalisationUtils
					.getI18nString(COMMON_OTHER_NAME));
			items.add(PaymentViewCsvUtils.MARKER_CLIENT_PHONE);
			items.add(InternationalisationUtils.getI18nString(COMMON_PHONE));

			for (CustomField cf : usedCustomFields) {
				items.add(PaymentViewUtils.getMarkerFromString(cf.getReadableName()));
				items.add(cf.getReadableName());
			}
			String[] str = new String[items.size()];
			CsvUtils.writeLine(out, clientFormat, items.toArray(str));

			for (Client client : clients) {
				List<CustomValue> allCustomValues = customValueDao
						.getCustomValuesByClientId(client.getId());

				items = new ArrayList<String>();

				items.add(PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME);
				items.add(client.getFirstName());
				items.add(PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME);
				items.add(client.getOtherName());
				items.add(PaymentViewCsvUtils.MARKER_CLIENT_PHONE);
				items.add(client.getPhoneNumber());

				if (!usedCustomFields.isEmpty()) {
					CustomField curr = null;
					Iterator<CustomField> cfi = usedCustomFields.iterator();

					for (boolean markerReplaced = false; cfi.hasNext(); markerReplaced = false) {
						curr = cfi.next();
						for (CustomValue cv : allCustomValues) {
							if (cv.getCustomField().equals(curr)) {
								items.add(PaymentViewUtils.getMarkerFromString(curr.getReadableName()));
								items.add(cv.getStrValue());
								markerReplaced = true;
							}
						}
						if (!markerReplaced) {
							items.add(PaymentViewUtils.getMarkerFromString(curr.getReadableName()));
							items.add("");
						}
					}
				}

				str = new String[items.size()];
				str = items.toArray(str);
				CsvUtils.writeLine(out, clientFormat, str);
			}
		} finally {
			if (out != null)
				out.close();
			LOG.trace("EXIT");
		}

	}

	public static void exportIncomingPayment(File exportFile,
			Collection<IncomingPayment> incomingPayments,
			CsvRowFormat incomingPaymentFormat) throws IOException {
		LOG.trace("ENTER");
		LOG.debug("IncomingPayment format [" + incomingPaymentFormat + "]");
		LOG.debug("Filename [" + exportFile.getAbsolutePath() + "]");

		Utf8FileWriter out = null;

		try {
			out = new Utf8FileWriter(exportFile);
			CsvUtils
					.writeLine(out, incomingPaymentFormat,
							PaymentViewCsvUtils.MARKER_PAYMENT_BY,
							InternationalisationUtils.getI18nString(PAYMENT_BY),
							PaymentViewCsvUtils.MARKER_INCOMING_PHONE_NUMBER,
							InternationalisationUtils
									.getI18nString(COMMON_PHONE),
							PaymentViewCsvUtils.MARKER_INCOMING_AMOUNT_PAID,
							InternationalisationUtils
									.getI18nString(COMMON_AMOUNT_PAID),
							PaymentViewCsvUtils.MARKER_INCOMING_TIME_PAID,
							InternationalisationUtils
									.getI18nString(COMMON_TIME_PAID),
							PaymentViewCsvUtils.MARKER_INCOMING_ACCOUNT,
							InternationalisationUtils
									.getI18nString(COMMON_ACCOUNT));
			for (IncomingPayment incomingPayment : incomingPayments) {
				CsvUtils.writeLine(out, incomingPaymentFormat,
						PaymentViewCsvUtils.MARKER_PAYMENT_BY,
						incomingPayment.getPaymentBy(),
						PaymentViewCsvUtils.MARKER_INCOMING_PHONE_NUMBER,
						incomingPayment.getPhoneNumber(),
						PaymentViewCsvUtils.MARKER_INCOMING_AMOUNT_PAID,
						incomingPayment.getAmountPaid().toString(),
						PaymentViewCsvUtils.MARKER_INCOMING_TIME_PAID,
						Long.toString(incomingPayment.getTimePaid()),
						PaymentViewCsvUtils.MARKER_INCOMING_ACCOUNT,
						incomingPayment.getAccount().getAccountNumber());

			}
		} finally {
			if (out != null)
				out.close();
			LOG.trace("EXIT");
		}

	}

	public static void exportOutgoingPayment(File exportFile,
			Collection<OutgoingPayment> outgoingPayments,
			CsvRowFormat outgoingPaymentFormat) throws IOException {
		LOG.trace("ENTER");
		LOG.debug("OutgoingPayment format [" + outgoingPaymentFormat + "]");
		LOG.debug("Filename [" + exportFile.getAbsolutePath() + "]");

		Utf8FileWriter out = null;

		try {
			out = new Utf8FileWriter(exportFile);
			CsvUtils
					.writeLine(out, outgoingPaymentFormat,
							PaymentViewCsvUtils.MARKER_CLIENT_NAME,
							"Name",
							PaymentViewCsvUtils.MARKER_INCOMING_PHONE_NUMBER,
							InternationalisationUtils.getI18nString(COMMON_PHONE),
							PaymentViewCsvUtils.MARKER_INCOMING_AMOUNT_PAID,
							InternationalisationUtils.getI18nString(COMMON_AMOUNT_PAID),
							PaymentViewCsvUtils.MARKER_OUTGOING_NOTES,
							InternationalisationUtils.getI18nString(COMMON_NOTES));
			
			for (OutgoingPayment outgoingPayment : outgoingPayments) {
				CsvUtils.writeLine(out, outgoingPaymentFormat,
						PaymentViewCsvUtils.MARKER_CLIENT_NAME,
						outgoingPayment.getClient().getFullName(),
						PaymentViewCsvUtils.MARKER_CLIENT_PHONE,
						outgoingPayment.getClient().getPhoneNumber(),
						PaymentViewCsvUtils.MARKER_INCOMING_AMOUNT_PAID,
						outgoingPayment.getAmountPaid().toString(),
						PaymentViewCsvUtils.MARKER_OUTGOING_NOTES,
						outgoingPayment.getNotes());

			}
		} finally {
			if (out != null)
				out.close();
			LOG.trace("EXIT");
		}

	}

}
