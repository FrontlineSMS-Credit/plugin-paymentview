package org.creditsms.plugins.paymentview.ui.handler.tabsettings;

import java.io.IOException;

import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.UpdateAuthorizationCodeDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings.MobilePaymentServiceSettingsInitialisationDialog;
import org.creditsms.plugins.paymentview.userhomepropeties.payment.balance.Balance.BalanceEventNotification;

public class SettingsTabHandler extends BaseTabHandler implements EventObserver{
	private static final String COMPONENT_SETTINGS_TABLE = "tbl_accounts";
	private static final String XML_SETTINGS_TAB = "/ui/plugins/paymentview/settings/settingsTab.xml";

	private Object settingsTab;
	private Object settingsTableComponent;
	private final PaymentViewPluginController pluginController;

	public SettingsTabHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		
		ui.getFrontlineController().getEventBus().registerObserver(this);
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
		for(MpesaPaymentService paymentService : pluginController.getPaymentServices()){
			ui.add(settingsTableComponent, getRow(paymentService));
		}
	}

	public void updateAccountBalance() throws PaymentServiceException, IOException {
		Object selectedItem = this.ui.getSelectedItem(settingsTableComponent);
		if (selectedItem != null) {
			PaymentService __paymentService = ui.getAttachedObject(selectedItem, PaymentService.class);
			__paymentService.checkBalance();
			ui.alert("Request has been sent. The Account balance will be updated shortly.");
		}else{
			ui.alert("Please select an account to update balance.");
		}
	}
	
	public void updateAuthCode() {
		new UpdateAuthorizationCodeDialog(ui, pluginController).showDialog();
	}
	
	public void deleteAccount() {
		// FIXME this method's contents appeared to have nothing to do with accounts
	}

	public void notify(final FrontlineEventNotification notification) {
		new FrontlineUiUpateJob() {
			public void run() {
				if (!(notification instanceof BalanceEventNotification)) {
					return;
				}else{
					ui.alert(((BalanceEventNotification)notification).getMessage());
					SettingsTabHandler.this.refresh();
				}
			}
		}.execute();
	}
}
