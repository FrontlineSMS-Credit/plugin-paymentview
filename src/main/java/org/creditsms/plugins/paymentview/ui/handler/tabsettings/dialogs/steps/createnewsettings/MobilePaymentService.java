package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class MobilePaymentService extends BaseDialog {
	private static final String XML_MOBILE_PAYMENT_SERVICE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep1.xml";

	public MobilePaymentService(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		init();
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_MOBILE_PAYMENT_SERVICE, this);
	}
	
	
	public void previous() {

	}
	
	public void next() {

	}
}
