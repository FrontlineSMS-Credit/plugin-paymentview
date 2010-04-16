/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.Utils;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.domain.PaymentService;
import org.creditsms.plugins.paymentview.data.domain.PaymentServiceTransaction;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceDao;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceTransactionDao;

import thinlet.Thinlet;

/**
 * 
 * @author Emmanuel Kala
 *
 */
public class PaymentViewThinletTabController implements ThinletUiEventHandler {
//> UI FILES
	private static final String UI_FILE_CLIENT_DIALOG = "/ui/plugins/paymentview/dgEditClient.xml";
	private static final String UI_FILE_PAYMENT_SERVICE_DIALOG = "/ui/plugins/paymentview/dgEditPaymentService.xml";
	private static final String UI_FILE_PAYMENT_SERVICE_TABLE = "/ui/plugins/paymentview/tbPaymentServices.xml";
	private static final String UI_FILE_NETWORK_OPERATOR_TABLE = "/ui/plugins/paymentview/tbNetworkOperators.xml";
	private static final String UI_FILE_NETWORK_OPERATOR_DIALOG = "/ui/plugins/paymentview/dgEditNetworkOperator.xml";
	
//> COMPONENT NAME CONSTANTS
	private static final String COMPONENT_BT_NEW_CLIENT = "btNewClient";
	private static final String COMPONENT_BT_DELETE_CLIENT = "btDeleteClient";
	private static final String COMPONENT_BT_EDIT_CLIENT = "btEditClient";
	private static final String COMPONENT_BT_ADD_NETWORK_OPERATOR = "btAddNetworkOperator";
	private static final String COMPONENT_BT_REMOVE_NETWORK_OPERATOR = "btRemoveNetworkOperator";
	private static final String COMPONENT_LS_CLIENTS = "lsClients";
	private static final String COMPONENT_LS_ALL_NETWORK_OPERATORS = "lstAllOperators";
	private static final String COMPONENT_LS_SELECTED_OPERATORS = "lstSelectedOperators";
	private static final String COMPONENT_TBL_DISPERSALS = "tblDispersals";
	private static final String COMPONENT_TBL_REPAYMENTS = "tblRepayments";
	private static final String COMPONENT_TBL_NETWORK_OPERATORS = "tblNetworkOperators";
	private static final String COMPONENT_TBL_PAYMENT_SERVICES = "tblPaymentServices";
	private static final String COMPONENT_FLD_CLIENT_NAME = "fldClientName";
	private static final String COMPONENT_FLD_PHONE_NUMBER = "fldPhoneNumber";
	private static final String COMPONENT_FLD_OTHER_PHONE_NUMBER = "fldOtherPhoneNumber";
	private static final String COMPONENT_FLD_EMAIL_ADDRESS = "fldEmailAddress";
	private static final String COMPONENT_FLD_SERVICE_NAME = "fldServiceName";
	private static final String COMPONENT_FLD_SMS_SHORT_CODE = "fldSmsShortCode";
	private static final String COMPONENT_FLD_PIN_NUMBER = "fldPinNumber";
	private static final String COMPONENT_FLD_SEND_MONEY_TEXT = "fldSendMoneyText";
	private static final String COMPONENT_FLD_WITHDRAW_MONEY_TEXT = "fldWithdrawMoneyText";
	private static final String COMPONENT_FLD_BALANCE_ENQUIRY_TEXT = "fldBalanceEnquiryText";
	private static final String COMPONENT_FLD_OPERATOR_NAME = "fldOperatorName";
	private static final String COMPONENT_DLG_CLIENT_DETAILS = "clientDetailsDialog";
	private static final String COMPONENT_DLG_NETWORK_OPERATOR = "networkOperatorDialog";
	private static final String COMPONENT_DLG_PAYMENT_SERVICE = "paymentServiceDialog";
	private static final String COMPONENT_PN_SETTINGS_RIGHT_PANE = "pnSettingsRightPane";
	
//> CONSTANTS
	private static final Logger LOG = Utils.getLogger(PaymentViewThinletTabController.class);
	
//> PROPERTIES
	/** Controller for the PaymentView plug-in */
	private final PaymentViewPluginController paymentViewController;
	/** Thinlet UI Controller */
	private final UiGeneratorController uiController;
	/** Thinlet tab component whose functionality is handled by this class */
	private Object paymentViewTab;
	/** DAO for {@link Client}s */
	private ClientDao clientDao;
	/** DAO for {@link NetworkOperator}s */
	private NetworkOperatorDao networkOperatorDao;
	/** DAO for {@link PaymentService}s */
	private PaymentServiceDao paymentServiceDao;
	/** DAO for {@link PaymentServiceTransaction}s */
	private PaymentServiceTransactionDao transactionDao;
	/** DAO for contacts */
	private ContactDao contactDao;

//> CONSTRUCTORS
	/**
	 * 
	 * @param paymentViewController value for {@link #paymentViewController}
	 * @param uiController value for {@linkplain #uiController}
	 */
	public PaymentViewThinletTabController(PaymentViewPluginController paymentViewController, UiGeneratorController uiController){
		this.paymentViewController = paymentViewController;
		this.uiController = uiController;
	}
	
	/**
	 * Refreshes the tab display
	 */
	public void refresh(){
		// Populate the clients list
		Object clientList = getClientList();
		uiController.removeAll(clientList);
		for(Client c: clientDao.getAllClients()){
			Object item = getListItem(c);
			uiController.add(clientList, item);
		}
		
		// Populate repayments table
		Object repaymentsTable = getRepaymentsTable();
		uiController.removeAll(repaymentsTable);
		for(PaymentServiceTransaction t: transactionDao.getTransactionsByType(PaymentServiceTransaction.TYPE_DEPOSIT)){
			Object row = getRow(t);
			uiController.add(repaymentsTable, row);
		}
		
		// Populate the dispersals table
		Object dispersalsTable = getDispersalsTable();
		uiController.removeAll(dispersalsTable);
		for(PaymentServiceTransaction t: transactionDao.getTransactionsByType(PaymentServiceTransaction.TYPE_WITHDRAWAL)){
			Object row = getRow(t);
			uiController.add(dispersalsTable, row);
		}
		
	}

//> MUTATORS
	
	/**
	 * Sets the {@link Thinlet} UI tab component for the "Payment View plugin". The tab component
	 * holds all the {@link Thinlet} UI components for the plugin.
	 * 
	 * @param paymentViewTab value for {@link #paymentViewTab}
	 */
	public void setTabComponent(Object paymentViewTab){
		this.paymentViewTab = paymentViewTab;
	}
	
	public void setClientDao(ClientDao clientDao){
		this.clientDao = clientDao;
	}
	
	public void setContactDao(ContactDao contactDao){
		this.contactDao = contactDao;
	}
	
	public void setNetworkOperatorDao(NetworkOperatorDao networkOperatorDao){
		this.networkOperatorDao = networkOperatorDao;
	}
	
	public void setPaymentServiceDao(PaymentServiceDao paymentServiceDao){
		this.paymentServiceDao = paymentServiceDao;
	}
	
	public void setPaymentServiceTranscationDao(PaymentServiceTransactionDao transactionDao){
		this.transactionDao = transactionDao;
	}
	
//>	THINLET UI & EVENT HELPER METHODS
	
	/**
	 * @return The client list component
	 */
	private Object getClientList(){
		Object clientList = uiController.find(paymentViewTab, COMPONENT_LS_CLIENTS);
		return clientList;
	}
	
	/** @return the dispersals table component */
	private Object getDispersalsTable(){
		Object dispersalsTable = uiController.find(paymentViewTab, COMPONENT_TBL_DISPERSALS);
		return dispersalsTable;
	}
	
	/** @return the repayments table component */
	private Object getRepaymentsTable(){
		Object repaymentsTable = uiController.find(paymentViewTab, COMPONENT_TBL_REPAYMENTS);
		return repaymentsTable;
	}
	
	/**
	 * Returns a {@link Thinlet} list items for the specified client
	 * @param client The client to represent as a node
	 * @return list item to insert to the thinlet list
	 */
	private Object getListItem(Client client){	
		Object node = uiController.createListItem(client.getName(), client);
		return node;
	}
	
	/**
	 * Returns a {@link Thinlet} UI list item for the specified network operator
	 * @param operator {@link NetworkOperator} instance represented by the list item
	 * @return list item
	 */
	private Object getListItem(NetworkOperator operator){
		return uiController.createListItem(operator.getOperatorName(), operator);
	}
	
	/**
	 * Returns a {@link Thinlet} UI table row containing the details of a {@link PaymentServiceTransaction}
	 * @param transaction {@link PaymentServiceTransaction} to be displayed in the row
	 * @return
	 */
	private Object getRow(PaymentServiceTransaction transaction){
		Object row = uiController.createTableRow(transaction);
		uiController.add(row, uiController.createTableCell(transaction.getClient().getContact().getDisplayName()));
		uiController.add(row, uiController.createTableCell(transaction.getPaymentService().getServiceName()));
		uiController.add(row, uiController.createTableCell(Double.toString(transaction.getAmount())));
		uiController.add(row, uiController.createTableCell(InternationalisationUtils.getDatetimeFormat().format(transaction.getDate())));
		return row;
	}
	
	/**
	 * Returns a {@link Thinlet} UI table row containing the name and short code of a {@link PaymentService}
	 * @param service {@link PaymentService} to be displayed in the row
	 * @return
	 */
	private Object getRow(PaymentService service){
		Object row = uiController.createTableRow(service);
		
		uiController.add(row, uiController.createTableCell(service.getServiceName()));
		uiController.add(row, uiController.createTableCell(service.getSmsShortCode()));
		
		return row;
	}
	
	/**
	 * Returns a {@link Thinlet} UI table row containing the name of a {@link NetworkOperator}
	 * @param operator
	 * @return
	 */
	private Object getRow(NetworkOperator operator){
		Object row = uiController.createTableRow(operator);
		
		uiController.add(row, uiController.createTableCell(operator.getOperatorName()));
		
		return row;
	}
	
	/**
	 * Gets the {@link Client} instance attached to the supplied component 
	 * @param component
	 * @return the attached {@link Client} instance
	 */
	public Client getClient(Object component){
		return(Client)uiController.getAttachedObject(component);
	}
	
	/**
	 * Gets the {@link PaymentServiceTransaction} instance attached to supplied component
	 * @param component
	 * @return the attached {@link PaymentServiceTransaction} instance
	 */
	public PaymentServiceTransaction getPaymentServiceTransaction(Object component){
		return (PaymentServiceTransaction)uiController.getAttachedObject(component);
	}
	
	/**
	 * Gets the {@link PaymentService} instance attached to the supplied component
	 * @param component
	 * @return the attached {@link PaymentService} instance
	 */
	public PaymentService getPaymentService(Object component){
		return (PaymentService)uiController.getAttachedObject(component);
	}
	
	/**
	 * Gets the {@link NetworkOperator} instance attached to the supplied component
	 * @param component
	 * @return the attached {@link NetworkOperator} instance
	 */
	public NetworkOperator getNetworkOperator(Object component){
		return (NetworkOperator)uiController.getAttachedObject(component);
	}
	
	/**
	 * Performs a live search of client data and updates the display in real-time
	 * @param textField 
	 */
	public void liveClientSearch(Object textField){
		LOG.info("Initiating live search");
		
		// Grab the typed text
		String searchText = uiController.getText(textField);

		// List object to hold the search data
		List<Client> searchList = new ArrayList<Client>();
		
		// Determine the search criteria to use
		if(searchText.trim().length() == 0)
			searchList = clientDao.getAllClients();
		else
			searchList = clientDao.filterClientsByName(searchText.trim());
		
		LOG.trace("Live search for ["+searchText+"]...");
		
		// Clear the lists
		Object clientList = getClientList();
		Object tblRepayments = getRepaymentsTable();
		Object tblDispersals = getDispersalsTable();
		
		uiController.removeAll(clientList);
		uiController.removeAll(tblRepayments);
		uiController.removeAll(tblDispersals);
		
		// Fetch the clients and their transactions
		for(Client c: searchList){
			Object item = getListItem(c);
			uiController.add(clientList, item);
			
			// Fetch the transactions for the client and populate appropriate lists
			for(PaymentServiceTransaction t: transactionDao.getTransactionsByClient(c)){
				Object row = getRow(t);
				if(t.getTransactionType() == PaymentServiceTransaction.TYPE_DEPOSIT){
					uiController.add(tblRepayments, row);
				}else if(t.getTransactionType() == PaymentServiceTransaction.TYPE_WITHDRAWAL){
					uiController.add(tblDispersals, row);
				}
			}
		}
		
		// Repaint the UI to reflect changes
		uiController.repaint(paymentViewTab);
	}
	
	/**
	 * Event handler that is triggered when a client is selected from the list of clients on the UI
	 * @param component reference to the client list UI component
	 */
	public void selectClient(Object component){
		// Get references to the edit and delete buttons
		Object btnEditClient = uiController.find(paymentViewTab, COMPONENT_BT_EDIT_CLIENT);
		Object btnDeleteClient = uiController.find(paymentViewTab, COMPONENT_BT_DELETE_CLIENT);
		
		// Get "the enabled" property of the buttons
		boolean editEnabled = uiController.getBoolean(btnEditClient, "enabled");
		boolean deleteEnabled = uiController.getBoolean(btnDeleteClient, "enabled");
		
		// Enable the buttons if disabled
		if(editEnabled == false){
			uiController.setEnabled(btnEditClient,  true);
		}
		
		if(deleteEnabled == false){
			uiController.setEnabled(btnDeleteClient, true);
		}
		
		Client client = getClient(uiController.getSelectedItem(component));
			
		Object tblRepayments = getRepaymentsTable();
		Object tblDispersals = getDispersalsTable();
		
		uiController.removeAll(tblRepayments);
		uiController.removeAll(tblDispersals);
		
		LOG.debug("Getting transactions for client: " + client);

		for(PaymentServiceTransaction t: transactionDao.getTransactionsByClient(client)){
			Object row = getRow(t);
			
			if(t.getTransactionType() == PaymentServiceTransaction.TYPE_WITHDRAWAL)
				uiController.add(tblDispersals, row);
			else if(t.getTransactionType() == PaymentServiceTransaction.TYPE_DEPOSIT)
				uiController.add(tblRepayments, row);
		}
		
		uiController.repaint(paymentViewTab);
	}
	
	/**
	 * Event handler that is triggered when a client record on the UI client list is double clicked
	 * @param component
	 */
	public void showClientDetails(Object component){
		Client client = getClient(uiController.getSelectedItem(component));
		
		// Load the client details dialog
		Object dialog = getClientDialog(client);
		uiController.add(dialog);
	}

	/**
	 * Removes a dialog from the screen when the close action is initated
	 * @param dialog {@link Thinlet} UI dialog to be removed from the display 
	 */
	public void removeDialog(Object dialog){
		uiController.remove(dialog);
	}
	
	/**
	 * Performs input validation for the required client properties 
	 */
	public void validateRequiredFields(){
		
	}
	
	/**
	 * Saves the client
	 */
	public void saveClient(){
		Object dialog = getClientDialog();
		
		// Check if a client object is attached to the client dialog
		boolean clientExists = (getClient(dialog) == null)?false:true;
		
		// Create/get client instance as appropriate
		Client client = (clientExists == true)? getClient(dialog):new Client();
		
		// Fetch the client info from the dialog
		client.setName(uiController.getText(uiController.find(dialog, COMPONENT_FLD_CLIENT_NAME)));
		client.setPhoneNumber(uiController.getText(uiController.find(dialog, COMPONENT_FLD_PHONE_NUMBER)));
		client.setOtherPhoneNumber(uiController.getText(uiController.find(dialog, COMPONENT_FLD_OTHER_PHONE_NUMBER)));
		client.setEmailAddress(uiController.getText(uiController.find(dialog, COMPONENT_FLD_EMAIL_ADDRESS)));
		
		// Check if a contact record is to be created for this client
		boolean createContact = uiController.getBoolean(uiController.find(dialog, "chkAddToContact"), "selected"); 
		if (createContact){
			Contact contact = new Contact(client.getName(), client.getPhoneNumber(), client.getOtherPhoneNumber(), 
					client.getEmailAddress(), null, true);
			
			// Check if a contact with the specified info already exists
			boolean contactExists = (contactDao.getContactByName(client.getName()) == null);
			try{
				if(contactExists)
					contactDao.updateContact(contact);
				else
					contactDao.saveContact(contact);
			}catch(DuplicateKeyException e){
				LOG.warn("A contact with the specified details already exists!" + e);
			}
				
			// Link the client with the newly saved contact record
			client.setContact(contact);
		}else{
			client.setContact(null);
		}
		
		//Save the client info
		try{
			if (clientExists ==  true) 
				clientDao.updateClient(client);
			else 
				clientDao.saveClient(client);
		}catch(DuplicateKeyException e){
			LOG.warn("A client with the phone number ["+client.getPhoneNumber()+"] already exists");
		}
		
		// Remove the item being updated from the client list
		if(clientExists){
			Object selectedItem = uiController.getSelectedItem(getClientList());
			uiController.remove(selectedItem);
		}
		
		//Update the UI
		Object item = getListItem(client);
		uiController.add(getClientList(), item);		
		
		removeDialog(dialog);
		uiController.repaint(paymentViewTab);
		
	}
	
	/**
	 * Shows the client dialog
	 */
	public void showNewClientForm(){
		uiController.add(getClientDialog());
	}
	
	/** @return a reference to the client dialog */
	private Object getClientDialog(){
		Object dialog = uiController.find(COMPONENT_DLG_CLIENT_DETAILS);
		
		if(dialog == null)
			return uiController.loadComponentFromFile(UI_FILE_CLIENT_DIALOG, this);
		else
			return dialog;
	}
	
	/**
	 * Returns an instance of the client dialog with the client details shown in the input fields
	 * @param client {@link Client} whose details shall be shown on the dialof
	 * @return
	 */
	private Object getClientDialog(Client client){
		Object dialog = getClientDialog();
		uiController.setAttachedObject(dialog, client);
		
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_CLIENT_NAME), client.getName());
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_PHONE_NUMBER), client.getPhoneNumber());
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_OTHER_PHONE_NUMBER), client.getOtherPhoneNumber());
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_EMAIL_ADDRESS), client.getEmailAddress());
		
		if(client.getContact() == null)
			uiController.setSelected(uiController.find(dialog, "chkAddToContact"), false);
		
		return dialog;
	}
	
	/**
	 * Gets the SHA-1 value of an string 
	 * @param code value whose SHA-1 hash is to be computed
	 * @return
	 */
	private String getHashString(String code){
		if(code == null || code.trim().length() == 0)
			return null;
		
		String hashString = null;
		byte[] defaultBytes = code.getBytes();
		try{
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.reset();
			digest.update(defaultBytes);
			
			byte[] messageDigest = digest.digest();
			
			// Temporary buffer to store the human readable hash
			StringBuffer digestBuffer = new StringBuffer();
			
			// Convert the digest output to hexadecimal
			for(int i=0; i<messageDigest.length; i++)
				digestBuffer.append(Integer.toHexString(0xFF & messageDigest[i]));
			
			hashString = digestBuffer.toString();
		}catch(NoSuchAlgorithmException ne){
			LOG.debug(ne);
		}		
		
		return hashString;
	}
	
	/**
	 * Gets an instance of the payment services panel
	 * @return
	 */
	private Object getPaymentServiceDialog(boolean load){
		Object dialog = uiController.find(COMPONENT_DLG_PAYMENT_SERVICE);
		
		if(dialog == null)
			dialog =  uiController.loadComponentFromFile(UI_FILE_PAYMENT_SERVICE_DIALOG, this);
		
		// Obtain reference to the "All Operators" list component
		Object allOperators = uiController.find(dialog, COMPONENT_LS_ALL_NETWORK_OPERATORS);

		// Populate the "All Operators" list
		if(load){
			for(NetworkOperator operator: networkOperatorDao.getAllNetworkOperators()){
				Object item = getListItem(operator);
				uiController.add(allOperators, item);
			}
		}
		
		return dialog;
	}
	
	/**
	 * Gets an instance of the payment services panel showing the details of the specified payment service
	 * @param service {@link PaymentService} whose details are to be displayed on the panel
	 * @return
	 */
	private Object getPaymentServiceDialog(PaymentService service){
		Object dialog = getPaymentServiceDialog(true);
		uiController.setAttachedObject(dialog, service);

		uiController.setText(uiController.find(dialog, COMPONENT_FLD_SERVICE_NAME), service.getServiceName());
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_SMS_SHORT_CODE), service.getSmsShortCode());
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_PIN_NUMBER), service.getPinNumber());
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_SEND_MONEY_TEXT), service.getSendMoneyTextMessage());
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_WITHDRAW_MONEY_TEXT), service.getWithdrawMoneyTextMessage());
		uiController.setText(uiController.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_TEXT), service.getBalanceEnquiryTextMessage());
		
		Object selectedOperators = uiController.find(dialog, COMPONENT_LS_SELECTED_OPERATORS);		
		
		// Populate the network operators list for the selected payment service
		for(NetworkOperator operator: service.getNetworkOperators()){
			Object item = getListItem(operator);
			uiController.add(selectedOperators, item);
		}
		
		return dialog;
	}
	
	
	/**
	 * Gets an instance of the network operator dialog
	 * @return
	 */
	private Object getNetworkOperatorDialog(){
		Object dialog = uiController.find(COMPONENT_DLG_NETWORK_OPERATOR);
		
		if(dialog == null)
			dialog = uiController.loadComponentFromFile(UI_FILE_NETWORK_OPERATOR_DIALOG, this);
		
		return dialog;
	}
	
	/**
	 * Event handler for the settings tree menu
	 * @param parentTab
	 * @param treeView
	 */
	public void showSettingsItem(Object parentTab, Object treeView){
		Object selectedNode = uiController.getSelectedItem(treeView);
		uiController.removeAll(uiController.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE));
				
		// No node has been selected
		if(uiController.getName(selectedNode) == null)
			return;
		
		// Change the contents of the right-most panel depending on the selected tree node 
		if(uiController.getName(selectedNode).equals("ndPaymentSystems")){
			Object table = getPaymentServicesTable();
			uiController.add(uiController.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE), uiController.getParent(table));
			uiController.repaint();
		}else if(uiController.getName(selectedNode).equals("ndNetworkOperators")){
			Object table = getNetworkOperatorsTable(true);
			uiController.add(uiController.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE), uiController.getParent(table));
			uiController.repaint();
		}
	}
	
	private Object getSettingsRightPane(){
		return uiController.find(paymentViewTab, COMPONENT_PN_SETTINGS_RIGHT_PANE);
	}
	
	/**
	 * Returns a {@link Thinlet} UI table object containing the list of payment services in the system 
	 * @return
	 */
	private Object getPaymentServicesTable(){		
		Object table = uiController.find(getSettingsRightPane(), COMPONENT_TBL_PAYMENT_SERVICES); 
		
		if(table == null){
			Object container = uiController.loadComponentFromFile(UI_FILE_PAYMENT_SERVICE_TABLE, this);
			table = uiController.find(container, COMPONENT_TBL_PAYMENT_SERVICES);
		}		
		
		for(PaymentService service: paymentServiceDao.getAllPaymentServices()){
			Object row = getRow(service);
			uiController.add(table, row);
		}
		return table;
	}
	
	/**
	 * Returns a {@link Thinlet} UI table object containing the list of network operators in the system
	 * @param reload designates whether the table is to be reloaded with the list of operators
	 * @return
	 */
	private Object getNetworkOperatorsTable(boolean reload){
		Object table = uiController.find(getSettingsRightPane(), COMPONENT_TBL_NETWORK_OPERATORS);
		
		if(table == null){
			Object container = uiController.loadComponentFromFile(UI_FILE_NETWORK_OPERATOR_TABLE, this);
			table = uiController.find(container, COMPONENT_TBL_NETWORK_OPERATORS);
		}
		
		if(reload){
			for(NetworkOperator operator: networkOperatorDao.getAllNetworkOperators()){
				Object row = getRow(operator);
				uiController.add(table, row);
			}
		}
		
		return table;
	}
	
	/**
	 * Displays the payment service dialog
	 */
	public void showPaymentServiceDialog(){
		uiController.add(getPaymentServiceDialog(true));
	}
	
	/**
	 * Displays the network operator dialog
	 */
	public void showNetworkOperatorDialog(){
		uiController.add(getNetworkOperatorDialog());
	}
	
	/**
	 * Saves a payment service
	 */
	public void savePaymentService(){
		
	}
	
	/**
	 * Saves a network operator record into the database
	 */
	public void saveNetworkOperator(){
		// Fetch the dialog
		Object dialog = getNetworkOperatorDialog();
		
		// Check if an existing record is being updated
		boolean operatorExists = (getNetworkOperator(dialog) == null)?false:true;

		// Fetch/Create a network operator instance as appropriate
		NetworkOperator operator = (operatorExists == true)?getNetworkOperator(dialog) : new NetworkOperator();

		// Fetch the name of the network operator
		String name = uiController.getText(uiController.find(dialog, COMPONENT_FLD_OPERATOR_NAME));
				
		// Prevent saving of zero-length names
		if(name.trim().length() == 0)
			return;
		
		operator.setOperatorName(name);
		
		// Begin update/create
		try{
			if(operatorExists)
				networkOperatorDao.updateNetworkOperator(operator);
			else
				networkOperatorDao.saveNetworkOperator(operator);
		}catch(DuplicateKeyException e){
			LOG.debug("A network operator with the specified name already exists" + e);
		}
		
		// If update operation, remove the selected row from the table
		if(operatorExists){
			Object row = uiController.getSelectedItem(getNetworkOperatorsTable(false));
			if(row != null){
				uiController.remove(row);
			}
		}
		
		// Close the dialog
		removeDialog(dialog);
		
		// Add new/updated record to the display list
		uiController.add(getNetworkOperatorsTable(false), getRow(operator));
		uiController.repaint();
	}
	
	/**
	 * Toggles the enabled property of the buttons that move network operators between the lists
	 * of available and selected operators
	 * @param list
	 * @param button
	 */
	public void toggleMoveOperatorButton(Object list, Object button){
		if(uiController.getSelectedIndex(list) != -1)
			uiController.setEnabled(button, true);
		else
			uiController.setEnabled(button, false);
	}
	

	/**
	 * Moves a network operator from one list to another
	 * @param sourceList {@link Thinlet} UI list from which the item is to be moved
	 * @param targetList {@link Thinlet} UI list to which the item is being moved
	 */
	public void moveNetworkOperator(Object sourceList, Object targetList){
		Object selectedItem = uiController.getSelectedItem(sourceList);
		
		if(selectedItem != null){
			uiController.remove(selectedItem);
			uiController.add(targetList, selectedItem);
		}
	}

}
