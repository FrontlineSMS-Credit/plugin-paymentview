package org.creditsms.plugins.paymentview.ui.handler.tabsettings;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings.MobilePaymentService;

public class SettingsTabHandler extends BaseTabHandler {
	private static final String COMPONENT_SETTINGS_TABLE = "tbl_accounts";
	private static final String XML_SETTINGS_TAB = "/ui/plugins/paymentview/settings/settingsTab.xml";

	private AccountDao accountDao;

	private ClientDao clientDao;
	private IncomingPaymentDao incomingPaymentDao;
	
	private List<PaymentService> paymentServices;

	private Object settingsTab;
	private Object settingsTableComponent;
	private final PaymentViewPluginController pluginController;

	public SettingsTabHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.accountDao = pluginController.getAccountDao();
		this.clientDao = pluginController.getClientDao();
		paymentServices = new ArrayList<PaymentService>(0);
		init();
	}

	public void configureAccount() {
	}

	public void createNew() {
//		ui.add(new SafaricomPaymentServiceConfigUiHandler(ui, incomingPaymentDao, clientDao, accountDao).getDialog());
		new MobilePaymentService(ui, pluginController, this).showDialog();
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

	@Override
	protected Object initialiseTab() {
		settingsTab = ui.loadComponentFromFile(XML_SETTINGS_TAB, this);
		settingsTableComponent = ui.find(settingsTab, COMPONENT_SETTINGS_TABLE);
		return settingsTab;
	}
	
	public void addPaymentService(PaymentService paymentService) {
		paymentServices.add(paymentService);
		refresh();
	}
	
	public Object getRow(SmsService smsService){
		Object cellServiceName = ui.createTableCell(smsService.getServiceName());
		Object cellMsisdn = ui.createTableCell(smsService.getMsisdn());
		Object cellStatus = ui.createTableCell(smsService.getStatus().getI18nKey());
		Object cellDisplayPort = ui.createTableCell(smsService.getDisplayPort());
		Object cellStatusDetail = ui.createTableCell(smsService.getStatusDetail());
		
		Object row = ui.createTableRow(smsService);
		ui.add(row, cellServiceName);
		ui.add(row, cellMsisdn);
		ui.add(row, cellStatus);
		ui.add(row, cellDisplayPort);
		ui.add(row, cellStatusDetail);
		
		return row;
	}

	@Override
	public void refresh() {
		ui.removeAll(settingsTableComponent);
		for(PaymentService paymentService : paymentServices){
			ui.add(settingsTableComponent, getRow(paymentService.getSmsService()));
		}
	}

	public void updateAccountBalance() {
		// TODO Auto-generated method stub
	}
}
