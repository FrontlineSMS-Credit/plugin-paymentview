package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.StkMenu;

import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class SendPaymentAuthDialogHandler implements ThinletUiEventHandler {

	private static final String XML_SEND_PAYMENTAUTH_DIALOG = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendPaymentAuthDialog.xml";
	private Object dialog;
	private UiGeneratorController ui;

	public SendPaymentAuthDialogHandler(final UiGeneratorController ui) {
		this.ui = ui;
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
		// check MSISDN, amount available?
		
		// create outgoing payment
//		try {
//			final OutgoingPayment payment = new OutgoingPayment();
//			payment.setAccount(getAccount(message));
//			payment.setPhoneNumber(getPhoneNumber(message));
//			payment.setAmountPaid(getAmount(message));
//			payment.setConfirmationCode(getConfirmationCode(message));
//			payment.setPaymentTo(getPaymentTo(message));
//			payment.setTimePaid(getTimePaid(message));
//			payment.setStatus(OutgoingPayment.Status.CONFIRMED);
//				
//			outgoingPaymentDao.saveOutgoingPayment(payment);
//		} catch (IllegalArgumentException ex) {
//			log.warn("Message failed to parse; likely incorrect format", ex);
//			throw new RuntimeException(ex);
//		} catch (Exception ex) {
//			log.error("Unexpected exception parsing outgoing payment SMS.", ex);
//			throw new RuntimeException(ex);
//		}
		
		
		
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

