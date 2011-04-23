package org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.ui.handler.analytics.dialogs.CreateNewTargetHandler;
import org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.AddClientTabHandler;

public class CreateSettingsHandler extends BasePanelHandler {
	private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/createdashboard/stepcreatesettings.xml";
	private final AddClientTabHandler createDashBoardTabHandler;

	protected CreateSettingsHandler(UiGeneratorController ui, AddClientTabHandler createDashBoardTabHandler) {
		super(ui);
		this.createDashBoardTabHandler = createDashBoardTabHandler;
		this.loadPanel(XML_STEP_CREATE_SETTINGS);
	}

	public void evaluate(Object combo) {
		int index = ui.getSelectedIndex(combo);
		if (index == 2) {
			ui.add(new CreateNewTargetHandler((UiGeneratorController) ui)
					.getDialog());
		}
	}

	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		createDashBoardTabHandler.setCurrentStepPanel(new ReviewHandler(
				(UiGeneratorController) ui, createDashBoardTabHandler).getPanelComponent());
	}

	public void previous() {
		createDashBoardTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, createDashBoardTabHandler).getPanelComponent());
	}

	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}

	public void selectService() {
		createDashBoardTabHandler.setCurrentStepPanel(new SelectTargetSavingsHandler(
				(UiGeneratorController) ui, createDashBoardTabHandler).getPanelComponent());
	}

	public void targetedSavings() {
		selectService();
	}

	public void selectClient() {
		previous();
	}
}
