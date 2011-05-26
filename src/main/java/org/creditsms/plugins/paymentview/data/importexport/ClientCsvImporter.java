package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import net.frontlinesms.csv.CsvImportReport;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;

/**
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
public class ClientCsvImporter extends CsvImporter {
	/** The delimiter to use between group names when they are exported. */
	protected static final String GROUPS_DELIMITER = "\\\\";

	// > INSTANCE PROPERTIES

	// > CONSTRUCTORS
	public ClientCsvImporter(File importFile) throws CsvParseException {
		super(importFile);
	}

	private Collection<Account> getAccountsFromString(String strAccounts) {
		String[] accounts = strAccounts.split(",");

		Collection<Account> set_accounts = new ArrayList<Account>(
				accounts.length);
		for (String account : accounts) {
			if (account.length() == 0)
				continue;
			set_accounts.add(new Account(account));
		}

		return set_accounts;
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
	 * @param customFieldDao 
	 * @param customValueDao 
	 * @throws IOException
	 *             If there was a problem accessing the file
	 * @throws CsvParseException
	 *             If there was a problem with the format of the file
	 */
	public CsvImportReport importClients(ClientDao clientDao,
			CsvRowFormat rowFormat, CustomFieldDao customFieldDao, CustomValueDao customValueDao) {
		log.trace("ENTER");

		for (String[] lineValues : this.getRawValues()) {
			String firstname = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME);
			String otherName = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME);
			String phonenumber = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_CLIENT_PHONE);
			String accounts = rowFormat.getOptionalValue(lineValues,
					PaymentViewCsvUtils.MARKER_CLIENT_ACCOUNTS);

			/*Client c = new Client(firstname, otherName, phonenumber);
			for (Account a : getAccountsFromString(accounts)) {
				c.addAccount(a);
			}
			clientDao.saveClient(c);
			*/
			
			//FIXME: Ability to take in custom fields and data.
			/*
			 * 1. Get the Optional data since we gave the rowFormatter the markers,
			 * 2. If the column; CustomField is not in the db, create and mark it unused.
			 * 3. Else, pick the custom field to use.
			 * 4. Import the data into these new fields.
			 */
			/*
			List<CustomField> allCustomFields = customFieldDao
					.getAllActiveUsedCustomFields();

			if (!allCustomFields.isEmpty()) {
				for (CustomField cf : allCustomFields) {
					List<CustomValue> cvs = customValueDao
							.getCustomValuesByClientId(c.getId());
					CustomValue cv = null;

					for (CustomValue _cv : cvs) {
						if (_cv.getCustomField().equals(cf)) {
							cv = _cv;
						}

					}
					if (cv == null) {
						cv = new CustomValue(ui.getText(customComponents
								.get(cf)), cf, c);
						try {
							customValueDao.saveCustomValue(cv);
						} catch (DuplicateKeyException e) {
							throw new RuntimeException(e);
						}
					} else {
						cv.setStrValue(ui.getText(customComponents.get(cf)));

						try {
							customValueDao.updateCustomValue(cv);
						} catch (DuplicateKeyException e) {
							throw new RuntimeException(e);
						}
					}
				}
			}
			*/

		}

		log.trace("EXIT");

		return new CsvImportReport();
	}
}
