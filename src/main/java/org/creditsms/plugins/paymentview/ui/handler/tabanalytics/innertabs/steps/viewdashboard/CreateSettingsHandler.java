package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateAlertHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class CreateSettingsHandler extends BasePanelHandler {
	private static final String XML_STEP_VIEW_CLIENTS = "/ui/plugins/paymentview/analytics/viewdashboard/stepviewclients.xml";
	private ClientDao clientDao;
	private ViewDashBoardTabHandler viewDashBoardTabHandler;
	private CustomFieldDao customFieldDao;
	private CustomValueDao customDataDao;

	CreateSettingsHandler(UiGeneratorController ui,
			ClientDao clientDao,
			ViewDashBoardTabHandler viewDashBoardTabHandler,
			CustomFieldDao customFieldDao, CustomValueDao customDataDao) {
		super(ui);
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.customDataDao = customDataDao;
		this.loadPanel(XML_STEP_VIEW_CLIENTS);
	}

	public void createAlert() {
		ui.add(new CreateAlertHandler((UiGeneratorController) ui).getDialog());
	}

	public void export() {
		new ClientExportHandler((UiGeneratorController) ui, clientDao,
				customFieldDao, customDataDao).showWizard();
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void previous() {
		viewDashBoardTabHandler.setCurrentStepPanel(new SelectClientsHandler(
				(UiGeneratorController) ui, clientDao, customFieldDao, customDataDao, viewDashBoardTabHandler)
				.getPanelComponent());
	}

	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}
}
