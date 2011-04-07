package org.creditsms.plugins.paymentview.ui.handler.analytics;

import org.creditsms.plugins.paymentview.ui.handler.analytics.CreateDashBoardTabHandler.StepSelectClientsHandler;

import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class CreateDashBoardTabHandler extends BaseTabHandler{
	private static final String TAB_CREATE_DASHBOARD = "tab_createDashboard"; 
	private static final String XML_STEP_SELECT_CLIENTS  = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";
	
	private Object createDashboardTab;
	
	private StepCreateSettingsHandler stepCreateSettings;
	private StepSelectClientsHandler stepSelectClients; 
	
	public CreateDashBoardTabHandler(UiGeneratorController ui, Object tabAnalytics) {
		super(ui);
		createDashboardTab = ui.find(tabAnalytics, TAB_CREATE_DASHBOARD);
		this.init();
	}
	
	public void refresh(){		
	}
	
	public void deinit(){
	}
	
	public class StepCreateSettingsHandler extends BasePanelHandler{
		private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/createdashboard/stepcreatesettings.xml";
		protected StepCreateSettingsHandler(UiGeneratorController ui) {
			super(ui);			
			this.loadPanel(XML_STEP_CREATE_SETTINGS);
		}	
		
		public Object getPanelComponent() {
			return super.getPanelComponent();
		}		
		
		public void showDateSelecter(Object textField) {
			((UiGeneratorController)ui).showDateSelecter(textField);
		}
	}
	
	public class StepSelectClientsHandler extends BasePanelHandler{
		private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";
		protected StepSelectClientsHandler(UiGeneratorController ui) {
			super(ui);			
			this.loadPanel(XML_STEP_SELECT_CLIENT);
		}	
		
		public Object getPanelComponent() {
			return super.getPanelComponent();
		}	
						
	}


	@Override
	protected Object initialiseTab() { 
		stepCreateSettings = new StepCreateSettingsHandler(ui);
		stepSelectClients = new StepSelectClientsHandler(ui);
		
		//ui.add(createDashboardTab, stepCreateSettings.getPanelComponent());
		ui.add(createDashboardTab, stepSelectClients.getPanelComponent());
		return createDashboardTab;		
	}
}
