package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class AuthorizationCode extends BaseDialog {
	private static final String XML_ENTER_AUTHORIZATION_CODE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep4.xml";
	private final EnterPin previousEnterPin;
	
	public AuthorizationCode(UiGeneratorController ui, PaymentViewPluginController pluginController, EnterPin previousEnterPin){
		super(ui);
		this.previousEnterPin = previousEnterPin;
		init();
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_AUTHORIZATION_CODE, this);
	}
	
	public void previous() {
		previousEnterPin.showDialog();
		removeDialog();
	}
	
	public void create() {

	}
}