package org.creditsms.plugins.paymentview.ui.handler.client;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.client.dialogs.CustomizeClientDBHandler;
import org.creditsms.plugins.paymentview.ui.handler.client.dialogs.EditClientHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientImportHandler;

public class ClientsTabHandler extends BaseTabHandler implements
		PagedComponentItemProvider {
	// > STATIC CONSTANTS
	private static final String XML_CLIENTS_TAB = "/ui/plugins/paymentview/clients/clients.xml";
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clientList";
	private static final String COMPONENT_PANEL_CLIENT_LIST = "pnl_tbl_clientList";
	private Object clientsTableComponent;

	private ComponentPagingHandler clientsTablePager;

	// > INSTANCE PROPERTIES
	private ClientDao clientDao = DummyData.INSTANCE.getClientDao();
	private Object pnlClientsList;
	private String clientFilter;

	public ClientsTabHandler(UiGeneratorController ui) {
		super(ui);
		this.clientFilter = "";
		init();
	}

	// > PAGING METHODS
	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.clientsTableComponent) {
			return getContactListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	private PagedListDetails getContactListDetails(int startIndex, int limit) {
		List<Client> clients = null;
		if (this.clientFilter.equals("")) {
			clients = this.clientDao.getAllClients();
		} else {
			clients = this.clientDao.getClientsByName(clientFilter);
		}
		
		int totalItemCount = this.clientDao.getClientCount();
		Object[] listItems = toThinletComponents(clients);

		return new PagedListDetails(totalItemCount, listItems);

	}

	public void updateContactList() {
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}

	private Object[] toThinletComponents(List<Client> clients) {
		Object[] components = new Object[clients.size()];
		for (int i = 0; i < components.length; i++) {
			Client c = clients.get(i);
			components[i] = getRow(c);
		}
		return components;
	}

	public Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		ui.add(row,
				ui.createTableCell(client.getFirstName() + " "
						+ client.getOtherName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));
		String accountStr = "";
		for (Account a : client.getAccounts()) {
			accountStr += (Long.toString(a.getAccountNumber()) + ", ");
		}
		ui.add(row, ui.createTableCell(accountStr));
		return row;
	}

	@Override
	public void refresh() {
		this.updateContactList();
	}

	@Override
	protected Object initialiseTab() {
		Object clientsTab = ui.loadComponentFromFile(XML_CLIENTS_TAB, this);
		clientsTableComponent = ui.find(clientsTab, COMPONENT_CLIENT_TABLE);
		clientsTablePager = new ComponentPagingHandler(ui, this,
				clientsTableComponent);
		pnlClientsList = ui.find(clientsTab, COMPONENT_PANEL_CLIENT_LIST);
		this.ui.add(pnlClientsList, this.clientsTablePager.getPanel());
		return clientsTab;
	}

	// > EVENTS...
	public void customizeClientDB() {
		ui.add(new CustomizeClientDBHandler(ui).getDialog());
	}

	public void addClient() {
		ui.add(new EditClientHandler(ui).getDialog());
	}
	
	public void importClient() {
		new ClientImportHandler(ui, this).showWizard();
		this.refresh();
	}
	
	public void exportClient() {
		new ClientExportHandler(ui).showWizard();
		this.refresh();
	}

	public void editClient() {
		Object[] selectedClients = this.ui
				.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = (Client) ui.getAttachedObject(selectedClient);
			ui.add(new EditClientHandler(ui, c, clientDao).getDialog());
		}
	}

	public void deleteClient() {
		Object[] selectedClients = this.ui
				.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = (Client) ui.getAttachedObject(selectedClient);
			clientDao.deleteClient(c);
		}
		
		ui.removeDialog(ui.find(UiGeneratorControllerConstants.COMPONENT_CONFIRM_DIALOG));
		ui.infoMessage("You have succesfully deleted from the client!");
		this.refresh();
	}

	/**
	 * @param clientFilter
	 * the clientFilter to set
	 */
	public void setClientFilter(String clientFilter) {
		this.clientFilter = clientFilter;
		filterClients();
	}

	public void filterClients() {
		this.updateContactList();
	}

	/**
	 * @return the clientFilter
	 */
	public String getClientFilter() {
		return clientFilter;
	}
}
