package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class ConfigureAccountHandler extends BaseDialog {
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/dlgConfigureAccount.xml";

	public ConfigureAccountHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		init();
		refresh();
	}

	public void deleteAccount() {
		// TODO Auto-generated method stub
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CONFIGURE_ACCOUNT, this);
	}

	protected void refresh() {
	}

	public void updateAccPIN() {
		// TODO Auto-generated method stub
	}

	public void updateAuthSettings() {
		// TODO Auto-generated method stub
	}
}