package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;

public class EditClientHandler extends BaseDialog{
//> CONSTANTS
	private static final String COMPONENT_TEXT_FIRST_NAME = "fldFirstName";
	private static final String COMPONENT_TEXT_OTHER_NAME = "fldOtherName";
	private static final String COMPONENT_TEXT_PHONE_NUMBER = "fldPhoneNumber";
	private static String XML_EDIT_CLIENT = "/ui/plugins/paymentview/clients/dialogs/dlgEditClient.xml";

//>DAOs
	private ClientDao clientDao;
	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;
	private AccountDao accountDao; 

	private Client client;
	
//> UI FIELDS
	private Object fieldFirstName;
	private Object fieldOtherName;
	private Object fieldPhoneNumber;

//UI HELPERS	
	private ClientsTabHandler clientsTabHandler;
	
	private Object compPanelFields;
	private Map<CustomField, Object> customComponents;

	private boolean editMode;
	private ContactDao contactDao;
	
	public EditClientHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, ClientsTabHandler clientsTabHandler,
			boolean newClient) {
		super(ui);
		this.clientDao = pluginController.getClientDao();
		this.clientsTabHandler = clientsTabHandler;
		this.customValueDao = pluginController.getCustomValueDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		this.accountDao = pluginController.getAccountDao();
		this.editMode = false;
		this.contactDao = pluginController.getUiGeneratorController().getFrontlineController().getContactDao();
		
		if (newClient){
			XML_EDIT_CLIENT = "/ui/plugins/paymentview/clients/dialogs/dlgViewClient.xml";
		}else{
			XML_EDIT_CLIENT = "/ui/plugins/paymentview/clients/dialogs/dlgEditClient.xml";
		}
		
		init();
		refresh();
	}

	public EditClientHandler(UiGeneratorController ui, Client client,
			PaymentViewPluginController pluginController, ClientsTabHandler clientsTabHandler) {
		super(ui);
		this.client = client;
		this.clientsTabHandler = clientsTabHandler;
		this.customValueDao = pluginController.getCustomValueDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		this.clientDao = pluginController.getClientDao();
		this.accountDao = pluginController.getAccountDao();
		this.editMode = true;
		
		this.contactDao = pluginController.getUiGeneratorController().getFrontlineController().getContactDao();
		
		XML_EDIT_CLIENT = "/ui/plugins/paymentview/clients/dialogs/dlgEditClient.xml";
		
		init();
		refresh();
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EDIT_CLIENT, this);
		compPanelFields = ui.find(dialogComponent, "pnlFields");

		fieldFirstName = ui.find(dialogComponent, COMPONENT_TEXT_FIRST_NAME);
		fieldPhoneNumber = ui
				.find(dialogComponent, COMPONENT_TEXT_PHONE_NUMBER);
		fieldOtherName = ui.find(dialogComponent, COMPONENT_TEXT_OTHER_NAME);

		List<CustomField> allUsedCustomFields = customFieldDao
				.getAllActiveUsedCustomFields();

		customComponents = new HashMap<CustomField, Object>(
				allUsedCustomFields.size());

		for (CustomField cf : allUsedCustomFields) {
			addField(cf, cf.getCamelCaseName(), cf.getReadableName());
		}

	}

	public void addField(CustomField cf, String name, String readableName) {
		Object label = ui.createLabel(readableName);

		Object txtfield = ui.createTextfield(name, "");
		customComponents.put(cf, txtfield);

		ui.setColspan(txtfield, 2);
		ui.setColumns(txtfield, 50);

		ui.add(compPanelFields, label);
		ui.add(compPanelFields, txtfield);
	}

	@Override
	public void refresh() {
		if (editMode) {
			ui.setText(fieldFirstName, this.getClientObj().getFirstName());
			ui.setText(fieldOtherName, this.getClientObj().getOtherName());
			ui.setText(fieldPhoneNumber, this.getClientObj().getPhoneNumber());

			List<CustomField> allCustomFields = this.customFieldDao
					.getAllActiveUsedCustomFields();
			List<CustomValue> allCustomValues = this.customValueDao
					.getCustomValuesByClientId(client.getId());

			if (!allCustomFields.isEmpty()) {
				for (CustomField cf : allCustomFields) {
					for (CustomValue cv : allCustomValues) {
						if (cv.getCustomField().equals(cf)) {
							ui.setText(customComponents.get(cf),
									cv.getStrValue());
						}
					}
				}
			}

//			for (Account acc : this.accountDao.getAccountsByClientId(getClientObj().getId())) {
//				ui.add(fieldListAccounts, createListItem(acc));
//			}
		}
	}
	
	private boolean validate() {
		//Check phone number format
		String PHONE_PATTERN = "\\+2547[\\d]{8}";
		Matcher matcherPhoneNumber = Pattern.compile(PHONE_PATTERN).matcher(ui.getText(fieldPhoneNumber));
		boolean isValid = matcherPhoneNumber.matches();
		if (!isValid){
			ui.infoMessage("The phone number should be set with the following format: +254XXXXXXXXX.");
		}
		return isValid;
	}

	public void saveClient() throws DuplicateKeyException {
		new FrontlineUiUpateJob() {
			public void run() {
				try{
					if (editMode) {
						EditClientHandler.this.client.setFirstName(ui.getText(fieldFirstName));
						EditClientHandler.this.client.setOtherName(ui.getText(fieldOtherName));
						EditClientHandler.this.client.setPhoneNumber(ui.getText(fieldPhoneNumber));
						
						//test if phoneNumber already linked to another client
						Client clientInDb = clientDao.getClientByPhoneNumber(client.getPhoneNumber());
						if (clientInDb!=null && clientInDb.getId()!=client.getId()){
							ui.infoMessage("The phone number " + client.getPhoneNumber() + " is already set up for "+ clientInDb.getFullName() + ".");
						} else {
							EditClientHandler.this.clientDao.updateClient(EditClientHandler.this.client);
							
							Contact fromMsisdn = contactDao.getFromMsisdn(client.getPhoneNumber());
							if (fromMsisdn != null){
								fromMsisdn.setName(client.getFullName());
								fromMsisdn.setPhoneNumber(client.getPhoneNumber());
								contactDao.updateContact(fromMsisdn);
							}else{
								//Start Save the Client as a contact to the core project
								Contact contact = new Contact(client.getFullName(), client.getPhoneNumber(), "", "", "", true);
								contactDao.saveContact(contact);
								//Finish save
							}
				
							List<CustomField> allCustomFields = EditClientHandler.this.customFieldDao
									.getAllActiveUsedCustomFields();
				
							if (!allCustomFields.isEmpty()) {
								for (CustomField cf : allCustomFields) {
									List<CustomValue> cvs = customValueDao.getCustomValuesByClientId(client.getId());
									CustomValue cv = null;
				
									for (CustomValue _cv : cvs) {
										if (_cv.getCustomField().equals(cf)) {
											cv = _cv;
										}
				
									}
									if (cv == null) {
										cv = new CustomValue(ui.getText(customComponents.get(cf)), cf, client);
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
							removeDialog();
							clientsTabHandler.refresh();
						}
					} else {
						String fn = ui.getText(fieldFirstName);
						String on = ui.getText(fieldOtherName);
						String phone = ui.getText(fieldPhoneNumber);
						//test if phoneNumber already linked to another client
						Client clientInDb = clientDao.getClientByPhoneNumber(phone);
						if (clientInDb!=null ){
							if (clientInDb.isActive()){
								ui.infoMessage("The phone number " + phone + " is already set up for "+ clientInDb.getFullName() + ".");
							} else {
								//TODO: Make sure that the user is active if we add a client that has same phone number...
								//ui.showConfirmationDialog("An inactive client with this phone number '" + phone + "' exists." +
								//		"Would you like to reactivate it?", "", this);
								removeDialog();
								ui.infoMessage("The phone number " + phone + " was previously set up for "+ clientInDb.getFullName() + " and will be reactivated.");
								clientInDb.setActive(true);
								EditClientHandler.this.clientDao.updateClient(clientInDb);
							}
							
						} else {
							Client client = new Client(fn, on, phone);
							EditClientHandler.this.clientDao.saveClient(client);
							
							//Start Save the Client as a contact to the core project
							Contact contact = new Contact(client.getFullName(), client.getPhoneNumber(), "", "", "", true);
							contactDao.saveContact(contact);
							//Finish save
				
							List<CustomField> allUsedCustomFields = EditClientHandler.this.customFieldDao
									.getAllActiveUsedCustomFields();
				
							if (!allUsedCustomFields.isEmpty()) {
								for (CustomField cf : allUsedCustomFields) {
									CustomValue cv = new CustomValue(
											ui.getText(customComponents.get(cf)), cf, client);
									try {
										customValueDao.saveCustomValue(cv);
									} catch (DuplicateKeyException e) {
										throw new RuntimeException(e);
									}
								}
							}
							
							Account account = new Account(createAccountNumber(),client,false,true);
							EditClientHandler.this.accountDao.saveAccount(account);
							removeDialog();
							clientsTabHandler.refresh();
						}
					}
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (DuplicateKeyException e) {
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	/**
	 * 
	 * @return a generic account number
	 */
	public String createAccountNumber(){
		int accountNumberGenerated = this.accountDao.getAccountCount()+1;
		String accountNumberGeneratedStr = String.format("%05d", accountNumberGenerated);
		while (this.accountDao.getAccountByAccountNumber(accountNumberGeneratedStr) != null){
			accountNumberGeneratedStr = String.format("%05d", ++ accountNumberGenerated);
		}
		return accountNumberGeneratedStr;
	}

	/** 
	 * @return the clientObj
	 */
	public Client getClientObj() { 
		return client;
	}

}
