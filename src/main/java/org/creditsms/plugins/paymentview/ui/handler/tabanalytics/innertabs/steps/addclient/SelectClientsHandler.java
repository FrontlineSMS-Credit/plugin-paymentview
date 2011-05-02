package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.SelectClientsTableHandler;

public class SelectClientsHandler extends BasePanelHandler {
	private static final String PNL_CLIENTS_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/addclient/stepselectclients.xml";
	private ClientDao clientDao;
	private final AddClientTabHandler addClientTabHandler;
	private SelectClientsTableHandler selectClientsTableHandler;
	private CustomFieldDao customFieldDao;
	private CustomValueDao customDataDao;
	private Object pnlClientsTableHolder;

	protected SelectClientsHandler(UiGeneratorController ui,
			ClientDao clientDao, CustomFieldDao customFieldDao,
			CustomValueDao customDataDao,
			AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.customFieldDao = customFieldDao;
		this.customDataDao = customDataDao;
		this.addClientTabHandler = addClientTabHandler;
		this.clientDao = clientDao;
		this.loadPanel(XML_STEP_SELECT_CLIENT);
		initialise();
		refresh();
	}

	private void initialise() {
		this.selectClientsTableHandler = new SelectClientsTableHandler(
				(UiGeneratorController) ui, clientDao, customFieldDao,
				customDataDao);
		pnlClientsTableHolder = this.find(PNL_CLIENTS_TABLE_HOLDER);

		this.ui.add(pnlClientsTableHolder,
				selectClientsTableHandler.getClientsTablePanel());
	}

	private void refresh() {
		this.selectClientsTableHandler.updateClientsList();
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler.setCurrentStepPanel(new CreateSettingsHandler(
				(UiGeneratorController) ui, clientDao, customFieldDao, customDataDao, addClientTabHandler)
				.getPanelComponent());
	}

	public void previous() {
		addClientTabHandler.setCurrentStepPanel(new SelectTargetSavingsHandler(
				(UiGeneratorController) ui, clientDao, customDataDao, customFieldDao, addClientTabHandler)
				.getPanelComponent());
	}

	public void selectService() {
		previous();
	}

	public void targetedSavings() {
		previous();
	}
}
