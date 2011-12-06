package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class SelectTargetSavingsHandler extends BasePanelHandler {
	private static final String CHK_TARGET_SAVINGS_LAYAWAY = "target_savings_layaway";

//> CONSTANTS
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/addclient/stepselecttargetsavings.xml";

	private AddClientTabHandler addClientTabHandler;
	private PaymentViewPluginController pluginController;

	private Object chkTargetSavingsLayaway;

	public SelectTargetSavingsHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController,
			final AddClientTabHandler addClientTabHandler) {
		super(ui);

		this.pluginController = pluginController;
		this.addClientTabHandler = addClientTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
//		if (selectedRadiosButtons()){
//			addClientTabHandler.setCurrentStepPanel(new SelectClientsHandler(
//					(UiGeneratorController) ui, pluginController, addClientTabHandler, this).getPanelComponent());
//		}
	}

	private boolean selectedRadiosButtons() {
		if(chkTargetSavingsLayaway == null){
			chkTargetSavingsLayaway = ui.find(this.getPanelComponent(), CHK_TARGET_SAVINGS_LAYAWAY);
		}
		return ui.isSelected(chkTargetSavingsLayaway);
	}

	public void selectService() {
		// Do Nothing!
	}
}