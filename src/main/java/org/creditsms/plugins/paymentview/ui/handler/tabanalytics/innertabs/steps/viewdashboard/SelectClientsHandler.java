package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.SelectClientsTableHandler;

public class SelectClientsHandler extends BasePanelHandler {
	
	private static final String PNL_CLIENTS_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectclients.xml";

	private ViewDashBoardTabHandler viewDashBoardTabHandler;

	private SelectClientsTableHandler selectClientsTableHandler;

	private Object pnlClientsTableHolder;
	private PaymentViewPluginController pluginController;
	private SelectTargetSavingsHandler previousSelectTargetSavingsHandler;
	
	SelectClientsHandler(UiGeneratorController ui, PaymentViewPluginController pluginController,
			ViewDashBoardTabHandler viewDashBoardTabHandler, SelectTargetSavingsHandler previousSelectTargetSavingsHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.loadPanel(XML_STEP_SELECT_CLIENT);
		this.init();
		refresh();
		this.previousSelectTargetSavingsHandler = previousSelectTargetSavingsHandler;
	}

	private void init() {
		this.selectClientsTableHandler = new SelectClientsTableHandler(
				(UiGeneratorController) ui, pluginController);
		pnlClientsTableHolder = this.find(PNL_CLIENTS_TABLE_HOLDER);
		// Customize the Table Panel
		ui.setColspan(selectClientsTableHandler.getClientsTablePanel(), 2);
//		ui.setEnabledRecursively(selectClientsTableHandler.getClientsTable(),
//				false);

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

//	public void enable(Object checkbox) {
//		String name = ui.getName(checkbox);
//		if (name.equals(BY_PRODUCT)) {
//			ui.setEnabled(chkProduct, true);
//			// else
//			ui.setEnabled(chkClientsCharacteristics, false);
//			ui.setEnabledRecursively(
//					selectClientsTableHandler.getClientsTable(), false);
//			selectClientsTableHandler.unSelectAll();
//		} else if (name.equals(BY_CLIENTS_CHARACTERISTICS)) {
//			ui.setEnabled(chkClientsCharacteristics, true);
//			// else
//			ui.setEnabledRecursively(
//					selectClientsTableHandler.getClientsTable(), false);
//			ui.setEnabled(chkProduct, false);
//			selectClientsTableHandler.unSelectAll();
//		} else if (name.equals(BY_CLIENTS)) {
//			ui.setEnabledRecursively(
//					selectClientsTableHandler.getClientsTable(), true);
//			// else
//			ui.setEnabled(chkClientsCharacteristics, false);
//			ui.setEnabled(chkProduct, false);
//		} else {
//			selectClientsTableHandler.unSelectAll();
//			ui.setEnabledRecursively(
//					selectClientsTableHandler.getClientsTable(), false);
//			ui.setEnabled(chkClientsCharacteristics, false);
//			ui.setEnabled(chkProduct, false);			
//		}
//	}
}
