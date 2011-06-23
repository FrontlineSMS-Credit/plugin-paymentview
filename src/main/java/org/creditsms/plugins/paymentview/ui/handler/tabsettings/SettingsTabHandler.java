package org.creditsms.plugins.paymentview.ui.handler.tabsettings;

import net.frontlinesms.messaging.FrontlineMessagingServiceStatus;
import net.frontlinesms.messaging.mms.email.MmsEmailServiceStatus;
import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.messaging.sms.modem.SmsModemStatus;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.UpdateAuthorizationCodeDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings.MobilePaymentService;

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
		new MobilePaymentService(ui, pluginController, this).showDialog();
	}
	
	@Override
	protected Object initialiseTab() {
		settingsTab = ui.loadComponentFromFile(XML_SETTINGS_TAB, this);
		settingsTableComponent = ui.find(settingsTab, COMPONENT_SETTINGS_TABLE);
		return settingsTab;
	}
	
	public void addPaymentService(MpesaPaymentService paymentService) {
		if (!pluginController.getPaymentServices().contains(paymentService)){
			pluginController.getPaymentServices().add(paymentService);
		}else{
			ui.getFrontlineController().getEventBus().unregisterObserver(paymentService);
			paymentService = null;
		}
		
		refresh();
	}
	
	public Object getRow(MpesaPaymentService paymentService){
		SmsService service = paymentService.getSmsService();
		
		Object cellServiceName = ui.createTableCell(service.getServiceName());
		Object cellMsisdn = ui.createTableCell(service.getMsisdn());
		Object idCell = ui.createTableCell(service.getServiceIdentification());
		Object cellDisplayPort = ui.createTableCell(service.getDisplayPort());
		
		Object row = ui.createTableRow(paymentService);
		ui.add(row, cellDisplayPort);
		ui.add(row, idCell);
		ui.add(row, cellServiceName);
		ui.add(row, cellMsisdn);
		
		
		final String statusIcon;
		FrontlineMessagingServiceStatus status = paymentService.getSmsService().getStatus();
		if (status.equals(SmsModemStatus.CONNECTING) ||
			status.equals(SmsModemStatus.DETECTED) ||
			status.equals(SmsModemStatus.TRY_TO_CONNECT) ||
			status.equals(MmsEmailServiceStatus.FETCHING)) {
			statusIcon = Icon.LED_AMBER;	
		} else if (paymentService.getSmsService().isConnected()){
			statusIcon = Icon.LED_GREEN;
		} else {
			statusIcon = Icon.LED_RED;
		}
	
		Object cellStatus = ui.createTableCell(InternationalisationUtils.getI18nString(service.getStatus().getI18nKey(), service.getStatusDetail()));
		ui.setIcon(cellStatus, statusIcon);
		ui.add(row, cellStatus);
				
		Object cellDescription = ui.createTableCell(paymentService.toString());
		ui.add(row, cellDescription);
		
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
		new UpdateAuthorizationCodeDialog(ui, pluginController).showDialog();
	}
	
	public void deleteAccount() {
		Object[] selectedItems = this.ui
				.getSelectedItems(this.settingsTableComponent);
		
		if (selectedItems.length <= 0){
			for (Object selectedClient : selectedItems) {
				MpesaPaymentService m = ui.getAttachedObject(selectedClient, MpesaPaymentService.class);
				ui.getFrontlineController().getEventBus().unregisterObserver(m);
				pluginController.getPaymentServices().remove(m);
				m = null;
			}
			
			ui.removeDialog(ui.find(UiGeneratorControllerConstants.COMPONENT_CONFIRM_DIALOG));
			ui.infoMessage("You have successfully deleted from the operator!");
			this.refresh();
		}
	}
}
