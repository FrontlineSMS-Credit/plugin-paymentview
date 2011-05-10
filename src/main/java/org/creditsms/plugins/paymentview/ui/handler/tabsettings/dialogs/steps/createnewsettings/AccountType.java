package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class AccountType extends BaseDialog {
	private static final String XML_ACCOUNT_TYPE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep2.xml";

	public AccountType(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		init();
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_ACCOUNT_TYPE, this);
	}
	
	
	public void previous() {

	}
	
	public void next() {

	}
}
