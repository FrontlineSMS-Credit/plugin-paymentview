package org.creditsms.plugins.paymentview.ui.handler.export;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ExportClientHistoryTabHandler extends BaseTabHandler{
	private static final String XML_EXPORT_CLIENT_HISTORY_TAB = "/ui/plugins/paymentview/export/innertabs/tabexportclienthistory.xml";
	private Object clientsHistoryTab; 
	
	public ExportClientHistoryTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {		
	}
  
	@Override
	protected Object initialiseTab() {
		clientsHistoryTab = ui.loadComponentFromFile(XML_EXPORT_CLIENT_HISTORY_TAB, this);
		return clientsHistoryTab;
	}
}