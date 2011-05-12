package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
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
	private final SelectClientsHandler previousSelectClientsHandler;
	private final PaymentViewPluginController pluginController;

	protected CreateSettingsHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
			AddClientTabHandler addClientTabHandler, SelectClientsHandler selectClientsHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.addClientTabHandler = addClientTabHandler;
		this.previousSelectClientsHandler = selectClientsHandler;
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
				(UiGeneratorController) ui, this.pluginController, addClientTabHandler, this).getPanelComponent());
	}

	public void previous() {
		addClientTabHandler.setCurrentStepPanel(previousSelectClientsHandler.getPanelComponent());
	}

	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}

	public void selectService() {
		addClientTabHandler.setCurrentStepPanel(new SelectTargetSavingsHandler(
				(UiGeneratorController) ui, this.pluginController, addClientTabHandler).getPanelComponent());
	}

	public void targetedSavings() {
		selectService();
	}

	public void selectClient() {
		previous();
	}
}
