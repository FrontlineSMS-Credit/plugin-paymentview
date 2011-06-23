package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.data.events.EntityUpdatedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsImportHandler;

public class SentPaymentsTabHandler extends BaseTabHandler implements PagedComponentItemProvider, EventObserver {
	private static final String COMPONENT_SENT_PAYMENTS_TABLE = "tbl_clients";
	private static final String COMPONENT_PANEL_SENT_PAYMENTS_TABLE = "pnl_clients";
	private static final String XML_SENTPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sentpayments.xml";
	
	//KIM
	private static final String TAB_SENTPAYMENTS = "tab_sentOutgoingPayments";
	private Object sentPaymentsTab;

	private AccountDao accountDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private ClientDao clientDao;

	private Object sentPaymentsTableComponent;
	private ComponentPagingHandler sentPaymentsTablePager;
	private Object pnlSentPaymentsTableComponent;

	private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	private NumberFormat formatter = new DecimalFormat("#,000.00");
	SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss a");
	
	private Object sentPaymentsPanel;
	private String sentPaymentsFilter = "";


	public SentPaymentsTabHandler(UiGeneratorController ui, Object tabOutgoingPayments, PaymentViewPluginController pluginController) {
		super(ui);
		accountDao = pluginController.getAccountDao();
		outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		clientDao = pluginController.getClientDao();
		sentPaymentsTab = ui.find(tabOutgoingPayments, TAB_SENTPAYMENTS);//KIM
		
		ui.getFrontlineController().getEventBus().registerObserver(this);
		
		init();
	}

	private Object getRow(OutgoingPayment outgoingPayment) {
		Object row = ui.createTableRow();
		//TO DO
	//	ui.add(row,	ui.createTableCell(o.getPaymentTo()));
		Client client = clientDao.getClientByPhoneNumber(outgoingPayment.getPhoneNumber());
		
		ui.add(row, ui.createTableCell(client.getFullName()));
		ui.add(row, ui.createTableCell(outgoingPayment.getPhoneNumber()));
		ui.add(row, ui.createTableCell(formatter.format(outgoingPayment.getAmountPaid())));
		ui.add(row, ui.createTableCell(df.format(new Date(outgoingPayment.getTimePaid()))));
		ui.add(row, ui.createTableCell(tf.format(new Date(outgoingPayment.getTimePaid()))));
		ui.add(row, ui.createTableCell(outgoingPayment.getStatus().toString()));
		ui.add(row, ui.createTableCell(outgoingPayment.getConfirmationCode()));
		ui.add(row, ui.createTableCell(outgoingPayment.getPaymentId()));
		ui.add(row, ui.createTableCell(outgoingPayment.getNotes()));
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

	/* (non-Javadoc)
	 * @see net.frontlinesms.ui.handler.BaseTabHandler#initialiseTab()
	 */
	@Override
	protected Object initialiseTab() {
		sentPaymentsPanel = ui.loadComponentFromFile(XML_SENTPAYMENTS_TAB, this);
		
		sentPaymentsTableComponent = ui.find(sentPaymentsPanel, COMPONENT_SENT_PAYMENTS_TABLE);
		sentPaymentsTablePager = new ComponentPagingHandler(ui, this, sentPaymentsTableComponent);
		pnlSentPaymentsTableComponent = ui.find(sentPaymentsPanel, COMPONENT_PANEL_SENT_PAYMENTS_TABLE);
		
		this.ui.add(pnlSentPaymentsTableComponent, this.sentPaymentsTablePager.getPanel());
		this.ui.add(sentPaymentsTab, sentPaymentsPanel);
		return sentPaymentsTab;
	}

	private PagedListDetails getIncomingPaymentsListDetails(int startIndex,
			int limit) {
		List<OutgoingPayment> sentPayments = new ArrayList<OutgoingPayment>();

		if (this.sentPaymentsFilter.equals("")) {
			sentPayments = this.outgoingPaymentDao.getAllOutgoingPayments( startIndex, limit);
		} else {
			// TODO: change this to add more columns to be filtered.
			sentPayments = this.outgoingPaymentDao .getOutgoingPaymentsByPhoneNo(this.sentPaymentsFilter);
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

	public void notify(FrontlineEventNotification notification) {
		if (!(notification instanceof DatabaseEntityNotification)) {
			return;
		} else {
			Object entity;
			if (notification instanceof DatabaseEntityNotification){
				entity = ((DatabaseEntityNotification) notification).getDatabaseEntity();
				this.refresh();
			}
		}
	}
}
