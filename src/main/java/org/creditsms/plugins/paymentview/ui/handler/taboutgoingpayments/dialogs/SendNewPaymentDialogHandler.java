package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
//import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class SendNewPaymentDialogHandler extends BaseDialog {
	private static final String XML_SEND_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendNewPayment.xml";
	private static final String COMPONENT_TEXT_OP_NAME = "txt_Name";
	private static final String COMPONENT_TEXT_OP_MSISDN = "txt_Phone";
	private static final String COMPONENT_TEXT_OP_AMOUNT = "txt_Amount";
	private static final String COMPONENT_TEXT_OP_MOBILE_PAYMENT_SYSTEM = "txt_MobilePaymentSystem";
	private static final String COMPONENT_TEXT_OP_PAYMENT_ID = "txt_PaymentID";
	private static final String COMPONENT_TEXT_OP_NOTES = "txt_Notes";
	
	
	private Object schedulePaymentAuthDialog;
	private Object sendPaymentAuthDialog;
	

//	private AccountDao accountDao;
	private OutgoingPayment outgoingPayment;
	
	//UI fields
	private Object fieldOpName;
	private Object fieldOpMsisdn;
	private Object fieldOpAmount;
	private Object fieldOpMobilePaymentSystem;
	private Object fieldOpPaymentId;
	private Object fieldOpNotes;
	
	//UI data
	private String opMsisdn;
	private String opAmount;
	private String opMobilePaymentSystem;
	private String opPaymentId;
	private String opNotes;
	
	//UI HELPER
	//private Object compPanelFields;
	private Client client;
	private PaymentViewPluginController pluginController;




	public SendNewPaymentDialogHandler(UiGeneratorController ui,PaymentViewPluginController pluginController, Client client) {
		super(ui);
		this.pluginController = pluginController;
//		this.accountDao = pluginController.getAccountDao();
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
		fieldOpMobilePaymentSystem = ui.find(dialogComponent, COMPONENT_TEXT_OP_MOBILE_PAYMENT_SYSTEM);
		fieldOpPaymentId = ui.find(dialogComponent, COMPONENT_TEXT_OP_PAYMENT_ID);
		fieldOpNotes = ui.find(dialogComponent, COMPONENT_TEXT_OP_NOTES);
	}
	
	protected void refresh(){
		ui.setText(fieldOpName, this.getClientObj().getName());
		ui.setText(fieldOpMsisdn, this.getClientObj().getPhoneNumber());
		ui.setText(fieldOpMobilePaymentSystem, "MpesaPersonalService");
	}

	public void showScheduleNewPaymentsAuthDialog() {
		//TODO checks amount is A NUMBER
		
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui).getDialog();
		ui.add(schedulePaymentAuthDialog);
	}

	public void showSendNewPaymentsAuthDialog() {
		try {
			
			if (pluginController.getPaymentServices().size()>0){
				outgoingPayment = new OutgoingPayment();
				outgoingPayment.setPhoneNumber(ui.getText(fieldOpMsisdn));
				outgoingPayment.setAmountPaid(new BigDecimal(ui.getText(fieldOpAmount)));
				outgoingPayment.setTimePaid(Calendar.getInstance().getTime());
				outgoingPayment.setNotes(ui.getText(fieldOpNotes));
				outgoingPayment.setStatus(OutgoingPayment.Status.CREATED);
				outgoingPayment.setPaymentId(ui.getText(fieldOpPaymentId));
				outgoingPayment.setConfirmationCode("");
	
				//TODO the account would have to be filled when specifications are clear!!!!!!!!!!!!!!!1
				//System.out.println("account:"+accountDao.getAccountsByClientId(client.getId()).get(0).getAccountNumber());
	
				sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui, pluginController, outgoingPayment, ui.getText(fieldOpMobilePaymentSystem)).getDialog();
				ui.add(sendPaymentAuthDialog);
			} else {
				ui.infoMessage("Please set up a mobile payment account in the setting tab.");
			}
		} catch (NumberFormatException ex){
			ui.infoMessage("Please enter an amount");
		}
	}
	
	public OutgoingPayment getOutgoingPayment() {
		return outgoingPayment;
	}

	public String getOpMsisdn() {
		return opMsisdn;
	}

	public String getOpAmount() {
		return opAmount;
	}

	public String getOpMobilePaymentSystem() {
		return opMobilePaymentSystem;
	}

	public String getOpPaymentId() {
		return opPaymentId;
	}

	public String getOpNotes() {
		return opNotes;
	}

	public Client getClientObj() { 
		return client;
	}
}
