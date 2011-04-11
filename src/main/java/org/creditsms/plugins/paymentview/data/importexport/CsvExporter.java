package org.creditsms.plugins.paymentview.data.importexport;

import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_ACCOUNTS;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_FIRST_NAME;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_OTHER_NAME;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_PHONE;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.csv.Utf8FileWriter;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.csv.CsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.utils.FrontlineUtils;

public class CsvExporter extends net.frontlinesms.csv.CsvExporter {
	private static final String ACCOUNTS_DELIMITER = ",";

	/**
	 * @param exportFile
	 * @param clients
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
			CsvUtils.writeLine(out, clientFormat,
					CsvUtils.MARKER_CLIENT_FIRST_NAME, InternationalisationUtils.getI18nString(COMMON_FIRST_NAME),
					CsvUtils.MARKER_CLIENT_OTHER_NAME, InternationalisationUtils.getI18nString(COMMON_OTHER_NAME),
					CsvUtils.MARKER_CLIENT_PHONE, InternationalisationUtils.getI18nString(COMMON_PHONE),
					CsvUtils.MARKER_CLIENT_ACCOUNTS, InternationalisationUtils.getI18nString(COMMON_ACCOUNTS));
			for (Client client : clients) {
				CsvUtils.writeLine(out, clientFormat, 
					CsvUtils.MARKER_CLIENT_FIRST_NAME, client.getFirstName(),
					CsvUtils.MARKER_CLIENT_OTHER_NAME, client.getOtherName(),
					CsvUtils.MARKER_CLIENT_PHONE, client.getPhoneNumber(),
					CsvUtils.MARKER_CLIENT_ACCOUNTS, FrontlineUtils.accountsAsString(client.getAccounts(), ACCOUNTS_DELIMITER)); 
			}
		} finally {
			if(out!= null) out.close();
			LOG.trace("EXIT");
		}

	}

	public static void exportIncomingPayments(File exportFile,
			Collection<IncomingPayment> payments, CsvRowFormat messageFormat)
			throws IOException {

	}
	
	public static void exportOutcomingPayments(File exportFile,
			Collection<OutgoingPayment> payments, CsvRowFormat messageFormat)
			throws IOException {

	}
}
