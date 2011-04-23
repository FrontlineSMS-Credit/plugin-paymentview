package org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs.steps.addclient.SelectTargetSavingsHandler;

public class AddClientTabHandler extends BaseTabHandler {

	private static final String TAB_CREATE_DASHBOARD = "tab_createDashboard";

	
	private Object createDashboardTab;
	private static Object currentPanel;

	private ClientDao clientDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private IncomingPaymentDao incomingPaymentDao;
	private AccountDao accountDao;
	private TargetDao targetDao;

	public AddClientTabHandler(UiGeneratorController ui,
			Object tabAnalytics, ClientDao clientDao, AccountDao accountDao,
			TargetDao targetDao, IncomingPaymentDao incomingPaymentDao,
			OutgoingPaymentDao outgoingPaymentDao) {
		super(ui);
		this.clientDao = clientDao;
		this.accountDao = accountDao;
		this.targetDao = targetDao;
		this.incomingPaymentDao = incomingPaymentDao;
		this.outgoingPaymentDao = outgoingPaymentDao;
		createDashboardTab = ui.find(tabAnalytics, TAB_CREATE_DASHBOARD);
		this.init();
	}

	public void deinit() {
	}

	@Override
	protected Object initialiseTab() {
		// ui.add(createDashboardTab, stepCreateSettings.getPanelComponent());
		setCurrentStepPanel(new SelectTargetSavingsHandler(ui, clientDao, this)
				.getPanelComponent());
		return createDashboardTab;
	}

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
