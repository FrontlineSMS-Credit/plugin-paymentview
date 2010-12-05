package org.creditsms.plugins.paymentview.ui.handler;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.domain.PaymentService;
import org.creditsms.plugins.paymentview.data.domain.QuickDialCode;
import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceDao;
import org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;

import thinlet.Thinlet;

/**
 * Thinlet UI handler for the "settings" tab in the payment view plugin. The settings tab
 * is used for configuring the plugin
 * 
 * @author Emmanuel Kala
 * <li>emkala(at)gmail(dot)com
 */
public class SettingsTabHandler extends BaseTabHandler {

    private static final String UI_FILE_SETTINGS_TAB = "/ui/plugins/paymentview/settingsTab.xml";
    private static final String UI_FILE_PAYMENT_SERVICE_TABLE = "/ui/plugins/paymentview/tbPaymentServices.xml";
    private static final String UI_FILE_NETWORK_OPERATOR_TABLE = "/ui/plugins/paymentview/tbNetworkOperators.xml";
    private static final String UI_FILE_NETWORK_OPERATOR_DIALOG = "/ui/plugins/paymentview/dgEditNetworkOperator.xml";
    private static final String UI_FILE_RESPONSE_TEXTS_DIALOG = "/ui/plugins/paymentview/dgPaymentServiceResponseTexts.xml";
    private static final String UI_FILE_QUICKDIAL_CODES_PANEL = "/ui/plugins/paymentview/pnQuickDialCodes.xml";
    private static final String UI_FILE_PAYMENT_SERVICE_DIALOG = "/ui/plugins/paymentview/dgEditPaymentService.xml";
    private static final String UI_FILE_QUICK_DIAL_CODE_DIALOG = "/ui/plugins/paymentview/dgQuickDialCode.xml";
    
    private static final String COMPONENT_LS_ALL_NETWORK_OPERATORS = "lstAllOperators";
    private static final String COMPONENT_LS_SELECTED_OPERATORS = "lstSelectedOperators";
    private static final String COMPONENT_PN_SETTINGS_RIGHT_PANE = "pnSettingsRightPane";
    private static final String COMPONENT_TBL_NETWORK_OPERATORS = "tblNetworkOperators";    
    private static final String COMPONENT_BT_EDIT_PAYMENT_SERVICE = "btEditPaymentService";
    private static final String COMPONENT_BT_DELETE_PAYMENT_SERVICE = "btDeletePaymentService";
    private static final String COMPONENT_TBL_PAYMENT_SERVICES = "tblPaymentServices";
    private static final String COMPONENT_FLD_DISPESRAL_CONFIRM_TEXT = "fldDispersalConfirmText";
    private static final String COMPONENT_FLD_DISPERSAL_CONFIRM_TEXT_KEYWORD = "fldDispersalConfirmKeyword";
    private static final String COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT = "fldRepaymentConfirmText";
    private static final String COMPONENT_FLD_REPAYMENT_CONFIRM_TEXT_KEYWORD = "fldRepaymentConfirmKeyword";
    private static final String COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT = "fldBalanceEnquiryConfirmText";
    private static final String COMPONENT_FLD_BALANCE_ENQUIRY_CONFIRM_TEXT_KEYWORD = "fldBalanceEnquiryConfirmKeyword";
    private static final String COMPONENT_PN_PAYMENT_SERVICES = "pnPaymentServiceList";
    private static final String COMPONENT_DLG_QUICK_DIAL_CODE = "quickDialCodeDialog";
    private static final String COMPONENT_TBL_QUICK_DIAL_CODES = "tblQuickDialCodes";
    private static final String COMPONENT_FLD_OPERATOR_NAME = "fldOperatorName";
    private static final String COMPONENT_DLG_NETWORK_OPERATOR = "networkOperatorDialog";
    private static final String COMPONENT_DLG_PAYMENT_SERVICE = "paymentServiceDialog";
    private static final String COMPONENT_FLD_SERVICE_NAME = "fldServiceName";
    private static final String COMPONENT_FLD_SMS_SHORT_CODE = "fldSmsShortCode";
    private static final String COMPONENT_FLD_PIN_NUMBER = "fldPinNumber";
    private static final String COMPONENT_FLD_SEND_MONEY_TEXT = "fldSendMoneyText";
    private static final String COMPONENT_FLD_WITHDRAW_MONEY_TEXT = "fldWithdrawMoneyText";
    private static final String COMPONENT_FLD_BALANCE_ENQUIRY_TEXT = "fldBalanceEnquiryText";    

    
    private static final String PAYMENT_VIEW_SAME_KEYWORD_ERROR = "paymentview.error.same.keyword";

    private NetworkOperatorDao networkOperatorDao;
    private PaymentServiceDao paymentServiceDao;
    private QuickDialCodeDao quickDialCodeDao;
    private PaymentViewThinletTabController tabController;
    private PaymentService selectedPaymentService;
    private QuickDialCode selectedQuickDialCode;
        
    public SettingsTabHandler(PaymentViewPluginController pluginController, UiGeneratorController ui){
        super(ui);
        
        this.networkOperatorDao = pluginController.getNetworkOperatorDao();
        this.paymentServiceDao = pluginController.getPaymentServiceDao();
        this.quickDialCodeDao = pluginController.getQuickDialCodeDao();
        
        this.tabController = pluginController.getTabController();
    }
    
    @Override
    protected Object initialiseTab() {
        Object settingsTab = ui.loadComponentFromFile(UI_FILE_SETTINGS_TAB, this);
        
        return settingsTab;
    }

    @Override
    public void refresh() {
        // TODO Auto-generated method stub
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
        if(ui.getName(selectedNode).equals("ndPaymentSystems")) {
            Object table = getPaymentServicesTable(true);
            ui.add(ui.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE), ui.getParent(table));
            ui.repaint();
        } else if(ui.getName(selectedNode).equals("ndNetworkOperators")) {
            Object table = getNetworkOperatorsTable(true);
            ui.add(ui.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE), ui.getParent(table));
            ui.repaint();
        } else if (ui.getName(selectedNode).equals("ndQuickDialCodes")) {
            Object table = getQuickDialCodesTable(true);
            ui.add(ui.find(parentTab, COMPONENT_PN_SETTINGS_RIGHT_PANE), ui.getParent(table));
            ui.repaint();
        }
    }
    
    private Object getSettingsRightPane(){
        return ui.find(tabController.getPaymentViewTab(), COMPONENT_PN_SETTINGS_RIGHT_PANE);
    }
    
    /**
     * Returns a {@link Thinlet} UI table object containing the list of network operators in the system
     * @param reload designates whether the table is to be reloaded with the list of operators
     * @return
     */
    private Object getNetworkOperatorsTable(boolean reload){
        Object table = ui.find(getSettingsRightPane(), COMPONENT_TBL_NETWORK_OPERATORS);
        
        if(table == null) {
            Object container = ui.loadComponentFromFile(UI_FILE_NETWORK_OPERATOR_TABLE, this);
            table = ui.find(container, COMPONENT_TBL_NETWORK_OPERATORS);
        }
        
        if(reload) {
            for(NetworkOperator operator: networkOperatorDao.getAllNetworkOperators()){
                Object row = tabController.getRow(operator);
                ui.add(table, row);
            }
        }
        
        return table;
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
                Object row = tabController.getRow(service);
                ui.add(table, row);
            }
        }
        return table;
    }
    
    /**
     * Returns a {@link Thinlet} UI table object containing the list of quick dial codes in the database
     * @param reload Whether the table is to be reloaded with the list of quick dial codes 
     * @return
     */
    private Object getQuickDialCodesTable(boolean reload) {
        Object table = ui.find(getSettingsRightPane(), COMPONENT_TBL_QUICK_DIAL_CODES);
        
        if(table == null) {
            Object container = ui.loadComponentFromFile(UI_FILE_QUICKDIAL_CODES_PANEL, this);
            table = ui.find(container, COMPONENT_TBL_QUICK_DIAL_CODES);
        }
        
        if(reload) {
            for (QuickDialCode quickDialCode: quickDialCodeDao.getAllQuickDialCodes()) {
                Object row = tabController.getRow(quickDialCode);
                ui.add(table, row);
            }
        }
        
        return table;
    }
    
    /**
     * Event helper for handling selection of a payment service from the list of payment services
     * @param component
     */
    public void selectPaymentService(Object component){
        Object selectedItem = ui.getSelectedItem(component);
        
        selectedPaymentService = tabController.getPaymentService(selectedItem);
        
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
        
        ui.repaint();
        
        log.trace("EXIT");
        
    }

    /**
     * Saves a network operator record into the database
     */
    public void saveNetworkOperator(){
        // Fetch the dialog
        Object dialog = getNetworkOperatorDialog();
        
        // Check if an existing record is being updated
        boolean operatorExists = (tabController.getNetworkOperator(dialog) == null)?false:true;
    
        // Fetch/Create a network operator instance as appropriate
        NetworkOperator operator = (operatorExists == true)?tabController.getNetworkOperator(dialog) : new NetworkOperator();
    
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
            log.debug("A network operator with the specified name already exists" + e);
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
        ui.add(getNetworkOperatorsTable(false), tabController.getRow(operator));
        ui.repaint();
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
        
        ui.repaint();
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
            Object item = tabController.getListItem(operator);
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
        for(NetworkOperator operator: service.getNetworkOperators()) {
            Object item = tabController.getListItem(operator);
            ui.add(selectedOperators, item);
        }
        
        return dialog;
    }
    
    
    
    /**
     * Displays the payment service dialog
     */
    public void showPaymentServiceDialog(){
        ui.add(getPaymentServiceDialog());
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
        
        NetworkOperator operator = tabController.getNetworkOperator(listItem);
        
        for(int i=0; i<items.length; i++){
            NetworkOperator n = tabController.getNetworkOperator(items[i]);
            if(n.getId() == operator.getId())
                return true;
        }
        
        return false;
    }
    
    /**
     * Saves a payment service
     */
    public void savePaymentService(Object dialog){
        // Fetch the payment service
        PaymentService service = tabController.getPaymentService(dialog);
        
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
            log.debug("The payment service could not be saved", e);
        }
        
        
        // If update, remove the payment service being updated from the list of payment services 
        if(serviceExists){
            Object row = ui.getSelectedItem(getPaymentServicesTable(false));
            ui.remove(row);
        }
        
        // Update the list of payment services
        ui.add(getPaymentServicesTable(false), tabController.getRow(service));
        ui.repaint();
        
        // Close the dialog
        removeDialog(dialog);       
    }
    
    /**
     * Shows the dialog for defining the response text messages for a payment service
     * @param serviceDialog
     */
    public void showNextPaymentServiceDialog(){
        Object dialog = getPaymentServiceDialog();
        
        // Fetch the attached PaymentService object
        PaymentService service = tabController.getPaymentService(dialog);
        
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
            operators.add(tabController.getNetworkOperator(operatorList[i]));
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
        if(tabController.containsKeyword(repaymentText, repaymentKeyword)) {
            service.setRepaymentConfirmationText(repaymentText);
            service.setRepaymentConfirmationKeyword(repaymentKeyword);
        }
        
        if(tabController.containsKeyword(dispersalText, dispersalKeyword)){
            service.setDispersalConfirmationKeyword(dispersalKeyword);
            service.setDispersalConfirmationText(dispersalText);
        }
        
        if(tabController.containsKeyword(enquiryText, enquiryKeyword)){
            service.setBalanceEnquiryTextMessage(enquiryText);
        }
    }

    public void showPreviousPaymentServiceDialog(Object currentDialog){
        // Get the attached payment service
        PaymentService service = tabController.getPaymentService(currentDialog);
        
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
     * Gets an instance of the quick dial code dialog
     * @return
     */
    private Object getQuickDialCodeDialog() {
        Object dialog = ui.find(COMPONENT_DLG_QUICK_DIAL_CODE);
        
        if(dialog == null) {
            dialog = ui.loadComponentFromFile(UI_FILE_QUICK_DIAL_CODE_DIALOG, this);
            
            for(NetworkOperator operator: networkOperatorDao.getAllNetworkOperators()) {
                Object choice = tabController.getChoice(operator);
                ui.add(ui.find(dialog, "cboNetworkOperator"), choice);
            }
        }
        
        return dialog;
    }
    
    /**
     * Gets an instance of the quick dial code dialog loaded with the details of the specified
     * quick dial code
     * 
     * @param quickDialCode
     * @return
     */
    private Object getQuickDialCodeDialog(QuickDialCode quickDialCode) {
        Object dialog = getQuickDialCodeDialog();
        ui.setAttachedObject(dialog, quickDialCode);
        
        ui.setText(ui.find(dialog, "fldUSSD_DialString"), quickDialCode.getQuickDialString());
        ui.setText(ui.find(dialog, "fldUSSD_Description"), quickDialCode.getQuickDialDescription());
        //ui.setS
        
        return dialog;
    }
    
    /**
     * Event helper method for saving and updating a quick dial code in the database
     */
    public void saveQuickDialCode() {
        // Get the quick dial code dialog
        Object dialog = getQuickDialCodeDialog();
        
        // Get the attached QuickDialCode instance
        QuickDialCode quickDialCode = tabController.getQuickDialCode(dialog);
        
        // Flag to determine whether it's a create or update operation
        boolean freshEntry = false;
        
        // If no instance is attached to the dialog, it's a new entry therefore create a new instance
        if (quickDialCode == null) {
            quickDialCode = new QuickDialCode();
            freshEntry = false;
        }
        
        String dialString = ui.getText(ui.find(dialog, "fldUSSD_DialString"));
        String description = ui.getText(ui.find(dialog, "txtUSSD_Description"));
        
        Object cboNetworkOperator = ui.find(dialog, "cboNetworkOperator");
        Object selectedNetworkOperator = ui.getChoice(cboNetworkOperator, Thinlet.SELECTED);
        NetworkOperator operator = tabController.getNetworkOperator(selectedNetworkOperator);
       
        // Set the properties
        quickDialCode.setQuickDialString(dialString);
        quickDialCode.setQuickDialDescription(description);
        quickDialCode.setNetworkOperator(operator);
        
        //TODO Set the additional properties such as the response type
       
        // Save the quick dial code
        try {
            // Check if operation is a create or update
            if(freshEntry) {
                quickDialCodeDao.saveQuickDialCode(quickDialCode);
            } else {
                quickDialCodeDao.updateQuickDialCode(quickDialCode);
            }
        } catch (DuplicateKeyException d) {
            log.debug("Error in saving the quick dial code entry", d);
        }
    }
    
    /**
     * Toggles the status of the action buttons
     * @param component
     */
    public void selectQuickDialCode(Object component) {
        selectedQuickDialCode = tabController.getQuickDialCode(component);
    }
    
    /**
     * Event helper to display the quick dial code dialog
     */
    public void showQuickDialCode() {
        ui.add(getQuickDialCodeDialog(selectedQuickDialCode));
    }
    
    public void showNewQuickDialCodeDialog() {
        ui.add(getQuickDialCodeDialog());
    }
    
}
