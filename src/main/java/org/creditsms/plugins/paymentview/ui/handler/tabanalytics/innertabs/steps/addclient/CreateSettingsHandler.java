package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateNewTargetHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class CreateSettingsHandler extends BasePanelHandler {
	private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/addclient/stepcreatesettings.xml";
	private final AddClientTabHandler addClientTabHandler;
	private ClientDao clientDao;
	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;

	protected CreateSettingsHandler(UiGeneratorController ui,ClientDao clientDao, CustomFieldDao customFieldDao, CustomValueDao customValueDao, AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.customFieldDao = customFieldDao;
		this.customValueDao = customValueDao;
		this.clientDao = clientDao;
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_CREATE_SETTINGS);
	}

	public void evaluate(Object combo) {
		int index = ui.getSelectedIndex(combo);
		if (index == 2) {
			ui.add(new CreateNewTargetHandler((UiGeneratorController) ui)
					.getDialog());
		}
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler.setCurrentStepPanel(new ReviewHandler(
				(UiGeneratorController) ui, clientDao, customFieldDao, customValueDao, addClientTabHandler).getPanelComponent());
	}

	public void previous() {
		addClientTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, clientDao, customFieldDao, customValueDao, addClientTabHandler).getPanelComponent());
	}

	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}

	public void selectService() {
		addClientTabHandler.setCurrentStepPanel(new SelectTargetSavingsHandler(
				(UiGeneratorController) ui, clientDao, customValueDao, customFieldDao, addClientTabHandler).getPanelComponent());
	}

	public void targetedSavings() {
		selectService();
	}

	public void selectClient() {
		previous();
	}
}
