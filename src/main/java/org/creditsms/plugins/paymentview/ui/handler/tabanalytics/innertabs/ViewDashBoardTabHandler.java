package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.SelectTargetSavingsHandler;

public class ViewDashBoardTabHandler extends BaseTabHandler {
	private static final String TAB_VIEW_DASHBOARD = "tab_viewDashBoard";

	public ClientDao clientDao;
	private Object currentPanel;
	private Object viewDashboardTab;

	private final CustomValueDao customDataDao;
	private final CustomFieldDao customFieldDao;

	public ViewDashBoardTabHandler(UiGeneratorController ui,
			Object tabAnalytics, PaymentViewPluginController pluginController) {
		super(ui);
		this.clientDao = pluginController.getClientDao();
		this.customDataDao = pluginController.getCustomValueDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		viewDashboardTab = ui.find(tabAnalytics, TAB_VIEW_DASHBOARD);
		this.init();
	}

	public void deinit() {
	}

	@Override
	public Object initialiseTab() {
		// ui.add(createDashboardTab, stepCreateSettings.getPanelComponent());
		setCurrentStepPanel(new SelectTargetSavingsHandler(ui, clientDao,
				customFieldDao, customDataDao, this).getPanelComponent());
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
		ViewDashBoardTabHandler.this.refresh();
	}
}
