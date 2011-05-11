package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class EnterPin extends BaseDialog {
	private static final String XML_ENTER_PIN = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep3.xml";
	private final PaymentViewPluginController pluginController;
	private AccountType previousAccountType;

	public EnterPin(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui);		
		this.pluginController = pluginController;
		init();
	}

	public EnterPin(UiGeneratorController ui, PaymentViewPluginController pluginController,
			AccountType previousAccountType){
		this(ui, pluginController);
		this.previousAccountType = previousAccountType;
	}
	
	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_PIN,
				this);
	}
	
	public void previous() {
		previousAccountType.showDialog();
		removeDialog();
	}
	
	public void next() {
		new AuthorizationCode(ui, pluginController, this).showDialog();
		removeDialog();
	}
}
