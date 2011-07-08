package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;

import java.math.BigDecimal;
import java.util.Calendar;

import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class SendNewPaymentDialogHandler extends BaseDialog {
	private static final String XML_SEND_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendNewPayment.xml";
	private static final String COMPONENT_TEXT_OP_NAME = "txt_Name";
	private static final String COMPONENT_TEXT_OP_MSISDN = "txt_Phone";
	private static final String COMPONENT_TEXT_OP_AMOUNT = "txt_Amount";
	private static final String COMPONENT_CMB_OP_MOBILE_PAYMENT_SYSTEM = "cmb_MobilePaymentSystem";
	private static final String COMPONENT_TEXT_OP_PAYMENT_ID = "txt_PaymentID";
	private static final String COMPONENT_TEXT_OP_NOTES = "txt_Notes";

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
	private MpesaPaymentService paymentService;

	public SendNewPaymentDialogHandler(UiGeneratorController ui,PaymentViewPluginController pluginController, Client client) {
		super(ui);
		this.pluginController = pluginController;
		outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
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
		ui.setText(fieldOpName, this.getClientObj().getFullName());
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
		if (!pluginController.getPaymentServices().isEmpty()){
			int itemPaymentServices=0;
			boolean flag = false;
			opMobilePaymentSystem = ((PaymentService.PaymentServiceType) ui.getAttachedObject(ui.getSelectedItem(cmbOpMobilePaymentSystem))).getType();
			System.out.println("payment service combobox:" + ((PaymentService.PaymentServiceType) ui.getAttachedObject(ui.getSelectedItem(cmbOpMobilePaymentSystem))).getType());
			//to pick up the right paymentService from the list initialised in enterPin.java
			while(!flag && itemPaymentServices<pluginController.getPaymentServices().size()){
				flag = pluginController.getPaymentServices().get(itemPaymentServices).getClass().toString().contains(opMobilePaymentSystem);
				if (flag){
					paymentService = pluginController.getPaymentServices().get(itemPaymentServices); 
				}
				itemPaymentServices++;
			}
			// Error message if the selected payment service has not been set
			if (!flag){
				ui.infoMessage("The payment service " + opMobilePaymentSystem + " has not been configured in the setting tab.");
			} else {
				try {					
					outgoingPayment = new OutgoingPayment();
					outgoingPayment.setClient(client);
					outgoingPayment.setAmountPaid(new BigDecimal(ui.getText(fieldOpAmount)));
					outgoingPayment.setTimePaid(Calendar.getInstance().getTime());
					outgoingPayment.setNotes(ui.getText(fieldOpNotes));
					outgoingPayment.setStatus(OutgoingPayment.Status.CREATED);
					outgoingPayment.setPaymentId(ui.getText(fieldOpPaymentId));
					outgoingPayment.setConfirmationCode("");

					//TODO the account would have to be filled when specifications are clear!!!!!!!!!!!!!!!1
					//System.out.println("account:"+accountDao.getAccountsByClientId(client.getId()).get(0).getAccountNumber());

					new AuthorisationCodeHandler(ui, pluginController).showAuthorizationCodeDialog("sendPayment", this);

					ui.remove(dialogComponent);
				} catch (NumberFormatException ex){
					ui.infoMessage("Please enter an amount");
				}
			}
		} else {
			ui.infoMessage("Please set up a mobile payment account in the setting tab.");
		}
	}



	public void sendPayment() throws PaymentServiceException {
		//TODO check MSISDN, amount available?
		try {
				outgoingPaymentDao.saveOutgoingPayment(outgoingPayment);
				paymentService.makePayment(client, outgoingPayment.getAmountPaid());
				outgoingPayment.setStatus(OutgoingPayment.Status.UNCONFIRMED);
				outgoingPaymentDao.updateOutgoingPayment(outgoingPayment);
		} catch (IllegalArgumentException ex) {
			throw new RuntimeException(ex);
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
		ui.infoMessage("The outgoing payment has been created and successfully sent");
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