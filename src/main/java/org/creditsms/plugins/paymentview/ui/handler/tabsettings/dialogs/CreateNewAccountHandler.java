package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class CreateNewAccountHandler extends BaseDialog {
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep1.xml";

	private UiGeneratorController ui;

	public CreateNewAccountHandler(UiGeneratorController ui) {
		super(ui);
		init();
		refresh();
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CONFIGURE_ACCOUNT, this);
	}
	
	protected void refresh() { }
}