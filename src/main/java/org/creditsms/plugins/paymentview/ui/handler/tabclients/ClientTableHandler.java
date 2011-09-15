package org.creditsms.plugins.paymentview.ui.handler.tabclients;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTableHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.ThirdPartySMSDialogHandler;

public class ClientTableHandler extends BaseClientTableHandler {
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

	public void viewIncomingPaymentByClient() {
		clientsTabHandler.viewIncomingPaymentByClient();
	}

	public void deleteClient() {
		clientsTabHandler.deleteClient();
	}

	public void editClient() {
		clientsTabHandler.editClient();
	}
	
	public void exportClient() {
		clientsTabHandler.exportClient(clientFilter);
	}	
	
	public void showDeleteConfirmationDialog(String methodToBeCalled){
		clientsTabHandler.showDeleteConfirmationDialog(methodToBeCalled);
	}
	
	public void copyToContacts() throws NumberFormatException, DuplicateKeyException{
		clientsTabHandler.copyToContacts();
	}
	
	public void designateThirdPartySMSRecipient() {
		Client theClient = new Client();
		for (int u =0 ; u<this.getSelectedClients().size(); u++) {
			Client c = (Client) this.getSelectedClients().get(u);
			if(u==0){
				theClient = c;
			}
		}
		ui.add(new ThirdPartySMSDialogHandler(ui, pluginController, theClient).getDialogComponent());
	}
}
