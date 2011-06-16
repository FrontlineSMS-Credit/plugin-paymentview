package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.StkMenu;

import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class SendPaymentAuthDialogHandler implements ThinletUiEventHandler {

	private static final String XML_SEND_PAYMENTAUTH_DIALOG = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendPaymentAuthDialog.xml";
	private Object dialog;
	private UiGeneratorController ui;
	private OutgoingPayment outgoingPayment;
	private OutgoingPaymentDao outgoingPaymentDao;

	public SendPaymentAuthDialogHandler(final UiGeneratorController ui, PaymentViewPluginController pluginController, OutgoingPayment outgoingPayment) {
		this.ui = ui;
		this.outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		this.outgoingPayment = outgoingPayment;
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
		
		// create outgoing payment
		try {

			System.out.println("msisdn:" + outgoingPayment.getPhoneNumber());
			System.out.println("amount:" + outgoingPayment.getAmountPaid());
			System.out.println("confirmationCode:" + outgoingPayment.getConfirmationCode());
			System.out.println("amount:" + outgoingPayment.getNotes());
			//save inDB
			outgoingPaymentDao.saveOutgoingPayment(outgoingPayment);
			//send payment
			
			//update DB
			

			
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
		
		// save outgoing payment into DB
		// stkRequest to send payment
//		try {
//			StkMenu mPesaMenu = getMpesaMenu();
//			
//		} catch (SMSLibDeviceException ex) {
//			throw new PaymentServiceException(ex);
//		}

	}
}

