package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
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
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateNewServiceItemHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;
import org.creditsms.plugins.paymentview.utils.PvUtils;

public class CreateSettingsHandler extends BasePanelHandler implements EventObserver{
	private static final String PNL_FIELDS = "pnlFields";
	private static final String PNL_TOTAL_COST = "pnlTotalCost";
	private static final String CMBTARGETS = "cmbtargets";
	private static final String TXT_END_DATE = "txt_EndDate";
	private static final String TXT_START_DATE = "txt_StartDate";
	private static final String TXT_TOTAL_AMOUNT = "txt_TotalAmount";
	private static final String ENTER_NEW_TARGET = "Enter New Target";
	private static final String TXT_QTY = "qty";
	private static final String TBL_SERVICE_ITEMS_LIST = "tbl_serviceItemList";
	private static final String XML_STEP_CREATE_SETTINGS = "/ui/plugins/paymentview/analytics/addclient/stepcreatesettings.xml";
	private static String CONFIRM_ACCEPT_PARSED_DATE = "";
	
	private final AddClientTabHandler addClientTabHandler;
	private final SelectClientsHandler previousSelectClientsHandler;
	private final PaymentViewPluginController pluginController;
	
	private Object cmbtargets;
	private Object txtQty;
	private Object pnlFields;
	private Object pnlTotalCost;
	private Object txtStartDate;
	private Object txtEndDate;
	private Object txtTotalAmount;
	private Object serviceItemsTableComponent;
	private Object dialogConfimParsedEndDate;
	private Date startDate;
	private Date endDate;
	private Date tempStartDate;
	private Date tempEndDate;
	
	private ServiceItem selectedServiceItem;
	private List<TargetServiceItem> lstTargetServiceItems = new ArrayList<TargetServiceItem>(); 
	private BigDecimal totalAmount = BigDecimal.ZERO;

	protected CreateSettingsHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
			AddClientTabHandler addClientTabHandler, SelectClientsHandler selectClientsHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.addClientTabHandler = addClientTabHandler;
		this.previousSelectClientsHandler = selectClientsHandler;
		ui.getFrontlineController().getEventBus().registerObserver(this);
		
		init();
	}
	
	private void init() {
		this.loadPanel(XML_STEP_CREATE_SETTINGS);
		cmbtargets = ui.find(this.getPanelComponent(), CMBTARGETS);
		txtQty = ui.find(this.getPanelComponent(), TXT_QTY);
		pnlFields = ui.find(this.getPanelComponent(), PNL_FIELDS);
		pnlTotalCost = ui.find(this.getPanelComponent(), PNL_TOTAL_COST);
		txtTotalAmount = ui.find(this.pnlTotalCost, TXT_TOTAL_AMOUNT);
		txtStartDate = ui.find(this.pnlFields, TXT_START_DATE);
		txtEndDate = ui.find(this.pnlFields, TXT_END_DATE);
		serviceItemsTableComponent = ui.find(this.getPanelComponent(), TBL_SERVICE_ITEMS_LIST);
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
	
	private Calendar setStartOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 0);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	private Calendar setEndOfDay(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 24);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, 0);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	private Calendar setEndOfDayFormat(Calendar cal){
		cal.set(Calendar.HOUR_OF_DAY, 24);  
		cal.set(Calendar.MINUTE, 0);  
		cal.set(Calendar.SECOND, -1);  
		cal.set(Calendar.MILLISECOND, 0);
		return cal;
	}
	
	boolean validateStartDate(Date startDate){
		Calendar calStartDate = Calendar.getInstance();
		calStartDate = setStartOfDay(calStartDate);
		Date srtDate = calStartDate.getTime();
		if(srtDate.compareTo(startDate)<=0){
			return true;
		}else{
			return false;
		}
	}
	
	private int getMonthsDiffFromStart(Calendar calStartDate,
			Calendar calNowDate) {
		return (calStartDate.get(Calendar.YEAR) - calNowDate.get(Calendar.YEAR)) * 12 +
		(calStartDate.get(Calendar.MONTH)- calNowDate.get(Calendar.MONTH)) + 
		(calStartDate.get(Calendar.DAY_OF_MONTH) >= calNowDate.get(Calendar.DAY_OF_MONTH)? 0: -1); 
	}
	
	boolean validateEndDate(Date startDate, Date endDate){
		String methodToBeCalled = "setParsedEndDate";
		
		if(startDate.compareTo(endDate)<0){
			Calendar calStartDate = Calendar.getInstance();
			calStartDate.setTime(startDate);
			calStartDate = setStartOfDay(calStartDate);

			Calendar calEndDate = Calendar.getInstance();
			calEndDate.setTime(endDate);
			calEndDate = setEndOfDay(calEndDate);		
			
			int startDay = calStartDate.get(Calendar.DAY_OF_MONTH);
			int endDay = calEndDate.get(Calendar.DAY_OF_MONTH);
			
			if (calStartDate.get(Calendar.YEAR)==calEndDate.get(Calendar.YEAR)) {
				if(calStartDate.get(Calendar.MONTH)==calEndDate.get(Calendar.MONTH)){
					ui.alert("Target duration cannot be less than a month.");
					return false;
				} else {
					int monthDiff = getMonthsDiffFromStart(calEndDate, calStartDate);
					if(monthDiff==0){
						ui.alert("Target duration cannot be less than a month.");
						return false;
					} else {
						calEndDate.setTime(calStartDate.getTime());
						calEndDate.add(Calendar.MONTH, monthDiff);
						calEndDate.add(Calendar.DATE, -1);
						calEndDate = setEndOfDayFormat(calEndDate);

						CONFIRM_ACCEPT_PARSED_DATE="The selected end date is incorrect. The parsed end date is: "+ calEndDate.getTime() +". Do you want to proceed?";
						if(startDay!=endDay){
							setTempStartDate(calStartDate.getTime());
							setTempEndDate(calEndDate.getTime());
							dialogConfimParsedEndDate = ((UiGeneratorController) ui).showConfirmationDialog(methodToBeCalled, this, CONFIRM_ACCEPT_PARSED_DATE);
							return false;
						} else {
							this.endDate = calEndDate.getTime();
							this.startDate = calStartDate.getTime();
							return true;
						}
					}
				}
			} else {
				int monthDiff = getMonthsDiffFromStart(calEndDate, calStartDate);

				calEndDate.setTime(calStartDate.getTime());
				calEndDate.add(Calendar.MONTH, monthDiff);
				calEndDate.add(Calendar.DATE, -1);
				calEndDate = setEndOfDayFormat(calEndDate);

				CONFIRM_ACCEPT_PARSED_DATE="The selected end date is incorrect. The parsed end date is: "+ PvUtils.formatDate(calEndDate.getTime()) +". Do you want to proceed?";
				if(startDay!=endDay){
					setTempStartDate(calStartDate.getTime());
					setTempEndDate(calEndDate.getTime());
					dialogConfimParsedEndDate = ((UiGeneratorController) ui).showConfirmationDialog(methodToBeCalled, this, CONFIRM_ACCEPT_PARSED_DATE);
					return false;
				} else {
					this.endDate = calEndDate.getTime();
					this.startDate = calStartDate.getTime();
					return true;
				}
			}
		}else{
			ui.alert("Invalid End Date");
			return false;
		}
	}
	
	public void setParsedEndDate(){
		ui.remove(dialogConfimParsedEndDate);
		this.endDate = getTempEndDate();
		this.startDate = getTempStartDate();
		
		readServiceItem();
		addClientTabHandler.setCurrentStepPanel(new ReviewHandler(
				(UiGeneratorController) ui, this.pluginController, addClientTabHandler, this).getPanelComponent());
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
				return false;
			}
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	public void addServiceItem(String qty){
		if (checkIfInt(qty)){
			readServiceItem();
			ServiceItem sItem = getSelectedServiceItem();
			if (checkIfItemExists(sItem)) {
				ui.alert(sItem.getTargetName() + " has already been added to the target.");
			} else {
				TargetServiceItem tsi = new TargetServiceItem();
				tsi.setServiceItem(sItem);	
				tsi.setAmount(sItem.getAmount());
				tsi.setServiceItemQty(Integer.parseInt(qty));
			    this.lstTargetServiceItems.add(tsi);
				totalAmount = totalAmount.add(sItem.getAmount().multiply(new BigDecimal(qty)));
				ui.add(this.serviceItemsTableComponent, getRow(tsi));
				ui.setText(txtTotalAmount, totalAmount.toString());
				ui.setText(txtQty, "");
			}
		} else {
			ui.alert("Invalid Quantity");
		}
	}
	public void refreshSelectedTheTargetTable(){
		List<TargetServiceItem> lstServiceItem = getTargetLstServiceItems();
		ui.removeAll(serviceItemsTableComponent);
		totalAmount = BigDecimal.ZERO;
		ui.setText(txtTotalAmount, "0.00");
		for(TargetServiceItem tsi: lstServiceItem){
			totalAmount = totalAmount.add(tsi.getAmount().multiply(new BigDecimal(tsi.getServiceItemQty())));
			ui.add(this.serviceItemsTableComponent, getRow(tsi));
			ui.setText(txtTotalAmount, totalAmount.toString());
		}
	}
	
	public boolean checkIfItemExists(ServiceItem sItem) {
		List<TargetServiceItem> tsiLst = getTargetLstServiceItems();
		for (TargetServiceItem tsi: tsiLst) {
			if (tsi.getServiceItem().equals(sItem)){
				return true;
			}
		}
		return false;
	}
	
    boolean checkIfInt(String in) {
        try {
            if(Integer.parseInt(in)==0){
            	return false;
            }
        } catch (NumberFormatException ex) {
            return false;
        }
        return true;
    }
    
    public void editQty(){
    	TargetServiceItem tgtServiceItem = getSelectedTgtServiceItemInTable();
    	if(tgtServiceItem!=null){
			EditTargetItemQtyHandler editTargetItemQtyHandler = new EditTargetItemQtyHandler(pluginController, tgtServiceItem, this);
			ui.add(editTargetItemQtyHandler.getDialog());
    	} else {
    		ui.alert("No selected Service Items");
    	}
    }
    
	public Object getSelectedServiceItemRow() {
		return ui.getSelectedItem(serviceItemsTableComponent);
	}
    
	public TargetServiceItem getSelectedTgtServiceItemInTable() {
		Object row = getSelectedServiceItemRow();
		TargetServiceItem targetServiceItem = ui.getAttachedObject(row, TargetServiceItem.class);
		return targetServiceItem;
	}
	
	public void removeServiseItemFromTarget(){
		TargetServiceItem tgtServiceItem = getSelectedTgtServiceItemInTable();
		TargetServiceItem rmvTgtServiceItem = new TargetServiceItem();
		List<TargetServiceItem> lstSTI = getTargetLstServiceItems();
		for (TargetServiceItem tsi: lstSTI){
			if (tsi.equals(tgtServiceItem)) {
				rmvTgtServiceItem = tsi;
			}
		}
		if(rmvTgtServiceItem!=null){
			lstSTI.remove(rmvTgtServiceItem);	
		}
		setTargetLstServiceItems(lstSTI);
		refreshSelectedTheTargetTable();
	}
	
	protected Object getRow(TargetServiceItem targeServiceItem) {
		Object row = ui.createTableRow(targeServiceItem);
		ui.add(row, ui.createTableCell(targeServiceItem.getServiceItem().getTargetName()));
		ui.add(row, ui.createTableCell(targeServiceItem.getServiceItemQty()));
		ui.add(row, ui.createTableCell(targeServiceItem.getAmount().toString()));
		return row;
	}
	
	public List<TargetServiceItem> getTargetLstServiceItems() {
		return lstTargetServiceItems;
	}

	public void setTargetLstServiceItems(List<TargetServiceItem> lstTargetServiceItems) {
		this.lstTargetServiceItems = lstTargetServiceItems;
	}
	
	public BigDecimal getTotalAmount() {
		return totalAmount;
	}
	
	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount =  totalAmount;
	}
	
	public Date getStartDate() {
		return startDate;
	}
	
	public Date getEndDate() {
		return endDate;
	}
	public Date getTempStartDate() {
		return tempStartDate;
	}
	public void setTempStartDate(Date tempStartDate) {
		this.tempStartDate = tempStartDate;
	}

	public Date getTempEndDate() {
		return tempEndDate;
	}
	public void setTempEndDate(Date tempEndDate) {
		this.tempEndDate = tempEndDate;
	}

	public ServiceItem getSelectedServiceItem() {
		return selectedServiceItem;
	}
	
//> WIZARD NAVIGATORS
	public void next() {	
		//Check Service,start and end date set up
		if (ui.getSelectedItem(cmbtargets) == null) {
			ui.infoMessage("Please enter a savings target/goal.");
			return;
		}
		if (ui.getText(txtStartDate).equals("")) {
			ui.infoMessage("Please enter a start date.");
			return;
		}
		if (ui.getText(txtEndDate).equals("")) {
			ui.infoMessage("Please enter an end date.");
			return;
		}
		
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
		((UiGeneratorController)ui).getFrontlineController().getEventBus().unregisterObserver(this);
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
		}
	}
}