package org.creditsms.plugins.paymentview.ui.handler.tabsettings;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings.MobilePaymentService;

public class SettingsTabHandler extends BaseTabHandler implements
		PagedComponentItemProvider {
	private static final String COMPONENT_SETTINGS_TABLE = "tbl_accounts";
	private static final String XML_SETTINGS_TAB = "/ui/plugins/paymentview/settings/settingsTab.xml";

	private AccountDao accountDao;

	private ClientDao clientDao;
	private IncomingPaymentDao incomingPaymentDao;

	private Object settingsTab;
	private Object settingsTableComponent;
	private final PaymentViewPluginController pluginController;

	public SettingsTabHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.accountDao = pluginController.getAccountDao();
		this.clientDao = pluginController.getClientDao();
		init();
	}

	public void configureAccount() {
	}

	public void createNew() {
//		ui.add(new SafaricomPaymentServiceConfigUiHandler(ui, incomingPaymentDao, clientDao, accountDao).getDialog());
		new MobilePaymentService(ui, pluginController).showDialog();
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
