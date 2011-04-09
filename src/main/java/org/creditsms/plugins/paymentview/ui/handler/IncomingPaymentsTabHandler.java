package org.creditsms.plugins.paymentview.ui.handler;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class IncomingPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_INCOMING_PAYMENTS_TAB = "/ui/plugins/paymentview/incomingpayments/tabincomingpayments.xml";
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
	private Object incomingPaymentsTab;
	private ClientDao clientDao = DummyData.INSTANCE.getClientDao(); 
	
	public IncomingPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();		
	}

	@Override
	public void refresh() {	
		populateClientsTable();
	}

	@Override
	protected Object initialiseTab() {
		incomingPaymentsTab = ui.loadComponentFromFile(XML_INCOMING_PAYMENTS_TAB, this);
		return incomingPaymentsTab;
	}

//> EVENTS...
	public void customizeClientDB(){		
	}
	
	public void addClient(){			
	}
	
	public void importClient(){		
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
		ui.add(row, ui.createTableCell(c.getFirstName()));
		ui.add(row, ui.createTableCell(c.getPhoneNumber()));
		return row;
	}

}
