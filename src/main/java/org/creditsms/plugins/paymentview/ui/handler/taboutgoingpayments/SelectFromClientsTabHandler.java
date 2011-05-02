package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SendPaymentAuthDialogHandler;

public class SelectFromClientsTabHandler extends BaseTabHandler {
	private static final String XML_SELECT_FROM_CLIENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/selectfromclients.xml";
	
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientsTableHolder";
	private ClientDao clientDao;

	private Object schedulePaymentAuthDialog;
	private Object selectFromClientsTab;
	private Object sendPaymentAuthDialog;

	private Object clientTableHolder;
	private SelectClientsTableHandler clientsTableHandler;

	private CustomFieldDao customFieldDao;
	private CustomValueDao customValueDao;

	public SelectFromClientsTabHandler(UiGeneratorController ui,
			ClientDao clientDao,CustomValueDao customValueDao,
			CustomFieldDao customFieldDao) {
		super(ui);
		this.clientDao = clientDao;
		this.customValueDao = customValueDao;
		this.customFieldDao = customFieldDao;
		init();
		refresh();
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsTab = ui.loadComponentFromFile(
				XML_SELECT_FROM_CLIENTS_TAB, this);
				
		clientTableHolder = ui.find(selectFromClientsTab, PNL_CLIENT_TABLE_HOLDER);
		clientsTableHandler = new SelectClientsTableHandler(ui, clientDao, customFieldDao, customValueDao);
		
		ui.add(clientTableHolder, clientsTableHandler.getClientsTablePanel());
		
		return selectFromClientsTab;
	}

	@Override
	public void refresh() {
		this.clientsTableHandler.updateClientsList();
	}

	public void showSchedulePaymentAuthDialog() {
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(schedulePaymentAuthDialog);
	}

	public void showSendPaymentAuthDialog() {
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(sendPaymentAuthDialog);
	}
	
	public void selectAll() {
		clientsTableHandler.selectAll();
	}
}
