package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class SelectTargetSavingsHandler extends BasePanelHandler {
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectservice.xml";
	private ViewDashBoardTabHandler viewDashBoardTabHandler;
	private ClientDao clientDao;
	private CustomFieldDao customFieldDao;
	private CustomValueDao customDataDao;

	public SelectTargetSavingsHandler(UiGeneratorController ui,
			ClientDao clientDao, CustomFieldDao customFieldDao,
			CustomValueDao customDataDao,
			ViewDashBoardTabHandler viewDashBoardTabHandler) {
		super(ui);
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.customDataDao = customDataDao;
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
	}

	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		viewDashBoardTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, clientDao, customFieldDao,
				customDataDao, viewDashBoardTabHandler).getPanelComponent());
	}
}