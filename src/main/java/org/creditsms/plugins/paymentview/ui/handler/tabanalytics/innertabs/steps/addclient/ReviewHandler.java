package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class ReviewHandler extends BasePanelHandler {
	private static final String XML_STEP_REVIEW = "/ui/plugins/paymentview/analytics/addclient/stepreview.xml";
	private AddClientTabHandler addClientTabHandler;
	private final CreateSettingsHandler previousCreateSettingsHandler;
	private TargetDao targetDao;

	protected ReviewHandler(UiGeneratorController ui, 
			PaymentViewPluginController pluginController, AddClientTabHandler addClientTabHandler, 
			CreateSettingsHandler createSettingsHandler) {
		super(ui);
		this.previousCreateSettingsHandler = createSettingsHandler;
		this.addClientTabHandler = addClientTabHandler;
		this.targetDao = pluginController.getTargetDao();
		this.loadPanel(XML_STEP_REVIEW);
	}

	public void create() { 
		List<Client> selectedClients = previousCreateSettingsHandler.
		getPreviousSelectClientsHandler().getSelectedClients();
		
		for (Client client : selectedClients) {
			//
			if (!client.getAccounts().isEmpty()) {
				Account account = new ArrayList<Account>(client.getAccounts()).get(0);
				
				Target target = new Target();
				target.setStartDate(previousCreateSettingsHandler.getStartDate());
				target.setEndDate(previousCreateSettingsHandler.getEndDate());
				target.setServiceItem(previousCreateSettingsHandler.getSelectedServiceItem());
				target.setAccount(account);
				
//				ui.alert(target.toString());
				targetDao.saveTarget(target);
			} else {
				//alert if a client does not have an account
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
