package org.creditsms.plugins.paymentview.data.importexport;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_LOG_DESCRIPTION;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_LOG_LEVEL;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_LOG_MESSAGE;
import static org.creditsms.plugins.paymentview.utils.PaymentPluginConstants.COMMON_LOG_TIME_DATE;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.csv.CsvUtils;
import net.frontlinesms.csv.Utf8FileWriter;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.LogMessage;

public class LogsViewCsvExporter extends net.frontlinesms.csv.CsvExporter {

	public static void exportLogs(File exportFile,
			List<LogMessage> logMessageList,
			CsvRowFormat logFormat) throws IOException {
		LOG.trace("ENTER");
		LOG.debug("Target format [" + logFormat + "]");
		LOG.debug("Filename [" + exportFile.getAbsolutePath() + "]");

		Utf8FileWriter out = null;

		try {
			out = new Utf8FileWriter(exportFile);
			CsvUtils
					.writeLine(out, logFormat,
							PaymentViewCsvUtils.MARKER_LEVEL,
							InternationalisationUtils.getI18nString(COMMON_LOG_LEVEL),
							PaymentViewCsvUtils.MARKER_DESCRIPTION,
							InternationalisationUtils.getI18nString(COMMON_LOG_DESCRIPTION),
							PaymentViewCsvUtils.MARKER_LOG_MESSAGE,
							InternationalisationUtils.getI18nString(COMMON_LOG_MESSAGE),
							PaymentViewCsvUtils.MARKER_LOG_DATE_TIME,
							InternationalisationUtils.getI18nString(COMMON_LOG_TIME_DATE));
			
			
			for (LogMessage logMessage : logMessageList) {
				CsvUtils.writeLine(out, logFormat,
						PaymentViewCsvUtils.MARKER_LEVEL,
						logMessage.getLogLevel().toString(),
						PaymentViewCsvUtils.MARKER_DESCRIPTION,
						logMessage.getLogTitle(),
						PaymentViewCsvUtils.MARKER_LOG_MESSAGE,
						logMessage.getLogContent(),
						PaymentViewCsvUtils.MARKER_LOG_DATE_TIME,
						InternationalisationUtils.getDatetimeFormat().format(new Date(logMessage.getTimestamp())));
			}
		} finally {
			if (out != null)
				out.close();
			LOG.trace("EXIT");
		}
	}
}
