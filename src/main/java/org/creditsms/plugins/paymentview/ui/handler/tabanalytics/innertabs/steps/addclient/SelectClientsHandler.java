package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class SelectClientsHandler extends BasePanelHandler {
	private static final String PNL_CLIENTS_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/addclient/stepselectclients.xml";
	private final AddClientTabHandler addClientTabHandler;
	private SelectClientsTableHandler selectClientsTableHandler;
	private Object pnlClientsTableHolder;
	private final PaymentViewPluginController pluginController;

	public SelectClientsHandler(UiGeneratorController ui,
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
		if (this.getSelectedClients().size()==0){
			ui.infoMessage("Please select a client");
		} else {
			addClientTabHandler.setCurrentStepPanel(new CreateSettingsHandler(
					(UiGeneratorController) ui, this.pluginController, addClientTabHandler, this)
					.getPanelComponent());
		}
	}

	List<Client> getSelectedClients() {
		return selectClientsTableHandler.getSelectedClients();
	}
}