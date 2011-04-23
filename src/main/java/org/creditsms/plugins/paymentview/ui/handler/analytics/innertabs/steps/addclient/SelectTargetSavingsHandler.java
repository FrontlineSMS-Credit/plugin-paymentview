package org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.steps.addclient;

import org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.AddClientTabHandler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

public class SelectTargetSavingsHandler extends BasePanelHandler {
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/createdashboard/stepselecttargetsavings.xml";
	private AddClientTabHandler addClientTabHandler;

	public SelectTargetSavingsHandler(UiGeneratorController ui, final AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
	}

	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, addClientTabHandler).getPanelComponent());
	}

	public void selectService() {
		// Do Nothing!
	}
}