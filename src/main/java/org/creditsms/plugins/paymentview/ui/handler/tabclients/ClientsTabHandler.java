package org.creditsms.plugins.paymentview.ui.handler.tabclients;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseClientTable;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.CustomizeClientDBHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.EditClientHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.IncomingPaymentsDialogHandler;

public class ClientsTabHandler implements ThinletUiEventHandler {
//> STATIC CONSTANTS
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientTableHolder";
	private static final String XML_CLIENTS_TAB = "/ui/plugins/paymentview/clients/clients.xml";
	private static final String I18N_CONFIRM_DELETE_CLIENT = "message.confirm.delete.client";
	
//> INSTANCE PROPERTIES
	private final ClientDao clientDao;
	private final CustomFieldDao customFieldDao;

	private Object clientsTableComponent;
	private UiGeneratorController ui;
	private Object clientsTab;
	private Object clientTableHolder;
	private BaseClientTable clientTableHandler;
	private final PaymentViewPluginController pluginController;
	private Object incomingPaymentsDialog;
	
	public ClientsTabHandler(UiGeneratorController ui,
			final PaymentViewPluginController pluginController) {
		this.ui = ui;
		
		this.pluginController = pluginController;
		this.clientDao = pluginController.getClientDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		
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
		ui.add(new EditClientHandler(ui, pluginController, this, true).getDialog());
	}
	
	public void customizeClientDB() {
		ui.add(new CustomizeClientDBHandler(pluginController, customFieldDao).getDialog());
	}

	public void deleteClient() {
		Object[] selectedClients = this.ui.getSelectedItems(clientsTableComponent);
		String clientNameList = "";
		for (Object selectedClient : selectedClients) {
			Client attachedClient = ui.getAttachedObject(selectedClient, Client.class);
			attachedClient.setActive(false);
			clientNameList = clientNameList + " - " + attachedClient.getFullName();
			clientDao.updateClient(attachedClient);
		}

		ui.removeDialog(ui
				.find(UiGeneratorControllerConstants.COMPONENT_CONFIRM_DIALOG));
		if (selectedClients.length >0){
			ui.infoMessage("You have successfully deleted the following client(s)"+ clientNameList + ".");	
		}
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
		exportClient(clientTableHandler.getClientFilter());
	}
	
	public void exportClient(String clientFilter) {
		Object[] selectedItems = ui.getSelectedItems(clientsTableComponent);
		if (selectedItems.length <= 0){
			if (clientFilter.isEmpty()){
				exportClients(clientDao.getAllClients());
			} else {
				exportClients(clientDao.getClientsByFilter(clientFilter));
			}
			
		}else{
			List<Client> clients = new ArrayList<Client>(selectedItems.length);
			for (Object o : selectedItems) {
				clients.add(ui.getAttachedObject(o, Client.class));
			}
			exportClients(clients);
		}
	}
	
	private void exportClients(List<Client> clients){
		new ClientExportHandler(ui, pluginController, clients).showWizard();
		this.refresh();
	}
	
	public void copyToContacts() throws NumberFormatException, DuplicateKeyException {
		Object[] selectedItems = ui.getSelectedItems(clientsTableComponent);
		if (selectedItems.length <= 0){
			copyClientsToContacts(clientDao.getAllClients());
		}else{
			List<Client> clients = new ArrayList<Client>(selectedItems.length);
			for (Object o : selectedItems) {
				clients.add(ui.getAttachedObject(o, Client.class));
			}
			copyClientsToContacts(clients);
		}
	}

	private void copyClientsToContacts(List<Client> clients) throws NumberFormatException, DuplicateKeyException {
		ContactDao contactDao = pluginController.getUiGeneratorController().getFrontlineController().getContactDao();
		for (Client c : clients) {
			Contact fromMsisdn = contactDao.getFromMsisdn(c.getPhoneNumber());
			if (fromMsisdn != null){
				fromMsisdn.setName(c.getFullName());
				fromMsisdn.setPhoneNumber(c.getPhoneNumber());
				contactDao.updateContact(fromMsisdn);
			}else{
				//Start Save the Client as a contact to the core project
				Contact contact = new Contact(c.getFullName(), c.getPhoneNumber(), "", "", "", true);
				contactDao.saveContact(contact);
				//Finish save
			}
		}
	}
	
	public void importClient() {
		new ClientImportHandler(pluginController, this).showWizard();
		this.refresh();
	}
	
	public void viewIncomingPaymentByClient() {
		Object[] selectedItems = ui.getSelectedItems(clientsTableComponent);
		List<Client> selectedClients = new ArrayList<Client>(selectedItems.length);
		if (selectedItems.length <= 0){
			if (clientTableHandler.getClientFilter().isEmpty()){
				selectedClients = this.clientDao.getAllActiveClients();
			} else {
				selectedClients = this.clientDao.getClientsByFilter(clientTableHandler.getClientFilter());
			}
			
		}else{
			for (Object o : selectedItems) {
				selectedClients.add(ui.getAttachedObject(o, Client.class));
			}
		}
		
		incomingPaymentsDialog = new IncomingPaymentsDialogHandler(ui,pluginController, selectedClients).getDialog();
		ui.add(incomingPaymentsDialog);
	}
	
	public final void showDeleteConfirmationDialog(String methodToBeCalled){
		this.ui.showConfirmationDialog(methodToBeCalled, this, I18N_CONFIRM_DELETE_CLIENT);
	}
}
