package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.util.List;

import net.frontlinesms.csv.CsvImportReport;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;
import org.creditsms.plugins.paymentview.utils.PvUtils;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
public class ClientCsvImporter extends CsvImporter {
	/** The delimiter to use between group names when they are exported. */
	protected static final String GROUPS_DELIMITER = "\\\\";

	// > INSTANCE PROPERTIES

	// > CONSTRUCTORS
	public ClientCsvImporter(File importFile, PaymentViewPluginController pluginController) throws CsvParseException {
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
	 * @param customFieldDao 
	 * @param customValueDao 
	 * @throws DuplicateKeyException 
	 * @throws IOException
	 *             If there was a problem accessing the file
	 * @throws CsvParseException
	 *             If there was a problem with the format of the file
	 */
	/**
	 * @param clientDao
	 * @param rowFormat
	 * @param pluginController
	 * @return
	 * @throws DuplicateKeyException
	 */
	public CsvImportReport importClients(ClientDao clientDao,
			CsvRowFormat rowFormat, PaymentViewPluginController pluginController) throws DuplicateKeyException {
		log.trace("ENTER");

		for (String[] lineValue : this.getRawValues()) {
			String firstname = rowFormat.getOptionalValue(lineValue, PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME);
			String otherName = rowFormat.getOptionalValue(lineValue, PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME);
			String phonenumber = rowFormat.getOptionalValue(lineValue, PaymentViewCsvUtils.MARKER_CLIENT_PHONE);
			
			PvUtils.parsePhoneFromExcel(phonenumber);
			
			
			Client c = new Client(firstname, otherName, phonenumber);
			try{
				clientDao.saveClient(c);
			}catch (Exception e){
				if (e instanceof NonUniqueObjectException ||
					e instanceof DataIntegrityViolationException ||
					e instanceof ConstraintViolationException){
					c = clientDao.getClientByPhoneNumber(phonenumber);
					
					if (c.isActive()){
						pluginController
						.getUiGeneratorController()
						.alert("A user with similar phone number exists, Update the details?");
					}else{
						pluginController
						.getUiGeneratorController()
						.alert("A user with similar phone number exists, But is Disabled, Update the details?");
					}
					
				}
			}
			
			//FIXME: Ability to take in custom fields and data.
			/*
			 * 1. Get the Optional data since we gave the rowFormatter the markers,
			 * 2. If the column; CustomField is not in the db, create and mark it unused.
			 * 3. Else, pick the custom field to use.
			 * 4. Import the data into these new fields.
			 */
			
			List<CustomField> allCustomFields = pluginController.getCustomFieldDao().getAllActiveUsedCustomFields();

			if (!allCustomFields.isEmpty()) {
				for (CustomField cf : allCustomFields) {
					String strValue = rowFormat.getOptionalValue(lineValue, PaymentViewUtils.getMarkerFromString(cf.getReadableName()));
					CustomValue cv = new CustomValue(strValue, cf, c);
					pluginController.getCustomValueDao().saveCustomValue(cv);
				}
			}

		}

		log.trace("EXIT");

		return new CsvImportReport();
	}
}
