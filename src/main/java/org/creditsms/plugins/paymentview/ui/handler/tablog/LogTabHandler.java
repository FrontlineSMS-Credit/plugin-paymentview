package org.creditsms.plugins.paymentview.ui.handler.tablog;

import java.util.ArrayList;
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
import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.data.repository.LogMessageDao;

public class LogTabHandler extends BaseTabHandler implements
PagedComponentItemProvider, EventObserver{
	private static final String COMPONENT_LOGS_TABLE = "tbl_logs";
	private static final String COMPONENT_PANEL_LOGS_TABLE = "pnl_logs";
	private static final String XML_LOG_TAB = "/ui/plugins/paymentview/log/logsTab.xml";
	private Object logsTableComponent;
	private Object logsTab;
	private ComponentPagingHandler logsTablePager;
	private Object pnlLogsTableComponent;
	
	private final PaymentViewPluginController pluginController;
	private LogMessageDao logMessageDao;
	private String logMessagesFilter = "";

	
	public LogTabHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		this.logMessageDao = pluginController.getLogMessageDao();
		ui.getFrontlineController().getEventBus().registerObserver(this);
		
		//lastly
		init();
	}

	@Override
	protected Object initialiseTab() {
		logsTab = ui.loadComponentFromFile(XML_LOG_TAB, this);
		logsTableComponent = ui.find(logsTab, COMPONENT_LOGS_TABLE);
		logsTablePager = new ComponentPagingHandler(ui, this, logsTableComponent);
		pnlLogsTableComponent = ui.find(logsTab, COMPONENT_PANEL_LOGS_TABLE);
		this.ui.add(pnlLogsTableComponent, this.logsTablePager.getPanel());

		return logsTab;
	}
	
	@Override
	public void refresh() {
		this.updateLogMessagesList();
	}
	
	private void updateLogMessagesList() {
		this.logsTablePager.setCurrentPage(0);
		this.logsTablePager.refresh();
		
	}
	
	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.logsTableComponent) {
			return getLogsListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}


	private PagedListDetails getLogsListDetails(int startIndex, int limit) {
		List<LogMessage> logMessages = new ArrayList<LogMessage>();
		
		if (this.logMessagesFilter.equals("")) {
			logMessages = this.logMessageDao.getAllLogMessage(startIndex, limit);
		} else {
			//TODO: change this to add more columns to be filtered.
			//logMessages = this.logMessageDao.getActiveIncomingPaymentsByPhoneNo(this.incomingPaymentsFilter);
			logMessages = this.logMessageDao.getAllLogMessage(startIndex, limit);
		}

		int totalItemCount = logMessageDao.getLogMessageCount();
		Object[] listItems = toThinletComponents(logMessages);

		return new PagedListDetails(totalItemCount, listItems);
	}

	private Object[] toThinletComponents(List<LogMessage> logMessages) {
		Object[] components = new Object[logMessages.size()];
		for (int i = 0; i < components.length; i++) {
			LogMessage lm = logMessages.get(i);
			components[i] = getRow(lm);
		}
		return components;
	}

	private Object getRow(LogMessage logMessage) {
		Object row = ui.createTableRow(logMessage);

		ui.add(row, ui.createTableCell(logMessage.getLogLevel().toString()));
		ui.add(row, ui.createTableCell(logMessage.getLogTitle()));
		ui.add(row, ui.createTableCell(logMessage.getLogContent()));
		ui.add(row, ui.createTableCell(InternationalisationUtils.getDatetimeFormat().format(new Date(logMessage.getTimestamp()))));
		return row;
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
				if (entity instanceof LogMessage) {
					LogTabHandler.this.refresh();
				}
			}
		}.execute();
	}
}
