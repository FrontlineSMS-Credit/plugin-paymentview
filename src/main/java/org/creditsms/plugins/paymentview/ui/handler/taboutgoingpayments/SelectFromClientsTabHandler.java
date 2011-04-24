package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SendPaymentAuthDialogHandler;

public class SelectFromClientsTabHandler extends BaseTabHandler {
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
	private static final String XML_SELECT_FROM_CLIENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/selectfromclients.xml";
	ClientDao clientDao;

	private Object schedulePaymentAuthDialog;
	private Object selectFromClientsTab;
	private Object sendPaymentAuthDialog;

	public SelectFromClientsTabHandler(UiGeneratorController ui,
			ClientDao clientDao) {
		super(ui);
		this.clientDao = clientDao;
		init();
	}

	private Object createRow(Client c) {
		Object row = ui.createTableRow();
		Object cell = ui.createTableCell("");
		ui.setIcon(cell, "/icons/header/checkbox-unselected.png");
		ui.add(row, cell);
		ui.add(row, ui.createTableCell(c.getFirstName()));
		ui.add(row, ui.createTableCell(c.getPhoneNumber()));
		return row;
	}
	
	public void selectUser(Object tbl_clients) {
		Object selectedItem = ui.getSelectedItem(tbl_clients);		
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsTab = ui.loadComponentFromFile(
				XML_SELECT_FROM_CLIENTS_TAB, this);
		return selectFromClientsTab;
	}

	// > PRIVATE HELPER METHODS
	private void populateClientsTable() {
		Object table = find(COMPONENT_CLIENT_TABLE);
		List<Client> clients = clientDao.getAllClients();
		for (Client c : clients) {
			ui.add(table, createRow(c));
		}
	}

	@Override
	public void refresh() {
		populateClientsTable();
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
