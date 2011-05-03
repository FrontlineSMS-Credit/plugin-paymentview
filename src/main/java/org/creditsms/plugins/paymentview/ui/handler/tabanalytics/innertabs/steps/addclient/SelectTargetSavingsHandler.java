package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class SelectTargetSavingsHandler extends BasePanelHandler {
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/addclient/stepselecttargetsavings.xml";
	private AddClientTabHandler addClientTabHandler;
	private ClientDao clientDao;

	public SelectTargetSavingsHandler(UiGeneratorController ui, ClientDao clientDao, final AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.clientDao = clientDao;
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
	}

	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, clientDao, addClientTabHandler).getPanelComponent());
	}

	public void selectService() {
		// Do Nothing!
	}
}