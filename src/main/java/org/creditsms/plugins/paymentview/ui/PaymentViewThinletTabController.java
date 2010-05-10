/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.frontlinesms.Utils;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Message;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.PaymentServiceSmsProcessor;
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
public class PaymentViewThinletTabController implements ThinletUiEventHandler, PagedComponentItemProvider {
//> UI FILES
    private static final String UI_FILE_CLIENT_DIALOG = "/ui/plugins/paymentview/dgEditClient.xml";
    private static final String UI_FILE_PAYMENT_SERVICE_DIALOG = "/ui/plugins/paymentview/dgEditPaymentService.xml";
    private static final String UI_FILE_PAYMENT_SERVICE_TABLE = "/ui/plugins/paymentview/tbPaymentServices.xml";
    private static final String UI_FILE_NETWORK_OPERATOR_TABLE = "/ui/plugins/paymentview/tbNetworkOperators.xml";
    private static final String UI_FILE_NETWORK_OPERATOR_DIALOG = "/ui/plugins/paymentview/dgEditNetworkOperator.xml";
    private static final String UI_FILE_RESPONSE_TEXTS_DIALOG = "/ui/plugins/paymentview/dgPaymentServiceResponseTexts.xml";
	
//> COMPONENT NAME CONSTANTS
    private static final String COMPONENT_BT_NEW_CLIENT = "btNewClient";
    private static final String COMPONENT_BT_DELETE_CLIENT = "btDeleteClient";
    private static final String COMPONENT_BT_EDIT_CLIENT = "btEditClient";
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
    private static final String COMPONENT_FLD_DISPESRAL_CONFIRM_TEXT = "fldDispersalConfirmText";
    private static final String COMPONENT_FLD_DISPERSAL_CONFIRM_TEXT_KEYWORD = "fldDispersalConfirmKeyword";
    private static final String COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT = "fldRepaymentConfirmText";
    private static final String COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT_KEYWORD = "fldRepaymentConfirmKeyword";
    private static final String COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT = "fldBalanceEnquiryConfirmText";
    private static final String COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT_KEYWORD = "fldBalanceEnquiryConfirmKeyword";
    private static final String COMPONENT_DLG_CLIENT_DETAILS = "clientDetailsDialog";
    private static final String COMPONENT_DLG_NETWORK_OPERATOR = "networkOperatorDialog";
    private static final String COMPONENT_DLG_PAYMENT_SERVICE = "paymentServiceDialog";
    private static final String COMPONENT_PN_SETTINGS_RIGHT_PANE = "pnSettingsRightPane";
    private static final String COMPONENT_PN_CLIENTS = "pnClients";
    private static final String COMPONENT_PN_DISPERSALS = "pnDispersals";
    private static final String COMPONENT_PN_REPAYMENTS = "pnRepayments";

//> I18N KEYS
    private static final String PAYMENTVIEW_LOADED = "paymentview.loaded";
	
//> CONSTANTS
    private static final Logger LOG = Utils.getLogger(PaymentViewThinletTabController.class);
	
//> UI COMPONENTS
    /** Controller for the PaymentView plug-in */
    private final PaymentViewPluginController paymentViewController;
    /** Thinlet UI Controller */
    private final UiGeneratorController uiController;
    /** Thinlet tab component whose functionality is handled by this class */
    private Object paymentViewTab;
	
    /** Paging handler for clients */
    private ComponentPagingHandler clientsPagingHandler;
    /** Paging handler for dispersals */
    private ComponentPagingHandler dispersalsPagingHandler;
    /** Paging handler for repayments*/
    private ComponentPagingHandler repaymentsPagingHandler;

//> DAO OBJECTS	
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
    
//> PROPERTIES
    /** String used for the live search */
    private String liveSearchString;
    /** Keeps track of the currently selected client */
    private Client selectedClient;
	
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
        // Set the status message
        uiController.setStatus(InternationalisationUtils.getI18NString(PAYMENTVIEW_LOADED));
    	
        // Check messages that have not been processed and push them through
        processPendingTransactions();
    	
        // Populate the clients list and add the paging controls just below the list of clients
        Object clientList = getClientList();		
        clientsPagingHandler = new ComponentPagingHandler(this.uiController, this, clientList);		
        Object pnClients = uiController.find(this.paymentViewTab, COMPONENT_PN_CLIENTS);
        Object clientPageControls = clientsPagingHandler.getPanel();
        
        uiController.setHAlign(clientPageControls, Thinlet.RIGHT);
        uiController.add(pnClients, clientPageControls, 2);
        clientsPagingHandler.refresh();	
    
        // Populate the repayments table and add the paging controls just below the list of repayments
        Object repaymentsTable = getRepaymentsTable();		
        repaymentsPagingHandler = new ComponentPagingHandler(this.uiController, this, repaymentsTable);		
        Object pnRepayments = uiController.find(this.paymentViewTab, COMPONENT_PN_REPAYMENTS);
        uiController.add(pnRepayments, repaymentsPagingHandler.getPanel(), 1);
        repaymentsPagingHandler.refresh();
    	
        // Populate the dispersals table and add the paging controls just below the list of dispersals 
        Object dispersalsTable = getDispersalsTable();		
        dispersalsPagingHandler = new ComponentPagingHandler(this.uiController, this, dispersalsTable);
        Object pnDispersals = uiController.find(this.paymentViewTab, COMPONENT_PN_DISPERSALS);
        uiController.add(pnDispersals, dispersalsPagingHandler.getPanel(), 1);
        dispersalsPagingHandler.refresh();
    }
    
    /**
     * Internal helper method to process transaction-related messages
     */
    private void processPendingTransactions(){
        List<Message> pendingTransactions = transactionDao.getPendingTransactions();
    	for(Message message: pendingTransactions)
    		processIncomingMessage(message);
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
    
    /**
     * Sets the client DAO
     * @param clientDao
     */
    public void setClientDao(ClientDao clientDao){
        this.clientDao = clientDao;
    }
    
    /**
     * Sets the contacts DAO
     * @param contactDao
     */
    public void setContactDao(ContactDao contactDao){
        this.contactDao = contactDao;
    }
    
    /**
     * Sets the network operator DAO
     * @param networkOperatorDao
     */
    public void setNetworkOperatorDao(NetworkOperatorDao networkOperatorDao){
        this.networkOperatorDao = networkOperatorDao;
    }
    
    /**
     * Sets the payment service DAO
     * @param paymentServiceDao
     */
    public void setPaymentServiceDao(PaymentServiceDao paymentServiceDao){
        this.paymentServiceDao = paymentServiceDao;
    }
    
    /**
     * Sets the payment service transaction DAO
     * @param transactionDao
     */
    public void setPaymentServiceTranscationDao(PaymentServiceTransactionDao transactionDao){
        this.transactionDao = transactionDao;
    }
	
// LIST PAGING METHODS
    /** @see PagedComponentItemProvider#getListDetails(Object, int, int) */
    public PagedListDetails getListDetails(Object list, int startIndex,	int limit) {
        if(list.equals(clientsPagingHandler.getList())){
            return getClientListPagingDetails(startIndex, limit);
        } else if(list.equals(repaymentsPagingHandler.getList())){
            return getRepaymentListPagingDetails(startIndex, limit);
        } else if(list.equals(dispersalsPagingHandler.getList())){
            return getDispersalListPagingDetails(startIndex, limit);
        } else throw new IllegalStateException();
    }
    
    private PagedListDetails getClientListPagingDetails(int startIndex, int limit){
    	List<Client> clients = null;
    	int totalClientCount = 0;
    	
    	// Check if there's a search string
    	if(liveSearchString != null){
    		clients = clientDao.filterClientsByName(liveSearchString, startIndex, limit);			
    		totalClientCount = clientDao.getFilteredClientCount(liveSearchString);
    		liveSearchString  = null;
    	}else{
    		clients = clientDao.getAllClients(startIndex, limit);
    		totalClientCount = clientDao.getClientCount();
    	}
    			
    	Object[] clientRows = new Object[clients.size()];
    	
    	for(int i=0; i< clients.size(); i++){
    		Client client = clients.get(i);
    		clientRows[i] = getListItem(client);
    	}
    	
    	return new PagedListDetails(totalClientCount, clientRows);
    }
    
    private PagedListDetails getRepaymentListPagingDetails(int startIndex, int limit){
    	List<PaymentServiceTransaction> transactions; 
    	
    	// Check for selected client
    	if(selectedClient != null){
    		transactions = transactionDao.getTransactionsByClient(selectedClient, PaymentServiceTransaction.TYPE_DEPOSIT);
    	} else {
    		transactions = transactionDao.getTransactionsByType(PaymentServiceTransaction.TYPE_DEPOSIT);
    	}
    	
    	// Get the number of items returned
    	int totalItems = transactions.size();
    	
    	Object[] transactionRows = new Object[totalItems];
    	for(int i=0; i<totalItems; ++i){
    		PaymentServiceTransaction t = transactions.get(i);
    		transactionRows[i] = getRow(t);
    	}
    	
    	return new PagedListDetails(totalItems, transactionRows);
    }
    
    private PagedListDetails getDispersalListPagingDetails(int startIndex, int limit){
    	List<PaymentServiceTransaction> transactions;
    	
    	// Check for selected client
    	if(selectedClient != null) {
    		transactions = transactionDao.getTransactionsByClient(selectedClient, PaymentServiceTransaction.TYPE_WITHDRAWAL);
    	} else {
    		transactions = transactionDao.getTransactionsByType(PaymentServiceTransaction.TYPE_WITHDRAWAL);
    	}
    	
    	// Get the number of items returned
    	int totalItems = transactions.size();
    	
    	Object[] transactionRows = new Object[totalItems];
    	for(int i=0; i<totalItems; ++i){
    		PaymentServiceTransaction t = transactions.get(i);
    		transactionRows[i] = getRow(t);
    	}
    	
    	return new PagedListDetails(totalItems, transactionRows);
    }
    
    /**
     * Processes the incoming message by extracting the necessary information and updating the client
     * records. The processing makes use of message filtering rules. If no rules are present, the 
     * no information is extracted from the message
     * 
     * @param message
     */
    public void processIncomingMessage(Message message){
    	String sender = message.getSenderMsisdn();
    	String content = message.getTextContent();
    	
    	// Get the payment service from which the text message has been sent
    	PaymentService paymentService = paymentServiceDao.getPaymentServiceByShortCode(sender);
    	if(paymentService == null)
    		return;
    
    	//Instantiate a transaction
    	PaymentServiceTransaction transaction = new PaymentServiceTransaction();
    	
    	// Set the message used to create the transaction
    	transaction.setMessage(message);
    	
    	// Mark the message as not posted to an MIS
    	transaction.setPostedToMIS(false);
    	
    	//Set the payment service
    	transaction.setPaymentService(paymentService);
    	
    	//Get the keywords to be used to determine the transaction type in the text message
    	//NOTE: confirm 1 and confirm2 *MUST* not be the same
    	String confirm1 = paymentService.getRepaymentConfirmationKeyword();
    	String confirm2 = paymentService.getDispersalConfirmationKeyword();
    	
    	// Store the template message to be used for extracting the transaction meta data
    	String smsTemplate = null;
    	
    	//Extra validation check just in case...
    	if(confirm1.trim().equalsIgnoreCase(confirm2.trim()))
    		return;
    	
    	//Begin pattern matching and extraction of information from the text message
    	if(containsKeyword(content, confirm1)){
    		transaction.setTransactionType(PaymentServiceTransaction.TYPE_DEPOSIT);
    		smsTemplate = paymentService.getRepaymentConfirmationText();
    	}else if(containsKeyword(content, confirm2)){
    		transaction.setTransactionType(PaymentServiceTransaction.TYPE_WITHDRAWAL);
    		smsTemplate = paymentService.getDispersalConfirmationText();
    	}else{
    		return;
    	}
    	
    	PaymentServiceSmsProcessor processor = new PaymentServiceSmsProcessor(content, smsTemplate);
    	transaction.setTransactionAmount(processor.getAmount());
    	transaction.setTransactionCode(processor.getTransactionCode());
    	transaction.setClient(clientDao.getClientByPhoneNumber(processor.getPhoneNumber()));
    	
    	//Set the transaction date to the date when the message was received
    	transaction.setTransactionDate(new Date(message.getDate()));
    	
    	//Information has been extracted, therefore save
    	try{
    		transactionDao.savePaymentServiceTransaction(transaction);
    	}catch(DuplicateKeyException e){
    		LOG.debug("Error: A transaction with the specified code already exists", e);
    	}
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
    
    	// Determine the search criteria to use
    	if(searchText.trim().length() > 0){
    		liveSearchString = searchText.trim();
    		LOG.trace("Live search for ["+searchText+"]...");
    	}
    
    	clientsPagingHandler.refresh();
    	dispersalsPagingHandler.refresh();
    	repaymentsPagingHandler.refresh();
    			
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
    	
    	// Get the client instance attached to the list item
    	Client client = getClient(uiController.getSelectedItem(component));
    	
    	// Set the selected client
    	selectedClient = client;
    				
    	LOG.debug("Getting transactions for client: " + client);
    	
    	dispersalsPagingHandler.refresh();
    	repaymentsPagingHandler.refresh();
    
    	uiController.repaint(paymentViewTab);
    }
    
    /**
     * Event handler that is triggered when a client record on the UI client list is double clicked
     * @param component
     */
    public void showClientDetails(){		
    	// Load the client details dialog
    	Object dialog = getClientDialog(selectedClient);
    	uiController.add(dialog);
    }
    
    public void deleteClient(){
    	clientDao.deleteClient(selectedClient);
    	clientsPagingHandler.refresh();
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
    	//TODO: Implement validation checks
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
     * @param client {@link Client} whose details shall be shown on the dialog
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
     * @param load specified whether the network operator lists are to be loaded
     * @return
     */
    private Object getPaymentServiceDialog(){
        Object dialog = uiController.find(COMPONENT_DLG_PAYMENT_SERVICE);
    	
    	if(dialog == null)
    	    dialog =  uiController.loadComponentFromFile(UI_FILE_PAYMENT_SERVICE_DIALOG, this);
    	
    	// Obtain reference to the "All Operators" list component
    	Object allOperators = uiController.find(dialog, COMPONENT_LS_ALL_NETWORK_OPERATORS);
    
    	// Populate the "All Operators" list
    	for(NetworkOperator operator: networkOperatorDao.getAllNetworkOperators()){
    	    Object item = getListItem(operator);
    		uiController.add(allOperators, item);
    	}
    	
    	return dialog;
    }
    
    /**
     * Gets an instance of the payment services panel showing the details of the specified payment service
     * @param service {@link PaymentService} whose details are to be displayed on the panel
     * @return
     */
    private Object getPaymentServiceDialog(PaymentService service){
        Object dialog = getPaymentServiceDialog();
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
    	    Object table = getPaymentServicesTable(true);
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
     * @param load Specifes whether to pupulate the table with the list of payment services
     * @return
     */
    private Object getPaymentServicesTable(boolean load){		
        Object table = uiController.find(getSettingsRightPane(), COMPONENT_TBL_PAYMENT_SERVICES); 
    	
        if(table == null){
    	    Object container = uiController.loadComponentFromFile(UI_FILE_PAYMENT_SERVICE_TABLE, this);
    		table = uiController.find(container, COMPONENT_TBL_PAYMENT_SERVICES);
        }		
    	
        if(load){
            for(PaymentService service: paymentServiceDao.getAllPaymentServices()){
    	        Object row = getRow(service);
    		    uiController.add(table, row);
    	    }
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
    	uiController.add(getPaymentServiceDialog());
    }
    
    public void showPaymentService(Object component){
        Object selectedItem = uiController.getSelectedItem(component);
    	uiController.add(getPaymentServiceDialog(getPaymentService(selectedItem)));
    }
    
    /**
     * Displays the network operator dialog
     */
    public void showNetworkOperatorDialog(){
        uiController.add(getNetworkOperatorDialog());
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
    	
    	// If update, remove it from the list and add it again
    	if(operatorExists){
    	    Object row = uiController.getSelectedItem(getNetworkOperatorsTable(false));
    		uiController.remove(row);
    	}
    	
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
    	    if(operatorExists(sourceList, selectedItem) && !operatorExists(targetList, selectedItem)){
    		    uiController.remove(selectedItem);
    			uiController.add(targetList, selectedItem);
    		}
    		
    		if(operatorExists(sourceList, selectedItem) && operatorExists(targetList, selectedItem)){
    		    uiController.remove(selectedItem);
    		}
    			
    	}
    }
    
    /**
     * Helper method to check if a network operator exists in the target list
     * @param targetList List to which the network operator is to be added
     * @param listItem {@link Thinlet} UI list item to be added to the target list
     * @return
     */
    private boolean operatorExists(Object targetList, Object listItem){
        Object[] items = uiController.getItems(targetList);
    	
    	NetworkOperator operator = getNetworkOperator(listItem);
    	
    	for(int i=0; i<items.length; i++){
    	    NetworkOperator n = getNetworkOperator(items[i]);
    	    if(n.getId() == operator.getId())
    	        return true;
    	}
    	
    	return false;
    }
    
    /**
     * Shows the dialog for defining the response text messages for a payment service
     * @param serviceDialog
     */
    public void showNextPaymentServiceDialog(){
        Object dialog = getPaymentServiceDialog();
    	
    	// Fetch the attached PaymentService object
    	PaymentService service = getPaymentService(dialog);
    	
    	// No payment service found therefore create one
    	if(service == null){
    	    service = new PaymentService();
    	}
    	
    	// Set the properties for the payment service
    	service.setServiceName(uiController.getText(uiController.find(dialog, COMPONENT_FLD_SERVICE_NAME)));
    	service.setSmsShortCode(uiController.getText(uiController.find(dialog, COMPONENT_FLD_SMS_SHORT_CODE)));
    	service.setPinNumber(uiController.getText(uiController.find(dialog, COMPONENT_FLD_PIN_NUMBER)));
    	service.setSendMoneyTextMessage(uiController.getText(uiController.find(dialog, COMPONENT_FLD_SEND_MONEY_TEXT)));
    	service.setWithdrawMoneyTextMessage(uiController.getText(uiController.find(dialog, COMPONENT_FLD_WITHDRAW_MONEY_TEXT)));
    	service.setBalanceEnquiryTextMessage(uiController.getText(uiController.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_TEXT)));
    	
    	Object[] operatorList = uiController.getItems(uiController.find(dialog, COMPONENT_LS_SELECTED_OPERATORS));
    	
    	// Set to hold the list of selected operators
    	Set<NetworkOperator> operators = new HashSet<NetworkOperator>();
    	
    	for(int i=0; i<operatorList.length; i++){
    	    operators.add(getNetworkOperator(operatorList[i]));
    	}
    	
    	service.setNetworkOperators(operators);
    	
    	// Remove the initial dialog
    	removeDialog(dialog);
    	
    	// Load the response text dialog
    	Object responseTextDialog = uiController.loadComponentFromFile(UI_FILE_RESPONSE_TEXTS_DIALOG, this);
    	
    	// Set the values for the text fields
    	setPaymentServiceResponseTexts(responseTextDialog, service);
    	
    	// Attach the payment service to the response text dialog, add it to the UI and reload
    	uiController.setAttachedObject(responseTextDialog, service);		
    	uiController.add(responseTextDialog);
    	uiController.repaint();
    }
    
    public void showPreviousPaymentServiceDialog(Object currentDialog){
        // Get the attached payment service
        PaymentService service = getPaymentService(currentDialog);
    	
    	// Null check
    	if(service == null) return;
    	
    	// Cache the response texts before proceeding to the previous dialog
    	fetchPaymentServiceResponseTexts(currentDialog, service);
    	
    	removeDialog(currentDialog);
    	Object previousDialog = getPaymentServiceDialog(service);
    	
    	uiController.add(previousDialog);
    }
    
    
    /**
     * Helper method for fetching the values of the response texts from the UI fields
     * and updating the specified PaymentService instance
     * @param service
     */
    private void fetchPaymentServiceResponseTexts(Object dialog, PaymentService service){
        String repaymentText = uiController.getText(uiController.find(dialog, COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT)).trim();
    	String repaymentKeyword = uiController.getText(uiController.find(dialog, COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT_KEYWORD)).trim();
    	String dispersalText = uiController.getText(uiController.find(dialog, COMPONENT_FLD_DISPERSAL_CONFIRM_TEXT_KEYWORD)).trim();
    	String dispersalKeyword = uiController.getText(uiController.find(dialog, COMPONENT_FLD_DISPESRAL_CONFIRM_TEXT)).trim();
    	String enquiryText = uiController.getText(uiController.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT)).trim();
    	String enquiryKeyword = uiController.getText(uiController.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT_KEYWORD)).trim();
    	
    	// If the response texts and their keywords have been specified, perform validation checks
    	if(repaymentKeyword.length() > 0 && dispersalKeyword.length() > 0  && dispersalText.length() >0 
    			&& repaymentText.length() > 0){
    		
    	    // Prevent the keywords for dispersals and repayments from being the same
    	    if(repaymentKeyword.equalsIgnoreCase(dispersalKeyword)){
    		    //TODO: Show dialog with appropriate error message
    		    return;
    		}
    	
    	}
    
    	// Set the confirmation texts and their keywords
    	if(containsKeyword(repaymentText, repaymentKeyword)) {
    	    service.setRepaymentConfirmationText(repaymentText);
    		service.setRepaymentConfirmationKeyword(repaymentKeyword);
    	}
    	
    	if(containsKeyword(dispersalText, dispersalKeyword)){
    	    service.setDispersalConfirmationKeyword(dispersalText);
    		service.setDispersalConfirmationText(dispersalKeyword);
    	}
    	
    	if(containsKeyword(enquiryText, enquiryKeyword)){
    	    service.setBalanceEnquiryTextMessage(enquiryText);
    	}
    }
    
    /**
     * Helper method to check existence of a keyword in a reponse text
     * @param responseText
     * @param keyword
     * @return
     */
    private boolean containsKeyword(String responseText, String keyword){
    	if(responseText.length() == 0 && keyword.length() == 0) {
    	    return true;
    	} else if(responseText.length() == 0 || keyword.length() ==0) {
    	    return false;
    	} else if (responseText.length() > 0 && keyword.length() > 0){
    	    Pattern pattern = Pattern.compile(keyword);
    	    Matcher matcher = pattern.matcher(responseText);
    	    return matcher.find();
    	}
    	return false;
    }
    
    /**
     * Helper method for setting values for the response text fields in the specified dialog
     * @param dialog
     * @param dialog
     */
    private void setPaymentServiceResponseTexts(Object dialog, PaymentService service){
        uiController.setText(uiController.find(dialog, COMPONENT_FLD_DISPERSAL_CONFIRM_TEXT_KEYWORD), service.getDispersalConfirmationKeyword());
        uiController.setText(uiController.find(dialog, COMPONENT_FLD_DISPESRAL_CONFIRM_TEXT), service.getDispersalConfirmationText());
        uiController.setText(uiController.find(dialog, COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT), service.getRepaymentConfirmationText());
        uiController.setText(uiController.find(dialog, COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT_KEYWORD), service.getRepaymentConfirmationKeyword());
        uiController.setText(uiController.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT), service.getBalanceEnquiryTextMessage());
    }
    
    /**
     * Saves a payment service
     */
    public void savePaymentService(Object dialog){
        // Fetch the payment service
        PaymentService service = getPaymentService(dialog);
        
        // Fetch the response text values
        fetchPaymentServiceResponseTexts(dialog, service);
        		
        // Flag to check existence of the payment service in the DB
        boolean serviceExists = (service.getId() >= 1) ? true:false;
        
        // Save/update the payment service
        try{
            if(serviceExists) {
                paymentServiceDao.updatePaymentService(service);
        	}else {
        	    paymentServiceDao.savePaymentService(service);
        	}
        	
        }catch(DuplicateKeyException e){
            LOG.debug("The payment service could not be saved", e);
        }
        
        
        // If update, remove the payment service being updated from the list of payment services 
        if(serviceExists){
            Object row = uiController.getSelectedItem(getPaymentServicesTable(false));
            uiController.remove(row);
        }
        
        // Update the list of payment services
        uiController.add(getPaymentServicesTable(false), getRow(service));
        uiController.repaint();
        
        // Close the dialog
        removeDialog(dialog);    	
    }
	
}