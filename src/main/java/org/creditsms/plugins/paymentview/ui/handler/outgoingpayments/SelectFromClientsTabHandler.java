package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SendPaymentAuthDialogHandler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
 
public class SelectFromClientsTabHandler extends BaseTabHandler{
	private static final String XML_SELECT_FROM_CLIENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/selectfromclients.xml";
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
	private Object selectFromClientsTab;  
	
	ClientDao clientDao = DummyData.INSTANCE.getClientDao();
	private Object schedulePaymentAuthDialog;
	private Object sendPaymentAuthDialog;
	
	
	public SelectFromClientsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {
		populateClientsTable();
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsTab = ui.loadComponentFromFile(XML_SELECT_FROM_CLIENTS_TAB, this);
		return selectFromClientsTab;
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
	
	public void showSendPaymentAuthDialog(){
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui).getDialog();
		ui.add(sendPaymentAuthDialog);
	}
	
	public void showSchedulePaymentAuthDialog(){
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui).getDialog();
		ui.add(schedulePaymentAuthDialog);
	}
}

