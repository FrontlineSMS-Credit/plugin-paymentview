package org.creditsms.plugins.paymentview.ui.handler.export;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ExportClientsTabHandler extends BaseTabHandler{
	private static final String XML_EXPORT_CLIENTS_TAB = "/ui/plugins/paymentview/export/innertabs/tabexportclients.xml";
	private Object clientsTab;   
	
	public ExportClientsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {		
	}
  
	@Override
	protected Object initialiseTab() {
		clientsTab = ui.loadComponentFromFile(XML_EXPORT_CLIENTS_TAB, this);
		return clientsTab;
	}
}
