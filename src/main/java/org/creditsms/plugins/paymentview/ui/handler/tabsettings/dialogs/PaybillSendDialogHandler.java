package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs;

import java.math.BigDecimal;

import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;

public class PaybillSendDialogHandler extends BaseDialog {
	private static final String AMOUNT_TO_TRANSFER = "amountToTransfer";
	private static final String BUSINESS_NO = "businessNo";
	private static final String ACCOUNT_NO = "accountNo";
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/dlgPayBillSend.xml";
	private final MpesaPaymentService paymentService;
	private Object tfAmountToTransfer;
	private Object tfAccountNo;
	private Object tfBusinessNo;

	public PaybillSendDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, MpesaPaymentService paymentService) {
		super(ui);
		this.paymentService = paymentService;
		init();
		refresh();
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CONFIGURE_ACCOUNT, this);
		tfAmountToTransfer = ui.find(dialogComponent, AMOUNT_TO_TRANSFER);
		tfAccountNo = ui.find(dialogComponent, ACCOUNT_NO);
		tfBusinessNo = ui.find(dialogComponent, BUSINESS_NO);
	}
	
	public void sendPayment() {
		new AuthorisationCodeHandler(ui).showAuthorizationCodeDialog("sendPaymentToPaymentService", this);
	}
	
	public void sendPaymentToPaymentService() {
 		String businessNo = ui.getText(tfBusinessNo);
 		String accountNo = ui.getText(tfAccountNo);
 		BigDecimal amountToTransfer = BigDecimal.ZERO;
 		try{
 			amountToTransfer = new BigDecimal(ui.getText(tfAmountToTransfer));
 		}catch(NumberFormatException e){
 			ui.alert("Please enter a valid amount");
 			return;
 		}
		paymentService.sendAmountToPaybillAccount(businessNo, accountNo, amountToTransfer);
		removeDialog();
	}
	
	@Override
	protected void refresh() {
	}
}