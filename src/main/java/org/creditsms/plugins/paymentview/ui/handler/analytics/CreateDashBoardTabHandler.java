package org.creditsms.plugins.paymentview.ui.handler.analytics;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.analytics.dialogs.CreateNewTargetHandler;

public class CreateDashBoardTabHandler extends BaseTabHandler {
	
	private static final String TAB_CREATE_DASHBOARD = "tab_createDashboard";

	private static final String XML_STEP_SELECT_CLIENTS = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";

	private ClientDao clientDao;

	private Object createDashboardTab;

	private Object currentPanel;

	public CreateDashBoardTabHandler(UiGeneratorController ui,
			Object tabAnalytics, ClientDao clientDao) {
		super(ui);
		this.clientDao = clientDao;
		createDashboardTab = ui.find(tabAnalytics, TAB_CREATE_DASHBOARD);
		this.init();
	}

	public void deinit() {
	}

	@Override
	protected Object initialiseTab() {
		// ui.add(createDashboardTab, stepCreateSettings.getPanelComponent());
		setCurrentStepPanel(new StepSelectTargetSavingsHandler(ui)
				.getPanelComponent());
		return createDashboardTab;
	}

	public void refresh() {

	}

	public void setCurrentStepPanel(Object panel) {
		if (currentPanel != null)
			ui.remove(currentPanel);

		ui.add(createDashboardTab, panel);
		currentPanel = panel;
		CreateDashBoardTabHandler.this.refresh();
	}
	
	public class StepCreateSettingsHandler extends BasePanelHandler {
		private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/createdashboard/stepcreatesettings.xml";

		protected StepCreateSettingsHandler(UiGeneratorController ui) {
			super(ui);
			this.loadPanel(XML_STEP_CREATE_SETTINGS);
		}

		public void evaluate(Object combo) {
			int index = ui.getSelectedIndex(combo);
			if (index == 2) {
				ui.add(new CreateNewTargetHandler((UiGeneratorController) ui)
						.getDialog());
			}
		}

		public Object getPanelComponent() {
			return super.getPanelComponent();
		}

		public void next() {
			setCurrentStepPanel(new StepReviewHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}

		public void previous() {
			setCurrentStepPanel(new StepSelectClientsHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}

		public void showDateSelecter(Object textField) {
			((UiGeneratorController) ui).showDateSelecter(textField);
		}
		
		public void selectService() {
			setCurrentStepPanel(new StepSelectTargetSavingsHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}

		public void targetedSavings() {
			selectService();
		}
		public void selectClient() {
			previous();
		}
	}

	public class StepReviewHandler extends BasePanelHandler {
		private static final String XML_STEP_REVIEW = "/ui/plugins/paymentview/analytics/createdashboard/stepreview.xml";

		protected StepReviewHandler(UiGeneratorController ui) {
			super(ui);
			this.loadPanel(XML_STEP_REVIEW);
		}

		public void create() {
		}

		public Object getPanelComponent() {
			return super.getPanelComponent();
		}

		public void previous() {
			setCurrentStepPanel(new StepCreateSettingsHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}
		
		public void selectService() {
			setCurrentStepPanel(new StepSelectTargetSavingsHandler(
					(UiGeneratorController) ui).getPanelComponent());

		}
		public void targetedSavings() {
			selectService();

		}
		public void selectClient() {
			setCurrentStepPanel(new StepSelectClientsHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}
		public void createSettings() {
			previous();
		}

		public void showDateSelecter(Object textField) {
			((UiGeneratorController) ui).showDateSelecter(textField);
		}
	}

	public class StepSelectClientsHandler extends BasePanelHandler {
		private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
		private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/createdashboard/stepselectclients.xml";

		protected StepSelectClientsHandler(UiGeneratorController ui) {
			super(ui);
			this.loadPanel(XML_STEP_SELECT_CLIENT);
			populateClientsTable();
		}

		private Object createRow(Client c) {
			Object row = ui.createTableRow();
			ui.add(row,
					ui.createTableCell(c.getFirstName() + " "
							+ c.getOtherName()));
			ui.add(row, ui.createTableCell(c.getPhoneNumber()));
			String accountStr = "";
			for (Account a : c.getAccounts()) {
				accountStr += (Long.toString(a.getAccountNumber()) + ", ");
			}
			ui.add(row, ui.createTableCell(accountStr));

			return row;
		}

		public Object getPanelComponent() {
			return super.getPanelComponent();
		}

		public void next() {
			setCurrentStepPanel(new StepCreateSettingsHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}

		// > PRIVATE HELPER METHODS
		private void populateClientsTable() {
			Object table = find(COMPONENT_CLIENT_TABLE);
			List<Client> clients = clientDao.getAllClients();
			for (Client c : clients) {
				ui.add(table, createRow(c));
			}
		}

		public void previous() {
			setCurrentStepPanel(new StepSelectTargetSavingsHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}
		
		public void selectService() {
			setCurrentStepPanel(new StepSelectTargetSavingsHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}
		
		public void targetedSavings() {
			previous();
		}
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
			setCurrentStepPanel(new StepSelectClientsHandler(
					(UiGeneratorController) ui).getPanelComponent());
		}
		
		public void selectService() {
			//Do Nothing!
		}
	}
}
