package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetCreationProcess;
import org.creditsms.plugins.paymentview.analytics.TargetPayBillProcess;
import org.creditsms.plugins.paymentview.analytics.TargetStandardProcess;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class ReviewHandler extends BasePanelHandler {
	private static final String XML_STEP_REVIEW = "/ui/plugins/paymentview/analytics/addclient/stepreview.xml";
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientTableHolder";
	private static String PAYMENT_PROCESS = "StandardPaymentService";
	//private static final String PAYMENT_PROCESS = "PayBillPaymentService";

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
		this.selectedClients = new ArrayList<Client>(previousCreateSettingsHandler.getPreviousSelectClientsHandler().getSelectedClients());
		//TODO to be modified if several phones are connected
		//List<PaymentService> paymentServices = pluginController.getPaymentServices();
		//PAYMENT_PROCESS = (paymentServices instanceof MpesaPersonalService? "StandardPaymentService" : "PayBillPaymentService");
		init();
	}

	private void init() {
		this.loadPanel(XML_STEP_REVIEW);
		clientTableHolder = ui.find(this.getPanelComponent(), PNL_CLIENT_TABLE_HOLDER);
		clientTableHandler = new ReviewClientTableHandler((UiGeneratorController) ui, pluginController, this);
		clientTableHandler.setClients(selectedClients);
		ui.add(clientTableHolder, clientTableHandler.getClientsTablePanel());
	}

	public void create() throws DuplicateKeyException {
		for (Client client : selectedClients) {
			if (PAYMENT_PROCESS.equals("StandardPaymentService")){
				TargetCreationProcess targetCreationProcess = new TargetStandardProcess(
						client, previousCreateSettingsHandler.getStartDate(), 
						previousCreateSettingsHandler.getEndDate(), 
						previousCreateSettingsHandler.getTargetLstServiceItems(), pluginController, getTotalAmount(), getStatus());

				if(targetCreationProcess.canCreateTarget()){
					targetCreationProcess.createTarget();
					ui.alert("New target created for client " + client.getFullName()+".");
				}else{
					ui.alert("The client "+client.getFullName()
							+" already has an active standart payment target.");
				}
			}else if(PAYMENT_PROCESS.equals("PayBillPaymentService")){
				TargetCreationProcess targetCreationProcess = new TargetPayBillProcess(
						client, previousCreateSettingsHandler.getStartDate(), 
						previousCreateSettingsHandler.getEndDate(), 
						previousCreateSettingsHandler.getTargetLstServiceItems(), pluginController, getTotalAmount(), getStatus());
					targetCreationProcess.createTarget();
					ui.alert("New target created for client "+ client.getFullName()+ ".");
			}
		}
		previousCreateSettingsHandler.getPreviousSelectClientsHandler().getSelectedClients().clear();
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
		((UiGeneratorController)ui).getFrontlineController().getEventBus().unregisterObserver(clientTableHandler);
	}

//	public void selectService() {
//		addClientTabHandler.setCurrentStepPanel(
//				previousCreateSettingsHandler.
//				getPreviousSelectClientsHandler().getSelectTargetSavingsHandler().getPanelComponent()
//		);
//	}

//	public void targetedSavings() {
//		selectService();
//	}

	public void selectClient() {
		addClientTabHandler.setCurrentStepPanel(previousCreateSettingsHandler.getPreviousSelectClientsHandler().getPanelComponent());
	}

	public void createSettings() {
		previous();
	}

	public ServiceItem getSelectedServiceItem() {
		return previousCreateSettingsHandler.getSelectedServiceItem();
	}

	public List<TargetServiceItem> getSelectedServiceItems() {
		return previousCreateSettingsHandler.getTargetLstServiceItems();
	}

	public BigDecimal getTotalAmount() {
		return previousCreateSettingsHandler.getTotalAmount();
	}

	public Date getStartDate() {
		return previousCreateSettingsHandler.getStartDate();
	}

	public Date getEndDate() {
		return previousCreateSettingsHandler.getEndDate();
	}

	public List<Client> getSelectedClients() {
		return selectedClients;
	}

	public String getStatus() {
		return previousCreateSettingsHandler.getStatus();
	}

}
