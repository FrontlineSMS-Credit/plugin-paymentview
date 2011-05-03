package org.creditsms.plugins.paymentview.data.importexport;

import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.ACCOUNTS_DELIMITER;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_ACCOUNT;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_ACCOUNTS;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_AMOUNT_PAID;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_CONFIRMATION;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_FIRST_NAME;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_NOTES;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_OTHER_NAME;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_PHONE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_TIME_PAID;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.csv.Utf8FileWriter;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

public class PaymentViewCsvExporter extends net.frontlinesms.csv.CsvExporter {
	/**
	 * @param exportFile
	 * @param incomingPayments
	 * @param messageFormat
	 * @throws IOException
	 */
	public static void exportClients(File exportFile,
			Collection<Client> clients, CsvRowFormat clientFormat)
			throws IOException {
		LOG.trace("ENTER");
		LOG.debug("Client format [" + clientFormat + "]");
		LOG.debug("Filename [" + exportFile.getAbsolutePath() + "]");

		Utf8FileWriter out = null;

		try {
			out = new Utf8FileWriter(exportFile);
			PaymentViewCsvUtils.writeLine(out, clientFormat,
					PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME,
					InternationalisationUtils.getI18nString(COMMON_FIRST_NAME),
					PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME,
					InternationalisationUtils.getI18nString(COMMON_OTHER_NAME),
					PaymentViewCsvUtils.MARKER_CLIENT_PHONE,
					InternationalisationUtils.getI18nString(COMMON_PHONE),
					PaymentViewCsvUtils.MARKER_CLIENT_ACCOUNTS,
					InternationalisationUtils.getI18nString(COMMON_ACCOUNTS));
			for (Client client : clients) {
				PaymentViewCsvUtils.writeLine(out, clientFormat,
						PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME, client
								.getFirstName(),
						PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME, client
								.getOtherName(),
						PaymentViewCsvUtils.MARKER_CLIENT_PHONE, client
								.getPhoneNumber(),
						PaymentViewCsvUtils.MARKER_CLIENT_ACCOUNTS,
						PaymentViewUtils.accountsAsString(client.getAccounts(),
								ACCOUNTS_DELIMITER));
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
			PaymentViewCsvUtils
					.writeLine(out, incomingPaymentFormat,
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
				PaymentViewCsvUtils.writeLine(out, incomingPaymentFormat,
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
			PaymentViewCsvUtils
					.writeLine(out, outgoingPaymentFormat,
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
									.getI18nString(COMMON_ACCOUNT),
							PaymentViewCsvUtils.MARKER_OUTGOING_NOTES,
							InternationalisationUtils
									.getI18nString(COMMON_NOTES),
							PaymentViewCsvUtils.MARKER_OUTGOING_CONFIRMATION,
							InternationalisationUtils
									.getI18nString(COMMON_CONFIRMATION));
			for (OutgoingPayment outgoingPayment : outgoingPayments) {
				PaymentViewCsvUtils.writeLine(out, outgoingPaymentFormat,
						PaymentViewCsvUtils.MARKER_INCOMING_PHONE_NUMBER,
						outgoingPayment.getPhoneNumber(),
						PaymentViewCsvUtils.MARKER_INCOMING_AMOUNT_PAID,
						outgoingPayment.getAmountPaid().toString(),
						PaymentViewCsvUtils.MARKER_INCOMING_TIME_PAID,
						Long.toString(outgoingPayment.getTimePaid()),
						PaymentViewCsvUtils.MARKER_INCOMING_ACCOUNT,
						outgoingPayment.getAccount().getAccountNumber(),
						PaymentViewCsvUtils.MARKER_OUTGOING_NOTES,
						outgoingPayment.getNotes(),
						PaymentViewCsvUtils.MARKER_OUTGOING_CONFIRMATION,
						outgoingPayment.getStatus().toString());

			}
		} finally {
			if (out != null)
				out.close();
			LOG.trace("EXIT");
		}

	}

}
