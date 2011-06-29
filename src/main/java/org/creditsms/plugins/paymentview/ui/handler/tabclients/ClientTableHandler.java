package org.creditsms.plugins.paymentview.ui.handler.tabclients;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseClientTable;

public class ClientTableHandler extends BaseClientTable {
	private static final String PNL_TBL_CLIENT_LIST = "tbl_clientList";
	private static final String XML_CLIENTS_PANEL = "/ui/plugins/paymentview/clients/clientsTable.xml";
	private ClientsTabHandler clientsTabHandler;

	public ClientTableHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, ClientsTabHandler clientsTabHandler) {
		super(ui, pluginController);
		this.clientsTabHandler = clientsTabHandler;
	}

	@Override
	protected String getClientsTableName() {
		return PNL_TBL_CLIENT_LIST;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return XML_CLIENTS_PANEL;
	}

	public void deleteClient() {
		clientsTabHandler.deleteClient();
	}

	public void editClient() {
		clientsTabHandler.editClient();
	}
	
	public void exportClient() {
		clientsTabHandler.exportClient();
	}	
	
}
