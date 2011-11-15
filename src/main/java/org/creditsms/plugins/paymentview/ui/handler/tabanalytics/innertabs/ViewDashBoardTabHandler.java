package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.CreateSettingsHandler;

public class ViewDashBoardTabHandler extends BaseTabHandler {
	private static final String TAB_VIEW_DASHBOARD = "tab_viewDashBoard";

	private Object currentPanel;
	private Object viewDashboardTab;

	private PaymentViewPluginController pluginController;

	public ViewDashBoardTabHandler(UiGeneratorController ui,
			Object tabAnalytics, PaymentViewPluginController pluginController) {
		super(ui, false);
		this.pluginController = pluginController;
		viewDashboardTab = ui.find(tabAnalytics, TAB_VIEW_DASHBOARD);
		this.init();
	}

	public void deinit() {
	}

	@Override
	public Object initialiseTab() {
		setCurrentStepPanel(new CreateSettingsHandler(ui, this, pluginController).getPanelComponent());
		return viewDashboardTab;
	}

	@Override
	public void refresh() {

	}

	public void setCurrentStepPanel(Object panel) {
		if (currentPanel != null)
			ui.remove(currentPanel);

		ui.add(viewDashboardTab, panel);
		currentPanel = panel;
		this.refresh();
	}
}
