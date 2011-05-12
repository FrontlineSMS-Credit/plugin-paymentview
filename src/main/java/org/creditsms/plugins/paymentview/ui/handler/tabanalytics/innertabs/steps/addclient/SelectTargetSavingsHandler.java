package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class SelectTargetSavingsHandler extends BasePanelHandler {
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/addclient/stepselecttargetsavings.xml";
	private AddClientTabHandler addClientTabHandler;
	private PaymentViewPluginController pluginController;

	public SelectTargetSavingsHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController,
			final AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, pluginController, addClientTabHandler, this).getPanelComponent());
	}

	public void selectService() {
		// Do Nothing!
	}
}