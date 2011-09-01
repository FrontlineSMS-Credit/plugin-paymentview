package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.events.DatabaseEntityNotification;
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
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsExportHandler;

public class SentPaymentsTabHandler extends BaseTabHandler implements PagedComponentItemProvider, EventObserver {
	private static final String COMPONENT_SENT_PAYMENTS_TABLE = "tbl_clients";
	private static final String COMPONENT_PANEL_SENT_PAYMENTS_TABLE = "pnl_clients";
	private static final String XML_SENTPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sentpayments.xml";
	
	private static final String TAB_SENTPAYMENTS = "tab_sentOutgoingPayments";
	private Object sentPaymentsTab;

	private OutgoingPaymentDao outgoingPaymentDao;

	private Object sentPaymentsTableComponent;
	private Object sentPaymentsPanel;
	private ComponentPagingHandler sentPaymentsTablePager;
	private Object pnlSentPaymentsTableComponent;

	private SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	private NumberFormat formatter = new DecimalFormat("#,000.00");
	SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss a");
	
	private int totalItemCount;
	private Object fldStartDate;
	private Object fldEndDate;
	private Date startDate;
	private Date endDate;
	private PaymentViewPluginController pluginController;

	public SentPaymentsTabHandler(UiGeneratorController ui, Object tabOutgoingPayments, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		sentPaymentsTab = ui.find(tabOutgoingPayments, TAB_SENTPAYMENTS);
		ui.getFrontlineController().getEventBus().registerObserver(this);
		init();
	}

	private Object getRow(OutgoingPayment outgoingPayment) {
		Object row = ui.createTableRow(outgoingPayment);
		ui.add(row, ui.createTableCell(outgoingPayment.getClient().getFullName()));
		ui.add(row, ui.createTableCell(outgoingPayment.getClient().getPhoneNumber()));
		ui.add(row, ui.createTableCell(formatter.format(outgoingPayment.getAmountPaid())));
		ui.add(row, ui.createTableCell(df.format(new Date(outgoingPayment.getTimePaid()))));
		ui.add(row, ui.createTableCell(tf.format(new Date(outgoingPayment.getTimePaid()))));
		ui.add(row, ui.createTableCell(outgoingPayment.getStatus().toString()));
		ui.add(row, ui.createTableCell(outgoingPayment.getConfirmationCode()));
		ui.add(row, ui.createTableCell(outgoingPayment.getPaymentId()));
		ui.add(row, ui.createTableCell(outgoingPayment.getNotes()));
		return row;
	}

	/* (non-Javadoc)
	 * @see net.frontlinesms.ui.handler.BaseTabHandler#initialiseTab()
	 */
	@Override
	protected Object initialiseTab() {
		sentPaymentsPanel = ui.loadComponentFromFile(XML_SENTPAYMENTS_TAB, this);
		fldStartDate = ui.find(sentPaymentsPanel, "txt_startDate");
		fldEndDate = ui.find(sentPaymentsPanel, "txt_endDate");
		sentPaymentsTableComponent = ui.find(sentPaymentsPanel, COMPONENT_SENT_PAYMENTS_TABLE);
		sentPaymentsTablePager = new ComponentPagingHandler(ui, this, sentPaymentsTableComponent);
		pnlSentPaymentsTableComponent = ui.find(sentPaymentsPanel, COMPONENT_PANEL_SENT_PAYMENTS_TABLE);
		
		this.ui.add(pnlSentPaymentsTableComponent, this.sentPaymentsTablePager.getPanel());
		this.ui.add(sentPaymentsTab, sentPaymentsPanel);
		return sentPaymentsTab;
	}

	public PagedListDetails getListDetails(Object list, int startIndex,	int limit) {
		if (list == this.sentPaymentsTableComponent) {
			return getSentPaymentsListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}
	
	private PagedListDetails getSentPaymentsListDetails(int startIndex,	int limit) {
		List<OutgoingPayment> sentPayments = new ArrayList<OutgoingPayment>();
		sentPayments = getSentPaymentsForUI(startIndex, limit);
		Object[] listItems = toThinletComponents(sentPayments);
		return new PagedListDetails(totalItemCount, listItems);
	}

	private List<OutgoingPayment> getSentPaymentsForUI(int startIndex, int limit) {
		List<OutgoingPayment> sentPayments ;
		String strStartDate = ui.getText(fldStartDate);
		String strEndDate = ui.getText(fldEndDate);
		
		if (!strStartDate.equals("")){
			try {
				startDate = InternationalisationUtils.getDateFormat().parse(strStartDate);
			} catch (ParseException e) {
				ui.infoMessage("Please enter a correct starting date.");
				return Collections.emptyList();
			}
		}

		if (!strEndDate.equals("")){
			try {
				endDate = InternationalisationUtils.getDateFormat().parse(strEndDate);
			} catch (ParseException e) {
				ui.infoMessage("Please enter a correct ending date.");
				return Collections.emptyList();
			}
		}
		
		if (strStartDate.equals("") && strEndDate.equals("")) {
			totalItemCount = this.outgoingPaymentDao.getOutgoingPaymentsCount();
			sentPayments = this.outgoingPaymentDao.getAllOutgoingPayments(startIndex, limit);
		} else {
			if (strStartDate.equals("") && endDate != null){
				totalItemCount = this.outgoingPaymentDao.getOutgoingPaymentsByEndDate(endDate).size();
				sentPayments = this.outgoingPaymentDao.getOutgoingPaymentsByEndDate(endDate, startIndex, limit);
				
			} else {
				if (strEndDate.equals("") && startDate != null){
					totalItemCount = this.outgoingPaymentDao.getOutgoingPaymentsByStartDate(startDate).size();
					sentPayments = this.outgoingPaymentDao.getOutgoingPaymentsByStartDate(startDate, startIndex, limit);
				} else {
					totalItemCount = this.outgoingPaymentDao.getOutgoingPaymentsByDateRange(startDate, endDate).size();
					sentPayments = this.outgoingPaymentDao.getOutgoingPaymentsByDateRange(startDate, endDate, startIndex, limit);
				}
			}
		}
		return sentPayments;
	}

	private Object[] toThinletComponents(List<OutgoingPayment> outgoingPayments) {
		Object[] components = new Object[outgoingPayments.size()];
		for (int i = 0; i < components.length; i++) {
			OutgoingPayment out = outgoingPayments.get(i);
			components[i] = getRow(out);
		}
		return components;
	}

	// > EXPORTS...
	public void exportSentPayments() {
		Object[] selectedItems = ui.getSelectedItems(sentPaymentsTableComponent);
		if (selectedItems.length <= 0){
			exportSentPayments(getOutgoingPaymentsForExport());
		}else{
			List<OutgoingPayment> outgoingPayments = new ArrayList<OutgoingPayment>(selectedItems.length);
			for (Object o : selectedItems) {
				outgoingPayments.add(ui.getAttachedObject(o, OutgoingPayment.class));
			}
			exportSentPayments(outgoingPayments);
		}
	}
	
	private List<OutgoingPayment> getOutgoingPaymentsForExport() {
		List<OutgoingPayment> outgoingPayments;
		String strStartDate = ui.getText(fldStartDate);
		String strEndDate = ui.getText(fldEndDate);
		try {
			startDate = InternationalisationUtils.getDateFormat().parse(strStartDate);
		} catch (ParseException e) {
		}
		try {
			endDate = InternationalisationUtils.getDateFormat().parse(strEndDate);
		} catch (ParseException e) {
		}
			
		if (strStartDate.equals("") && strEndDate.equals("")) {
			outgoingPayments = this.outgoingPaymentDao.getAllOutgoingPayments();
		} else {
			if (strStartDate.equals("")){
				outgoingPayments = this.outgoingPaymentDao.getOutgoingPaymentsByEndDate(endDate);
				
			} else {
				if (strEndDate.equals("")){
					outgoingPayments = this.outgoingPaymentDao.getOutgoingPaymentsByStartDate(startDate);
				} else {
					outgoingPayments = this.outgoingPaymentDao.getOutgoingPaymentsByDateRange(startDate, endDate);
				}
			}
		}
		return outgoingPayments;
	}

	public void exportSentPayments(List<OutgoingPayment> outgoingPayments) {
		new OutgoingPaymentsExportHandler(ui, pluginController, outgoingPayments).showWizard();
		this.refresh();
	}
	
	@Override
	public void refresh() {
		this.sentPaymentsTablePager.setCurrentPage(0);
		this.sentPaymentsTablePager.refresh();
	}
	
	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}

	public void notify(final FrontlineEventNotification notification) {
		new FrontlineUiUpateJob() {
			public void run() {
				if (!(notification instanceof DatabaseEntityNotification)) {
					return;
				} else {
					if (notification instanceof DatabaseEntityNotification){
						Object entity = ((DatabaseEntityNotification) notification).getDatabaseEntity();
						if (entity instanceof OutgoingPayment || entity instanceof Client) {
							SentPaymentsTabHandler.this.refresh();
						}
					}
				}
			}
		}.execute();
	}
}
