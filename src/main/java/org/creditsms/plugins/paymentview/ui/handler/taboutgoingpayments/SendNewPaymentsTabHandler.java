package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SendPaymentAuthDialogHandler;

public class SendNewPaymentsTabHandler extends BaseTabHandler {
	private static final String XML_SEND_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sendnewpayments.xml";
	private static final String COMPONENT_TEXT_OP_NAME = "lbl_Name";
	private static final String COMPONENT_TEXT_OP_MSISDN = "lbl_Phone";
	private static final String COMPONENT_TEXT_OP_VERIFY_MSISDN = "lbl_Verify_Phone";
	private static final String COMPONENT_TEXT_OP_AMOUNT = "lbl_Amount";
	private static final String COMPONENT_TEXT_OP_MOBILE_PAYMENT_SYSTEM = "lbl_MobilePaymentSystem";
	private static final String COMPONENT_TEXT_OP_PAYMENT_ID = "lbl_PaymentID";
	private static final String COMPONENT_TEXT_OP_NOTES = "lbl_Notes";
	
	
	private Object schedulePaymentAuthDialog;
	private Object sendNewPaymentsTab;
	private Object sendPaymentAuthDialog;
	
	private OutgoingPaymentDao outgoingPaymentDao;
	private OutgoingPayment outgoingPayment;
	
	//UI fields
	private Object fieldOpName;
	private Object fieldOpMsisdn;
	private Object fieldOpVerifyMsisdn;
	private Object fieldOpAmount;
	private Object fieldOpMobilePaymentSystem;
	private Object fieldOpPaymentId;
	private Object fieldOpNotes;
	
	//UI HELPER
	private Object compPanelFields;
	

	public SendNewPaymentsTabHandler(UiGeneratorController ui,PaymentViewPluginController pluginController) {
		super(ui);
		this.outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		init();
	}

	@Override
	protected Object initialiseTab() {
		sendNewPaymentsTab = ui.loadComponentFromFile(XML_SEND_NEW_PAYMENTS_TAB, this);
		compPanelFields = ui.find(sendNewPaymentsTab, "frm_customerDetails");
		
		fieldOpName = ui.find(sendNewPaymentsTab, COMPONENT_TEXT_OP_NAME);
		fieldOpMsisdn = ui.find(sendNewPaymentsTab, COMPONENT_TEXT_OP_MSISDN);
		fieldOpVerifyMsisdn = ui.find(sendNewPaymentsTab, COMPONENT_TEXT_OP_VERIFY_MSISDN);
		fieldOpAmount = ui.find(sendNewPaymentsTab, COMPONENT_TEXT_OP_AMOUNT);
		fieldOpMobilePaymentSystem = ui.find(sendNewPaymentsTab, COMPONENT_TEXT_OP_MOBILE_PAYMENT_SYSTEM);
		fieldOpPaymentId = ui.find(sendNewPaymentsTab, COMPONENT_TEXT_OP_PAYMENT_ID);
		fieldOpNotes = ui.find(sendNewPaymentsTab, COMPONENT_TEXT_OP_NOTES);
		return sendNewPaymentsTab;
	}

	@Override
	public void refresh() {
	}

	public void showScheduleNewPaymentsAuthDialog() {
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(schedulePaymentAuthDialog);
	}

	public void showSendNewPaymentsAuthDialog() {
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(sendPaymentAuthDialog);
	}
}
