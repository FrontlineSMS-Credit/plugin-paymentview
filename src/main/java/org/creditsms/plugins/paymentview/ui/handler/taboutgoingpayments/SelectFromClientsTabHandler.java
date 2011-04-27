package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SendPaymentAuthDialogHandler;

public class SelectFromClientsTabHandler extends BaseTabHandler implements PagedComponentItemProvider {
	private static final String XML_SELECT_FROM_CLIENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/selectfromclients.xml";
	
	private static final String XML_EXPORT_CLIENTS_TAB = "/ui/plugins/paymentview/export/innertabs/tabexportclients.xml";
	private static final String COMPONENT_TABLE_CLIENTS = "tbl_clients";
	private static final String COMPONENT_PANEL_CLIENTS = "pnl_clients";
	private ClientDao clientDao;

	private Object tblClients;
	private String clientFilter = "";
	private ComponentPagingHandler clientsTablePager;
	private Object pnlClients;


	private Object schedulePaymentAuthDialog;
	private Object selectFromClientsTab;
	private Object sendPaymentAuthDialog;

	public SelectFromClientsTabHandler(UiGeneratorController ui,
			ClientDao clientDao) {
		super(ui);
		this.clientDao = clientDao;
		init();
		refresh();
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsTab = ui.loadComponentFromFile(
				XML_SELECT_FROM_CLIENTS_TAB, this);
		
		pnlClients = ui.find(selectFromClientsTab, COMPONENT_PANEL_CLIENTS);
		tblClients = ui.find(selectFromClientsTab, COMPONENT_TABLE_CLIENTS);
		clientsTablePager = new ComponentPagingHandler(ui, this, tblClients);		
		
		this.ui.add(pnlClients, this.clientsTablePager.getPanel());		
		
		return selectFromClientsTab;
	}

	private Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		Object cell = ui.createTableCell("");
		ui.setIcon(cell, "/icons/header/checkbox-unselected.png");
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

	public void selectUsers(Object tbl_clients) {
		Object row = ui.getSelectedItem(tbl_clients);

		// TODO: Only Working partially with single selection;
		//Algo is not worth cracking the head
		
		Object cell = ui.getItem(row, 0);
		ui.remove(cell);
		Client attachedClient = (Client) ui.getAttachedObject(row);
		if (attachedClient.isSelected()) {
			attachedClient.setSelected(false);
			ui.setIcon(cell, "/icons/header/checkbox-unselected.png");
		} else {
			attachedClient.setSelected(true);
			ui.setIcon(cell, "/icons/header/checkbox-selected.png");
		}

		ui.add(row, cell, 0);
	}
	
	public void refresh() {
		this.updateClientList();
	}

	public void updateClientList() {
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}
	
	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.tblClients) {
			return getClientListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
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


	public void showSchedulePaymentAuthDialog() {
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(schedulePaymentAuthDialog);
	}

	public void showSendPaymentAuthDialog() {
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(sendPaymentAuthDialog);
	}
}
