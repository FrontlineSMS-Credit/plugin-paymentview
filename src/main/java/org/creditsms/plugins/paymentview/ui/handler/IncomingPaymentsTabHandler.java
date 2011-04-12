package org.creditsms.plugins.paymentview.ui.handler;

import java.text.SimpleDateFormat;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.IncomingPaymentsExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.IncomingPaymentsImportHandler;

public class IncomingPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_INCOMING_PAYMENTS_TAB = "/ui/plugins/paymentview/incomingpayments/tabincomingpayments.xml";
	private static final String COMPONENT_INCOMING_PAYMENTS_TABLE = "tbl_clients";
	private Object incomingPaymentsTab;
	private IncomingPaymentDao incomingPaymentDao = DummyData.INSTANCE.getIncomingPaymentDao();  
	SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
	
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
	public void exportPayments(){		
		new IncomingPaymentsExportHandler(ui).showWizard();
		this.refresh();
	}
	
	public void addClient(){			
	}
	
	public void importPayments(){
		new IncomingPaymentsImportHandler(ui).showWizard();
		this.refresh();
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
		ui.add(row, ui.createTableCell(i.getAmountPaid().toString()));		 
		ui.add(row, ui.createTableCell(df.format(i.getTimePaid())));
		ui.add(row, ui.createTableCell(tf.format(i.getTimePaid())));
		return row;
	}

}
