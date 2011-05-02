package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.SelectTargetSavingsHandler;

public class ViewDashBoardTabHandler extends BaseTabHandler {
	private static final String TAB_VIEW_DASHBOARD = "tab_viewDashBoard";

	public ClientDao clientDao;
	private Object currentPanel;
	private Object viewDashboardTab;

	private PaymentViewThinletTabController paymentViewThinletTabController;

	private CustomValueDao customDataDao;

	public ViewDashBoardTabHandler(UiGeneratorController ui,
			Object tabAnalytics,
			PaymentViewThinletTabController paymentViewThinletTabController) {
		super(ui);
		this.paymentViewThinletTabController = paymentViewThinletTabController;
		this.clientDao = paymentViewThinletTabController.getClientDao();
		this.customDataDao = paymentViewThinletTabController
				.getCustomValueDao();
		viewDashboardTab = ui.find(tabAnalytics, TAB_VIEW_DASHBOARD);
		this.init();
	}

	public void deinit() {
	}

	@Override
	public Object initialiseTab() {
		// ui.add(createDashboardTab, stepCreateSettings.getPanelComponent());
		setCurrentStepPanel(new SelectTargetSavingsHandler(ui, clientDao,
				paymentViewThinletTabController.getCustomFieldDao(),
				customDataDao, this).getPanelComponent());
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
