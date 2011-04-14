package org.creditsms.plugins.paymentview.ui.handler.analytics;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.analytics.CreateDashBoardTabHandler.StepSelectClientsHandler;

import net.frontlinesms.ui.FrontlineUI;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class CreateDashBoardTabHandler extends BaseTabHandler {
	private static final String TAB_CREATE_DASHBOARD = "tab_createDashboard";
	private static final String XML_STEP_SELECT_CLIENTS = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";

	private Object createDashboardTab;

	private Object currentPanel;

	public CreateDashBoardTabHandler(UiGeneratorController ui,
			Object tabAnalytics) {
		super(ui);
		createDashboardTab = ui.find(tabAnalytics, TAB_CREATE_DASHBOARD);
		this.init();
	}

	public void refresh() {
		
	}

	public void deinit() {
	}
	 
	public void setCurrentStepPanel(Object panel){
		if (currentPanel != null)
			ui.remove(currentPanel);
		
		ui.add(createDashboardTab, panel);
		currentPanel = panel;
		CreateDashBoardTabHandler.this.refresh();
	}
	
	public class StepSelectTargetSavingsHandler extends BasePanelHandler {
		private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/createdashboard/stepselecttargetsavings.xml";

		protected StepSelectTargetSavingsHandler(UiGeneratorController ui) {
			super(ui);
			this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
		}

		public Object getPanelComponent() {
			return super.getPanelComponent();
		} 

		public void next() {
			setCurrentStepPanel(new StepSelectClientsHandler((UiGeneratorController) ui).getPanelComponent());
		}
	}

	public class StepSelectClientsHandler extends BasePanelHandler {
		private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";
		private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
		private ClientDao clientDao = DummyData.INSTANCE.getClientDao(); 

		protected StepSelectClientsHandler(UiGeneratorController ui) {
			super(ui);
			this.loadPanel(XML_STEP_SELECT_CLIENT);
			populateClientsTable();
		}
 
		public Object getPanelComponent() {
			return super.getPanelComponent();
		} 
		
		public void previous() {
			setCurrentStepPanel(new StepSelectTargetSavingsHandler((UiGeneratorController) ui).getPanelComponent());
		}

		public void next() {
			setCurrentStepPanel(new StepCreateSettingsHandler((UiGeneratorController) ui).getPanelComponent());
		}		
		
		//> PRIVATE HELPER METHODS
		private void populateClientsTable() {
			Object table = find(COMPONENT_CLIENT_TABLE);
			List<Client> clients = clientDao.getAllClients(); 
			for(Client c : clients) { 
				ui.add(table, createRow(c));
			}
		}

		private Object createRow(Client c) {
			Object row = ui.createTableRow();
			ui.add(row, ui.createTableCell(c.getFirstName() + " "+ c.getOtherName()));
			ui.add(row, ui.createTableCell(c.getPhoneNumber()));
			String accountStr = "";
			for (Account a : c.getAccounts()) {
				accountStr += (Long.toString(a.getAccountNumber()) + ", ");
			}
			ui.add(row, ui.createTableCell(accountStr));
			
			return row;
		}
	}

	public class StepCreateSettingsHandler extends BasePanelHandler {
		private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/createdashboard/stepcreatesettings.xml";

		protected StepCreateSettingsHandler(UiGeneratorController ui) {
			super(ui);
			this.loadPanel(XML_STEP_CREATE_SETTINGS);
		}

		public Object getPanelComponent() {
			return super.getPanelComponent();
		}

		public void showDateSelecter(Object textField) {
			((UiGeneratorController) ui).showDateSelecter(textField);
		}

		public void previous() {
			setCurrentStepPanel(new StepSelectClientsHandler((UiGeneratorController) ui).getPanelComponent());
		}
		
		public void next() {
			setCurrentStepPanel(new StepReviewHandler((UiGeneratorController) ui).getPanelComponent());
		}
	}
	
	public class StepReviewHandler extends BasePanelHandler {
		private static final String XML_STEP_REVIEW = "/ui/plugins/paymentview/analytics/createdashboard/stepreview.xml";

		protected StepReviewHandler(UiGeneratorController ui) {
			super(ui);
			this.loadPanel(XML_STEP_REVIEW);
		}

		public Object getPanelComponent() {
			return super.getPanelComponent();
		}

		public void showDateSelecter(Object textField) {
			((UiGeneratorController) ui).showDateSelecter(textField);
		}

		public void create() {	
		}

		public void previous() {
			setCurrentStepPanel(new StepCreateSettingsHandler((UiGeneratorController) ui).getPanelComponent());
		}
	}

	@Override
	protected Object initialiseTab() {
		// ui.add(createDashboardTab, stepCreateSettings.getPanelComponent());
		setCurrentStepPanel(new StepSelectTargetSavingsHandler(ui).getPanelComponent());
		return createDashboardTab;
	}
}
