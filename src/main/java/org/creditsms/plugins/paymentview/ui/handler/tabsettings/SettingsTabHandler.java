package org.creditsms.plugins.paymentview.ui.handler.tabsettings;

import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.UpdateAuthorizationCodeDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings.MobilePaymentServiceSettingsInitialisationDialog;

public class SettingsTabHandler extends BaseTabHandler {
	private static final String COMPONENT_SETTINGS_TABLE = "tbl_accounts";
	private static final String XML_SETTINGS_TAB = "/ui/plugins/paymentview/settings/settingsTab.xml";

	private Object settingsTab;
	private Object settingsTableComponent;
	private final PaymentViewPluginController pluginController;

	public SettingsTabHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		init();
	}

	public void createNew() {
		new MobilePaymentServiceSettingsInitialisationDialog(ui, pluginController).showDialog();
	}
	
	@Override
	protected Object initialiseTab() {
		settingsTab = ui.loadComponentFromFile(XML_SETTINGS_TAB, this);
		settingsTableComponent = ui.find(settingsTab, COMPONENT_SETTINGS_TABLE);
		return settingsTab;
	}
	
	public Object getRow(MpesaPaymentService paymentService) {
		Object row = ui.createTableRow(paymentService);
		Object paymentServiceName = ui.createTableCell(paymentService.toString());
		Object balance = ui.createTableCell(paymentService.getBalance().toString());
		ui.add(row, paymentServiceName);
		ui.add(row, balance);
		return row;
	}

	@Override
	public void refresh() {
		ui.removeAll(settingsTableComponent);
		ui.add(settingsTableComponent, getRow((MpesaPaymentService)this.pluginController.getPaymentService()));
	}

	public void updateAccountBalance() {
		// TODO Auto-generated method stub
	}
	
	public void updateAuthCode() {
		new UpdateAuthorizationCodeDialog(ui, pluginController).showDialog();
	}
	
	public void deleteAccount() {
		// FIXME this method's contents appeared to have nothing to do with accounts
	}
}
