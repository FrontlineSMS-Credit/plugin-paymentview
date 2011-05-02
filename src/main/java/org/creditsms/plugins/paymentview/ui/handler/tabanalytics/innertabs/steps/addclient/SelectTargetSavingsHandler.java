package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class SelectTargetSavingsHandler extends BasePanelHandler {
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/addclient/stepselecttargetsavings.xml";
	private AddClientTabHandler addClientTabHandler;
	private ClientDao clientDao;
	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;

	public SelectTargetSavingsHandler(UiGeneratorController ui,
			ClientDao clientDao, CustomValueDao customValueDao,
			CustomFieldDao customFieldDao,
			final AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.clientDao = clientDao;
		this.customValueDao = customValueDao;
		this.customFieldDao = customFieldDao;
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, clientDao,
				customFieldDao, customValueDao, addClientTabHandler).getPanelComponent());
	}

	public void selectService() {
		// Do Nothing!
	}
}