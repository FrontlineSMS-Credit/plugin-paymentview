package org.creditsms.plugins.paymentview.ui.handler.analytics;

import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class CreateDashBoardTabHandler extends BaseTabHandler{
	private static final String TAB_CREATE_DASHBOARD = "tab_createDashboard"; 
	private static final String XML_STEP_SELECT_CLIENTS  = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";
	
	private Object createDashboardTab;
	
	private StepCreateSettingsHandler stepsCreateSettings; 
	
	public CreateDashBoardTabHandler(UiGeneratorController ui, Object tabAnalytics) {
		super(ui);
		createDashboardTab = ui.find(tabAnalytics, TAB_CREATE_DASHBOARD);
		this.init();
	}
	
	public void refresh(){		
	}
	
	public void deinit(){
	}
	
	private class StepCreateSettingsHandler extends BasePanelHandler{
		private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/createdashboard/stepcreatesettings.xml";
		protected StepCreateSettingsHandler(UiGeneratorController ui) {
			super(ui);
			
			this.loadPanel(XML_STEP_CREATE_SETTINGS);
		}	
		
		public Object getPanelComponent() {
			return super.getPanelComponent();
		}		
	}

	@Override
	protected Object initialiseTab() {
		stepsCreateSettings = new StepCreateSettingsHandler(ui);
		
		ui.add(createDashboardTab, stepsCreateSettings.getPanelComponent());		
		return createDashboardTab;		
	}
}
