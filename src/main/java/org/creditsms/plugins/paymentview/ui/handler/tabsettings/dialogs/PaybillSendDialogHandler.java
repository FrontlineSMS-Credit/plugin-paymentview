package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class PaybillSendDialogHandler extends BaseDialog {
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/dlgPayBillSend.xml";

	public PaybillSendDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		init();
		refresh();
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CONFIGURE_ACCOUNT, this);
	}
	
	public void sendPayment() {
		new AuthorisationCodeHandler(ui).showAuthorizationCodeDialog("sendPaymentToPaymentService", this);
	}
	
	public void sendPaymentToPaymentService() {
		
	}
	
	@Override
	protected void refresh() {
	}
}