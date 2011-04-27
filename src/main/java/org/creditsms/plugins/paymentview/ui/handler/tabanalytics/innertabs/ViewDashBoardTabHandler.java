package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.StepSelectTargetSavingsHandler;

public class ViewDashBoardTabHandler extends BaseTabHandler {
	private static final String TAB_VIEW_DASHBOARD = "tab_viewDashBoard";

	public ClientDao clientDao;
	private Object currentPanel;
	private Object viewDashboardTab;

	public ViewDashBoardTabHandler(UiGeneratorController ui,
			Object tabAnalytics, ClientDao clientDao) {
		super(ui);
		this.clientDao = clientDao;
		viewDashboardTab = ui.find(tabAnalytics, TAB_VIEW_DASHBOARD);
		this.init();
	}

	public void deinit() {
	}

	@Override
	public Object initialiseTab() {
		// ui.add(createDashboardTab, stepCreateSettings.getPanelComponent());
		setCurrentStepPanel(new StepSelectTargetSavingsHandler(ui, clientDao, this)
				.getPanelComponent());
		return viewDashboardTab;
	}

	public void refresh() {

	}

	public void setCurrentStepPanel(Object panel) {
		if (currentPanel != null)
			ui.remove(currentPanel);

		ui.add(viewDashboardTab, panel);
		currentPanel = panel;
		ViewDashBoardTabHandler.this.refresh();
	}
}
