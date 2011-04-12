package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import net.frontlinesms.csv.CsvImportReport;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.csv.CsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

/**
 * @author Ian Onesmus Mukewa <ian@frontlinesms.com> 
 */
public class OutgoingPaymentCsvImporter extends CsvImporter {
	/** The delimiter to use between group names when they are exported. */
	protected static final String GROUPS_DELIMITER = "\\\\";
	private ClientDao clientDao; 
	
//> INSTANCE PROPERTIES
	
//> CONSTRUCTORS
	public OutgoingPaymentCsvImporter(File importFile) throws CsvParseException {
		super(importFile);
	}

//> IMPORT METHODS
	/**
	 * Import Payments from a CSV file.
	 * @param importFile the file to import from
	 * @param contactDao ; paymentDao
	 * @param rowFormat 
	 * @throws IOException If there was a problem accessing the file
	 * @throws CsvParseException If there was a problem with the format of the file
	 */
	/**
	 * @param accountDao
	 * @param incomingPaymentDao
	 * @param rowFormat
	 */
	public CsvImportReport importOutgoingPayments(OutgoingPaymentDao outgoingPaymentDao, 
			AccountDao accountDao, CsvRowFormat rowFormat) {
		log.trace("ENTER");

		for (String[] lineValues : this.getRawValues()) {
			String phoneNumber = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_PHONE_NUMBER);
			String amountPaid = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_AMOUNT_PAID);
			String timePaid = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_TIME_PAID);
			String account = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_INCOMING_ACCOUNT);
			String notes = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_OUTGOING_NOTES);
			String confirmation = rowFormat.getOptionalValue(lineValues,
					CsvUtils.MARKER_OUTGOING_CONFIRMATION);

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

			OutgoingPayment outgoingPayment = new OutgoingPayment(phoneNumber, new BigDecimal(amountPaid), new Date(
							Long.parseLong(timePaid)), acc, notes, Boolean.parseBoolean(confirmation));
			try {
				outgoingPaymentDao.saveOrUpdateOutgoingPayment(outgoingPayment);
			} catch (DuplicateKeyException e) {
				// FIXME should actually pass details of this back to the user.
				log.debug("An incoming Payment already exist with this id", e);
			}

		}

		log.trace("EXIT");

		return new CsvImportReport();
		
	}
	private Set<Account> getAccountsFromString(String strAccounts) {
		String[] accounts = strAccounts.split(",");		
		
		Set<Account> set_accounts = new HashSet<Account>(accounts.length);
		for (String account : accounts){
			if (account.length() == 0) continue;
			set_accounts.add(new Account(Long.parseLong(account)));
		}
		
		return set_accounts;
	}
}
