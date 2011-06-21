package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.BaseClientTable;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.EditClientHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SendNewPaymentDialogHandler;

public class SelectFromClientsTabHandler extends BaseTabHandler {
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_SELECT_FROM_CLIENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/selectfromclients.xml";
	private static final String TAB_SEND_NEW_OUTGOING_PAYMENTS = "tab_sendOutgoingPayments";
	
	private Object sendNewPaymentsTab;
	private Object selectFromClientsPanel;
	private Object sendPaymentAuthDialog;

	private Object clientTableHolder;
	private BaseClientTable clientsTableHandler;

	private PaymentViewPluginController pluginController;
	private Object clientsTableComponent;

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
		clientsTableComponent = clientsTableHandler.getClientsTable();
		ui.add(clientTableHolder, clientsTableHandler.getClientsTablePanel());
		this.ui.add(sendNewPaymentsTab, selectFromClientsPanel);
		return sendNewPaymentsTab;
	}

	@Override
	public void refresh() {
		this.clientsTableHandler.updateClientsList();
	}
	

	public void sendPaymentForClient() {
		Object[] selectedClients = this.ui.getSelectedItems(clientsTableComponent);
		Client client = null;
		if (selectedClients.length >0 ){
			for (Object selectedClient : selectedClients) {
				client = (Client) ui.getAttachedObject(selectedClient);
				sendPaymentAuthDialog = new SendNewPaymentDialogHandler(ui, pluginController, client).getDialog();
				ui.add(sendPaymentAuthDialog);
			}
		} else {
			ui.infoMessage("Please select a client");
			this.refresh();
		}


	}

}
