package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateNewServiceItemHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class CreateSettingsHandler extends BasePanelHandler implements EventObserver{
	private static final String PNL_FIELDS = "pnlFields";
	private static final String CMBTARGETS = "cmbtargets";
	private static final String TXT_END_DATE = "txt_EndDate";
	private static final String TXT_START_DATE = "txt_StartDate";
	private static final String ENTER_NEW_TARGET = "Enter New Target";
	private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/addclient/stepcreatesettings.xml";
	
	private final AddClientTabHandler addClientTabHandler;
	private final SelectClientsHandler previousSelectClientsHandler;
	private final PaymentViewPluginController pluginController;
	
	private Object cmbtargets;
	private Object pnlFields;
	private Object txtStartDate;
	private Object txtEndDate;
	private Date startDate;
	private Date endDate;
	private ServiceItem selectedServiceItem;

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
		cmbtargets = ui.find(this.getPanelComponent(), CMBTARGETS);
		pnlFields = ui.find(this.getPanelComponent(), PNL_FIELDS);
		txtStartDate = ui.find(this.pnlFields, TXT_START_DATE);
		txtEndDate = ui.find(this.pnlFields, TXT_END_DATE);
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
	
	boolean validateStartDate(Date startDate){
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);  
		calendar.set(Calendar.MINUTE, 0);  
		calendar.set(Calendar.SECOND, 0);  
		calendar.set(Calendar.MILLISECOND, 0);
		Date srtDate = calendar.getTime();

		if(srtDate.compareTo(startDate)<=0){
			return true;
		}else{
			return false;
		}
	}
	
	boolean validateEndDate(Date startDate, Date endDate){
		if(startDate.compareTo(endDate)<0){
			return true;
		}else{
			return false;
		}
	}
	
	public boolean parseDateRange(){
		try {
			String strStartDate = ui.getText(txtStartDate);
			String strEndDate = ui.getText(txtEndDate);
			startDate = InternationalisationUtils.getDateFormat().parse(strStartDate);
			if(validateStartDate(startDate)){
			}else{
				ui.alert("Invalid Start Date");
				return false;
			}
			endDate = InternationalisationUtils.getDateFormat().parse(strEndDate);
			if(validateEndDate(startDate, endDate)){
				return true;
			}else{
				ui.alert("Invalid End Date");
				return false;
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	
	public ServiceItem getSelectedServiceItem() {
		return selectedServiceItem;
	}
	
//> WIZARD NAVIGATORS
	public void next() {
		if(parseDateRange()){
			readServiceItem();
			addClientTabHandler.setCurrentStepPanel(new ReviewHandler(
					(UiGeneratorController) ui, this.pluginController, addClientTabHandler, this).getPanelComponent());
		}
	}

	public void readServiceItem() {
		Object selectedItem = ui.getSelectedItem(cmbtargets);
		selectedServiceItem = ui.getAttachedObject(selectedItem, ServiceItem.class);
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