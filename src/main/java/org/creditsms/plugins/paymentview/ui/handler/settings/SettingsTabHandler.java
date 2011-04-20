package org.creditsms.plugins.paymentview.ui.handler.settings;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;
import org.creditsms.plugins.paymentview.ui.handler.client.dialogs.EditClientHandler;
import org.creditsms.plugins.paymentview.ui.handler.settings.dialogs.ConfigureAccountHandler;
import org.creditsms.plugins.paymentview.ui.handler.settings.dialogs.CreateNewAccountHandler;

public class SettingsTabHandler extends BaseTabHandler implements
		PagedComponentItemProvider {
	private static final String XML_SETTINGS_TAB = "/ui/plugins/paymentview/settings/settingsTab.xml";

	private static final String COMPONENT_SETTINGS_TABLE = "tbl_accounts";
	private static final String COMPONENT_PANEL_SETTINGS_TABLE = "pnl_accounts";

	private Object settingsTab;

	private Object settingsTableComponent;
	private ComponentPagingHandler settingsTablePager;
	private Object pnlSettingsTableComponent;

	private NetworkOperatorDao networkOperatorDao;

	public SettingsTabHandler(UiGeneratorController ui) {
		super(ui);
		init();
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object initialiseTab() {
		settingsTab = ui.loadComponentFromFile(XML_SETTINGS_TAB, this);
		settingsTableComponent = ui.find(settingsTab, COMPONENT_SETTINGS_TABLE);
		// settingsTablePager = new ComponentPagingHandler(ui, this,
		// settingsTableComponent);
		// pnlSettingsTableComponent = ui.find(settingsTab,
		// COMPONENT_PANEL_SETTINGS_TABLE);
		// this.ui.add(pnlSettingsTableComponent,
		// this.settingsTablePager.getPanel());
		return settingsTab;
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	public void configureAccount() {
		Object[] selectedClients = this.ui
				.getSelectedItems(settingsTableComponent);
		for (Object selectedNetworkOperator : selectedClients) {
			NetworkOperator n = (NetworkOperator) ui
			.getAttachedObject(selectedNetworkOperator);
			ui.add(new ConfigureAccountHandler(ui, n, this.networkOperatorDao).getDialog());
		}
	}

	public void examineAccount() {
		// TODO Auto-generated method stub

	}

	public void deleteAccount() {
		Object[] selectedClients = this.ui
				.getSelectedItems(this.settingsTableComponent);
		for (Object selectedClient : selectedClients) {
			NetworkOperator n = (NetworkOperator) ui
					.getAttachedObject(selectedClient);
			networkOperatorDao.deleteNetworkOperator(n);
		}

		ui.removeDialog(ui
				.find(UiGeneratorControllerConstants.COMPONENT_CONFIRM_DIALOG));
		ui.infoMessage("You have succesfully deleted from the operator!");
		this.refresh();
	}

	public void createNew() {
		ui.add(new CreateNewAccountHandler(ui, this.networkOperatorDao).getDialog());
	}

	public void updateAccountBalance() {
		// TODO Auto-generated method stub

	}

}
