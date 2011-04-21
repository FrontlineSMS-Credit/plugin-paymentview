package org.creditsms.plugins.paymentview.ui.handler.settings;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
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

	private AccountDao accountDao;
	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private ClientDao clientDao;

	public SettingsTabHandler(UiGeneratorController ui,
			IncomingPaymentDao incomingPaymentDao,
			OutgoingPaymentDao outgoingPaymentDao, AccountDao accountDao,
			ClientDao clientDao) {
		
		super(ui);
		this.incomingPaymentDao = incomingPaymentDao;
		this.outgoingPaymentDao = outgoingPaymentDao;
		this.accountDao = accountDao;
		this.clientDao = clientDao;
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
			Account a = (Account) ui.getAttachedObject(selectedNetworkOperator);
			ui.add(new ConfigureAccountHandler(ui, a, this.accountDao)
					.getDialog());
		}
	}

	public void examineAccount() {
		// TODO Auto-generated method stub

	}

	public void deleteAccount() {
		Object[] selectedClients = this.ui
				.getSelectedItems(this.settingsTableComponent);
		for (Object selectedClient : selectedClients) {
			Account a = (Account) ui.getAttachedObject(selectedClient);
			accountDao.deleteAccount(a);
		}

		ui.removeDialog(ui
				.find(UiGeneratorControllerConstants.COMPONENT_CONFIRM_DIALOG));
		ui.infoMessage("You have succesfully deleted from the operator!");
		this.refresh();
	}

	public void createNew() {
		ui.add(new CreateNewAccountHandler(ui, this.accountDao).getDialog());
	}

	public void updateAccountBalance() {
		// TODO Auto-generated method stub

	}

}
