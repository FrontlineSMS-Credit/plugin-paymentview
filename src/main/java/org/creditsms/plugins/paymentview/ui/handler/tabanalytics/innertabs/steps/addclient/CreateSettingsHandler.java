package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import java.util.List;

import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateNewServiceItemHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class CreateSettingsHandler extends BasePanelHandler implements EventObserver{
	private static final String ENTER_NEW_TARGET = "Enter New Target";
	private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/addclient/stepcreatesettings.xml";
	
	private final AddClientTabHandler addClientTabHandler;
	private final SelectClientsHandler previousSelectClientsHandler;
	private final PaymentViewPluginController pluginController;
	
	private Object cmbtargets;
	private Object pnlFields;

	protected CreateSettingsHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
			AddClientTabHandler addClientTabHandler, SelectClientsHandler selectClientsHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.addClientTabHandler = addClientTabHandler;
		this.previousSelectClientsHandler = selectClientsHandler;
		ui.getFrontlineController().getEventBus().registerObserver(this);
		
		this.loadPanel(XML_STEP_CREATE_SETTINGS);
		init();
	}

	private void init() {
		cmbtargets = ui.find(this.getPanelComponent(), "cmbtargets");
		pnlFields = ui.find(this.getPanelComponent(), "pnlFields");
		addChoices();
	}

	public void addChoices() {
		List<ServiceItem> allServiceItems = pluginController.getServiceItemDao().getAllServiceItems();
		ui.removeAll(cmbtargets);
		for(ServiceItem serviceItem : allServiceItems){
			ui.add(cmbtargets, 
					ui.createComboboxChoice(serviceItem.getTargetName(), serviceItem));
		}
		
		ui.add(cmbtargets, ui.createComboboxChoice(ENTER_NEW_TARGET, ENTER_NEW_TARGET));
	}

	public void evaluate() {
		Object item = ui.getSelectedItem(cmbtargets);
		Object attachedObject = ui.getAttachedObject(item);
		
		if (attachedObject.equals(ENTER_NEW_TARGET)) {
			ui.add(new CreateNewServiceItemHandler((UiGeneratorController) ui, pluginController)
					.getDialog());
		}
	}
	
	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}
	
//> WIZARD NAVIGATORS
	public void next() {
		addClientTabHandler.setCurrentStepPanel(new ReviewHandler(
				(UiGeneratorController) ui, this.pluginController, addClientTabHandler, this).getPanelComponent());
	}

	public void previous() {
		addClientTabHandler.setCurrentStepPanel(previousSelectClientsHandler.getPanelComponent());
	}

	public void targetedSavings() {
		selectService();
	}

	public void selectClient() {
		previous();
	}
	
	public void selectService() {
		addClientTabHandler.setCurrentStepPanel(new SelectTargetSavingsHandler(
				(UiGeneratorController) ui, this.pluginController, addClientTabHandler).getPanelComponent());
	}
	
	public SelectClientsHandler getPreviousSelectClientsHandler() {
		return previousSelectClientsHandler;
	}
	
	public Object getComboFieldsComponent() {
		return cmbtargets;
	}

	public Object getPanelFieldsComponent() {
		return pnlFields;
	}

	@SuppressWarnings("rawtypes")
	public void notify(FrontlineEventNotification notification) {
		if (!(notification instanceof EntitySavedNotification)) {
			return;
		}

		Object entity = ((EntitySavedNotification) notification).getDatabaseEntity();
		if (!(entity instanceof ServiceItem)) {
			return;
		}else{
			this.addChoices();
			ui.add(cmbtargets, ui.createComboboxChoice(ENTER_NEW_TARGET, ENTER_NEW_TARGET));
		}
	}
}