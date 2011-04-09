package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class SentPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_SENTPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sentpayments.xml";
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
	private Object sentPaymentsTab;
	private ClientDao clientDao = DummyData.INSTANCE.getClientDao(); 
	
	public SentPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {
		populateClientsTable();
	}

	@Override
	protected Object initialiseTab() {
		sentPaymentsTab = ui.loadComponentFromFile(XML_SENTPAYMENTS_TAB, this);
		return sentPaymentsTab;
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
		ui.setAttachedObject(row, c);
		
		ui.add(row, ui.createTableCell(c.getFirstName()));
		ui.add(row, ui.createTableCell(c.getPhoneNumber()));
		
		return row;
	}
}
