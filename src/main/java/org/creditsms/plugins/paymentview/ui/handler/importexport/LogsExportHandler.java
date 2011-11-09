package org.creditsms.plugins.paymentview.ui.handler.importexport;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.importexport.ExportDialogHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.data.importexport.LogsViewCsvExporter;
import org.creditsms.plugins.paymentview.data.repository.LogMessageDao;

public class LogsExportHandler extends ExportDialogHandler<LogMessage> {

	private static final String COMPONENT_CB_LEVEL = "cbLevel";
	private static final String COMPONENT_CB_DESCRIPTION = "cbDescription";
	private static final String COMPONENT_CB_START_MESSAGE = "cbMessage";
	private static final String COMPONENT_CB_DATE_TIME = "cbDateTime";
	
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_OPTIONS_PANEL_LOGS = "/ui/plugins/paymentview/importexport/pnlogsExportDetails.xml";
	
	private LogMessageDao logMessageDao;
	private List<LogMessage> logMessageList;

	public LogsExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(LogMessage.class, ui);
		this.logMessageDao = pluginController.getLogMessageDao();
	}
	
	public LogsExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, List<LogMessage> selected) {
		super(LogMessage.class, ui);
		this.logMessageDao = pluginController.getLogMessageDao();
		this.logMessageList = selected;
	}
	

	@Override
	protected void doSpecialExport(String dataPath) throws IOException {
		if (logMessageList == null) {
			exportLogMessage(this.logMessageDao.getAllLogMessage(), dataPath);
		}else{
			exportLogMessage(logMessageList, dataPath);
		}
	}

	@Override
	protected void doSpecialExport(String dataPath, List<LogMessage> logMessageList)
			throws IOException {
		exportLogMessage(logMessageList, dataPath);
	}
	
	private void exportLogMessage(List<LogMessage> allLogMessage,
			String dataPath) throws IOException {
		CsvRowFormat rowFormat = getRowFormatForTarget();

		if (!rowFormat.hasMarkers()) {
			uiController.alert(InternationalisationUtils
					.getI18nString(MESSAGE_NO_FIELD_SELECTED));
			log.trace("EXIT");
			return;
		}

		log.debug("Row Format [" + rowFormat + "]");

		LogsViewCsvExporter.exportLogs(new File(dataPath),
				allLogMessage, rowFormat);
		uiController.setStatus(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
	}
	
	private CsvRowFormat getRowFormatForTarget() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_LEVEL,
				COMPONENT_CB_LEVEL);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_DESCRIPTION,
				COMPONENT_CB_DESCRIPTION);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_LOG_MESSAGE,
				COMPONENT_CB_START_MESSAGE);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_LOG_DATE_TIME,
				COMPONENT_CB_DATE_TIME);
		
		return rowFormat;
	}
	
	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_EXPORTING_SELECTED_CONTACTS;
	}
	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_LOGS;
	}
}
