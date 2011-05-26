package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;


import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetCreationProcess;
import org.creditsms.plugins.paymentview.analytics.TargetPayBillProcess;
import org.creditsms.plugins.paymentview.analytics.TargetStandardProcess;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class ReviewHandler extends BasePanelHandler {
	private static final String XML_STEP_REVIEW = "/ui/plugins/paymentview/analytics/addclient/stepreview.xml";
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientTableHolder";
	private static final String PAYMENT_PROCESS = "StandardPaymentService";
	
	private AddClientTabHandler addClientTabHandler;
	private final CreateSettingsHandler previousCreateSettingsHandler;
	private Object clientTableHolder;
	private ReviewClientTableHandler clientTableHandler;
	private final PaymentViewPluginController pluginController;
	private List<Client> selectedClients;

	protected ReviewHandler(UiGeneratorController ui, 
			PaymentViewPluginController pluginController, AddClientTabHandler addClientTabHandler, 
			CreateSettingsHandler createSettingsHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.previousCreateSettingsHandler = createSettingsHandler;
		this.addClientTabHandler = addClientTabHandler;
		this.selectedClients = previousCreateSettingsHandler.
		getPreviousSelectClientsHandler().getSelectedClients();
		init();
	}

	private void init() {
		this.loadPanel(XML_STEP_REVIEW);
		clientTableHolder = ui.find(this.getPanelComponent(), PNL_CLIENT_TABLE_HOLDER);
		clientTableHandler = new ReviewClientTableHandler((UiGeneratorController) ui, pluginController, this);
		clientTableHandler.setClients(selectedClients);
		ui.add(clientTableHolder, clientTableHandler.getClientsTablePanel());
	}

	public void create() {
		for (Client client : selectedClients) {
			if (PAYMENT_PROCESS.equals("StandardPaymentService")){
				TargetCreationProcess targetCreationProcess = new TargetStandardProcess(
						client, previousCreateSettingsHandler.getStartDate(), 
						previousCreateSettingsHandler.getEndDate(), 
						previousCreateSettingsHandler.getSelectedServiceItem(), pluginController);
				
				if(targetCreationProcess.canCreateTarget()){
					targetCreationProcess.createTarget();
					ui.alert("New target created for client "+client.getFirstName()+" "+client.getName()+".");
				}else{
					ui.alert("The client "+client.getFirstName()+" "+client.getName()
							+" already has a standart payment active target.");
				}
			}else if(PAYMENT_PROCESS.equals("PayBillPaymentService")){
				TargetCreationProcess targetCreationProcess = new TargetPayBillProcess(
						client, previousCreateSettingsHandler.getStartDate(), 
						previousCreateSettingsHandler.getEndDate(), 
						previousCreateSettingsHandler.getSelectedServiceItem(), pluginController);
				
					targetCreationProcess.createTarget();
			}
		}
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
				getPreviousSelectClientsHandler().getSelectTargetSavingsHandler().getPanelComponent()
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
