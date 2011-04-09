package org.creditsms.plugins.paymentview.ui.handler.client;

import java.awt.Component;
import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.PaymentsImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.client.dialogs.CustomizeClientHandler;

import thinlet.Thinlet;

import net.frontlinesms.data.Order;
import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.data.domain.Group;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

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
	

	public ClientsTabHandler(UiGeneratorController ui) {
		super(ui);
		init();
	}

	// > PAGING METHODS
	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.clientsTableComponent) {
			return getContactListDetails(startIndex, limit);
		} else
			throw new IllegalStateException();
	}

	private PagedListDetails getContactListDetails(int startIndex, int limit) {
		int totalItemCount = this.clientDao.getClientCount();
		List<Client> clients = this.clientDao.getAllClients();
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
	}

	public void addClient() {
		ui.add(new CustomizeClientHandler(ui).getDialog());
	}

	public void showImportWizard(String typeName) {
		new PaymentsImportHandler(ui).showWizard();
	}

	public void editClient() {
		Object[] selectedClients = this.ui
				.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = (Client) ui.getAttachedObject(selectedClient);
			ui.add(new CustomizeClientHandler(ui, c, clientDao).getDialog());
		}
	}

	public void deleteClient() {
		Object[] selectedClients = this.ui
				.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = (Client) ui.getAttachedObject(selectedClient);
			clientDao.deleteClient(c);
		}
		this.refresh();
	}

}
