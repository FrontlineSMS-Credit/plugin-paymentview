package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.io.IOException;
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
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;

/**
 * @author Ian Onesmus Mukewa <ian@frontlinesms.com> 
 */
public class ClientCsvImporter extends CsvImporter {
	/** The delimiter to use between group names when they are exported. */
	protected static final String GROUPS_DELIMITER = "\\\\";
	
//> INSTANCE PROPERTIES
	
//> CONSTRUCTORS
	public ClientCsvImporter(File importFile) throws CsvParseException {
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
	public CsvImportReport importClients(ClientDao clientDao, CsvRowFormat rowFormat) {
		log.trace("ENTER");
		
		for(String[] lineValues : this.getRawValues()) {
			String firstname = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_CLIENT_FIRST_NAME);
			String otherName = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_CLIENT_OTHER_NAME);
			String phonenumber = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_CLIENT_PHONE);
			String accounts = rowFormat.getOptionalValue(lineValues, CsvUtils.MARKER_CLIENT_ACCOUNTS);
						
			Client c = new Client(firstname, otherName, phonenumber, getAccountsFromString(accounts));					
			try {
				clientDao.saveUpdateClient(c); 
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
