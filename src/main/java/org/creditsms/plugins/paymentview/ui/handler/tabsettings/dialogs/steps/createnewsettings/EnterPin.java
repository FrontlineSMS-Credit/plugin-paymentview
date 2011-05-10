package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class EnterPin extends BaseDialog {
	private static final String XML_ENTER_PIN = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep3.xml";
	
	public EnterPin(UiGeneratorController ui, PaymentViewPluginController pluginController){
		super(ui);
		init();
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_PIN,
				this);
	}
	
	public void previous() {

	}
	
	public void next() {

	}
}
