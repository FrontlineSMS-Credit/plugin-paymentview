package org.creditsms.plugins.paymentview.ui.handler.client;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.PaymentsImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.client.dialogs.CustomizeClientHandler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ClientsTabHandler extends BaseTabHandler{
//> STATIC CONSTANTS
	private static final String XML_CLIENTS_TAB = "/ui/plugins/paymentview/clients/clients.xml";
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clientList";
	
//> INSTANCE PROPERTIES	
	private ClientDao clientDao = DummyData.INSTANCE.getClientDao();
	
	
	public ClientsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {
		populateClientsTable();
	}

	@Override
	protected Object initialiseTab() {
		Object clientsTab = ui.loadComponentFromFile(XML_CLIENTS_TAB, this);
		return clientsTab;
	}

//> EVENTS...
	public void customizeClientDB(){
	}
	
	public void addClient(){		
		ui.add(new CustomizeClientHandler(ui).getDialog());
	}
	
	public void showImportWizard(String typeName){
		new PaymentsImportHandler(ui).showWizard();
	}
	
//> PRIVATE HELPER METHODS
	private void populateClientsTable() {
		Object table = find(COMPONENT_CLIENT_TABLE);
		List<Client> clients = clientDao.getAllClients();
		for(Client c : clients) {
			ui.add(table, createRow(c));
		}
	}

	private Object createRow(Client c) {
		Object row = ui.createTableRow();
		ui.add(row, ui.createTableCell(c.getName()));
		ui.add(row, ui.createTableCell(c.getPhoneNumber()));
		return row;
	}
}
