package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient.SelectTargetSavingsHandler;

public class AddClientTabHandler extends BaseTabHandler {
	private static final String TAB_CREATE_DASHBOARD = "tab_createDashboard";
	
	private Object createDashboardTab;
	private Object currentPanel;

	private PaymentViewPluginController pluginController;

	public AddClientTabHandler(UiGeneratorController ui, Object tabAnalytics, final PaymentViewPluginController pluginController) {
		super(ui, false);
		this.pluginController = pluginController;
		createDashboardTab = ui.find(tabAnalytics, TAB_CREATE_DASHBOARD);
		this.init();
	}

	public void deinit() {
	}

	@Override
	protected Object initialiseTab() {
		setCurrentStepPanel(new SelectTargetSavingsHandler(ui, pluginController, this)
				.getPanelComponent());
		return createDashboardTab;
	}

	@Override
	public void refresh() {
	}

	public void setCurrentStepPanel(Object panel) {
		if (currentPanel != null)
			ui.remove(currentPanel);

		ui.add(createDashboardTab, panel);
		currentPanel = panel;
		this.refresh();
	}
}
