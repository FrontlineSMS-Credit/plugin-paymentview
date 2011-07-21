package org.creditsms.plugins.paymentview.ui.handler.tabsettings;

import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
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
		ui.add(row, "Hi there is something configured here.  I deleted the details.");
		return row;
	}

	@Override
	public void refresh() {
		ui.removeAll(settingsTableComponent);
		for(MpesaPaymentService paymentService : pluginController.getPaymentServices()){
			ui.add(settingsTableComponent, getRow(paymentService));
		}
	}

	public void updateAccountBalance() {
		// TODO Auto-generated method stub
	}
	
	public void updateAuthCode() {
		// TODO Auto-generated method stub
	}
	
	public void deleteAccount() {
		// FIXME this method's contents appeared to have nothing to do with accounts
	}
}
