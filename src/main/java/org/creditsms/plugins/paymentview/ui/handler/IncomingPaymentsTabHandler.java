package org.creditsms.plugins.paymentview.ui.handler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.IncomingPaymentsExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.IncomingPaymentsImportHandler;

public class IncomingPaymentsTabHandler extends BaseTabHandler implements PagedComponentItemProvider{ 
	private static final String XML_INCOMING_PAYMENTS_TAB = "/ui/plugins/paymentview/incomingpayments/tabincomingpayments.xml";
	private static final String COMPONENT_INCOMING_PAYMENTS_TABLE = "tbl_clients";
	private static final String COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE = "pnl_clients";
	private Object incomingPaymentsTab;
	private IncomingPaymentDao incomingPaymentDao;  
	private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	private SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
	private NumberFormat formatter = new DecimalFormat("#,000.00");
	
	private Object incomingPaymentsTableComponent;
	private Object pnlIncomingPaymentsTableComponent;
	private ComponentPagingHandler incomingPaymentsTablePager;
	private String incomingPaymentsFilter;
	 
	public IncomingPaymentsTabHandler(UiGeneratorController ui, IncomingPaymentDao incomingPaymentDao) {
		super(ui);	
		this.incomingPaymentDao = incomingPaymentDao;
		init();		
	}
	
	public void updateContactList() {
		this.incomingPaymentsTablePager.setCurrentPage(0);
		this.incomingPaymentsTablePager.refresh();
	}

	@Override
	public void refresh() {	
		populateIncomingPaymentsTable();
	}

	@Override
	protected Object initialiseTab() {
		incomingPaymentsTab = ui.loadComponentFromFile(XML_INCOMING_PAYMENTS_TAB, this);
		incomingPaymentsTableComponent = ui.find(incomingPaymentsTab, COMPONENT_INCOMING_PAYMENTS_TABLE);
		incomingPaymentsTablePager = new ComponentPagingHandler(ui, this, incomingPaymentsTableComponent);
		pnlIncomingPaymentsTableComponent = ui.find(incomingPaymentsTab, COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE);
		this.ui.add(pnlIncomingPaymentsTableComponent, this.incomingPaymentsTablePager.getPanel());
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
		Object table = this.incomingPaymentsTableComponent; 
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
		ui.add(row, ui.createTableCell(formatter.format(i.getAmountPaid())));
		ui.add(row, ui.createTableCell(df.format(i.getTimePaid())));
		ui.add(row, ui.createTableCell(tf.format(i.getTimePaid())));
		return row;
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.incomingPaymentsTableComponent) {
			return getIncomingPaymentsListDetails(startIndex, limit); 
		} else {
			throw new IllegalStateException();
		}
	}

	private PagedListDetails getIncomingPaymentsListDetails(int startIndex,
			int limit) {
		List<IncomingPayment> incomingPayments = null;
		if (this.incomingPaymentsFilter.equals("")) {
			incomingPayments = this.incomingPaymentDao.getAllIncomingPayments();
		} else {
			incomingPayments = this.incomingPaymentDao.getIncomingPaymentsByPhoneNo(this.incomingPaymentsFilter);
		}
		
		int totalItemCount = incomingPayments.size();
		Object[] listItems = toThinletComponents(incomingPayments);

		return new PagedListDetails(totalItemCount, listItems);
	}
	
	private Object[] toThinletComponents(List<IncomingPayment> incomingPayments) {
		Object[] components = new Object[incomingPayments.size()];
		for (int i = 0; i < components.length; i++) {
			IncomingPayment in = incomingPayments.get(i);
			components[i] = getRow(in);
		}
		return components;
	}

	public Object getRow(IncomingPayment incomingPayment) {
		Object row = ui.createTableRow(incomingPayment);

		ui.add(row,	ui.createTableCell(incomingPayment.getPaymentBy()));
		ui.add(row, ui.createTableCell(incomingPayment.getPhoneNumber()));
		//ui.add(row, ui.createTableCell(incomingPayment.getAccount()));
		return row;
	}
}
