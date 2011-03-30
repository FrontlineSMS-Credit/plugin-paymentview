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
import org.creditsms.plugins.paymentview.data.domain.PaymentServiceTransaction.TransactionType;
import org.creditsms.plugins.paymentview.data.domain.PaymentViewError;
import org.creditsms.plugins.paymentview.data.domain.QuickDialCode;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceDao;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceTransactionDao;
import org.creditsms.plugins.paymentview.data.repository.PaymentViewErrorDao;
import org.creditsms.plugins.paymentview.data.repository.QuickDialCodeDao;
import org.creditsms.plugins.paymentview.ui.handler.SettingsTabHandler;

import thinlet.Thinlet;

/**
 * 
 * @author Emmanuel Kala
 * @author Ian Mukewa
 */
public class PaymentViewThinletTabController extends
		BasePluginThinletTabController<PaymentViewPluginController> implements
		ThinletUiEventHandler, PagedComponentItemProvider {
	// > UI FILES
	private static final String UI_FILE_SEND_MONEY_DIALOG = "/ui/plugins/paymentview/dgSendMoneyForm.xml";

	// > ICON FILES
	private static final String ICON_STATUS_ATTENTION = "/icons/status_attention.png";
	// > COMPONENT NAME CONSTANTS
	/*
	 * Tabs
	 */
	private static final String TAB_TAB_EXPORT_PAYMENTS = "tab_tab_export_payments";
	private static final String TAB_EXPORT = "tab_export";
	private static final String TAB_TAB_OUTGOINGPAYMENTS_SELECTFROMCLIENTS = "tab_tab_outgoingPayments_selectFromClients";
	private static final String TAB_EXCEPTIONS = "tab_exceptions";
	private static final String TAB_TAB_EXPORT_CLIENTS = "tab_tab_export_clients";
	private static final String TAB_CLIENTLIST = "tab_clientList";
	private static final String TAB_TAB_OUTGOINGPAYMENTS_IMPORTNEWPAYMENTS = "tab_tab_outgoingPayments_importNewPayments";
	private static final String TAB_TAB_EXPORT_CLIENTHISTORY = "tab_tab_export_clientHistory";
	private static final String TAB_INCOMINGPAYMENTS = "tab_incomingPayments";
	private static final String TAB_OUTGOINGPAYMENTS = "tab_outgoingPayments";
	private static final String TABP_TAB_OUTGOINGPAYMENTS_TABS = "tabP_tab_outgoingPayments_tabs";
	private static final String TABP_TAB_OUTGOINGPAYMENTS_SENTPAYMENTS = "tabP_tab_outgoingPayments_sentPayments";
	private static final String TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS = "tab_tab_outgoingPayments_sendNewPayments";

	/*
	 * Panels
	 */
	private static final String PNL_TBL_CLIENTLIST = "pnl_tbl_clientList";
	private static final String PNL_TBL_TAB_OUTGOINGPAYMENTS_SENTPAYMENTS_CLIENTS = "pnl_tbl_tab_outgoingPayments_sentPayments_clients";
	private static final String PNL_TBL_TAB_INCOMINGPAYMENTS_CLIENTS = "pnl_tbl_tab_incomingPayments_clients";
	private static final String PNL_TAB_INCOMINGPAYMENTS_BUTTONS = "pnl_tab_incomingPayments_buttons";

	/*
	 * Combo boxes
	 */
	private static final String CMB_TAB_EXPORT_EXPORTALLRECORDSFROMGROUP = "cmb_tab_export_exportAllRecordsFromGroup";
	private static final String CMB_TAB_OUTGOINGPAYMENTS_SENTPAYMENTS_MOBILEPAYMENTACCOUNT = "cmb_tab_outgoingPayments_sentPayments_mobilePaymentAccount";
	private static final String CMB_TAB_INCOMINGPAYMENTS_MOBILEPAYMENTACCOUNT = "cmb_tab_incomingPayments_mobilePaymentAccount";
	private static final String CMB_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_MOBILEPAYMENTSYSTEM = "cmb_tab_tab_outgoingPayments_sendNewPayments_mobilePaymentSystem";

	/*
	 * Buttons
	 */
	private static final String BTN_TAB_INCOMINGPAYMENTS_ANALYSE = "btn_tab_incomingPayments_analyse";
	private static final String BTN_TAB_INCOMINGPAYMENTS_EXPORT = "btn_tab_incomingPayments_export";
	private static final String BTN_CUSTOMIZE_CLIENT_DB = "btn_tab_clientList_customizeClientDB";
	private static final String BTN_TAB_CLIENTLIST_ADD = "btn_tab_clientList_add";
	private static final String BTN_TAB_CLIENTLIST_IMPORT = "btn_tab_clientList_import";
	private static final String BTN_TAB_CLIENTLIST_EXPORT = "btn_tab_clientList_export";
	private static final String BTN_TAB_TAB_EXPORT_PAYMENTS_EXPORT_EXPORT = "btn_tab_tab_export_payments_export_export";
	private static final String BTN_TAB_OUTGOINGPAYMENTS_SELECTFROMCLIENTS_SENDPAYMENTTOSELECTION = "btn_tab_outgoingPayments_selectFromClients_sendPaymentToSelection";
	private static final String BTN_TAB_OUTGOINGPAYMENTS_SELECTFROMCLIENTS_SCHEDULEPAYMENTTOSELECTION = "btn_tab_outgoingPayments_selectFromClients_schedulePaymentToSelection";
	private static final String BTN_TAB_OUTGOINGPAYMENTS_SENTPAYMENTS_EXPORT = "btn_tab_outgoingPayments_sentPayments_export";
	private static final String BTN_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_SENDPAYMENTNOW = "btn_tab_tab_outgoingPayments_sendNewPayments_sendPaymentNow";
	private static final String BTN_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_SCHEDULEPAYMENT = "btn_tab_tab_outgoingPayments_sendNewPayments_schedulePayment";
	private static final String BROWSE_TAB_TAB_OUTGOINGPAYMENTS_IMPORTNEWPAYMENTS_FILE = "browse_tab_tab_outgoingPayments_importNewPayments_file";
	private static final String BTN_TAB_TAB_OUTGOINGPAYMENTS_IMPORTNEWPAYMENTS_SCHEDULEPAYMENT = "btn_tab_tab_outgoingPayments_importNewPayments_schedulePayment";
	private static final String BTN_TAB_TAB_OUTGOINGPAYMENTS_IMPORTNEWPAYMENTS_IMPORT = "btn_tab_tab_outgoingPayments_importNewPayments_import";
	private static final String BTN_TAB_TAB_OUTGOINGPAYMENTS_IMPORTNEWPAYMENTS_SENDPAYMENTNOW = "btn_tab_tab_outgoingPayments_importNewPayments_sendPaymentNow";
	private static final String BTN_TAB_TAB_EXPORT_CLIENTS_EXPORTSELECTEDCLIENTS = "btn_tab_tab_export_clients_exportSelectedClients";
	private static final String BTN_TAB_TAB_EXPORT_CLIENTS_SELECTALL = "btn_tab_tab_export_clients_selectAll";
	private static final String BTN_TAB_TAB_EXPORT_CLIENTS_EXPORTALLRECORDSFROMGROUP = "btn_tab_tab_export_clients_exportAllRecordsFromGroup";
	private static final String BTN_TAB_TAB_EXPORT_PAYMENTS_BANNER = "btn_tab_tab_export_payments_banner";

	/*
	 * Group Buttons
	 */
	private static final String GRP_TAB_EXPORT_PAYMENTS_OUTGOINGPAYMENTS = "grp_tab_export_payments_outgoingPayments";
	private static final String GRP_TAB_EXPORT_PAYMENTS_INCOMINGPAYMENTS = "grp_tab_export_payments_incomingPayments";


	/*
	 * Tables
	 */
	private static final String TBL_TAB_INCOMINGPAYMENTS_CLIENTS = "tbl_tab_incomingPayments_clients";
	private static final String TBL_CLIENTLIST = "tbl_clientList";
	private static final String TBL_TAB_TAB_OUTGOINGPAYMENTS_IMPORTNEWPAYMENTS_CLIENTS = "tbl_tab_tab_outgoingPayments_importNewPayments_clients";
	private static final String TBL_TAB_TAB_EXPORT_PAYMENTS_CLIENTS = "tbl_tab_tab_export_payments_clients";
	private static final String TBL_TAB_OUTGOINGPAYMENTS_SENTPAYMENTS_CLIENTS = "tbl_tab_outgoingPayments_sentPayments_clients";
	private static final String TBL_TAB_OUTGOINGPAYMENTS_SELECTFROMCLIENTS_CLIENTS = "tbl_tab_outgoingPayments_selectFromClients_clients";
	private static final String TBL_TAB_TAB_EXPORT_CLIENTS_CLIENTSLIST = "tbl_tab_tab_export_clients_clientsList";
	
	//Form
	private static final String FRM_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_CUSTOMERDETAILS = "frm_tab_tab_outgoingPayments_sendNewPayments_customerDetails";
	
	/*
	 * Textfields
	 */
	private static final String TXT_TAB_EXPORT_PAYMENTS_FROMDATE = "txt_tab_export_payments_fromDate";
	private static final String TXT_TAB_EXPORT_PAYMENTS_TODATE = "txt_tab_export_payments_toDate";
	private static final String TXT_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_PHONE = "txt_tab_tab_outgoingPayments_sendNewPayments_phone";
	private static final String TXT_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_NAME = "txt_tab_tab_outgoingPayments_sendNewPayments_name";
	private static final String TXT_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_VERIFYPHONENUMBER = "txt_tab_tab_outgoingPayments_sendNewPayments_verifyPhoneNumber";
	private static final String TXT_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_AMOUNT = "txt_tab_tab_outgoingPayments_sendNewPayments_amount";
	private static final String TXT_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_PAYMENTID = "txt_tab_tab_outgoingPayments_sendNewPayments_paymentID";
	private static final String TXT_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_NOTES = "txt_tab_tab_outgoingPayments_sendNewPayments_notes";
	private static final String TXT_TAB_TAB_EXPORT_CLIENTHISTORY_PHONENUMBER = "txt_tab_tab_export_clientHistory_phoneNumber";
	private static final String TXT_TAB_TAB_EXPORT_CLIENTHISTORY_ACCOUNTNUMBER = "txt_tab_tab_export_clientHistory_accountNumber";

	/*
	 * Labels
	 */
	private static final String LBL_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_AMOUNT = "lbl_tab_tab_outgoingPayments_sendNewPayments_amount";
	private static final String LBL_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_MOBILEPAYMENTSYSTEM = "lbl_tab_tab_outgoingPayments_sendNewPayments_mobilePaymentSystem";
	private static final String LBL_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_NOTES = "lbl_tab_tab_outgoingPayments_sendNewPayments_notes";
	private static final String LBL_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_PAYMENTID = "lbl_tab_tab_outgoingPayments_sendNewPayments_paymentID";
	private static final String LBL_TAB_OUTGOINGPAYMENTS_SELECTFROMCLIENTS_BANNER = "lbl_tab_outgoingPayments_selectFromClients_banner";
	private static final String LBL_TAB_EXPORT_HINT = "lbl_tab_export_hint";
	private static final String LBL_TAB_TAB_EXPORT_CLIENTHISTORY_BANNER = "lbl_tab_tab_export_clientHistory_banner";
	private static final String LBL_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_NAME = "lbl_tab_tab_outgoingPayments_sendNewPayments_Name";
	private static final String LBL_TAB_EXPORT_PAYMENTS_FROMDATE = "lbl_tab_export_payments_fromDate";
	private static final String LBL_TAB_EXPORT_PAYMENTS_TODATE = "lbl_tab_export_payments_toDate";
	private static final String LBL_TAB_TAB_EXPORT_PAYMENTS_TYPEOFPAYMENT = "lbl_tab_tab_export_payments_typeOfPayment";
	private static final String LBL_TAB_TAB_EXPORT_CLIENTS_EXPORTALLRECORDSFROMGROUP = "lbl_tab_tab_export_clients_exportAllRecordsFromGroup";
	private static final String LBL_TAB_TAB_OUTGOINGPAYMENTS_SENDNEWPAYMENTS_PHONE = "lbl_tab_tab_outgoingPayments_sendNewPayments_Phone";

	/*
	 * Checkboxes
	 */
	private static final String CHK_TAB_TAB_EXPORT_CLIENTHISTORY_PDF = "chk_tab_tab_export_clientHistory_PDF";
	private static final String CHK_TAB_TAB_EXPORT_CLIENTHISTORY_INCOMINGPAYMENTS = "chk_tab_tab_export_clientHistory_incomingPayments";
	private static final String CHK_TAB_TAB_EXPORT_CLIENTHISTORY_OUTGOINGPAYMENTS = "chk_tab_tab_export_clientHistory_outgoingPayments";
	private static final String CHK_TAB_TAB_EXPORT_CLIENTHISTORY_CSV = "chk_tab_tab_export_clientHistory_CSV";
	private static final String CHK_TAB_TAB_EXPORT_CLIENTHISTORY_PHONENUMBER = "chk_tab_tab_export_clientHistory_phoneNumber";
	private static final String CHK_TAB_TAB_EXPORT_CLIENTHISTORY_ACCOUNTNUMBER = "chk_tab_tab_export_clientHistory_accountNumber";
	private static final String CHK_EXPORTBYSELECTION = "chk_exportBySelection";
	private static final String CHK_DATERANGE = "chk_dateRange";

	// > I18N KEYS 
	private static final String PAYMENTVIEW_LOADED = "paymentview.loaded";
	private static final String PAYMENTVIEW_NO_TRANSFER_AMOUNT = "paymentview.error.empty.amount";
	private static final String PAYMENTVIEW_INVALID_TRANSFER_AMOUNT = "paymentview.error.invalid.amount";

	// > CONSTANTS
	private static final Logger LOG = FrontlineUtils.getLogger(PaymentViewThinletTabController.class);

	// > UI COMPONENTS
	/** Thinlet tab component whose functionality is handled by this class */
	private Object paymentViewTab;

	/** Paging handler for clients */
	private ComponentPagingHandler clientsPagingHandler;
	/** Paging handler for dispersals */
	private ComponentPagingHandler dispersalsPagingHandler;
	/** Paging handler for repayments */
	private ComponentPagingHandler repaymentsPagingHandler;

	

	private PaymentViewPluginController controller;
	// > PROPERTIES
	/** String used for the live search */
	private String liveSearchString;
	/** Keeps track of the currently selected client */
	private Client selectedClient;

	// > CONSTRUCTORS
	/**
	 * 
	 * @param paymentViewController
	 *            value for {@link #controller}
	 * @param uiController
	 *            value for {@linkplain #ui}
	 */
	public PaymentViewThinletTabController(
			PaymentViewPluginController controller, UiGeneratorController ui) {
		super(controller, ui);

		this.controller = controller;
	}

	/**
	 * Refreshes the tab display
	 */
	public void refresh() {
		// Set the status message
		ui.setStatus(InternationalisationUtils
				.getI18nString(PAYMENTVIEW_LOADED));

		// Check messages that have not been processed and push them through
		processPendingTransactions();

		// Populate the clients list and add the paging controls just below the
		// list of clients
		Object clientList = getClientList();
		//clientsPagingHandler = new ComponentPagingHandler(this.ui, this, clientList);
		//Object pnClients = ui.find(this.paymentViewTab, COMPONENT_PN_CLIENTS);
		Object clientPageControls = clientsPagingHandler.getPanel();

		ui.setHAlign(clientPageControls, Thinlet.RIGHT);
		//ui.add(pnClients, clientPageControls, 2);
		//clientsPagingHandler.refresh();

		// Populate the repayments table and add the paging controls just below
		// the list of repayments
		Object repaymentsTable = getRepaymentsTable();
		//repaymentsPagingHandler = new ComponentPagingHandler(this.ui, this, repaymentsTable);
		//Object pnRepayments = ui.find(this.paymentViewTab, COMPONENT_PN_REPAYMENTS);
		//ui.add(pnRepayments, repaymentsPagingHandler.getPanel(), 1);
		//repaymentsPagingHandler.refresh();

		// Populate the dispersals table and add the paging controls just below
		// the list of dispersals
		Object dispersalsTable = getDispersalsTable();
		//dispersalsPagingHandler = new ComponentPagingHandler(this.ui, this, dispersalsTable);
		//Object pnDispersals = ui.find(this.paymentViewTab, COMPONENT_PN_DISPERSALS);
		//ui.add(pnDispersals, dispersalsPagingHandler.getPanel(), 1);
		//dispersalsPagingHandler.refresh();

		// Populate the exceptions table and add a paging handler for the
		// exceptions
		Object exceptionsTable = getExceptionsTable();
		///exceptionsPagingHandler = new ComponentPagingHandler(ui, this, exceptionsTable);

		//Object pnExceptions = ui.find(this.paymentViewTab, COMPONENT_PN_EXCEPTIONS);
		//ui.add(pnExceptions, exceptionsPagingHandler.getPanel(), 1);
		///exceptionsPagingHandler.refresh();

		//Object paymentViewTabbedPane = ui.find(this.paymentViewTab, COMPONENT_TB_PAYMENT_VIEW);
		SettingsTabHandler settingsTab = new SettingsTabHandler(controller, ui);
		settingsTab.init();

		//ui.add(paymentViewTabbedPane, settingsTab.getTab());
	}

	/**
	 * Internal helper method to process transaction-related messages
	 */
	private void processPendingTransactions() {
	}

	// > MUTATORS

	/**
	 * Sets the {@link Thinlet} UI tab component for the "Payment View plugin".
	 * The tab component holds all the {@link Thinlet} UI components for the
	 * plugin.
	 * 
	 * @param paymentViewTab
	 *            value for {@link #paymentViewTab}
	 */
	public void setTabComponent(Object paymentViewTab) {
		this.paymentViewTab = paymentViewTab;
	}

		
	// LIST PAGING METHODS
	/** @see PagedComponentItemProvider#getListDetails(Object, int, int) */
	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list.equals(clientsPagingHandler.getList())) {
			return getClientListPagingDetails(startIndex, limit);
		} else if (list.equals(repaymentsPagingHandler.getList())) {
			return getRepaymentListPagingDetails(startIndex, limit);
		} else if (list.equals(dispersalsPagingHandler.getList())) {
			return getDispersalListPagingDetails(startIndex, limit);
		//} else if (list.equals(exceptionsPagingHandler.getList())) {
		//	return getExceptionsListPagingDetails(startIndex, limit);
		} else
			throw new IllegalStateException();
	}

	private PagedListDetails getClientListPagingDetails(int startIndex,
			int limit) {
		List<Client> clients = null;
		int totalClientCount = 0;

		Object[] clientRows = new Object[2];

		return new PagedListDetails(totalClientCount, clientRows);
	}

	private PagedListDetails getRepaymentListPagingDetails(int startIndex,
			int limit) {
		// Get the number of items returned
		int totalItems = 2;
		Object[] transactionRows = new Object[totalItems];
		return new PagedListDetails(totalItems, transactionRows);
	}

	private PagedListDetails getDispersalListPagingDetails(int startIndex,
			int limit) {
		List<PaymentServiceTransaction> transactions;

		
		// Get the number of items returned
		int totalItems = 2;

		Object[] transactionRows = new Object[totalItems];
		return new PagedListDetails(totalItems, transactionRows);
	}

	private PagedListDetails getExceptionsListPagingDetails(int startIndex,
			int limit) {
		int itemCount = 2;
		Object[] rows = new Object[itemCount];
		return new PagedListDetails(itemCount, rows);
	}

	/**
	 * Processes the incoming message by extracting the necessary information
	 * and updating the client records. The processing makes use of message
	 * filtering rules. If no rules are present, the no information is extracted
	 * from the message
	 * 
	 * @param message
	 */
	public void processIncomingMessage(FrontlineMessage message) {
	}

	// > THINLET UI & EVENT HELPER METHODS

	/**
	 * @return The client list component
	 */
	private Object getClientList() {
		Object clientList = new Object();//ui.find(paymentViewTab, COMPONENT_LS_CLIENTS);
		return clientList;
	}

	/** @return the dispersals table component */
	private Object getDispersalsTable() {
		Object dispersalsTable = new Object();//ui.find(paymentViewTab, COMPONENT_TBL_DISPERSALS);
		return dispersalsTable;
	}

	/** @return the repayments table component */
	private Object getRepaymentsTable() {
		Object repaymentsTable = new Object();//ui.find(paymentViewTab, COMPONENT_TBL_REPAYMENTS);
		return repaymentsTable;
	}

	/** @return the exceptions table UI component */
	private Object getExceptionsTable() {
		Object exceptionsTable = new Object();//ui.find(paymentViewTab, COMPONENT_TBL_EXCEPTIONS);
		return exceptionsTable;
	}

	/**
	 * Returns a {@link Thinlet} list items for the specified client
	 * 
	 * @param client
	 *            The client to represent as a node
	 * @return list item to insert to the thinlet list
	 */
	private Object getListItem(Client client) {
		Object node = ui.createListItem(client.getName(), client);
		return node;
	}

	/**
	 * Returns a {@link Thinlet} UI list item for the specified network operator
	 * 
	 * @param operator
	 *            {@link NetworkOperator} instance represented by the list item
	 * @return list item
	 */
	public Object getListItem(NetworkOperator operator) {
		return ui.createListItem(operator.getOperatorName(), operator);
	}

	/**
	 * Returns a {@link Thinlet} UI table row containing the details of a
	 * {@link PaymentServiceTransaction}
	 * 
	 * @param transaction
	 *            {@link PaymentServiceTransaction} to be displayed in the row
	 * @return
	 */
	public Object getRow(PaymentServiceTransaction transaction) {
		Object row = ui.createTableRow(transaction);
		ui.add(row,
				ui.createTableCell(transaction.getClient().getContact()
						.getDisplayName()));
		ui.add(row, ui.createTableCell(transaction.getPaymentService()
				.getServiceName()));
		ui.add(row,
				ui.createTableCell(Double.toString(transaction.getAmount())));
		ui.add(row, ui.createTableCell(InternationalisationUtils
				.getDatetimeFormat().format(transaction.getDate())));
		return row;
	}

	/**
	 * Returns a {@link Thinlet} UI table row containing the name and short code
	 * of a {@link PaymentService}
	 * 
	 * @param service
	 *            {@link PaymentService} to be displayed in the row
	 * @return
	 */
	public Object getRow(PaymentService service) {
		Object row = ui.createTableRow(service);

		ui.add(row, ui.createTableCell(service.getServiceName()));
		ui.add(row, ui.createTableCell(service.getSmsShortCode()));

		return row;
	}

	/**
	 * Returns a {@link Thinlet} UI table row containing the name of a
	 * {@link NetworkOperator}
	 * 
	 * @param operator
	 * @return
	 */
	public Object getRow(NetworkOperator operator) {
		Object row = ui.createTableRow(operator);

		ui.add(row, ui.createTableCell(operator.getOperatorName()));

		return row;
	}

	/**
	 * Gets a table row containing an entry for quick dial code
	 * 
	 * @param quickDialCode
	 * @return
	 */
	public Object getRow(QuickDialCode quickDialCode) {
		Object row = ui.createTableRow(quickDialCode);

		ui.add(row, ui.createTableCell(quickDialCode.getQuickDialString()));
		ui.add(row, ui.createTableCell(quickDialCode.getQuickDialDescription()));

		return row;
	}

	/**
	 * Gets a table row containing an payment view error message
	 * 
	 * @param error
	 * @return
	 */
	public Object getRow(PaymentViewError error) {
		Object row = ui.createTableRow(error);

		Date errorDate = new Date(error.getErrorDate());

		Object iconCell = ui.createTableCell("");
		ui.setIcon(iconCell, ICON_STATUS_ATTENTION);

		ui.add(row, iconCell);
		ui.add(row, ui.createTableCell(error.getErrorType().toString()));
		ui.add(row, ui.createTableCell(InternationalisationUtils
				.getDatetimeFormat().format(errorDate)));
		ui.add(row, ui.createTableCell(error.getErrorDescription()));

		return row;
	}

	/**
	 * Returns a {@link Thinlet} UI combobox choice containing the name of a
	 * {@link PaymentService}
	 * 
	 * @param service
	 * @return
	 */
	public Object getChoice(PaymentService service) {
		Object choice = ui.createComboboxChoice(service.getServiceName(),
				service);
		return choice;
	}

	public Object getChoice(NetworkOperator operator) {
		Object choice = ui.createComboboxChoice(operator.getOperatorName(),
				operator);
		return choice;
	}

	/**
	 * Gets the {@link Client} instance attached to the supplied component
	 * 
	 * @param component
	 * @return the attached {@link Client} instance
	 */
	public Client getClient(Object component) {
		return (Client) ui.getAttachedObject(component);
	}

	/**
	 * Gets the {@link PaymentServiceTransaction} instance attached to supplied
	 * component
	 * 
	 * @param component
	 * @return the attached {@link PaymentServiceTransaction} instance
	 */
	public PaymentServiceTransaction getPaymentServiceTransaction(
			Object component) {
		return (PaymentServiceTransaction) ui.getAttachedObject(component);
	}

	/**
	 * Gets the {@link PaymentService} instance attached to the supplied
	 * component
	 * 
	 * @param component
	 * @return the attached {@link PaymentService} instance
	 */
	public PaymentService getPaymentService(Object component) {
		return (PaymentService) ui.getAttachedObject(component);
	}

	/**
	 * Gets the {@link NetworkOperator} instance attached to the supplied
	 * component
	 * 
	 * @param component
	 * @return the attached {@link NetworkOperator} instance
	 */
	public NetworkOperator getNetworkOperator(Object component) {
		return (NetworkOperator) ui.getAttachedObject(component);
	}

	/**
	 * Gets the {@link QuickDialCode} instance attached to the supplied
	 * component
	 * 
	 * @param component
	 * @return The attached {@link QuickDialCode} instance
	 */
	public QuickDialCode getQuickDialCode(Object component) {
		return (QuickDialCode) ui.getAttachedObject(component);
	}

	/**
	 * Gets the Thinlet UI tab component for this plugin
	 * 
	 * @return
	 */
	public Object getPaymentViewTab() {
		return paymentViewTab;
	}

	/**
	 * Performs a live search of client data and updates the display in
	 * real-time
	 * 
	 * @param textField
	 */
	public void liveClientSearch(Object textField) {
		LOG.info("Initiating live search");

		// Grab the typed text
		String searchText = ui.getText(textField);

		// Determine the search criteria to use
		if (searchText.trim().length() > 0) {
			liveSearchString = searchText.trim();
			LOG.trace("Live search for [" + searchText + "]...");
		}

		clientsPagingHandler.refresh();
		dispersalsPagingHandler.refresh();
		repaymentsPagingHandler.refresh();

		// Repaint the UI to reflect changes
		ui.repaint(paymentViewTab);
	}

	/**
	 * Event handler that is triggered when a client is selected from the list
	 * of clients on the UI
	 * 
	 * @param component
	 *            reference to the client list UI component
	 */
	public void selectClient(Object component) {
		// Get references to the edit and delete buttons
		Object btnEditClient = new Object();//ui.find(paymentViewTab, COMPONENT_BT_EDIT_CLIENT);
		Object btnDeleteClient = new Object();//ui.find(paymentViewTab, COMPONENT_BT_DELETE_CLIENT);

		// Get "the enabled" property of the buttons
		boolean editEnabled = ui.getBoolean(btnEditClient, Thinlet.ENABLED);
		boolean deleteEnabled = ui.getBoolean(btnDeleteClient, Thinlet.ENABLED);

		// Enable the buttons if disabled
		if (editEnabled == false) {
			ui.setEnabled(btnEditClient, true);
		}

		if (deleteEnabled == false) {
			ui.setEnabled(btnDeleteClient, true);
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
	 * Event handler that is triggered when a client record on the UI client
	 * list is double clicked
	 * 
	 * @param component
	 */
	public void showClientDetails() {
		// Load the client details dialog
		Object dialog = getClientDialog(selectedClient);
		ui.add(dialog);
	}

	/**
	 * Removes a dialog from the screen when the close action is initated
	 * 
	 * @param dialog
	 *            {@link Thinlet} UI dialog to be removed from the display
	 */
	public void removeDialog(Object dialog) {
		ui.remove(dialog);
	}

	/**
	 * Performs input validation for the required client properties
	 */
	/*public void validateRequiredFields() {
		Object dialog = getClientDialog();
		String clientName = ui.getText(
				ui.find(dialog, COMPONENT_FLD_CLIENT_NAME)).trim();
		String phoneNumber = ui.getText(
				ui.find(dialog, COMPONENT_FLD_PHONE_NUMBER)).trim();

		// Check if the client name and phone number have been provided
		if (clientName.length() > 0 && phoneNumber.length() > 0) {
			ui.setEnabled(ui.find(dialog, COMPONENT_BT_SAVE_CLIENT), true);
		} else {
			ui.setEnabled(ui.find(dialog, COMPONENT_BT_SAVE_CLIENT), false);
		}

		ui.repaint();
	}
	*/
	

	/**
	 * Saves the client
	 */
	public void saveClient() {
		Object dialog = getClientDialog();

		// Check if a client object is attached to the client dialog
		boolean clientExists = (getClient(dialog) == null) ? false : true;

		// Create/get client instance as appropriate
		Client client = (clientExists == true) ? getClient(dialog)
				: new Client();

		// Fetch the client info from the dialog
		//client.setName(ui.getText(ui.find(dialog, COMPONENT_FLD_CLIENT_NAME)));
		//client.setPhoneNumber(ui.getText(ui.find(dialog, COMPONENT_FLD_PHONE_NUMBER)));
		//client.setOtherPhoneNumber(ui.getText(ui.find(dialog, COMPONENT_FLD_OTHER_PHONE_NUMBER)));
		//client.setEmailAddress(ui.getText(ui.find(dialog, COMPONENT_FLD_EMAIL_ADDRESS)));

		// Check if a contact record is to be created for this client
		boolean createContact = ui.getBoolean(ui.find(dialog, "chkAddToContact"), "selected");

		
		// Remove the item being updated from the client list
		if (clientExists) {
			Object selectedItem = ui.getSelectedItem(getClientList());
			ui.remove(selectedItem);
		}

		// Update the UI
		Object item = getListItem(client);
		ui.add(getClientList(), item);

		removeDialog(dialog);
		ui.repaint(paymentViewTab);

	}

	/**
	 * Shows the client dialog
	 */
	public void showNewClientForm() {
		ui.add(getClientDialog());
	}

	/** @return a reference to the client dialog */
	private Object getClientDialog() {
		Object dialog = new Object();//ui.find(COMPONENT_DLG_CLIENT_DETAILS);
		return dialog;
	}

	/**
	 * Returns an instance of the client dialog with the client details shown in
	 * the input fields
	 * 
	 * @param client
	 *            {@link Client} whose details shall be shown on the dialog
	 * @return
	 */
	private Object getClientDialog(Client client) {
		Object dialog = getClientDialog();
		ui.setAttachedObject(dialog, client);

		/*ui.setText(ui.find(dialog, COMPONENT_FLD_CLIENT_NAME), client.getName());
		ui.setText(ui.find(dialog, COMPONENT_FLD_PHONE_NUMBER),
				client.getPhoneNumber());
		ui.setText(ui.find(dialog, COMPONENT_FLD_OTHER_PHONE_NUMBER),
				client.getOtherPhoneNumber());
		ui.setText(ui.find(dialog, COMPONENT_FLD_EMAIL_ADDRESS),
				client.getEmailAddress());
		ui.setEnabled(ui.find(dialog, COMPONENT_BT_SAVE_CLIENT), true);
		*/

		if (client.getContact() == null)
			ui.setSelected(ui.find(dialog, "chkAddToContact"), false);

		return dialog;
	}

	/**
	 * Gets the SHA-1 value of an string
	 * 
	 * @param code
	 *            value whose SHA-1 hash is to be computed
	 * @return
	 */
	public String getHashString(String code) {
		if (code == null || code.trim().length() == 0)
			return null;

		String hashString = null;
		byte[] defaultBytes = code.getBytes();

		try {
			MessageDigest digest = MessageDigest.getInstance("SHA");
			digest.reset();
			digest.update(defaultBytes);

			byte[] messageDigest = digest.digest();

			// Temporary buffer to store the human readable hash
			StringBuffer digestBuffer = new StringBuffer();

			// Convert the digest output to hexadecimal
			for (int i = 0; i < messageDigest.length; i++)
				digestBuffer.append(Integer
						.toHexString(0xFF & messageDigest[i]));

			hashString = digestBuffer.toString();
		} catch (NoSuchAlgorithmException ne) {
			LOG.debug(ne);
		}

		return hashString;
	}

	/**
	 * Helper method to check existence of a keyword in a reponse text
	 * 
	 * @param responseText
	 * @param keyword
	 * @return
	 */
	public boolean containsKeyword(String responseText, String keyword) {
		if (responseText.length() == 0 && keyword.length() == 0) {
			return true;
		} else if (responseText.length() == 0 || keyword.length() == 0) {
			return false;
		} else if (responseText.length() > 0 && keyword.length() > 0) {
			Pattern pattern = Pattern.compile(keyword);
			Matcher matcher = pattern.matcher(responseText);
			return matcher.find();
		}
		return false;
	}

	/**
	 * Gets the {@link Thinlet} UI dialog for sending money to client
	 * 
	 * @return
	 */
	public Object getSendMoneyDialog() {
		Object dialog = new Object();//ui.find(COMPONENT_DLG_SEND_MONEY);
		return dialog;
	}

	/**
	 * {@link Thinlet} UI event helper method for displaying the send money
	 * dialog
	 */
	public void showSendMoneyDialog() {
		Object dialog = getSendMoneyDialog();

		ui.setAttachedObject(dialog, selectedClient);
		//ui.setText(ui.find(dialog, COMPONENT_LB_CLIENT_NAME), selectedClient.getName());

		ui.add(dialog);
	}

	/**
	 * Event helper for initiating the send money transaction
	 */
	public void sendMoney() {
		Object dialog = getSendMoneyDialog();

		// Get the selected payment service from the combobox
		//Object comboChoice = ui.getSelectedItem(ui.find(dialog, COMPONENT_CB_PAYMENT_SERVICE));
		//PaymentService service = getPaymentService(comboChoice);

		// Check if an amount has been specified
		//String amount = ui.getText(ui.find(dialog, COMPONENT_FLD_TRANSFER_AMOUNT)).trim();
		/*if (amount.length() == 0) {
			ui.alert(InternationalisationUtils
					.getI18nString(PAYMENTVIEW_NO_TRANSFER_AMOUNT));
			return;
		}
		*/

		// Validate the specified amount
		/*if (!validateAmount(amount)) {
			ui.alert(InternationalisationUtils
					.getI18nString(PAYMENTVIEW_INVALID_TRANSFER_AMOUNT));
			return;
		}*/

		// Get the client attached to the dialog
		Client client = getClient(dialog);

		// Get the text message to be sent to the payment service
		String textMessage = "";//getDispersalTextMessage(service,client.getPhoneNumber(), amount);

		// Null check
		if (textMessage != null) {
			//ui.getFrontlineController().sendTextMessage(service.getSmsShortCode(), textMessage);
			removeDialog(dialog);
		}
	}

	/**
	 * Helper method for constructing the message for triggering dispersal of
	 * funds
	 * 
	 * @param service
	 * @param recipient
	 * @param amount
	 * @return
	 */
	private String getDispersalTextMessage(PaymentService service, String recipient, String amount) {
		// Get the template message
		String template = service.getSendMoneyTextMessage();

		if (template == null) {
			return null;
		}

		// TODO We may need an agnostic way to do this

		// Replace the tags for the template with the actual values
		String message = template.replace(
				PaymentServiceSmsProcessor.TG_PIN_NUMBER,
				service.getPinNumber());
		message = message.replace(PaymentServiceSmsProcessor.TG_PHONENUMBER,
				recipient);
		message = message.replace(PaymentServiceSmsProcessor.TG_AMOUNT, amount);

		return message;
	}

	/**
	 * Helper method for performing number validation
	 * 
	 * @param amount
	 * @return
	 */
	private boolean validateAmount(String amount) {
		double numericalValue;

		try {
			numericalValue = Double.parseDouble(amount);
		} catch (NumberFormatException ne) {
			LOG.debug("Invalid number", ne);
			return false;
		}

		return (numericalValue <= 0) ? false : true;
	}
	
	/*
	 * Additions for the Dialogs and UI methods
	 */
	public Object customizeClientDB() {
		Object dialog = new Object();//ui.find(COMPONENT_DLG_CLIENT_DETAILS);

		return dialog;
	}
	
	public Object addClient() {
		Object dialog = new Object();//ui.find(COMPONENT_DLG_CLIENT_DETAILS);

		return dialog;
	}
	
	public Object importClient() {
		Object dialog = new Object();//ui.find(COMPONENT_DLG_CLIENT_DETAILS);

		return dialog;
	}

}