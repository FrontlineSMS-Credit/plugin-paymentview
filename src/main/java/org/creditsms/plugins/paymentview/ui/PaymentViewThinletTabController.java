/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;


import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.plugins.BasePluginThinletTabController;
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
public class PaymentViewThinletTabController extends BasePluginThinletTabController<PaymentViewPluginController> implements ThinletUiEventHandler, PagedComponentItemProvider {
//> UI FILES
    private static final String UI_FILE_CLIENT_DIALOG = "/ui/plugins/paymentview/dgEditClient.xml";
    private static final String UI_FILE_PAYMENT_SERVICE_DIALOG = "/ui/plugins/paymentview/dgEditPaymentService.xml";
    private static final String UI_FILE_PAYMENT_SERVICE_TABLE = "/ui/plugins/paymentview/tbPaymentServices.xml";
    private static final String UI_FILE_NETWORK_OPERATOR_TABLE = "/ui/plugins/paymentview/tbNetworkOperators.xml";
    private static final String UI_FILE_NETWORK_OPERATOR_DIALOG = "/ui/plugins/paymentview/dgEditNetworkOperator.xml";
    private static final String UI_FILE_RESPONSE_TEXTS_DIALOG = "/ui/plugins/paymentview/dgPaymentServiceResponseTexts.xml";
    private static final String UI_FILE_SEND_MONEY_DIALOG = "/ui/plugins/paymentview/dgSendMoneyForm.xml";
	
//> COMPONENT NAME CONSTANTS
    private static final String COMPONENT_BT_SAVE_CLIENT = "btSaveClient";
    private static final String COMPONENT_BT_DELETE_CLIENT = "btDeleteClient";
    private static final String COMPONENT_BT_EDIT_CLIENT = "btEditClient";
    private static final String COMPONENT_BT_SEND_MONEY = "btSendMoney";
    private static final String COMPONENT_BT_EDIT_PAYMENT_SERVICE = "btEditPaymentService";
    private static final String COMPONENT_BT_DELETE_PAYMENT_SERVICE = "btDeletePaymentService";
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
    private static final String COMPONENT_FLD_TRANSFER_AMOUNT = "fldAmount";
    private static final String COMPONENT_DLG_CLIENT_DETAILS = "clientDetailsDialog";
    private static final String COMPONENT_DLG_NETWORK_OPERATOR = "networkOperatorDialog";
    private static final String COMPONENT_DLG_PAYMENT_SERVICE = "paymentServiceDialog";
    private static final String COMPONENT_DLG_SEND_MONEY  = "sendMoneyDialog";
    private static final String COMPONENT_PN_SETTINGS_RIGHT_PANE = "pnSettingsRightPane";
    private static final String COMPONENT_PN_CLIENTS = "pnClients";
    private static final String COMPONENT_PN_DISPERSALS = "pnDispersals";
    private static final String COMPONENT_PN_REPAYMENTS = "pnRepayments";
    private static final String COMPONENT_PN_PAYMENT_SERVICES = "pnPaymentServiceList";
    private static final String COMPONENT_LB_CLIENT_NAME = "lbClientName";
    private static final String COMPONENT_CB_PAYMENT_SERVICE = "cbPaymentService";

//> I18N KEYS
    private static final String PAYMENTVIEW_LOADED = "paymentview.loaded";
    private static final String PAYMENT_VIEW_SAME_KEYWORD_ERROR = "paymentview.error.same.keyword";
    private static final String PAYMENTVIEW_NO_TRANSFER_AMOUNT = "paymentview.error.empty.amount";
    private static final String PAYMENTVIEW_INVALID_TRANSFER_AMOUNT = "paymentview.error.invalid.amount";
	
//> CONSTANTS
    private static final Logger LOG = FrontlineUtils.getLogger(PaymentViewThinletTabController.class);
	
//> UI COMPONENTS
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
    /** Currently selected payment service*/
    private PaymentService selectedPaymentService;
	
//> CONSTRUCTORS
    /**
     * 
     * @param paymentViewController value for {@link #controller}
     * @param uiController value for {@linkplain #ui}
     */
    public PaymentViewThinletTabController(PaymentViewPluginController controller, UiGeneratorController ui){
        super(controller, ui);
    }
	
    /**
     * Refreshes the tab display
     */
    public void refresh(){
        // Set the status message
        ui.setStatus(InternationalisationUtils.getI18NString(PAYMENTVIEW_LOADED));
        
        // Check messages that have not been processed and push them through
        processPendingTransactions();
    	
        // Populate the clients list and add the paging controls just below the list of clients
        Object clientList = getClientList();		
        clientsPagingHandler = new ComponentPagingHandler(this.ui, this, clientList);		
        Object pnClients = ui.find(this.paymentViewTab, COMPONENT_PN_CLIENTS);
        Object clientPageControls = clientsPagingHandler.getPanel();
        
        ui.setHAlign(clientPageControls, Thinlet.RIGHT);
        ui.add(pnClients, clientPageControls, 2);
        clientsPagingHandler.refresh();	
    
        // Populate the repayments table and add the paging controls just below the list of repayments
        Object repaymentsTable = getRepaymentsTable();		
        repaymentsPagingHandler = new ComponentPagingHandler(this.ui, this, repaymentsTable);		
        Object pnRepayments = ui.find(this.paymentViewTab, COMPONENT_PN_REPAYMENTS);
        ui.add(pnRepayments, repaymentsPagingHandler.getPanel(), 1);
        repaymentsPagingHandler.refresh();
    	
        // Populate the dispersals table and add the paging controls just below the list of dispersals 
        Object dispersalsTable = getDispersalsTable();		
        dispersalsPagingHandler = new ComponentPagingHandler(this.ui, this, dispersalsTable);
        Object pnDispersals = ui.find(this.paymentViewTab, COMPONENT_PN_DISPERSALS);
        ui.add(pnDispersals, dispersalsPagingHandler.getPanel(), 1);
        dispersalsPagingHandler.refresh();
    }
    
    /**
     * Internal helper method to process transaction-related messages
     */
    private void processPendingTransactions(){
        List<FrontlineMessage> pendingTransactions = transactionDao.getPendingTransactions();
    	for(FrontlineMessage message: pendingTransactions)
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
    public void processIncomingMessage(FrontlineMessage message){
        String sender = message.getSenderMsisdn();
    	String content = message.getTextContent();
    	
    	// Get the payment service from which the text message has been sent
    	PaymentService paymentService = paymentServiceDao.getPaymentServiceByShortCode(sender);
    	if(paymentService == null){
    		LOG.debug("FrontlineMessage not from a registered payment service");
    	    return;
    	}
    
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
    	    LOG.debug("Error: Keyword match failed");
    		return;
    	}
    	
    	PaymentServiceSmsProcessor processor = new PaymentServiceSmsProcessor(content, smsTemplate);
    	transaction.setTransactionAmount(processor.getAmount());
    	transaction.setTransactionCode(processor.getTransactionCode());
    	transaction.setClient(clientDao.getClientByPhoneNumber(processor.getPhoneNumber()));
    	
    	//Set the transaction date to the date when the message was received
    	transaction.setTransactionDate(new Date(message.getDate()));
    	
    	//TODO: Final transaction validation before writing to the database
    	
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
    	Object clientList = ui.find(paymentViewTab, COMPONENT_LS_CLIENTS);
    	return clientList;
    }
    
    /** @return the dispersals table component */
    private Object getDispersalsTable(){
    	Object dispersalsTable = ui.find(paymentViewTab, COMPONENT_TBL_DISPERSALS);
    	return dispersalsTable;
    }
    
    /** @return the repayments table component */
    private Object getRepaymentsTable(){
    	Object repaymentsTable = ui.find(paymentViewTab, COMPONENT_TBL_REPAYMENTS);
    	return repaymentsTable;
    }
    
    /**
     * Returns a {@link Thinlet} list items for the specified client
     * @param client The client to represent as a node
     * @return list item to insert to the thinlet list
     */
    private Object getListItem(Client client){	
    	Object node = ui.createListItem(client.getName(), client);
    	return node;
    }
    
    /**
     * Returns a {@link Thinlet} UI list item for the specified network operator
     * @param operator {@link NetworkOperator} instance represented by the list item
     * @return list item
     */
    private Object getListItem(NetworkOperator operator){
    	return ui.createListItem(operator.getOperatorName(), operator);
    }
    
    /**
     * Returns a {@link Thinlet} UI table row containing the details of a {@link PaymentServiceTransaction}
     * @param transaction {@link PaymentServiceTransaction} to be displayed in the row
     * @return
     */
    private Object getRow(PaymentServiceTransaction transaction){
    	Object row = ui.createTableRow(transaction);
    	ui.add(row, ui.createTableCell(transaction.getClient().getContact().getDisplayName()));
    	ui.add(row, ui.createTableCell(transaction.getPaymentService().getServiceName()));
    	ui.add(row, ui.createTableCell(Double.toString(transaction.getAmount())));
    	ui.add(row, ui.createTableCell(InternationalisationUtils.getDatetimeFormat().format(transaction.getDate())));
    	return row;
    }
    
    /**
     * Returns a {@link Thinlet} UI table row containing the name and short code of a {@link PaymentService}
     * @param service {@link PaymentService} to be displayed in the row
     * @return
     */
    private Object getRow(PaymentService service){
    	Object row = ui.createTableRow(service);
    	
    	ui.add(row, ui.createTableCell(service.getServiceName()));
    	ui.add(row, ui.createTableCell(service.getSmsShortCode()));
    	
    	return row;
    }
    
    /**
     * Returns a {@link Thinlet} UI table row containing the name of a {@link NetworkOperator}
     * @param operator
     * @return
     */
    private Object getRow(NetworkOperator operator){
    	Object row = ui.createTableRow(operator);
    	
    	ui.add(row, ui.createTableCell(operator.getOperatorName()));
    	
    	return row;
    }
    
    /**
     * Returns a {@link Thinlet} UI combobox choice containing the name of a {@link PaymentService}
     * @param service
     * @return
     */
    private Object getChoice(PaymentService service){
        Object choice = ui.createComboboxChoice(service.getServiceName(), service);
        return choice;
    }
    
    /**
     * Gets the {@link Client} instance attached to the supplied component 
     * @param component
     * @return the attached {@link Client} instance
     */
    public Client getClient(Object component){
    	return(Client)ui.getAttachedObject(component);
    }
    
    /**
     * Gets the {@link PaymentServiceTransaction} instance attached to supplied component
     * @param component
     * @return the attached {@link PaymentServiceTransaction} instance
     */
    public PaymentServiceTransaction getPaymentServiceTransaction(Object component){
    	return (PaymentServiceTransaction)ui.getAttachedObject(component);
    }
    
    /**
     * Gets the {@link PaymentService} instance attached to the supplied component
     * @param component
     * @return the attached {@link PaymentService} instance
     */
    public PaymentService getPaymentService(Object component){
    	return (PaymentService)ui.getAttachedObject(component);
    }
    
    /**
     * Gets the {@link NetworkOperator} instance attached to the supplied component
     * @param component
     * @return the attached {@link NetworkOperator} instance
     */
    public NetworkOperator getNetworkOperator(Object component){
    	return (NetworkOperator)ui.getAttachedObject(component);
    }
    
    /**
     * Performs a live search of client data and updates the display in real-time
     * @param textField 
     */
    public void liveClientSearch(Object textField){
    	LOG.info("Initiating live search");
    	
    	// Grab the typed text
    	String searchText = ui.getText(textField);
    
    	// Determine the search criteria to use
    	if(searchText.trim().length() > 0){
    		liveSearchString = searchText.trim();
    		LOG.trace("Live search for ["+searchText+"]...");
    	}
    
    	clientsPagingHandler.refresh();
    	dispersalsPagingHandler.refresh();
    	repaymentsPagingHandler.refresh();
    			
    	// Repaint the UI to reflect changes
    	ui.repaint(paymentViewTab);
    }
    
    /**
     * Event handler that is triggered when a client is selected from the list of clients on the UI
     * @param component reference to the client list UI component
     */
    public void selectClient(Object component){
    	// Get references to the edit and delete buttons
    	Object btnEditClient = ui.find(paymentViewTab, COMPONENT_BT_EDIT_CLIENT);
    	Object btnDeleteClient = ui.find(paymentViewTab, COMPONENT_BT_DELETE_CLIENT);
    	Object btnSendMoney = ui.find(paymentViewTab, COMPONENT_BT_SEND_MONEY);
    	
    	// Get "the enabled" property of the buttons
    	boolean editEnabled = ui.getBoolean(btnEditClient, Thinlet.ENABLED);
    	boolean deleteEnabled = ui.getBoolean(btnDeleteClient, Thinlet.ENABLED);
    	boolean sendMoneyEnabled = ui.getBoolean(btnSendMoney, Thinlet.ENABLED);
    	
    	// Enable the buttons if disabled
    	if(editEnabled == false){
    		ui.setEnabled(btnEditClient,  true);
    	}
    	
    	if(deleteEnabled == false){
    		ui.setEnabled(btnDeleteClient, true);
    	}
    	
    	if(sendMoneyEnabled == false){
    	    ui.setEnabled(btnSendMoney, true);
    	}
    	
    	// Get the client instance attached to the list item
    	Client client = getClient(ui.getSelectedItem(component));
    	
    	// Set the selected client
    	selectedClient = client;
    				
    	LOG.debug("Getting transactions for client: " + client);
    	
    	dispersalsPagingHandler.refresh();
    	repaymentsPagingHandler.refresh();
    
    	ui.repaint(paymentViewTab);
    }
    
    /**
     * Event handler that is triggered when a client record on the UI client list is double clicked
     * @param component
     */
    public void showClientDetails(){		
    	// Load the client details dialog
    	Object dialog = getClientDialog(selectedClient);
    	ui.add(dialog);
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
    	ui.remove(dialog);
    }
    
    /**
     * Performs input validation for the required client properties 
     */
    public void validateRequiredFields(){
    	Object dialog = getClientDialog();
    	String clientName = ui.getText(ui.find(dialog, COMPONENT_FLD_CLIENT_NAME)).trim();
    	String phoneNumber = ui.getText(ui.find(dialog, COMPONENT_FLD_PHONE_NUMBER)).trim();
    	
    	// Check if the client name and phone number have been provided 
    	if(clientName.length() > 0 && phoneNumber.length() > 0){
    	  ui.setEnabled(ui.find(dialog, COMPONENT_BT_SAVE_CLIENT), true);  
    	}else{
    	    ui.setEnabled(ui.find(dialog, COMPONENT_BT_SAVE_CLIENT), false);
    	}
    	
    	ui.repaint();
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
    	client.setName(ui.getText(ui.find(dialog, COMPONENT_FLD_CLIENT_NAME)));
    	client.setPhoneNumber(ui.getText(ui.find(dialog, COMPONENT_FLD_PHONE_NUMBER)));
    	client.setOtherPhoneNumber(ui.getText(ui.find(dialog, COMPONENT_FLD_OTHER_PHONE_NUMBER)));
    	client.setEmailAddress(ui.getText(ui.find(dialog, COMPONENT_FLD_EMAIL_ADDRESS)));
    	
    	// Check if a contact record is to be created for this client
    	boolean createContact = ui.getBoolean(ui.find(dialog, "chkAddToContact"), "selected"); 
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
    		Object selectedItem = ui.getSelectedItem(getClientList());
    		ui.remove(selectedItem);
    	}
    	
    	//Update the UI
    	Object item = getListItem(client);
    	ui.add(getClientList(), item);		
    	
    	removeDialog(dialog);
    	ui.repaint(paymentViewTab);
    	
    }
    
    /**
     * Shows the client dialog
     */
    public void showNewClientForm(){
    	ui.add(getClientDialog());
    }
    
    /** @return a reference to the client dialog */
    private Object getClientDialog(){
    	Object dialog = ui.find(COMPONENT_DLG_CLIENT_DETAILS);
    	
    	if(dialog == null)
    		return ui.loadComponentFromFile(UI_FILE_CLIENT_DIALOG, this);
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
    	ui.setAttachedObject(dialog, client);
    	
    	ui.setText(ui.find(dialog, COMPONENT_FLD_CLIENT_NAME), client.getName());
    	ui.setText(ui.find(dialog, COMPONENT_FLD_PHONE_NUMBER), client.getPhoneNumber());
    	ui.setText(ui.find(dialog, COMPONENT_FLD_OTHER_PHONE_NUMBER), client.getOtherPhoneNumber());
    	ui.setText(ui.find(dialog, COMPONENT_FLD_EMAIL_ADDRESS), client.getEmailAddress());
    	ui.setEnabled(ui.find(dialog, COMPONENT_BT_SAVE_CLIENT), true);
    	
    	if(client.getContact() == null)
    		ui.setSelected(ui.find(dialog, "chkAddToContact"), false);
    	
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
        Object dialog = ui.find(COMPONENT_DLG_PAYMENT_SERVICE);
    	
    	if(dialog == null)
    	    dialog =  ui.loadComponentFromFile(UI_FILE_PAYMENT_SERVICE_DIALOG, this);
    	
    	// Obtain reference to the "All Operators" list component
    	Object allOperators = ui.find(dialog, COMPONENT_LS_ALL_NETWORK_OPERATORS);
    
    	// Populate the "All Operators" list
    	for(NetworkOperator operator: networkOperatorDao.getAllNetworkOperators()){
    	    Object item = getListItem(operator);
    		ui.add(allOperators, item);
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
        ui.setAttachedObject(dialog, service);
    
    	ui.setText(ui.find(dialog, COMPONENT_FLD_SERVICE_NAME), service.getServiceName());
    	ui.setText(ui.find(dialog, COMPONENT_FLD_SMS_SHORT_CODE), service.getSmsShortCode());
    	ui.setText(ui.find(dialog, COMPONENT_FLD_PIN_NUMBER), service.getPinNumber());
    	ui.setText(ui.find(dialog, COMPONENT_FLD_SEND_MONEY_TEXT), service.getSendMoneyTextMessage());
    	ui.setText(ui.find(dialog, COMPONENT_FLD_WITHDRAW_MONEY_TEXT), service.getWithdrawMoneyTextMessage());
    	ui.setText(ui.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_TEXT), service.getBalanceEnquiryTextMessage());
    	
    	Object selectedOperators = ui.find(dialog, COMPONENT_LS_SELECTED_OPERATORS);		
    	
    	// Populate the network operators list for the selected payment service
    	for(NetworkOperator operator: service.getNetworkOperators()){
    	    Object item = getListItem(operator);
    		ui.add(selectedOperators, item);
    	}
    	
    	return dialog;
    }
    
    
    /**
     * Gets an instance of the network operator dialog
     * @return
     */
    private Object getNetworkOperatorDialog(){
    	Object dialog = ui.find(COMPONENT_DLG_NETWORK_OPERATOR);
    	
    	if(dialog == null)
    	    dialog = ui.loadComponentFromFile(UI_FILE_NETWORK_OPERATOR_DIALOG, this);
    	
    	return dialog;
    }
    
    /**
     * Event handler for the settings tree menu
     * @param parentTab
     * @param treeView
     */
    public void showSettingsItem(Object parentTab, Object treeView){
        Object selectedNode = ui.getSelectedItem(treeView);
    	ui.removeAll(ui.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE));
    			
    	// No node has been selected
    	if(ui.getName(selectedNode) == null)
    		return;
    	
    	// Change the contents of the right-most panel depending on the selected tree node 
    	if(ui.getName(selectedNode).equals("ndPaymentSystems")){
    	    Object table = getPaymentServicesTable(true);
    		ui.add(ui.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE), ui.getParent(table));
    		ui.repaint();
    	}else if(ui.getName(selectedNode).equals("ndNetworkOperators")){
    	    Object table = getNetworkOperatorsTable(true);
    		ui.add(ui.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE), ui.getParent(table));
    		ui.repaint();
    	}
    }
    
    private Object getSettingsRightPane(){
        return ui.find(paymentViewTab, COMPONENT_PN_SETTINGS_RIGHT_PANE);
    }
    
    /**
     * Returns a {@link Thinlet} UI table object containing the list of payment services in the system
     * @param load Specifes whether to pupulate the table with the list of payment services
     * @return
     */
    private Object getPaymentServicesTable(boolean load){		
        Object table = ui.find(getSettingsRightPane(), COMPONENT_TBL_PAYMENT_SERVICES); 
    	
        if(table == null){
    	    Object container = ui.loadComponentFromFile(UI_FILE_PAYMENT_SERVICE_TABLE, this);
    		table = ui.find(container, COMPONENT_TBL_PAYMENT_SERVICES);
        }		
    	
        if(load){
            for(PaymentService service: paymentServiceDao.getAllPaymentServices()){
    	        Object row = getRow(service);
    		    ui.add(table, row);
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
        Object table = ui.find(getSettingsRightPane(), COMPONENT_TBL_NETWORK_OPERATORS);
    	
    	if(table == null){
    	    Object container = ui.loadComponentFromFile(UI_FILE_NETWORK_OPERATOR_TABLE, this);
    		table = ui.find(container, COMPONENT_TBL_NETWORK_OPERATORS);
    	}
    	
    	if(reload){
    	    for(NetworkOperator operator: networkOperatorDao.getAllNetworkOperators()){
    		    Object row = getRow(operator);
    			ui.add(table, row);
    		}
    	}
    	
    	return table;
    }
    
    /**
     * Displays the payment service dialog
     */
    public void showPaymentServiceDialog(){
    	ui.add(getPaymentServiceDialog());
    }
    
    /**
     * Event helper for handling selection of a payment service from the list of payment services
     * @param component
     */
    public void selectPaymentService(Object component){
        Object selectedItem = ui.getSelectedItem(component);
        
        selectedPaymentService = getPaymentService(selectedItem);
        
        Object panel = ui.find(getSettingsRightPane(), COMPONENT_PN_PAYMENT_SERVICES);
        Object btnEdit =  ui.find(panel, COMPONENT_BT_EDIT_PAYMENT_SERVICE);
        Object btnDelete = ui.find(panel, COMPONENT_BT_DELETE_PAYMENT_SERVICE);
        
        boolean editEnabled = ui.getBoolean(btnEdit, Thinlet.ENABLED);
        boolean deleteEnabled = ui.getBoolean(btnDelete, Thinlet.ENABLED);
        
        if(editEnabled == false){
            ui.setEnabled(btnEdit, true);
        }
        
        if(deleteEnabled == false){
            ui.setEnabled(btnDelete, true);
        }
        
        ui.repaint(paymentViewTab);
        
        LOG.trace("EXIT");
        
    }
    
    /**
     * Event helper method to display the create/edit {@link PaymentService} dialog
     */
    public void showPaymentService(){
    	ui.add(getPaymentServiceDialog(selectedPaymentService));
    }
    
    /**
     * Displays the network operator dialog
     */
    public void showNetworkOperatorDialog(){
        ui.add(getNetworkOperatorDialog());
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
    	String name = ui.getText(ui.find(dialog, COMPONENT_FLD_OPERATOR_NAME));
    			
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
    	    Object row = ui.getSelectedItem(getNetworkOperatorsTable(false));
    		if(row != null){
    		    ui.remove(row);
    		}
    	}
    	
    	// Close the dialog
    	removeDialog(dialog);
    	
    	// If update, remove it from the list and add it again
    	if(operatorExists){
    	    Object row = ui.getSelectedItem(getNetworkOperatorsTable(false));
    		ui.remove(row);
    	}
    	
    	// Add new/updated record to the display list
    	ui.add(getNetworkOperatorsTable(false), getRow(operator));
    	ui.repaint();
    }
    
    /**
     * Toggles the enabled property of the buttons that move network operators between the lists
     * of available and selected operators
     * @param list
     * @param button
     */
    public void toggleMoveOperatorButton(Object list, Object button){
    	if(ui.getSelectedIndex(list) != -1)
    	    ui.setEnabled(button, true);
    	else
    	    ui.setEnabled(button, false);
    }
    
    
    /**
     * Moves a network operator from one list to another
     * @param sourceList {@link Thinlet} UI list from which the item is to be moved
     * @param targetList {@link Thinlet} UI list to which the item is being moved
     */
    public void moveNetworkOperator(Object sourceList, Object targetList){
    	Object selectedItem = ui.getSelectedItem(sourceList);
    	
    	if(selectedItem != null){
    	    if(operatorExists(sourceList, selectedItem) && !operatorExists(targetList, selectedItem)){
    		    ui.remove(selectedItem);
    			ui.add(targetList, selectedItem);
    		}
    		
    		if(operatorExists(sourceList, selectedItem) && operatorExists(targetList, selectedItem)){
    		    ui.remove(selectedItem);
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
        Object[] items = ui.getItems(targetList);
    	
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
    	service.setServiceName(ui.getText(ui.find(dialog, COMPONENT_FLD_SERVICE_NAME)));
    	service.setSmsShortCode(ui.getText(ui.find(dialog, COMPONENT_FLD_SMS_SHORT_CODE)));
    	service.setPinNumber(ui.getText(ui.find(dialog, COMPONENT_FLD_PIN_NUMBER)));
    	service.setSendMoneyTextMessage(ui.getText(ui.find(dialog, COMPONENT_FLD_SEND_MONEY_TEXT)));
    	service.setWithdrawMoneyTextMessage(ui.getText(ui.find(dialog, COMPONENT_FLD_WITHDRAW_MONEY_TEXT)));
    	service.setBalanceEnquiryTextMessage(ui.getText(ui.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_TEXT)));
    	
    	Object[] operatorList = ui.getItems(ui.find(dialog, COMPONENT_LS_SELECTED_OPERATORS));
    	
    	// Set to hold the list of selected operators
    	Set<NetworkOperator> operators = new HashSet<NetworkOperator>();
    	
    	for(int i=0; i<operatorList.length; i++){
    	    operators.add(getNetworkOperator(operatorList[i]));
    	}
    	
    	service.setNetworkOperators(operators);
    	
    	// Remove the initial dialog
    	removeDialog(dialog);
    	
    	// Load the response text dialog
    	Object responseTextDialog = ui.loadComponentFromFile(UI_FILE_RESPONSE_TEXTS_DIALOG, this);
    	
    	// Set the values for the text fields
    	setPaymentServiceResponseTexts(responseTextDialog, service);
    	
    	// Attach the payment service to the response text dialog, add it to the UI and reload
    	ui.setAttachedObject(responseTextDialog, service);		
    	ui.add(responseTextDialog);
    	ui.repaint();
    }
    
    public void showPreviousPaymentServiceDialog(Object currentDialog){
        // Get the attached payment service
        PaymentService service = getPaymentService(currentDialog);
    	
    	// Null check
    	if(service == null) return;
    	
    	// Cache the response texts before proceeding to the previous dialog
    	fetchPaymentServiceResponseTexts(currentDialog, service);
    	
    	if(service.getRepaymentConfirmationKeyword().length() > 0 && service.getDispersalConfirmationKeyword().length() > 0){
        	if(service.getRepaymentConfirmationKeyword().equalsIgnoreCase(service.getDispersalConfirmationKeyword())){
        	    ui.alert(InternationalisationUtils.getI18NString(PAYMENT_VIEW_SAME_KEYWORD_ERROR));
        	    return;
        	}
    	}
    	
    	removeDialog(currentDialog);
    	Object previousDialog = getPaymentServiceDialog(service);
    	
    	ui.add(previousDialog);
    }
    
    
    /**
     * Helper method for fetching the values of the response texts from the UI fields
     * and updating the specified PaymentService instance
     * @param service
     */
    private void fetchPaymentServiceResponseTexts(Object dialog, PaymentService service){
        String repaymentText = ui.getText(ui.find(dialog, COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT)).trim();
    	String repaymentKeyword = ui.getText(ui.find(dialog, COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT_KEYWORD)).trim();
    	String dispersalText = ui.getText(ui.find(dialog, COMPONENT_FLD_DISPESRAL_CONFIRM_TEXT)).trim();
    	String dispersalKeyword = ui.getText(ui.find(dialog, COMPONENT_FLD_DISPERSAL_CONFIRM_TEXT_KEYWORD)).trim();
    	String enquiryText = ui.getText(ui.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT)).trim();
    	String enquiryKeyword = ui.getText(ui.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT_KEYWORD)).trim();
    	    
    	// Set the confirmation texts and their keywords
    	if(containsKeyword(repaymentText, repaymentKeyword)) {
    	    service.setRepaymentConfirmationText(repaymentText);
    		service.setRepaymentConfirmationKeyword(repaymentKeyword);
    	}
    	
    	if(containsKeyword(dispersalText, dispersalKeyword)){
    	    service.setDispersalConfirmationKeyword(dispersalKeyword);
    		service.setDispersalConfirmationText(dispersalText);
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
        ui.setText(ui.find(dialog, COMPONENT_FLD_DISPERSAL_CONFIRM_TEXT_KEYWORD), service.getDispersalConfirmationKeyword());
        ui.setText(ui.find(dialog, COMPONENT_FLD_DISPESRAL_CONFIRM_TEXT), service.getDispersalConfirmationText());
        ui.setText(ui.find(dialog, COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT), service.getRepaymentConfirmationText());
        ui.setText(ui.find(dialog, COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT_KEYWORD), service.getRepaymentConfirmationKeyword());
        ui.setText(ui.find(dialog, COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT), service.getBalanceEnquiryTextMessage());
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
        
        // Prevent same keyword for both dispersals and repayments
        if(service.getRepaymentConfirmationKeyword().length() > 0 && service.getDispersalConfirmationKeyword().length() > 0){
            if(service.getRepaymentConfirmationKeyword().equalsIgnoreCase(service.getDispersalConfirmationKeyword())){
                ui.alert(InternationalisationUtils.getI18NString(PAYMENT_VIEW_SAME_KEYWORD_ERROR));
                return;
            }
        }
        
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
            Object row = ui.getSelectedItem(getPaymentServicesTable(false));
            ui.remove(row);
        }
        
        // Update the list of payment services
        ui.add(getPaymentServicesTable(false), getRow(service));
        ui.repaint();
        
        // Close the dialog
        removeDialog(dialog);    	
    }
    
    /**
     * Event helper method for deleting a {@link PaymentService} record
     */
    public void deletePaymentService(){
        // Delete the payment service
        paymentServiceDao.deletePaymentService(selectedPaymentService, true);
        
        // Get the selected item
        Object item = ui.getSelectedItem(getPaymentServicesTable(false));
        
        // Delete the row from the table
        ui.remove(item);
        
        // Set the current payment service to null
        selectedPaymentService = null;
        
        ui.repaint(paymentViewTab);
    }
    
    /**
     * Gets the {@link Thinlet} UI dialog for sending money to client
     * @return
     */
    public Object getSendMoneyDialog(){
        Object dialog = ui.find(COMPONENT_DLG_SEND_MONEY);
        
        if(dialog == null){
            dialog = ui.loadComponentFromFile(UI_FILE_SEND_MONEY_DIALOG, this);
            Object cbPaymentService = ui.find(dialog, COMPONENT_CB_PAYMENT_SERVICE);
            for(PaymentService service: paymentServiceDao.getAllPaymentServices()){
                ui.add(cbPaymentService, getChoice(service));
            }
        }
        
        return dialog;
    }
    
    /**
     * {@link Thinlet} UI event helper method for displaying the send money dialog
     */
    public void showSendMoneyDialog(){
        Object dialog = getSendMoneyDialog();
        
        ui.setAttachedObject(dialog, selectedClient);
        ui.setText(ui.find(dialog, COMPONENT_LB_CLIENT_NAME), selectedClient.getName());
        
        ui.add(dialog);
    }
    /**
     * Event helper for initiating the send money transaction
     */
    public void sendMoney(){
        Object dialog = getSendMoneyDialog();
        
        // Get the selected payment service from the combobox
        Object comboChoice = ui.getSelectedItem(ui.find(dialog, COMPONENT_CB_PAYMENT_SERVICE));
        PaymentService service = getPaymentService(comboChoice);
        
        // Check if an amount has been specified
        String amount = ui.getText(ui.find(dialog, COMPONENT_FLD_TRANSFER_AMOUNT)).trim();
        if(amount.length() == 0){
            ui.alert(InternationalisationUtils.getI18NString(PAYMENTVIEW_NO_TRANSFER_AMOUNT));
            return;
        }
        
        // Validate the specified amount
        if(!validateAmount(amount)){
            ui.alert(InternationalisationUtils.getI18NString(PAYMENTVIEW_INVALID_TRANSFER_AMOUNT));
            return;
        }
        
        // Get the client attached to the dialog
        Client client = getClient(dialog);
        
        // Get the text message to be sent to the payment service
        String textMessage = getDispersalTextMessage(service, client.getPhoneNumber(), amount);
        
        // Null check
        if(textMessage != null){
            ui.getFrontlineController().sendTextMessage(service.getSmsShortCode(), textMessage);
            removeDialog(dialog);
        }
    }
    
    /**
     * Helper method for constructing the message for triggering dispersal of funds
     * @param service
     * @param recipient
     * @param amount
     * @return
     */
    private String getDispersalTextMessage(PaymentService service, String recipient, String amount){
        // Get the template message
        String template = service.getSendMoneyTextMessage();
        
        if(template == null){
            return null;
        }
        
        //TODO We may need an agnostic way to do this
        
        // Replace the tags for the template with the actual values
        String message = template.replace(PaymentServiceSmsProcessor.TG_PIN_NUMBER, service.getPinNumber());
        message = message.replace(PaymentServiceSmsProcessor.TG_PHONENUMBER, recipient);
        message = message.replace(PaymentServiceSmsProcessor.TG_AMOUNT, amount);
        
        return message;
    }
    
    /**
     * Helper method for performing number validation
     * @param amount
     * @return
     */
    private boolean validateAmount(String amount){
        double numericalValue;
        
        try{
           numericalValue = Double.parseDouble(amount); 
        }catch(NumberFormatException ne){
            LOG.debug("Invalid number", ne);
            return false;
        }       
        
        return (numericalValue <=0 )? false: true;
    }
	
}