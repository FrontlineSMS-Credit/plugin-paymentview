package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import net.frontlinesms.csv.CsvRowFormat;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Payment;

public class CsvExporter extends net.frontlinesms.csv.CsvExporter {
	/**
	 * @param exportFile
	 * @param clients
	 * @param messageFormat
	 * @throws IOException
	 */
	public static void exportClients(File exportFile,
			Collection<Client> clients, CsvRowFormat messageFormat)
			throws IOException {

	}

	public static void exportPayments(File exportFile,
			Collection<Payment> payments, CsvRowFormat messageFormat)
			throws IOException {

	}
}
