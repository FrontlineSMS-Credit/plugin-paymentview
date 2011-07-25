package org.creditsms.plugins.paymentview.ui.handler.tablog;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

public class LogTabHandler extends BaseTabHandler {
	private static final String COMPONENT_LOG_TABLE = "tbl_log";
	private static final String XML_LOG_TAB = "/ui/plugins/paymentview/log/logsTab.xml";
	private Object logsTableComponent;
	private Object logsTab;
	
	private final PaymentViewPluginController pluginController;
	
	public LogTabHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		
		//lastly
		init();
	}

	@Override
	protected Object initialiseTab() {
		logsTab = ui.loadComponentFromFile(XML_LOG_TAB, this);
		logsTableComponent = ui.find(logsTab, COMPONENT_LOG_TABLE);
		return logsTab;
	}
	
	@Override
	public void refresh() {
		
	}
}
