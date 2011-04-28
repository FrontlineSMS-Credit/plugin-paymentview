package org.creditsms.plugins.paymentview.ui.handler.tabsettings;

import net.frontlinesms.payment.safaricom.ui.SafaricomAccountCreationUiHandler;
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
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.ConfigureAccountHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.CreateNewAccountHandler;

public class SettingsTabHandler extends BaseTabHandler implements
		PagedComponentItemProvider {
	private static final String COMPONENT_PANEL_SETTINGS_TABLE = "pnl_accounts";

	private static final String COMPONENT_SETTINGS_TABLE = "tbl_accounts";
	private static final String XML_SETTINGS_TAB = "/ui/plugins/paymentview/settings/settingsTab.xml";

	private AccountDao accountDao;

	private ClientDao clientDao;
	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao;

	private Object pnlSettingsTableComponent;
	private Object settingsTab;
	private Object settingsTableComponent;
	private ComponentPagingHandler settingsTablePager;

	private final PaymentViewThinletTabController paymentViewThinletTabController;

	public SettingsTabHandler(UiGeneratorController ui, PaymentViewThinletTabController paymentViewThinletTabController) {
		super(ui);
		
		this.paymentViewThinletTabController = paymentViewThinletTabController;
		this.incomingPaymentDao = paymentViewThinletTabController.getIncomingPaymentDao();
		this.outgoingPaymentDao = paymentViewThinletTabController.getOutgoingPaymentDao();
		this.accountDao = paymentViewThinletTabController.getAccountDao();
		this.clientDao = paymentViewThinletTabController.getClientDao();
		init();
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

	public void createNew() {
//		ui.add(new CreateNewAccountHandler(ui, this.accountDao).getDialog());
		ui.add(new SafaricomAccountCreationUiHandler(ui).getDialog());
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

	public void examineAccount() {
		// TODO Auto-generated method stub

	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		// TODO Auto-generated method stub
		return null;
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

	@Override
	public void refresh() {
		// TODO Auto-generated method stub

	}

	public void updateAccountBalance() {
		// TODO Auto-generated method stub

	}

}
