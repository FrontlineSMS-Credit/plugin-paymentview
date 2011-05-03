package org.creditsms.plugins.paymentview.ui.handler.tabclients;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
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
	private ClientDao clientDao;
	private CustomFieldDao customFieldDao;
	private CustomValueDao customValueDao;

	private Object clientsTableComponent;
	private PaymentViewThinletTabController paymentViewThinletTabController;
	private UiGeneratorController ui;
	private Object clientsTab;
	private Object clientTableHolder;
	private BaseClientTable clientTableHandler;

	public ClientsTabHandler(
			UiGeneratorController ui,
			final PaymentViewThinletTabController paymentViewThinletTabController) {
		this.ui = ui;
		this.paymentViewThinletTabController = paymentViewThinletTabController;
		this.clientDao = this.paymentViewThinletTabController.getClientDao();
		this.customFieldDao = this.paymentViewThinletTabController
				.getCustomFieldDao();
		this.customValueDao = this.paymentViewThinletTabController
				.getCustomValueDao();
		init();
	}
	
	public void init() {
		clientsTab = ui.loadComponentFromFile(XML_CLIENTS_TAB, this);
		clientTableHolder = ui.find(clientsTab, PNL_CLIENT_TABLE_HOLDER);
		clientTableHandler = new ClientTableHandler(ui, this, clientDao, customFieldDao, customValueDao);
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
		ui.add(new EditClientHandler(ui, clientDao, this, customValueDao,
				customFieldDao).getDialog());
	}

	public void analyseClient() {
		// TODO Auto-generated method stub
	}

	
	public void customizeClientDB() {
		ui.add(new CustomizeClientDBHandler(ui, customFieldDao, this)
				.getDialog());
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
		Object[] selectedClients = this.ui
				.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = (Client) ui.getAttachedObject(selectedClient);
			ui.add(new EditClientHandler(ui, c, clientDao, this,
					customValueDao, customFieldDao).getDialog());
		}
	}

	public void exportClient() {
		new ClientExportHandler(ui, clientDao, customFieldDao, customValueDao)
				.showWizard();
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
