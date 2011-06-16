package org.creditsms.plugins.paymentview.ui.handler.tabclients;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseClientTable;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.CustomizeClientDBHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.EditClientHandler;

public class ClientsTabHandler implements ThinletUiEventHandler {
//> STATIC CONSTANTS
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientTableHolder";
	private static final String XML_CLIENTS_TAB = "/ui/plugins/paymentview/clients/clients.xml";
	
//> INSTANCE PROPERTIES
	private final ClientDao clientDao;
	private final CustomFieldDao customFieldDao;
	private final CustomValueDao customValueDao;

	private Object clientsTableComponent;
	private UiGeneratorController ui;
	private Object clientsTab;
	private Object clientTableHolder;
	private BaseClientTable clientTableHandler;
	private final PaymentViewPluginController pluginController;

	public ClientsTabHandler(UiGeneratorController ui,
			final PaymentViewPluginController pluginController) {
		this.ui = ui;
		this.pluginController = pluginController;
		this.clientDao = pluginController.getClientDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		this.customValueDao = pluginController.getCustomValueDao();
		init();
	}
	
	public void init() {
		clientsTab = ui.loadComponentFromFile(XML_CLIENTS_TAB, this);
		clientTableHolder = ui.find(clientsTab, PNL_CLIENT_TABLE_HOLDER);
		clientTableHandler = new ClientTableHandler(ui, pluginController, this);
		clientsTableComponent = clientTableHandler.getClientsTable();
		ui.add(clientTableHolder, clientTableHandler.getClientsTablePanel());
	}

	public void refresh() {
		this.clientTableHandler.refresh();
	}
	
	public Object getTab() {
		return this.clientsTab;
	}
	
//> ACTION HANDLERS
	
	public void addClient() {
		ui.add(new EditClientHandler(ui, pluginController, this).getDialog());
	}
	
	public void customizeClientDB() {
		ui.add(new CustomizeClientDBHandler(pluginController, customFieldDao, this).getDialog());
	}

	public void deleteClient() {
		Object[] selectedClients = this.ui
				.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = ui.getAttachedObject(selectedClient, Client.class);
			clientDao.deleteClient(c);
		}

		ui.removeDialog(ui
				.find(UiGeneratorControllerConstants.COMPONENT_CONFIRM_DIALOG));
		ui.infoMessage("You have succesfully deleted from the client!");
		this.refresh();
	}

	public void editClient() {
		Object[] selectedClients = this.ui.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = (Client) ui.getAttachedObject(selectedClient);
			ui.add(new EditClientHandler(ui, c, pluginController, this).getDialog());
		}
	}

	public void exportClient() {
		Object[] selectedItems = ui.getSelectedItems(clientsTableComponent);
		List<Client> attachedClients = new ArrayList<Client>();
		for(Object selectedItem : selectedItems){
			attachedClients.add(ui.getAttachedObject(selectedItem, Client.class));
		}

		new ClientExportHandler(ui, pluginController, attachedClients).showWizard();
		this.refresh();
	}

	public void importClient() {
		new ClientImportHandler(ui, this, clientDao, this.customFieldDao,
				this.customValueDao).showWizard();
		this.refresh();
	}
	
	public final void showConfirmationDialog(String methodToBeCalled){
		this.ui.showConfirmationDialog(methodToBeCalled, this);
	}

	public void revalidateTable() {
		this.clientTableHandler.revalidateTable();
	}
}
