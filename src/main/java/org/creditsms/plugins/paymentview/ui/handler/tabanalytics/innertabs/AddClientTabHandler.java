package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient.SelectTargetSavingsHandler;

public class AddClientTabHandler extends BaseTabHandler {

	private static final String TAB_CREATE_DASHBOARD = "tab_createDashboard";

	
	private Object createDashboardTab;
	private static Object currentPanel;

	private ClientDao clientDao;


	private final CustomValueDao customValueDao;
	private final CustomFieldDao customFieldDao;

	public AddClientTabHandler(UiGeneratorController ui, Object tabAnalytics, final PaymentViewPluginController pluginController) {
		super(ui);
		this.clientDao = pluginController.getClientDao();
		this.customValueDao = pluginController.getCustomValueDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		createDashboardTab = ui.find(tabAnalytics, TAB_CREATE_DASHBOARD);
		this.init();
	}

	public void deinit() {
	}

	@Override
	protected Object initialiseTab() {
		setCurrentStepPanel(new SelectTargetSavingsHandler(ui, clientDao, customValueDao, customFieldDao, this)
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
