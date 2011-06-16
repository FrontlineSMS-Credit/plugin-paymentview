package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class SelectTargetSavingsHandler extends BasePanelHandler implements EventObserver {
	private static final String CMB_SERVICE_ITEMS = "cmbServiceItems";
	private static final String XML_STEP_SELECT_TARGET_SAVING = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectservice.xml";
	
	private ViewDashBoardTabHandler viewDashBoardTabHandler;
	private final PaymentViewPluginController pluginController;
	private Object cmbServiceItems;
	private ServiceItemDao serviceItemDao;
	private ServiceItem attachedServiceItem;

	public SelectTargetSavingsHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController,
			ViewDashBoardTabHandler viewDashBoardTabHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.serviceItemDao = pluginController.getServiceItemDao(); 
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.loadPanel(XML_STEP_SELECT_TARGET_SAVING);
		ui.getFrontlineController().getEventBus().registerObserver(this);
		init();
	}

	private void init() {
		cmbServiceItems = ui.find(this.getPanelComponent(), CMB_SERVICE_ITEMS);
		refresh();
	}

	public void refresh() {
		ui.removeAll(cmbServiceItems);
		List<ServiceItem> serviceItems = serviceItemDao.getAllServiceItems();
		for(ServiceItem serviceItem : serviceItems){
			Object choice = ui.createComboboxChoice(serviceItem.getTargetName(), serviceItem);
			ui.add(cmbServiceItems, choice);
		}
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		if (ui.getSelectedIndex(cmbServiceItems) > -1) {
			int index = ui.getSelectedIndex(cmbServiceItems);
			//Get Service Item from ComboBox
			attachedServiceItem = ui.getAttachedObject(ui.getItem(cmbServiceItems, index), ServiceItem.class);
			
			List<Target> targets = pluginController.getTargetDao().getTargetsByServiceItem(attachedServiceItem.getId());
			Map<Client,ServiceItem> clients_serviceItems = new HashMap<Client,ServiceItem>();
			//FIXME: THIS IS JUST TOO MUCH PRESSURE FOR THE DB
			for(Target target : targets){
				Account a = target.getAccount();
				if ((a != null) & (a.getClient() != null)) {
					clients_serviceItems.put(a.getClient(), target.getServiceItem());
				} 	
			}
			viewDashBoardTabHandler.setCurrentStepPanel(new SelectClientsHandler(
					(UiGeneratorController) ui, pluginController, viewDashBoardTabHandler, this, clients_serviceItems).getPanelComponent());
		}
	}
	
	public ServiceItem getAttachedServiceItem() {
		return attachedServiceItem;
	}

	public void notify(FrontlineEventNotification notification) {
		if (!(notification instanceof EntitySavedNotification)) {
			return;
		}

		Object entity = ((EntitySavedNotification) notification).getDatabaseEntity();
		if (entity instanceof ServiceItem) {
			this.refresh();
		}
	}
}