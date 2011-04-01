package org.creditsms.plugins.paymentview.ui.handler.export;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ExportTabHandler extends BaseTabHandler{
	private static final String XML_EXPORT_TAB = "/ui/plugins/paymentview/export/tabexport.xml";
	private Object exportTab;
		
	public ExportTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {		
	}

	@Override
	protected Object initialiseTab() {
		exportTab = ui.loadComponentFromFile(XML_EXPORT_TAB, this);
		return exportTab;
	}

	//> EVENTS...
	public void customizeClientDB(){		
	}
	
	public void addClient(){			
	}
	
	public void importClient(){		
	}	

}
