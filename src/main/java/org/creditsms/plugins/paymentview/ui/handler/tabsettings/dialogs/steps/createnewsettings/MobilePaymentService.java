package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class MobilePaymentService extends BaseDialog {
	private static final String XML_MOBILE_PAYMENT_SERVICE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep1.xml";
	private final PaymentViewPluginController pluginController;

	public MobilePaymentService(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		init();
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_MOBILE_PAYMENT_SERVICE, this);
	}
	
	
	public void next() {
		new AccountType(ui, pluginController, this).showDialog();
		removeDialog();
	}
}
