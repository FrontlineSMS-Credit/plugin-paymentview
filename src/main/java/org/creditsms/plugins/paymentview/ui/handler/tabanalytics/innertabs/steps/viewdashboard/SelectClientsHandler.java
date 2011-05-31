package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class SelectClientsHandler extends BasePanelHandler {
	
	private static final String PNL_CLIENTS_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectclients.xml";

	private ViewDashBoardTabHandler viewDashBoardTabHandler;

	private SelectClientsTableHandler selectClientsTableHandler;

	private Object pnlClientsTableHolder;
	private PaymentViewPluginController pluginController;
	private SelectTargetSavingsHandler previousSelectTargetSavingsHandler;
	private final Map<Client, Target> clients_targets;
	
	SelectClientsHandler(UiGeneratorController ui, PaymentViewPluginController pluginController,
			ViewDashBoardTabHandler viewDashBoardTabHandler, SelectTargetSavingsHandler previousSelectTargetSavingsHandler, Map<Client, Target> clients_targets) {
		super(ui);
		this.pluginController = pluginController;
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.clients_targets = clients_targets;
		this.init();
		refresh();
		this.previousSelectTargetSavingsHandler = previousSelectTargetSavingsHandler;
	}

	private void init() {
		this.loadPanel(XML_STEP_SELECT_CLIENT);
		this.selectClientsTableHandler = new SelectClientsTableHandler(
				(UiGeneratorController) ui, pluginController);
		pnlClientsTableHolder = this.find(PNL_CLIENTS_TABLE_HOLDER);
		// Customize the Table Panel
		ui.setColspan(selectClientsTableHandler.getClientsTablePanel(), 2);
//		ui.setEnabledRecursively(selectClientsTableHandler.getClientsTable(),
//				false);
		selectClientsTableHandler.setClients(new ArrayList<Client>(clients_targets.keySet()));
		this.ui.add(pnlClientsTableHolder,
				selectClientsTableHandler.getClientsTablePanel());
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		viewDashBoardTabHandler.setCurrentStepPanel(new CreateSettingsHandler(
				(UiGeneratorController) ui, viewDashBoardTabHandler,pluginController, this).getPanelComponent());
	}

	public void previous() {
		viewDashBoardTabHandler
				.setCurrentStepPanel(previousSelectTargetSavingsHandler.getPanelComponent());
	}

	public void refresh() {
		this.selectClientsTableHandler.updateClientsList();
	}
	
	List<Client> getSelectedClients(){
		return this.selectClientsTableHandler.getSelectedClients();
	}
	
	public Map<Client, Target> getClients_targets() {
		return clients_targets;
	}
}
