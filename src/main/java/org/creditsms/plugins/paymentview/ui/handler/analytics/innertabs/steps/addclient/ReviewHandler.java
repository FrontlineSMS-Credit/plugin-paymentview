package org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.AddClientTabHandler;

public class ReviewHandler extends BasePanelHandler {
	private static final String XML_STEP_REVIEW = "/ui/plugins/paymentview/analytics/createdashboard/stepreview.xml";
	private AddClientTabHandler createDashBoardTabHandler;

	protected ReviewHandler(UiGeneratorController ui, AddClientTabHandler createDashBoardTabHandler) {
		super(ui);
		this.loadPanel(XML_STEP_REVIEW);
	}

	public void create() {
	}

	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void previous() {
		createDashBoardTabHandler.setCurrentStepPanel(new CreateSettingsHandler(
				(UiGeneratorController) ui, createDashBoardTabHandler).getPanelComponent());
	}

	public void selectService() {
		createDashBoardTabHandler.setCurrentStepPanel(new SelectTargetSavingsHandler(
				(UiGeneratorController) ui, createDashBoardTabHandler).getPanelComponent());
	}

	public void targetedSavings() {
		selectService();
	}

	public void selectClient() {
		createDashBoardTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, createDashBoardTabHandler).getPanelComponent());
	}

	public void createSettings() {
		previous();
	}

	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}
}
