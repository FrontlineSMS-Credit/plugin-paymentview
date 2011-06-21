package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;

import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

public class SendPaymentAuthDialogHandler implements ThinletUiEventHandler {

	private static final String XML_SEND_PAYMENTAUTH_DIALOG = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendPaymentAuthDialog.xml";
	private Object dialog;
	private UiGeneratorController ui;
	//private OutgoingPayment outgoingPayment;
	private OutgoingPayment outgoingPayment;
	private OutgoingPaymentDao outgoingPaymentDao;
	private ClientDao clientDao;
	
	//TODO WARNING this dev is specific to Mpesa!!!
	private PaymentService paymentService;

	public SendPaymentAuthDialogHandler(final UiGeneratorController ui, PaymentViewPluginController pluginController,
			OutgoingPayment outgoingPayment, PaymentService paymentService) {
		this.ui = ui;
		this.outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		this.clientDao = pluginController.getClientDao();
		this.outgoingPayment = outgoingPayment;
		this.paymentService = paymentService;		
		init();

	}

	public Object getDialog() {
		return dialog;
	}

	private void init() {
		dialog = ui.loadComponentFromFile(XML_SEND_PAYMENTAUTH_DIALOG, this);
	}

	public void refresh() {
	}

	public void removeDialog(Object dialog) {
		ui.removeDialog(dialog);
	}

	public void sendPayment () throws PaymentServiceException {
		//TODO check authorisation code
		//TODO check MSISDN, amount available?
		
		// save the outgoing payment list
		try {
			//save the outgoing payment in DB
			outgoingPaymentDao.saveOutgoingPayment(outgoingPayment);

			//send payment
			Client client = clientDao.getClientByPhoneNumber(outgoingPayment.getPhoneNumber());
			// TODO ERROR: the paymentService list is initialised in enterPin.java - 
			// BUT the CService has an atHandler as CATHandler and not as CATHandler_Wavecom_stk
			//paymentService.makePayment(client, outgoingPayment.getAmountPaid());
			
			//update DB
			outgoingPayment.setStatus(OutgoingPayment.Status.UNCONFIRMED);
			outgoingPaymentDao.updateOutgoingPayment(outgoingPayment);
			
			// TODO this stuff probably shouldn't be happening directly on the UI Event Thread
			paymentService.makePayment(client, outgoingPayment.getAmountPaid());
		} catch (IllegalArgumentException ex) {
		//	log.warn("Message failed to parse; likely incorrect format", ex);
			throw new RuntimeException(ex);
		} catch (Exception ex) {
		//	log.error("Unexpected exception parsing outgoing payment SMS.", ex);
			throw new RuntimeException(ex);
		}
		
		System.out.println("fin");
		
		ui.removeDialog(dialog);
		ui.infoMessage("The outgoing payment has been created and sent");
	}
}

