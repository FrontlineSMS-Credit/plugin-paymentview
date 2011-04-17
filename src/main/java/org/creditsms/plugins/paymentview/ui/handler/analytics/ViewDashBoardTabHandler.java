package org.creditsms.plugins.paymentview.ui.handler.analytics;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.analytics.dialogs.CreateAlertHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;

public class ViewDashBoardTabHandler extends BaseTabHandler {
	private static final String TAB_VIEW_DASHBOARD = "tab_viewDashBoard";
	private static final String XML_STEP_SELECT_CLIENTS = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";

	private Object viewDashboardTab;
	private Object currentPanel;

	public ViewDashBoardTabHandler(UiGeneratorController ui,
			Object tabAnalytics) {
		super(ui);
		viewDashboardTab = ui.find(tabAnalytics, TAB_VIEW_DASHBOARD);
		this.init();
	}

	public void refresh() {
		
	}

	public void deinit() {
	}
	 
	public void setCurrentStepPanel(Object panel){
		if (currentPanel != null)
			ui.remove(currentPanel);
		
		ui.add(viewDashboardTab, panel);
		currentPanel = panel;
		ViewDashBoardTabHandler.this.refresh();
	}
	
	public class StepSelectTargetSavingsHandler extends BasePanelHandler {
		private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectservice.xml";

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
		private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectclients.xml";
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
		private static final String XML_STEP_VIEW_CLIENTS = "/ui/plugins/paymentview/analytics/viewdashboard/stepviewclients.xml";

		protected StepCreateSettingsHandler(UiGeneratorController ui) {
			super(ui);
			this.loadPanel(XML_STEP_VIEW_CLIENTS);
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
		
		public void export() {
			new ClientExportHandler((UiGeneratorController) ui).showWizard();
		}
		
		public void createAlert() {
			ui.add(new CreateAlertHandler((UiGeneratorController)ui).getDialog());
		}
	}

	@Override
	protected Object initialiseTab() {
		// ui.add(createDashboardTab, stepCreateSettings.getPanelComponent());
		setCurrentStepPanel(new StepSelectTargetSavingsHandler(ui).getPanelComponent());
		return viewDashboardTab;
	}
}
