package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class AccountType extends BaseDialog {
	private static final String XML_ACCOUNT_TYPE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep2.xml";
	private final PaymentViewPluginController pluginController;
	private MobilePaymentService previousMobilePaymentService;

	public AccountType(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		init();
	}
	
	public AccountType(UiGeneratorController ui, PaymentViewPluginController pluginController, MobilePaymentService previousMobilePaymentService) {
		this(ui, pluginController);
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_ACCOUNT_TYPE, this);
	}
		
	public void previous() {
		previousMobilePaymentService.showDialog();
		removeDialog();
	}
	
	public void next() {
		new EnterPin(ui, pluginController, this).showDialog();
		removeDialog();
	}
}
