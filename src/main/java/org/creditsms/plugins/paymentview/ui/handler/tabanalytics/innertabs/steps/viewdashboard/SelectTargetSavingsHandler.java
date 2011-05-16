package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class SelectTargetSavingsHandler extends BasePanelHandler {
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectservice.xml";
	private ViewDashBoardTabHandler viewDashBoardTabHandler;
	private final PaymentViewPluginController pluginController;

	public SelectTargetSavingsHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController,
			ViewDashBoardTabHandler viewDashBoardTabHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		viewDashBoardTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, pluginController, viewDashBoardTabHandler).getPanelComponent());
	}
}