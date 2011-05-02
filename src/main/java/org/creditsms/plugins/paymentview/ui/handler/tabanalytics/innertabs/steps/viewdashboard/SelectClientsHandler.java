package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.SelectClientsTableHandler;

public class SelectClientsHandler extends BasePanelHandler {
	private static final String COMPONENT_CMB_CLIENTS_CHARACTERISTICS = "cmbClientsCharacteristics";
	private static final String COMPONENT_CMB_PRODUCT = "cmbProduct";
	// private static final String BY_ALL_IN_PROGRESS = "by_all_in_progress";
	// private static final String BY_ALL_COMPLETED = "by_all_completed";
	// private static final String BY_ALL = "by_all";
	private static final String BY_CLIENTS = "by_clients";
	private static final String BY_CLIENTS_CHARACTERISTICS = "by_clients_characteristics";
	private static final String BY_PRODUCT = "by_product";

	private static final String PNL_CLIENTS_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectclients.xml";

	private ClientDao clientDao;
	private ViewDashBoardTabHandler viewDashBoardTabHandler;
	private CustomFieldDao customFieldDao;
	private CustomValueDao customDataDao;

	private SelectClientsTableHandler selectClientsTableHandler;

	private Object pnlClientsTableHolder;
	private Object cmbProduct;
	private Object cmbClientsCharacteristics;

	SelectClientsHandler(UiGeneratorController ui, ClientDao clientDao,
			CustomFieldDao customFieldDao, CustomValueDao customDataDao,
			ViewDashBoardTabHandler viewDashBoardTabHandler) {
		super(ui);
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.customDataDao = customDataDao;
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.loadPanel(XML_STEP_SELECT_CLIENT);
		this.init();
		refresh();
	}

	private void init() {
		this.selectClientsTableHandler = new SelectClientsTableHandler(
				(UiGeneratorController) ui, clientDao, customFieldDao,
				customDataDao);
		pnlClientsTableHolder = this.find(PNL_CLIENTS_TABLE_HOLDER);
		// Customize the Table Panel
		ui.setColspan(selectClientsTableHandler.getClientsTablePanel(), 2);
		ui.setEnabledRecursively(selectClientsTableHandler.getClientsTable(),
				false);

		cmbProduct = ui.find(this.pnlClientsTableHolder, COMPONENT_CMB_PRODUCT);
		cmbClientsCharacteristics = ui.find(this.pnlClientsTableHolder,
				COMPONENT_CMB_CLIENTS_CHARACTERISTICS);

		this.ui.add(pnlClientsTableHolder,
				selectClientsTableHandler.getClientsTablePanel());
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		viewDashBoardTabHandler.setCurrentStepPanel(new CreateSettingsHandler(
				(UiGeneratorController) ui, clientDao, viewDashBoardTabHandler,
				customFieldDao, customDataDao).getPanelComponent());
	}

	public void previous() {
		viewDashBoardTabHandler
				.setCurrentStepPanel(new SelectTargetSavingsHandler(
						(UiGeneratorController) ui, clientDao, customFieldDao,
						customDataDao, viewDashBoardTabHandler)
						.getPanelComponent());
	}

	public void refresh() {
		this.selectClientsTableHandler.updateClientsList();
	}

	public void enable(Object checkbox) {
		String name = ui.getName(checkbox);
		if (name.equals(BY_PRODUCT)) {
			ui.setEnabled(cmbProduct, true);
			// else
			ui.setEnabled(cmbClientsCharacteristics, false);
			ui.setEnabledRecursively(
					selectClientsTableHandler.getClientsTable(), false);
			selectClientsTableHandler.unSelectAll();
		} else if (name.equals(BY_CLIENTS_CHARACTERISTICS)) {
			ui.setEnabled(cmbClientsCharacteristics, true);
			// else
			ui.setEnabledRecursively(
					selectClientsTableHandler.getClientsTable(), false);
			ui.setEnabled(cmbProduct, false);
			selectClientsTableHandler.unSelectAll();
		} else if (name.equals(BY_CLIENTS)) {
			ui.setEnabledRecursively(
					selectClientsTableHandler.getClientsTable(), true);
			// else
			ui.setEnabled(cmbClientsCharacteristics, false);
			ui.setEnabled(cmbProduct, false);
		} else {
			selectClientsTableHandler.unSelectAll();
			ui.setEnabledRecursively(
					selectClientsTableHandler.getClientsTable(), false);
			ui.setEnabled(cmbClientsCharacteristics, false);
			ui.setEnabled(cmbProduct, false);			
		}
	}
}
