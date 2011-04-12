package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;

import net.frontlinesms.csv.CsvImportReport;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.csv.CsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

/**
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
public class IncomingPaymentCsvImporter extends CsvImporter {
	/** The delimiter to use between group names when they are exported. */

	// > INSTANCE PROPERTIES

	// > CONSTRUCTORS
	public IncomingPaymentCsvImporter(File importFile) throws CsvParseException {
		super(importFile);
	}

	// > IMPORT METHODS
	/**
	 * Import Payments from a CSV file.
	 * 
	 * @param importFile
	 *            the file to import from
	 * @param contactDao
	 *            ; paymentDao
	 * @param rowFormat
	 * @throws IOException
	 *             If there was a problem accessing the file
	 * @throws CsvParseException
	 *             If there was a problem with the format of the file
	 */
	public CsvImportReport importIncomingPayments(IncomingPaymentDao incomingPaymentDao,
			AccountDao accountDao, CsvRowFormat rowFormat) {
		log.trace("ENTER");

		for (String[] lineValues : this.getRawValues()) {
			String paymentBy = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_PAYMENT_BY);
			String phoneNumber = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_PHONE_NUMBER);
			String amountPaid = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_AMOUNT_PAID);
			String timePaid = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_TIME_PAID);
			String account = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_ACCOUNT);

			Account acc;

			try {
				acc = new Account(Long.parseLong(account));
				accountDao.saveUpdateAccount(acc);
			} catch (DuplicateKeyException e) {
				// FIXME should actually pass details of this back to the user.
				log.debug("An Account already exist with this number", e);
				acc = accountDao.getAccountByAccountNumber(Long
						.parseLong(account));
			}

			IncomingPayment incomingPayment = new IncomingPayment(paymentBy,
					phoneNumber, new BigDecimal(amountPaid), new Date(
							Long.parseLong(timePaid)), acc);
			try {
				incomingPaymentDao.saveIncomingPayment(incomingPayment);
			} catch (DuplicateKeyException e) {
				// FIXME should actually pass details of this back to the user.
				log.debug("An incoming Payment already exist with this id", e);
			}

		}

		log.trace("EXIT");

		return new CsvImportReport();
	}
 
	
}
