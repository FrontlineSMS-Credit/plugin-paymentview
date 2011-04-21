package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs;

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

	public void sendPayment() {

	}
}
