package org.creditsms.plugins.paymentview.ui.handler.analytics;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class AnalyticsTabHandler extends BaseTabHandler{
	private static final String XML_ANALYTICS_TAB = "/ui/plugins/paymentview/analytics/tabanalytics.xml"; 
	private Object analyticsTab; 
	
	public AnalyticsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {		
	}

	@Override
	protected Object initialiseTab() {
		analyticsTab = ui.loadComponentFromFile(XML_ANALYTICS_TAB, this);
		return analyticsTab;
	}

	//> EVENTS...
	public void customizeClientDB(){		
	}
	
	public void addClient(){			
	}
	
	public void importClient(){		
	}	
}
