package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsImportHandler;

public class SentPaymentsTabHandler extends BaseTabHandler implements PagedComponentItemProvider {
	private static final String COMPONENT_SENT_PAYMENTS_TABLE = "tbl_clients";
	private static final String COMPONENT_PANEL_SENT_PAYMENTS_TABLE = "pnl_clients";
	private static final String XML_SENTPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sentpayments.xml";

	private AccountDao accountDao;
	private ClientDao clientDao;

	private Object sentPaymentsTableComponent;
	private ComponentPagingHandler sentPaymentsTablePager;
	private Object pnlSentPaymentsTableComponent;

	private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	private NumberFormat formatter = new DecimalFormat("#,000.00");
	private OutgoingPaymentDao outgoingPaymentDao;
	//
	// For Dummy Purposes
	Random random = new Random();
	//

	private Object sentPaymentsTab;

	SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");
	private String sentPaymentsFilter = "";

	public SentPaymentsTabHandler(UiGeneratorController ui,
			ClientDao clientDao, OutgoingPaymentDao outgoingPaymentDao,
			AccountDao accountDao) {
		super(ui);
		this.clientDao = clientDao;
		this.accountDao = accountDao;
		this.outgoingPaymentDao = outgoingPaymentDao;
		init();
	}

	private Object getRow(OutgoingPayment o) {
		Client c = clientDao.getAllClients().get(
				random.nextInt(clientDao.getAllClients().size()));
		Object row = ui.createTableRow();
		ui.add(row,
				ui.createTableCell(c.getFirstName() + " " + c.getOtherName()));
		ui.add(row, ui.createTableCell(o.getPhoneNumber()));
		ui.add(row, ui.createTableCell(formatter.format(o.getAmountPaid())));
		ui.add(row, ui.createTableCell(df.format(o.getTimePaid())));
		ui.add(row, ui.createTableCell(tf.format(o.getTimePaid())));
		ui.add(row, ui.createTableCell(Long.toString(o.getAccount()
				.getAccountNumber())));
		ui.add(row, ui.createTableCell(o.getNotes()));
		return row;
	}

	public void exportPayments() {
		new OutgoingPaymentsExportHandler(ui, outgoingPaymentDao).showWizard();
		this.refresh();
	}

	public void importPayments() {
		new OutgoingPaymentsImportHandler(ui, accountDao).showWizard();
		this.refresh();
	}

	@Override
	protected Object initialiseTab() {
		sentPaymentsTab = ui.loadComponentFromFile(XML_SENTPAYMENTS_TAB, this);
		
		sentPaymentsTableComponent = ui.find(sentPaymentsTab,
				COMPONENT_SENT_PAYMENTS_TABLE);
		sentPaymentsTablePager = new ComponentPagingHandler(ui, this,
				sentPaymentsTableComponent);
		pnlSentPaymentsTableComponent = ui.find(sentPaymentsTab,
				COMPONENT_PANEL_SENT_PAYMENTS_TABLE);
		
		this.ui.add(pnlSentPaymentsTableComponent,
				this.sentPaymentsTablePager.getPanel());
		
		return sentPaymentsTab;
	}

	private PagedListDetails getIncomingPaymentsListDetails(int startIndex,
			int limit) {
		List<OutgoingPayment> sentPayments = new ArrayList<OutgoingPayment>();

		if (this.sentPaymentsFilter.equals("")) {
			sentPayments = this.outgoingPaymentDao.getAllOutgoingPayments(
					startIndex, limit);
		} else {
			// TODO: change this to add more columns to be filtered.
			sentPayments = this.outgoingPaymentDao
					.getOutgoingPaymentsByPhoneNo(this.sentPaymentsFilter);
		}

		int totalItemCount = outgoingPaymentDao.getOutgoingPaymentsCount();
		Object[] listItems = toThinletComponents(sentPayments);

		return new PagedListDetails(totalItemCount, listItems);
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.sentPaymentsTableComponent) {
			return getIncomingPaymentsListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	private Object[] toThinletComponents(List<OutgoingPayment> outgoingPayments) {
		Object[] components = new Object[outgoingPayments.size()];
		for (int i = 0; i < components.length; i++) {
			OutgoingPayment out = outgoingPayments.get(i);
			components[i] = getRow(out);
		}
		return components;
	}

	@Override
	public void refresh() {
		this.sentPaymentsTablePager.setCurrentPage(0);
		this.sentPaymentsTablePager.refresh();
	}
}
