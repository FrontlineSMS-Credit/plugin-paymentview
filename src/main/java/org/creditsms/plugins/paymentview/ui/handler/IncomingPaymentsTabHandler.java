package org.creditsms.plugins.paymentview.ui.handler;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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

public class IncomingPaymentsTabHandler extends BaseTabHandler implements
		PagedComponentItemProvider {
	private static final String COMPONENT_INCOMING_PAYMENTS_TABLE = "tbl_clients";
	private static final String COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE = "pnl_clients";
	private static final String XML_INCOMING_PAYMENTS_TAB = "/ui/plugins/paymentview/incomingpayments/tabincomingpayments.xml";
	
	private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	private NumberFormat formatter = new DecimalFormat("#,000.00");
	private SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss a");
	private IncomingPaymentDao incomingPaymentDao;
	
	private String incomingPaymentsFilter = "";
	
	private Object incomingPaymentsTab;

	private Object incomingPaymentsTableComponent;
	private ComponentPagingHandler incomingPaymentsTablePager;
	private Object pnlIncomingPaymentsTableComponent;

	public IncomingPaymentsTabHandler(UiGeneratorController ui,
			IncomingPaymentDao incomingPaymentDao) {
		super(ui);
		this.incomingPaymentDao = incomingPaymentDao;
		init();
	}

	public void addClient() {
	}

	// > EVENTS...
	public void exportPayments() {
		new IncomingPaymentsExportHandler(ui, incomingPaymentDao).showWizard();
		this.refresh();
	}

	private PagedListDetails getIncomingPaymentsListDetails(int startIndex,
			int limit) {
		List<IncomingPayment> incomingPayments = new ArrayList<IncomingPayment>();
		
		if (this.incomingPaymentsFilter.equals("")) {
			incomingPayments = this.incomingPaymentDao.getAllIncomingPayments(startIndex, limit);
		} else {
			//TODO: change this to add more columns to be filtered.
			incomingPayments = this.incomingPaymentDao
					.getIncomingPaymentsByPhoneNo(this.incomingPaymentsFilter);
		}

		int totalItemCount = incomingPaymentDao.getIncomingPaymentsCount();
		Object[] listItems = toThinletComponents(incomingPayments);

		return new PagedListDetails(totalItemCount, listItems);
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.incomingPaymentsTableComponent) {
			return getIncomingPaymentsListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	public Object getRow(IncomingPayment incomingPayment) {
		Object row = ui.createTableRow(incomingPayment);

		ui.add(row, ui.createTableCell(incomingPayment.getPaymentBy()));
		ui.add(row, ui.createTableCell(incomingPayment.getPhoneNumber()));
		ui.add(row, ui.createTableCell(Long.toString(incomingPayment.getAccount().getAccountNumber())));
		return row;
	}

	public void importPayments() {
		new IncomingPaymentsImportHandler(ui).showWizard();
		this.refresh();
	}

	@Override
	protected Object initialiseTab() {
		incomingPaymentsTab = ui.loadComponentFromFile(
				XML_INCOMING_PAYMENTS_TAB, this);
		incomingPaymentsTableComponent = ui.find(incomingPaymentsTab,
				COMPONENT_INCOMING_PAYMENTS_TABLE);
		incomingPaymentsTablePager = new ComponentPagingHandler(ui, this,
				incomingPaymentsTableComponent);
		pnlIncomingPaymentsTableComponent = ui.find(incomingPaymentsTab,
				COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE);
		this.ui.add(pnlIncomingPaymentsTableComponent,
				this.incomingPaymentsTablePager.getPanel());
		return incomingPaymentsTab;
	}

	@Override
	public void refresh() {
		this.updateIncomingPaymentsList();
	}

	private Object[] toThinletComponents(List<IncomingPayment> incomingPayments) {
		Object[] components = new Object[incomingPayments.size()];
		for (int i = 0; i < components.length; i++) {
			IncomingPayment in = incomingPayments.get(i);
			components[i] = getRow(in);
		}
		return components;
	}

	public void updateIncomingPaymentsList() {
		this.incomingPaymentsTablePager.setCurrentPage(0);
		this.incomingPaymentsTablePager.refresh();
	}
}
