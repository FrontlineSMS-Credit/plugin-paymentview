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
	private static final String PNL_CLIENT_TABLE_HOLDER = "pnlClientTableHolder";
	
	private AddClientTabHandler addClientTabHandler;
	private final CreateSettingsHandler previousCreateSettingsHandler;
	private TargetDao targetDao;
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
		this.targetDao = pluginController.getTargetDao();
		selectedClients = previousCreateSettingsHandler.
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
		for (Client c : selectedClients) {
			if (!c.getAccounts().isEmpty()) {
				Account account = new ArrayList<Account>(c.getAccounts()).get(0);
				
				Target target = new Target();
				target.setStartDate(previousCreateSettingsHandler.getStartDate());
				target.setEndDate(previousCreateSettingsHandler.getEndDate());
				target.setServiceItem(previousCreateSettingsHandler.getSelectedServiceItem());
				target.setAccount(account);
				
				ui.alert(target.toString());
				targetDao.saveTarget(target);
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
