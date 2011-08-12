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

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;

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
	public CsvImportReport importIncomingPayments(
			IncomingPaymentDao incomingPaymentDao, AccountDao accountDao, TargetDao targetDao,
			CsvRowFormat rowFormat) {
		log.trace("ENTER");

		for (String[] lineValues : this.getRawValues()) {
			String paymentBy = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_INCOMING_PAYMENT_BY);
			String phoneNumber = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_PHONE_NUMBER);
			String amountPaid = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_AMOUNT_PAID);
			String timePaid = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_TIME_PAID);
			String account = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_INCOMING_ACCOUNT);
			Account acc;

			//TODO - no client linked to the account????????????????????
			acc = new Account(account,false);
			try {
				accountDao.saveAccount(acc);
			} catch (DuplicateKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Target tgt;
			
			tgt = new Target();
			targetDao.saveTarget(tgt);

			IncomingPayment incomingPayment = new IncomingPayment(paymentBy,
					phoneNumber, new BigDecimal(amountPaid), new Date(
							Long.parseLong(timePaid)), acc, tgt);
			incomingPaymentDao.saveIncomingPayment(incomingPayment);
		}

		log.trace("EXIT");

		return new CsvImportReport();
	}

}
