package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseClientTable;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient.ReviewHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.SelectTargetSavingsHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SendPaymentAuthDialogHandler;

public class SelectFromClientsTabHandler extends BaseTabHandler {
	private static final String XML_SELECT_FROM_CLIENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/selectfromclients.xml";
	
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientsTableHolder";

	
	//KIM
	private static final String TAB_SEND_NEW_OUTGOING_PAYMENTS = "tab_sendOutgoingPayments";
	private Object sendNewPaymentsTab;

//	private Object currentPanel;
//	private Object sendNewOutgoingPayments;
	
	
	private Object schedulePaymentAuthDialog;
	private Object selectFromClientsPanel;
	private Object sendPaymentAuthDialog;

	private Object clientTableHolder;
	private BaseClientTable clientsTableHandler;

	private PaymentViewPluginController pluginController;

	public SelectFromClientsTabHandler(UiGeneratorController ui, Object tabOutgoingPayments, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		sendNewPaymentsTab = ui.find(tabOutgoingPayments, TAB_SEND_NEW_OUTGOING_PAYMENTS);//KIM
		init();
		refresh();
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsPanel = ui.loadComponentFromFile(XML_SELECT_FROM_CLIENTS_TAB, this);
		clientTableHolder = ui.find(selectFromClientsPanel, PNL_CLIENT_TABLE_HOLDER);
		clientsTableHandler = new SelectClientsTableHandler(ui, pluginController);
		
//		clientsTab = ui.loadComponentFromFile(XML_CLIENTS_TAB, this);
//		clientTableHolder = ui.find(clientsTab, PNL_CLIENT_TABLE_HOLDER);
//		clientTableHandler = new ClientTableHandler(ui, pluginController, this);
//		clientsTableComponent = clientTableHandler.getClientsTable();
//		ui.add(clientTableHolder, clientTableHandler.getClientsTablePanel());
		ui.add(clientTableHolder, clientsTableHandler.getClientsTablePanel());
		//return selectFromClientsTab;
		
		this.ui.add(sendNewPaymentsTab, selectFromClientsPanel);
		return sendNewPaymentsTab;
	}

	@Override
	public void refresh() {
		this.clientsTableHandler.updateClientsList();
	}
	
	public Object next() {
//		selectFromClientsTab.setCurrentStepPanel(new SendNewPaymentHandler(ui, pluginController, this).getPanelComponent());
//		
//		if(parseDateRange()){
//			readServiceItem();
//			addClientTabHandler.setCurrentStepPanel(new ReviewHandler(
//					(UiGeneratorController) ui, this.pluginController, addClientTabHandler, this).getPanelComponent());
//		}
		return null;
	}
	
//	public void setCurrentStepPanel(Object panel) {
//		if (currentPanel != null)
//			ui.remove(currentPanel);
//
//		ui.add(clientTableHolder, panel);
//		currentPanel = panel;
//		SelectFromClientsTabHandler.this.refresh();
//	}


//	public void showSchedulePaymentAuthDialog() {
//		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui)
//				.getDialog();
//		ui.add(schedulePaymentAuthDialog);
//	}
//
//	public void showSendPaymentAuthDialog() {
//		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui)
//				.getDialog();
//		ui.add(sendPaymentAuthDialog);
//	}
	
//	public void selectAll() {
//		clientsTableHandler.selectAll();
//	}
}
