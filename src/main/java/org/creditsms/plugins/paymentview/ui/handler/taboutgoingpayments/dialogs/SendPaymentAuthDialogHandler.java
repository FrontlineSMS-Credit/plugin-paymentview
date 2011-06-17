package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;



import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.TooManyListenersException;

import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.smslib.CService;
import org.smslib.DebugException;
import org.smslib.SMSLibDeviceException;

import serial.NoSuchPortException;
import serial.PortInUseException;
import serial.UnsupportedCommOperationException;

public class SendPaymentAuthDialogHandler implements ThinletUiEventHandler {

	private static final String XML_SEND_PAYMENTAUTH_DIALOG = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendPaymentAuthDialog.xml";
	private Object dialog;
	private UiGeneratorController ui;
	//private OutgoingPayment outgoingPayment;
	private List<OutgoingPayment> outgoingPaymentList;
	private OutgoingPaymentDao outgoingPaymentDao;
	private ClientDao clientDao;
	
	//TODO WARNING this dev is specific to Mpesa!!!
	private PaymentService paymentService;

	public SendPaymentAuthDialogHandler(final UiGeneratorController ui, PaymentViewPluginController pluginController, List<OutgoingPayment> outgoingPaymentList, String opMobilePaymentSystem) {
		this.ui = ui;
		this.outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		this.clientDao = pluginController.getClientDao();
		this.outgoingPaymentList = outgoingPaymentList;
		paymentService = pluginController.getPaymentServices().get(0); //TODO this is non sense - pick up the right paymentService from the list initialised in enterPin.java
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
		// for the import - maybe the outgoingPaymentList would already have been saved in outgoingPayment table
		try {
			if (!outgoingPaymentList.isEmpty()){
				for (OutgoingPayment outgoingPayment : outgoingPaymentList) {
					System.out.println("msisdn:" + outgoingPayment.getPhoneNumber());
					System.out.println("amount:" + outgoingPayment.getAmountPaid());
					System.out.println("confirmationCode:" + outgoingPayment.getConfirmationCode());
					System.out.println("notes:" + outgoingPayment.getNotes());
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
				}
			}

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

