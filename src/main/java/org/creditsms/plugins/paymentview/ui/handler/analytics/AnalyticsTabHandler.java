package org.creditsms.plugins.paymentview.ui.handler.analytics;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class AnalyticsTabHandler implements ThinletUiEventHandler{
	private static final String XML_ANALYTICS_TAB = "/ui/plugins/paymentview/analytics/tabanalytics.xml";
	
	private Object analyticsTab;
	private CreateDashBoardTabHandler createDashBoardHandler;
	private UiGeneratorController ui; 
	
	public AnalyticsTabHandler(UiGeneratorController ui) {
		this.ui = ui; 
		init();
	}
		
	public void refresh() {
	}

	
	protected Object init() {
		analyticsTab = ui.loadComponentFromFile(XML_ANALYTICS_TAB, this);
		createDashBoardHandler = new CreateDashBoardTabHandler(ui, this.analyticsTab);
		
		ui.add(analyticsTab, createDashBoardHandler.getMainPanel());
		return analyticsTab;
	}

	public Object getTab(){
		return analyticsTab;
	}

	//> EVENTS...

}
