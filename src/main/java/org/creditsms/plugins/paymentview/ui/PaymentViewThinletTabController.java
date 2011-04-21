/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;

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

	// Form
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
	private static final Logger LOG = FrontlineUtils
			.getLogger(PaymentViewThinletTabController.class);

	// > UI COMPONENTS
	/** Thinlet tab component whose functionality is handled by this class */
	private Object paymentViewTab;

	private PaymentViewPluginController controller;

	// > PROPERTIES

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
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}
}