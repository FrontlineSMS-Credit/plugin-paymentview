package org.creditsms.plugins.paymentview.ui.handler;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.IncomingPaymentsExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.IncomingPaymentsImportHandler;

public class IncomingPaymentsTabHandler extends BaseTabHandler implements
		PagedComponentItemProvider, EventObserver{
	private static final String COMPONENT_INCOMING_PAYMENTS_TABLE = "tbl_clients";
	private static final String COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE = "pnl_clients";
	private static final String XML_INCOMING_PAYMENTS_TAB = "/ui/plugins/paymentview/incomingpayments/tabincomingpayments.xml";
	
	private IncomingPaymentDao incomingPaymentDao;
	
	private String incomingPaymentsFilter = "";
	private Object incomingPaymentsTab;

	private Object incomingPaymentsTableComponent;
	private ComponentPagingHandler incomingPaymentsTablePager;
	private Object pnlIncomingPaymentsTableComponent;

	public IncomingPaymentsTabHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui);
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		ui.getFrontlineController().getEventBus().registerObserver(this);
		init();
	}

	public void addClient() {
	}

	// > EVENTS...
	public void exportPayments() {
		new IncomingPaymentsExportHandler(ui, incomingPaymentDao).showWizard();
		this.refresh();
	}

	public Object getRow(IncomingPayment incomingPayment) {
		Object row = ui.createTableRow(incomingPayment);

		ui.add(row, ui.createTableCell(incomingPayment.getPaymentBy()));
		ui.add(row, ui.createTableCell(incomingPayment.getPhoneNumber()));
		ui.add(row, ui.createTableCell(getAccount(incomingPayment)));
		ui.add(row, ui.createTableCell(incomingPayment.getAmountPaid().toPlainString()));
		ui.add(row, ui.createTableCell(InternationalisationUtils.getDatetimeFormat().format(new Date(incomingPayment.getTimePaid()))));
		return row;
	}

	private String getAccount(IncomingPayment incomingPayment) {
		Account account = incomingPayment.getAccount();
		String accountNumber = account.getAccountNumber();
		return accountNumber;
	}

	public void importPayments() {
		new IncomingPaymentsImportHandler(ui).showWizard();
		this.refresh();
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

	@Override
	public void refresh() {
		this.updateIncomingPaymentsList();
	}
//>INCOMING PAYMENT EVENT HANDLER
	public void incomingPaymentReceived(IncomingPayment i) {
		
	}
	
//>PAGING METHODS
	private PagedListDetails getIncomingPaymentsListDetails(int startIndex,
			int limit) {
		List<IncomingPayment> incomingPayments = new ArrayList<IncomingPayment>();
		
		if (this.incomingPaymentsFilter.equals("")) {
			incomingPayments = this.incomingPaymentDao.getAllIncomingPayments(startIndex, limit);
		} else {
			//TODO: change this to add more columns to be filtered.
			incomingPayments = this.incomingPaymentDao.getIncomingPaymentsByPhoneNo(this.incomingPaymentsFilter);
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
//> INCOMING PAYMENT NOTIFICATION...
	@SuppressWarnings("rawtypes")
	public void notify(final FrontlineEventNotification notification) {
		new FrontlineUiUpateJob() {
			public void run() {
				if (!(notification instanceof DatabaseEntityNotification)) {
					return;
				}
		
				Object entity = ((DatabaseEntityNotification) notification).getDatabaseEntity();
				if (entity instanceof IncomingPayment) {
					IncomingPaymentsTabHandler.this.refresh();
				}
			}
		}.execute();
	}
}
