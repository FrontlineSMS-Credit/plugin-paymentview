package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;

import java.math.BigDecimal;
import java.util.Calendar;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.plugins.payment.service.PaymentServiceException;
import net.frontlinesms.ui.UiGeneratorController;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.LogMessageDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;

public class SendNewPaymentDialogHandler extends BaseDialog {
	private static final String XML_SEND_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendNewPayment.xml";
	private static final String COMPONENT_TEXT_OP_NAME = "txt_Name";
	private static final String COMPONENT_TEXT_OP_MSISDN = "txt_Phone";
	private static final String COMPONENT_TEXT_OP_AMOUNT = "txt_Amount";
	private static final String COMPONENT_CMB_OP_MOBILE_PAYMENT_SYSTEM = "cmb_MobilePaymentSystem";
	private static final String COMPONENT_TEXT_OP_PAYMENT_ID = "txt_PaymentID";
	private static final String COMPONENT_TEXT_OP_NOTES = "txt_Notes";
	
	private final Logger log = FrontlineUtils.getLogger(getClass());

	private Object schedulePaymentAuthDialog;
	private OutgoingPayment outgoingPayment;

	//UI fields
	private Object fieldOpName;
	private Object fieldOpMsisdn;
	private Object fieldOpAmount;
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
	private Object cmbOpMobilePaymentSystem;
	private OutgoingPaymentDao outgoingPaymentDao;
	private PaymentService paymentService;
	private LogMessageDao logMessageDao;

	public SendNewPaymentDialogHandler(UiGeneratorController ui,PaymentViewPluginController pluginController, Client client) {
		super(ui);
		this.pluginController = pluginController;
		outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		logMessageDao = pluginController.getLogMessageDao();
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
		fieldOpPaymentId = ui.find(dialogComponent, COMPONENT_TEXT_OP_PAYMENT_ID);
		fieldOpNotes = ui.find(dialogComponent, COMPONENT_TEXT_OP_NOTES);
		
		setupPaymentServices();
	}

	private void setupPaymentServices() {
		for (PaymentService pService : pluginController.getActiveServices()){
			if (pService.isOutgoingPaymentEnabled()) {
				String serviceDescription = pService.toString() + " : " 
						+ pService.getSettings().getId();
				ui.add(cmbOpMobilePaymentSystem, ui.createComboboxChoice(serviceDescription, pService));
			}
		}
	}
	

	@Override
	protected void refresh(){
		ui.setText(fieldOpName, this.getClientObj().getFullName());
		ui.setText(fieldOpMsisdn, this.getClientObj().getPhoneNumber());
		ui.setSelectedIndex(cmbOpMobilePaymentSystem, -1);
	}

	public void showScheduleNewPaymentsAuthDialog() {
		//TODO checks amount is A NUMBER
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui).getDialog();
		ui.add(schedulePaymentAuthDialog);
	}

	public void showSendNewPaymentsAuthDialog() {
		// get the correct payment service in the paymentServices list
		Object selectedPaymentServiceItem = ui.getSelectedItem(cmbOpMobilePaymentSystem);
		if (selectedPaymentServiceItem != null){
			paymentService = ui.getAttachedObject(selectedPaymentServiceItem, PaymentService.class);
			try {				
				outgoingPayment = new OutgoingPayment();
				outgoingPayment.setClient(client);
				outgoingPayment.setAmountPaid(new BigDecimal(ui.getText(fieldOpAmount)));
				outgoingPayment.setTimePaid(Calendar.getInstance().getTime());
				outgoingPayment.setNotes(ui.getText(fieldOpNotes));
				outgoingPayment.setStatus(OutgoingPayment.Status.CREATED);
				outgoingPayment.setPaymentId(ui.getText(fieldOpPaymentId));
				outgoingPayment.setConfirmationCode("");
				outgoingPayment.setPaymentServiceSettings(paymentService.getSettings());
				outgoingPayment.setPayBillPayment(false);

				new AuthorisationCodeHandler(ui).showAuthorizationCodeDialog(this, "sendPayment");

				ui.remove(dialogComponent);
			} catch (NumberFormatException ex){
				ui.infoMessage("Please enter an amount");
			}
		} else {
			ui.infoMessage("Please set up a mobile payment account in the setting tab.");
		}
	}

	public void sendPayment() throws PaymentServiceException {
		try {
			outgoingPaymentDao.saveOutgoingPayment(outgoingPayment);
			try {
				paymentService.makePayment(outgoingPayment);
			} catch(Exception ex) {
				logMessageDao.saveLogMessage(
						new LogMessage(LogMessage.LogLevel.ERROR,"Outgoing Payment: Payment failed.",outgoingPayment.toStringForLogs()));
				log.warn("Payment failed.", ex);
				outgoingPayment.setStatus(OutgoingPayment.Status.ERROR);
			}
			// always update the payment - whether there was an exception
			// or not, there should still be a change in status 
			outgoingPaymentDao.updateOutgoingPayment(outgoingPayment);
			
			if(outgoingPayment.getStatus() == OutgoingPayment.Status.ERROR) {
				ui.infoMessage("Error Occured");
			} else {
				//ui.infoMessage("The outgoing payment has been created and successfully sent");
			}
		} catch (Exception ex) {
			logMessageDao.saveLogMessage(
					new LogMessage(LogMessage.LogLevel.ERROR,"Outgoing Payment: Payment failed.",outgoingPayment.toStringForLogs()));
			throw new RuntimeException(ex);
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