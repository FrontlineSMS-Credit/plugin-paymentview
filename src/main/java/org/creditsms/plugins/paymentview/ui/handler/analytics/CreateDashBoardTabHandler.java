package org.creditsms.plugins.paymentview.ui.handler.analytics;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class CreateDashBoardTabHandler implements ThinletUiEventHandler{
	private static final String XML_MAIN_CREATE_DASHBOARD_PANEL = "pnl_MainCreateDashboard";
	private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/createdashboard/stepcreatesettings.xml";
	private static final String XML_STEP_SELECT_CLIENTS  = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";
	
	private Object mainPanel;
	private UiGeneratorController ui;
	private Object stepsCreateSettings; 
	
	public CreateDashBoardTabHandler(UiGeneratorController ui, Object createDashboardTab) {
		this.ui = ui;
		mainPanel = ui.find(createDashboardTab, XML_MAIN_CREATE_DASHBOARD_PANEL);
		stepsCreateSettings = ui.loadComponentFromFile(XML_STEP_CREATE_SETTINGS, this);
		System.out.println("mainPanel>> " + mainPanel);
		System.out.println("mainPanel>> " + stepsCreateSettings); 
		ui.add(mainPanel, stepsCreateSettings);
	}	
	
	public Object getMainPanel (){
		return this.mainPanel;
	}
	
	public void refresh(){		
	}
	
	public void deinit(){
	}
}
