package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs;

import java.math.BigDecimal;

import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class PaybillSendDialogHandler extends BaseDialog {
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/dlgPayBillSend.xml";
	private final MpesaPaymentService paymentService;

	public PaybillSendDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, MpesaPaymentService paymentService) {
		super(ui);
		this.paymentService = paymentService;
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
 		String payBillNo = "";
 		String accountNo = "";
 		BigDecimal amountToTransfer = new BigDecimal("");
		
		paymentService.sendAmountToPaybillAccount(null);
	}
	
	@Override
	protected void refresh() {
	}
}