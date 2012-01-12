package org.creditsms.plugins.paymentview.data.importexport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.csv.CsvImportReport;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.domain.Contact;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.utils.MoveClientDetailsToContacts;
import org.creditsms.plugins.paymentview.utils.PhoneNumberPattern;
import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
public class ClientCsvImporter extends CsvImporter {
	/** The delimiter to use between group names when they are exported. */
	protected static final String GROUPS_DELIMITER = "\\\\";
	
	private List<CustomValue> selectedCustomValueslst = new ArrayList<CustomValue>();
	private List<CustomField> selectedCustomFieldlst = new ArrayList<CustomField>();
	private List<Client> selectedClientLst = new ArrayList<Client>();
	PhoneNumberPattern phonePattern = new PhoneNumberPattern();
	private ContactDao contactDao;
	private MoveClientDetailsToContacts moveClientDetailsToContacts = new MoveClientDetailsToContacts();
	
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
			CsvRowFormat rowFormat, PaymentViewPluginController pluginController, CustomValueDao customValueDao) throws DuplicateKeyException {
		log.trace("ENTER");
		this.contactDao = pluginController.getUiGeneratorController().getFrontlineController().getContactDao();
		
		for (Client client : selectedClientLst) {
			try{
				if(phonePattern.formatPhoneNumber(client.getPhoneNumber())) {
					client.setPhoneNumber(phonePattern.getNewPhoneNumberPattern());
					clientDao.saveClient(client);
					saveSelectedCustomFld(clientDao, client, pluginController, customValueDao);
					moveClientDetailsToContacts.addToContact(this.contactDao, client);
				}
			}catch (Exception e){
				if (e instanceof NonUniqueObjectException ||
					e instanceof DataIntegrityViolationException ||
					e instanceof ConstraintViolationException){
					Client c = clientDao.getClientByPhoneNumber(client.getPhoneNumber());
					
					if (!c.isActive()){
						pluginController
						.getUiGeneratorController()
						.alert("A user with similar phone number: "+client.getPhoneNumber()+" exists, but is inactive, The user's details will be updated.");
						c.setActive(true);
					}else{
						pluginController
						.getUiGeneratorController()
						.alert("A user with similar phone number: "+client.getPhoneNumber()+" exists, The user's details will be updated.");
					}
					c.setFirstName(client.getFirstName());
					c.setOtherName(client.getOtherName());
					clientDao.updateClient(c);
					moveClientDetailsToContacts.addToContact(this.contactDao, client);
					saveSelectedCustomFld(clientDao, client, pluginController, customValueDao);
				}
			}
		}
		log.trace("EXIT");

		return new CsvImportReport();
	}

	private void saveSelectedCustomFld(ClientDao clientDao, Client client, 
			PaymentViewPluginController pluginController, CustomValueDao customValueDao) throws DuplicateKeyException {

		for (int y=0; y<selectedCustomFieldlst.size(); y++) {
			CustomValue cv = selectedCustomValueslst.get(y);
//			if(cv.getStrValue().trim().length()==0){
//				//customValueDao.deleteCustomValue(cv);
//			} else {
				if(phonePattern.formatPhoneNumber(cv.getClient().getPhoneNumber())) {
					cv.getClient().setPhoneNumber(phonePattern.getNewPhoneNumberPattern());
					if(client.getPhoneNumber().equals(cv.getClient().getPhoneNumber())) {
						Client c = clientDao.getClientByPhoneNumber(cv.getClient().getPhoneNumber());
						cv.setClient(c);
						boolean cValExist = false;
						List<CustomValue> lstCustomValue = customValueDao.getCustomValuesByClientId(c.getId());
						if (lstCustomValue==null){
							customValueDao.saveCustomValue(cv);
						} else {
							for(CustomValue customVal: lstCustomValue) {
								if(customVal.getCustomField().getId()==cv.getCustomField().getId()){
									if (cv.getStrValue().trim().length()!=0) {
										customVal.setStrValue(cv.getStrValue());
										customValueDao.updateCustomValue(customVal);
									} else {
										customValueDao.deleteCustomValue(customVal);
									}
									cValExist=true;
								} 
							}
							if(cValExist==false){
								if (cv.getStrValue().trim().length()!=0) {
									customValueDao.saveCustomValue(cv);
								}
							}
						}	
					}
				}
			//}
		}	
	}
	
	public void setSelectedCustomValueslst(List<CustomValue> selectedCustomValueslst) {
		this.selectedCustomValueslst = selectedCustomValueslst;
	}

	public void setSelectedCustomFieldlst(List<CustomField> selectedCustomFieldlst) {
		this.selectedCustomFieldlst = selectedCustomFieldlst;
	}

	public void setSelectedClientLst(List<Client> selectedClientLst) {
		this.selectedClientLst = selectedClientLst;
	}
}
