package org.creditsms.plugins.paymentview.ui.handler.tabexport;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs.ExportByClientXticsStep1Handler;

public class ExportClientsTabHandler extends BaseTabHandler implements
		PagedComponentItemProvider {
	private static final String ICONS_CHECKBOX_SELECTED_PNG = "/icons/checkbox-selected.png";
	private static final String ICONS_CHECKBOX_UNSELECTED_PNG = "/icons/checkbox-unselected.png";
	private static final String XML_EXPORT_CLIENTS_TAB = "/ui/plugins/paymentview/export/innertabs/tabexportclients.xml";
	private static final String COMPONENT_TABLE_CLIENTS = "tbl_clients";
	private static final String COMPONENT_PANEL_CLIENTS = "pnl_clients";
	private ClientDao clientDao;
	private Object clientsTab;

	private Object tblClients;
	private String clientFilter = "";
	private ComponentPagingHandler clientsTablePager;
	private Object pnlClients;

	public ExportClientsTabHandler(UiGeneratorController ui, ClientDao clientDao) {
		super(ui);
		this.clientDao = clientDao;
		init();
		refresh();
	}

	public void exportSelectedClients() {
		ui.add(new ExportByClientXticsStep1Handler(ui, clientDao).getDialog());
	}

	@Override 
	protected Object initialiseTab() {
		clientsTab = ui.loadComponentFromFile(XML_EXPORT_CLIENTS_TAB, this);
		
		pnlClients = ui.find(clientsTab, COMPONENT_PANEL_CLIENTS);
		tblClients = ui.find(clientsTab, COMPONENT_TABLE_CLIENTS);
		clientsTablePager = new ComponentPagingHandler(ui, this, tblClients);		
		
		this.ui.add(pnlClients, this.clientsTablePager.getPanel());		
		return clientsTab;
	}

	private PagedListDetails getClientListDetails(int startIndex, int limit) {
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

	private Object[] toThinletComponents(List<Client> clients) {
		Object[] components = new Object[clients.size()];
		for (int i = 0; i < components.length; i++) {
			Client c = clients.get(i);
			components[i] = getRow(c);
		}
		return components;
	}

	private Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		Object cell = ui.createTableCell("");
		ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
		ui.add(row, cell);

		ui.add(row,
				ui.createTableCell(client.getFirstName() + " "
						+ client.getOtherName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));
		String accountStr = "";
		for (Account a : client.getAccounts()) {
			accountStr += a.getAccountNumber() + " ";
		}
		ui.add(row, ui.createTableCell(accountStr));
		ui.setAttachedObject(row, client);
		return row;
	}

	public void refresh() {
		this.updateClientList();
	}

	public void updateClientList() {
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}
	
	public void selectAll(Object tbl_clients){
		Object[] rows = ui.getItems(tbl_clients);
		
		for (Object row : rows){
			Object cell = ui.getItem(row, 0);
			ui.remove(cell);
			Client attachedClient = (Client) ui.getAttachedObject(row);
			if (!attachedClient.isSelected()) {
				attachedClient.setSelected(true);
				ui.setIcon(cell, ICONS_CHECKBOX_SELECTED_PNG);
			}

			ui.add(row, cell, 0);
		}
	}

	public void selectUsers(Object tbl_clients) {
		Object row = ui.getSelectedItem(tbl_clients);

		// TODO: Only Working partially with single selection;
		//Algo is not worth cracking the head
		
		Object cell = ui.getItem(row, 0);
		ui.remove(cell);
		Client attachedClient = (Client) ui.getAttachedObject(row);
		if (attachedClient.isSelected()) {
			attachedClient.setSelected(false);
			ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
		} else {
			attachedClient.setSelected(true);
			ui.setIcon(cell, ICONS_CHECKBOX_SELECTED_PNG);
		}

		ui.add(row, cell, 0);
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.tblClients) {
			return getClientListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

}
