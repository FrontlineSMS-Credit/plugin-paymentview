package org.creditsms.plugins.paymentview.ui.handler.tabclients;

import static net.frontlinesms.FrontlineSMSConstants.ACTION_ADD_TO_GROUP;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_GROUPS_MENU;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_MENU_ITEM_MSG_HISTORY;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_MENU_ITEM_VIEW_CONTACT;
import static net.frontlinesms.ui.UiGeneratorControllerConstants.COMPONENT_NEW_GROUP;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.data.repository.ContactDao;
import net.frontlinesms.data.repository.GroupDao;
import net.frontlinesms.data.repository.GroupMembershipDao;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTableHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.CustomizeClientDBHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.EditClientHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.IncomingPaymentsDialogHandler;

import thinlet.Thinlet;

public class ClientsTabHandler implements ThinletUiEventHandler {
//> STATIC CONSTANTS
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientTableHolder";
	private static final String XML_CLIENTS_TAB = "/ui/plugins/paymentview/clients/clients.xml";
	private static final String I18N_CONFIRM_DELETE_CLIENT = "message.confirm.delete.client";
	
//> INSTANCE PROPERTIES
	private final ClientDao clientDao;
	private final CustomFieldDao customFieldDao;

	protected UiGeneratorController ui;
	private Object clientsTab;
	private Object clientTableHolder;
	protected BaseClientTableHandler clientTableHandler;
	private final PaymentViewPluginController pluginController;
	private Object incomingPaymentsDialog;
	private GroupMembershipDao groupMembershipDao;
	private GroupDao groupDao;
	
	public ClientsTabHandler(UiGeneratorController ui,
			final PaymentViewPluginController pluginController) {
		this.ui = ui;
		
		this.pluginController = pluginController;
		this.clientDao = pluginController.getClientDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		init();
	}

	protected BaseClientTableHandler createClientTableHandler(UiGeneratorController ui,
			final PaymentViewPluginController pluginController) {
		return new ClientTableHandler(ui, pluginController, this);
	}
	
	public void init() {
		clientsTab = ui.loadComponentFromFile(getXMLFile(), this);
		clientTableHolder = ui.find(clientsTab, PNL_CLIENT_TABLE_HOLDER);
		clientTableHandler = createClientTableHandler(ui, pluginController);
		ui.add(clientTableHolder, clientTableHandler.getClientsTablePanel());
	}
	
	public BaseClientTableHandler getClientTableHandler() {
		return clientTableHandler;
	}

	protected String getXMLFile() {
		return XML_CLIENTS_TAB;
	}
	
	protected Object getClientTableHolder() {
		return clientTableHolder;
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
		Object[] selectedClients = clientTableHandler.getSelectedRows();
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
		for (Object selectedClient : clientTableHandler.getSelectedRows()) {
			Client c = (Client) ui.getAttachedObject(selectedClient);
			ui.add(new EditClientHandler(ui, c, pluginController, this).getDialog());
		}
	}

	public void exportClient() {
		exportClient(clientTableHandler.getClientFilter());
	}
	
	public void exportClient(String clientFilter) {
		Object[] selectedItems = clientTableHandler.getSelectedRows();
		if (selectedItems.length <= 0){
			if (clientFilter.isEmpty()){
				exportClients(clientDao.getAllActiveClients());
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
		Object[] selectedItems = clientTableHandler.getSelectedRows();
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
	
	/**
	 * Adds selected contacts to group.
	 * 
	 * @param item The item holding the destination group.
	 * @throws DuplicateKeyException 
	 */
	public void addToGroup(Object item) throws DuplicateKeyException {
		ContactDao contactDao = pluginController.getUiGeneratorController()
		.getFrontlineController().getContactDao();
		
		Object[] selected = null;
		selected = this.ui.getSelectedItems(clientTableHandler.getClientsTable());
		// Add to the selected groups...
		Group destination = this.ui.getGroup(item);
		// Let's check all the selected items.  Any that are groups should be added to!
		for (Object component : selected) {
			if (this.ui.isAttachment(component, Client.class)) {
				Client client = this.ui.getAttachedObject(component, Client.class);
				
				Contact contact = contactDao.getFromMsisdn(client.getPhoneNumber());
				if (contact == null){
					contact = new Contact(client.getFullName(), client.getPhoneNumber(), "", "", "", true);
					contactDao.saveContact(contact);
				}
				
				if(this.groupMembershipDao.addMember(destination, contact)) {
					groupDao.updateGroup(destination);
				}
			}
		}
	}
	
	/**
	 * Removes the contacts selected in the contacts list from the group which is selected in the groups tree.
	 * @param selectedGroup A set of thinlet components with group members attached to them.
	 * @throws DuplicateKeyException 
	 */
	public void removeFromGroup(Object selectedGroup) throws DuplicateKeyException {
		ContactDao contactDao = pluginController.getUiGeneratorController()
		.getFrontlineController().getContactDao();
		
		Object[] selectedClients = clientTableHandler.getSelectedRows();
		Group group = this.ui.getGroup(selectedGroup);
		
		for (Object selected : selectedClients) {
			Client c = this.ui.getAttachedObject(selected, Client.class);
			
			Contact contact = contactDao.getFromMsisdn(c.getPhoneNumber());
			if (contact == null){
				contact = new Contact(c.getFullName(), c.getPhoneNumber(), "", "", "", true);
				contactDao.saveContact(contact);
			}
			
			this.groupMembershipDao.removeMember(group, contact);
		}
		this.refresh();
	}
	
	public void populateGroups(Object popUp, Object list) throws DuplicateKeyException {
		FrontlineSMS frontlineController = pluginController.getUiGeneratorController()
				.getFrontlineController();
		groupDao = frontlineController.getGroupDao();
		groupMembershipDao = frontlineController.getGroupMembershipDao();
		Object[] selectedItems = this.ui.getSelectedItems(list);
		this.ui.setVisible(popUp, this.ui.getSelectedItems(list).length > 0);
		if (selectedItems.length == 0) {
			// Nothing selected
			boolean none = true;
			for (Object o : this.ui.getItems(popUp)) {
				if (this.ui.getName(o).equals(COMPONENT_NEW_GROUP)) {
					this.ui.setVisible(o, true);
					none = false;
				} else {
					this.ui.setVisible(o, false);
				}
			}
			this.ui.setVisible(popUp, !none);
		} else if (this.ui.getAttachedObject(selectedItems[0]) instanceof Client) {
			for (Object o : this.ui.getItems(popUp)) {
				String name = this.ui.getName(o);
				if (name.equals(COMPONENT_MENU_ITEM_MSG_HISTORY)
						|| name.equals(COMPONENT_MENU_ITEM_VIEW_CONTACT)) {
					this.ui.setVisible(o,
							this.ui.getSelectedItems(list).length == 1);
				} else if (!name.equals(COMPONENT_GROUPS_MENU)) {
					this.ui.setVisible(o, true);
				}
			}
			Object menu = this.ui.find(popUp, COMPONENT_GROUPS_MENU);
			this.ui.removeAll(menu);
			List<Group> allGroups = groupDao.getAllGroups();
			for (Group g : allGroups) {
				Object menuItem = Thinlet.create(Thinlet.MENUITEM);
				this.ui.setText(menuItem, g.getPath());
				this.ui.setIcon(menuItem, Icon.GROUP);
				this.ui.setAttachedObject(menuItem, g);
				this.ui.setAction(menuItem, "addToGroup(this)", menu, this);
				this.ui.add(menu, menuItem);
			}
			this.ui.setVisible(menu, allGroups.size() != 0);
			String menuName = InternationalisationUtils
					.getI18nString(ACTION_ADD_TO_GROUP);
			this.ui.setText(menu, menuName);

			Object menuRemove = this.ui.find(popUp, "groupsMenuRemove");
			if (menuRemove != null) {
				Client c = this.ui.getAttachedObject(this.ui.getSelectedItem(list), Client.class);
				this.ui.removeAll(menuRemove);
				
				ContactDao contactDao = frontlineController.getContactDao();
				
				Contact contact = contactDao.getFromMsisdn(c.getPhoneNumber());
				if (contact == null){
					contact = new Contact(c.getFullName(), c.getPhoneNumber(), "", "", "", true);
					contactDao.saveContact(contact);
				}
				
				List<Group> groups = groupMembershipDao.getGroups(contact);
				for (Group g : groups) {
					Object menuItem = Thinlet.create(Thinlet.MENUITEM);
					this.ui.setText(menuItem, g.getPath());
					this.ui.setIcon(menuItem, Icon.GROUP);
					this.ui.setAttachedObject(menuItem, g);
					this.ui.setAction(menuItem, "removeFromGroup(this)",
							menuRemove, this);
					this.ui.add(menuRemove, menuItem);
				}
				this.ui.setEnabled(menuRemove, groups.size() != 0);
			}
		}
	}
	
	public void importClient() {
		new ClientImportHandler(pluginController, this).showWizard();
		this.refresh();
	}
	
	public void viewIncomingPaymentByClient() {
		Object[] selectedClientRows = clientTableHandler.getSelectedRows();
		List<Client> selectedClientsList = null;
		if (selectedClientRows.length <= 0){
			if (clientTableHandler.getClientFilter().isEmpty()){
				selectedClientsList = this.clientDao.getAllActiveClients();
			} else {
				selectedClientsList = this.clientDao.getClientsByFilter(clientTableHandler.getClientFilter());
			}
		}else{
			selectedClientsList = new ArrayList<Client>(selectedClientRows.length);
			for (Object clientRow : selectedClientRows) {
				selectedClientsList.add(ui.getAttachedObject(clientRow, Client.class));
			}
		}
		
		incomingPaymentsDialog = new IncomingPaymentsDialogHandler(ui,pluginController, selectedClientsList).getDialog();
		ui.add(incomingPaymentsDialog);
	}
	
	public final void showDeleteConfirmationDialog(String methodToBeCalled){
		this.ui.showConfirmationDialog(methodToBeCalled, this, I18N_CONFIRM_DELETE_CLIENT);
	}
}
