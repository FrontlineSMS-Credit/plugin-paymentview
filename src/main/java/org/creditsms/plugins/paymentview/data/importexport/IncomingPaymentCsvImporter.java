package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.io.IOException;
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
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;

/**
 * @author Ian Onesmus Mukewa <ian@frontlinesms.com> 
 */
public class IncomingPaymentCsvImporter extends CsvImporter {
	/** The delimiter to use between group names when they are exported. */
	private IncomingPaymentDao incomingPaymentDao; 
	
//> INSTANCE PROPERTIES
	
//> CONSTRUCTORS
	public IncomingPaymentCsvImporter(File importFile) throws CsvParseException {
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
	public CsvImportReport importPayments(ClientDao contactDao, CsvRowFormat rowFormat) {
		log.trace("ENTER");
		
		for(String[] lineValues : this.getRawValues()) {
			String paymentBy = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_CLIENT_FIRST_NAME);
			String phoneNumber = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_CLIENT_OTHER_NAME);
			String timePaid = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_CONTACT_PHONE);
			String account = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_CLIENT_ACCOUNTS);
						 
			IncomingPayment incomingPayment = new IncomingPayment(paymentBy, phoneNumber, new Date(Long.parseLong(timePaid)),);					
			try {
				incomingPaymentDao.saveOrUpdateIncomingPayment(incomingPayment);
			} catch (DuplicateKeyException e) {
				// FIXME should actually pass details of this back to the user.
				log.debug("Client already exist with this number", e);
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
