package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class ReviewHandler extends BasePanelHandler {
	private static final String XML_STEP_REVIEW = "/ui/plugins/paymentview/analytics/addclient/stepreview.xml";
	private AddClientTabHandler addClientTabHandler;
	private final CreateSettingsHandler previousCreateSettingsHandler;

	protected ReviewHandler(UiGeneratorController ui, 
			PaymentViewPluginController pluginController, AddClientTabHandler addClientTabHandler, 
			CreateSettingsHandler createSettingsHandler) {
		super(ui);
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
	
	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}
	
//> WIZARD NAVIGATORS
	public void previous() {
		addClientTabHandler.setCurrentStepPanel(previousCreateSettingsHandler.getPanelComponent());
	}
	
	public void selectService() {
		addClientTabHandler.setCurrentStepPanel(
				previousCreateSettingsHandler.
				getPreviousSelectClientsHandler().getSelectTargetSavingsHandler().
				getPanelComponent()
		);
	}

	public void targetedSavings() {
		selectService();
	}

	public void selectClient() {
		addClientTabHandler.setCurrentStepPanel(previousCreateSettingsHandler.getPreviousSelectClientsHandler().getPanelComponent());
	}

	public void createSettings() {
		previous();
	}
}
