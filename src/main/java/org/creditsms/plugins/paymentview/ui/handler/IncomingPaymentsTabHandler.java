package org.creditsms.plugins.paymentview.ui.handler;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class IncomingPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_INCOMING_PAYMENTS_TAB = "/ui/plugins/paymentview/incomingpayments/tabincomingpayments.xml";
	private static final String COMPONENT_INCOMING_PAYMENTS_TABLE = "tbl_clients";
	private Object incomingPaymentsTab;
	private IncomingPaymentDao incomingPaymentDao = DummyData.INSTANCE.getIncomingPaymentDao();  
	
	public IncomingPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();		
	}

	@Override
	public void refresh() {	
		populateIncomingPaymentsTable();
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
	private void populateIncomingPaymentsTable() {
		Object table = find(COMPONENT_INCOMING_PAYMENTS_TABLE); 
		List<IncomingPayment> incomingPayments = incomingPaymentDao.getAllIncomingPayments();
		for(IncomingPayment i: incomingPayments) {
			ui.add(table, createRow(i));
		}
	}

	private Object createRow(IncomingPayment i) { 
		Object row = ui.createTableRow();
		ui.add(row, ui.createTableCell(i.getPaymentBy()));
		ui.add(row, ui.createTableCell(i.getPhoneNumber()));
		ui.add(row, ui.createTableCell(Long.toString(i.getAccount().getAccountNumber())));
		ui.add(row, ui.createTableCell(Float.toString(i.getAmountPaid())));		
		return row;
	}

}
