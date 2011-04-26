package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

public class StepSelectTargetSavingsHandler extends BasePanelHandler {
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectservice.xml";
	private ViewDashBoardTabHandler viewDashBoardTabHandler;
	private ClientDao clientDao;

	public StepSelectTargetSavingsHandler(UiGeneratorController ui,
			ClientDao clientDao, ViewDashBoardTabHandler viewDashBoardTabHandler) {
		super(ui);
		this.clientDao = clientDao;
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
	}

	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		viewDashBoardTabHandler.setCurrentStepPanel(new StepSelectClientsHandler(
				(UiGeneratorController) ui, clientDao, viewDashBoardTabHandler).getPanelComponent());
	}
}