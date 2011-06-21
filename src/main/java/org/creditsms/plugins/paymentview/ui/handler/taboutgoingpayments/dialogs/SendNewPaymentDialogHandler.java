package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
//import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;
import org.smslib.CService;

public class SendNewPaymentDialogHandler extends BaseDialog {
	private static final String XML_SEND_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendNewPayment.xml";
	private static final String COMPONENT_TEXT_OP_NAME = "txt_Name";
	private static final String COMPONENT_TEXT_OP_MSISDN = "txt_Phone";
	private static final String COMPONENT_TEXT_OP_AMOUNT = "txt_Amount";
	private static final String COMPONENT_CMB_OP_MOBILE_PAYMENT_SYSTEM = "cmb_MobilePaymentSystem";
	private static final String COMPONENT_TEXT_OP_PAYMENT_ID = "txt_PaymentID";
	private static final String COMPONENT_TEXT_OP_NOTES = "txt_Notes";
	
	
	private Object schedulePaymentAuthDialog;
	
//UI fields
	private Object fieldOpName;
	private Object fieldOpMsisdn;
	private Object fieldOpAmount;
	private Object fieldOpPaymentId;
	private Object fieldOpNotes;
	
//UI HELPER
	private Client client;
	private PaymentViewPluginController pluginController;
	private Object cmbOpMobilePaymentSystem;

	public SendNewPaymentDialogHandler(UiGeneratorController ui,PaymentViewPluginController pluginController, Client client) {
		super(ui);
		this.pluginController = pluginController;
		this.client = client;
		initialise();
		refresh();
	}

	protected void initialise() {
		dialogComponent = ui.loadComponentFromFile(XML_SEND_NEW_PAYMENTS_TAB, this);
		//compPanelFields = ui.find(dialogComponent, "frm_customerDetails");
		
		fieldOpName = ui.find(dialogComponent, COMPONENT_TEXT_OP_NAME);
		fieldOpMsisdn = ui.find(dialogComponent, COMPONENT_TEXT_OP_MSISDN);
		fieldOpAmount = ui.find(dialogComponent, COMPONENT_TEXT_OP_AMOUNT);
		cmbOpMobilePaymentSystem = ui.find(dialogComponent, COMPONENT_CMB_OP_MOBILE_PAYMENT_SYSTEM);
		ui.add(cmbOpMobilePaymentSystem,ui.createComboboxChoice("Safaricom Mpesa", PaymentService.PaymentServiceType.SAFARICOMMPESA) );
		fieldOpPaymentId = ui.find(dialogComponent, COMPONENT_TEXT_OP_PAYMENT_ID);
		fieldOpNotes = ui.find(dialogComponent, COMPONENT_TEXT_OP_NOTES);
	}
	
	protected void refresh(){
		ui.setText(fieldOpName, this.getClientObj().getName());
		ui.setText(fieldOpMsisdn, this.getClientObj().getPhoneNumber());
		ui.setSelectedIndex(cmbOpMobilePaymentSystem, 0);
		ui.setText(cmbOpMobilePaymentSystem, "Safaricom Mpesa");
	}

	public void showScheduleNewPaymentsAuthDialog() {
		//TODO checks amount is A NUMBER
		
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui).getDialog();
		ui.add(schedulePaymentAuthDialog);
	}

	public void showSendNewPaymentsAuthDialog() {
		// get the correct payment service in the paymentServices list
		if(pluginController.getPaymentService() == null) {
			ui.infoMessage("There is no payment service configured.");
		} else {
			OutgoingPayment payment = createOutgoingPaymentFromDialogFields();
			ui.add(new SendPaymentAuthDialogHandler(ui, pluginController, payment, pluginController.getPaymentService()).getDialog());
		}
	}
	
	private OutgoingPayment createOutgoingPaymentFromDialogFields() {
		OutgoingPayment outgoingPayment = new OutgoingPayment();
		outgoingPayment.setPhoneNumber(ui.getText(fieldOpMsisdn));
		outgoingPayment.setAmountPaid(new BigDecimal(ui.getText(fieldOpAmount)));
		outgoingPayment.setTimePaid(Calendar.getInstance().getTime());
		outgoingPayment.setNotes(ui.getText(fieldOpNotes));
		outgoingPayment.setStatus(OutgoingPayment.Status.CREATED);
		outgoingPayment.setPaymentId(ui.getText(fieldOpPaymentId));
		outgoingPayment.setConfirmationCode("");
		return outgoingPayment;
	}

	public Client getClientObj() { 
		return client;
	}
}
