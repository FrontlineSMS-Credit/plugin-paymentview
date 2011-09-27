package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateAlertHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class CreateSettingsHandler extends BasePanelHandler implements EventObserver  {
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_STEP_VIEW_CLIENTS = "/ui/plugins/paymentview/analytics/viewdashboard/stepviewclients.xml";
	private PaymentViewPluginController pluginController;
	private TargetAnalytics targetAnalytics;
	private Object clientTableHolder;
	private CreateSettingsTableHandler createSettingsTableHandler;

	public CreateSettingsHandler(UiGeneratorController ui,
			ViewDashBoardTabHandler viewDashBoardTabHandler,
			PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		
		targetAnalytics = new TargetAnalytics();
		targetAnalytics.setIncomingPaymentDao(pluginController.getIncomingPaymentDao());
		targetAnalytics.setTargetDao(pluginController.getTargetDao());
		
		ui.getFrontlineController().getEventBus().registerObserver(this);
		init();
	}

	
	private void init() {
		this.loadPanel(XML_STEP_VIEW_CLIENTS);
		clientTableHolder = ui.find(this.getPanelComponent(), PNL_CLIENT_TABLE_HOLDER);
		
		createSettingsTableHandler = new CreateSettingsTableHandler((UiGeneratorController) ui, pluginController, this);
		ui.add(clientTableHolder, createSettingsTableHandler.getClientsTablePanel());
		
		refresh();
	}
	
	public void refresh() {
		this.createSettingsTableHandler.refresh();
	}
	
	public void createAlert() {
		ui.add(new CreateAlertHandler((UiGeneratorController) ui).getDialog());
	}

	public void export() {
		new ClientExportHandler((UiGeneratorController) ui, pluginController).showWizard();
	}
	
	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}
	
	public void notify(final FrontlineEventNotification notification) {
		new FrontlineUiUpateJob() {
			public void run() {
				if (!(notification instanceof DatabaseEntityNotification)) {
					return;
				} else {
					if (notification instanceof DatabaseEntityNotification){
						Object entity = ((DatabaseEntityNotification) notification).getDatabaseEntity();
						if (entity instanceof IncomingPayment ) {
							CreateSettingsHandler.this.refresh();
						}
					}
				}
			}
		}.execute();
	}
}
