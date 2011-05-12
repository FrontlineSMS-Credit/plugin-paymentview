package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.SelectClientsTableHandler;

public class SelectClientsHandler extends BasePanelHandler {
	private static final String PNL_CLIENTS_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/addclient/stepselectclients.xml";
	private final AddClientTabHandler addClientTabHandler;
	private SelectClientsTableHandler selectClientsTableHandler;
	private Object pnlClientsTableHolder;
	private final PaymentViewPluginController pluginController;
	private SelectTargetSavingsHandler previousSelectTargetSavingsHandler;

	protected SelectClientsHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController, AddClientTabHandler addClientTabHandler, 
			SelectTargetSavingsHandler selectTargetSavingsHandler) {
		this(ui, pluginController, addClientTabHandler);
		previousSelectTargetSavingsHandler = selectTargetSavingsHandler;
	}
	
	protected SelectClientsHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController, AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_SELECT_CLIENT);
		initialise();
		refresh();
	}

	private void initialise() {
		this.selectClientsTableHandler = new SelectClientsTableHandler(
				(UiGeneratorController) ui, pluginController);
		pnlClientsTableHolder = this.find(PNL_CLIENTS_TABLE_HOLDER);

		this.ui.add(pnlClientsTableHolder,
				selectClientsTableHandler.getClientsTablePanel());
	}

	private void refresh() {
		this.selectClientsTableHandler.updateClientsList();
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler.setCurrentStepPanel(new CreateSettingsHandler(
				(UiGeneratorController) ui, this.pluginController, addClientTabHandler, this)
				.getPanelComponent());
	}

	public void previous() {
		addClientTabHandler.setCurrentStepPanel(previousSelectTargetSavingsHandler.getPanelComponent());
	}

	public void selectService() {
		previous();
	}

	public void targetedSavings() {
		previous();
	}
}
