package org.creditsms.plugins.paymentview.ui.handler.tablog.dialogs;

import java.util.Date;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;
import org.creditsms.plugins.paymentview.utils.PvUtils;

public class LogViewDialog extends BaseDialog{

	private static final String FLD_MESSAGE = "fldMessage";
	private static final String FLD_DETAILS = "fldDetails";
	private static final String FLD_DATETIME = "fldDatetime";
	private static final String FLD_LEVEL = "fldLevel";
	private static final String XML_LOG_VIEW_DIALOG = "/ui/plugins/paymentview/log/dialogs/dlgViewLog.xml";

	public LogViewDialog(UiGeneratorController ui, LogMessage logMessage) {
		super(ui);
		init(logMessage);
	}

	private void init(LogMessage logMessage) {
		dialogComponent = ui.loadComponentFromFile(XML_LOG_VIEW_DIALOG);
		ui.setText(ui.find(dialogComponent, FLD_LEVEL), logMessage.getLogLevel().toString());
		ui.setText(ui.find(dialogComponent, FLD_DATETIME), PvUtils.formatDate(new Date(logMessage.getTimestamp())));
		ui.setText(ui.find(dialogComponent, FLD_DETAILS), logMessage.getLogTitle());
		ui.setText(ui.find(dialogComponent, FLD_MESSAGE), logMessage.getLogContent());
	}
	
	

}
