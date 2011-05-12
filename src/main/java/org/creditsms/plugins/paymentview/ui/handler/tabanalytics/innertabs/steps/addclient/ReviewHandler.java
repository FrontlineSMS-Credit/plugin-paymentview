package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class ReviewHandler extends BasePanelHandler {
	private static final String XML_STEP_REVIEW = "/ui/plugins/paymentview/analytics/addclient/stepreview.xml";
	private AddClientTabHandler addClientTabHandler;
	private ClientDao clientDao;
	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;
	private final CreateSettingsHandler previousCreateSettingsHandler;
	private final PaymentViewPluginController pluginController;

	protected ReviewHandler(UiGeneratorController ui, 
			PaymentViewPluginController pluginController, AddClientTabHandler addClientTabHandler, 
			CreateSettingsHandler createSettingsHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.previousCreateSettingsHandler = createSettingsHandler;
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_REVIEW);
	}

	public void create() {
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void previous() {
		addClientTabHandler.setCurrentStepPanel(previousCreateSettingsHandler.getPanelComponent());
	}

	public void selectService() {
		addClientTabHandler.setCurrentStepPanel(new SelectTargetSavingsHandler(
				(UiGeneratorController) ui, pluginController, addClientTabHandler).getPanelComponent());
	}

	public void targetedSavings() {
		selectService();
	}

	public void selectClient() {
		addClientTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, pluginController, addClientTabHandler).getPanelComponent());
	}

	public void createSettings() {
		previous();
	}

	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}
}
