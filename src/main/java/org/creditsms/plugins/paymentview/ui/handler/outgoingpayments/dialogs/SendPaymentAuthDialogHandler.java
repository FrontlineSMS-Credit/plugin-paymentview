package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class SendPaymentAuthDialogHandler implements ThinletUiEventHandler {
	
	private static final String XML_SEND_PAYMENTAUTH_DIALOG = "/ui/plugins/paymentview/outgoingpayments/dialogs/sendPaymentAuthDialog.xml"; 
	private UiGeneratorController ui;
	private Object dialog;
	
	public SendPaymentAuthDialogHandler(final UiGeneratorController ui) {
		this.ui = ui;
		init();
	}

	private void init() { 
		dialog = ui.loadComponentFromFile(XML_SEND_PAYMENTAUTH_DIALOG, this);
	}

	public void refresh() {		
	}

	public Object getDialog() { 
		return dialog;
	}
	
	public void removeDialog(Object dialog){
		ui.removeDialog(dialog);
	}
	
	public void sendPayment(){
		
	}
}
