package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.dialogs;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.data.repository.TargetServiceItemDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.CreateSettingsTableHandler;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

public class EditTargetHandler implements ThinletUiEventHandler {
	private static final String XML_EDIT_TARGET = "/ui/plugins/paymentview/analytics/viewdashboard/dialogs/dlgEditTarget.xml";
	private static final String TXT_CLIENT_NAME ="clientName";
	private static final String TXT_START_DATE = "startDate";
	private static final String TXT_END_DATE = "txt_EndDate";
	private static final String TBL_SERVICE_ITEM = "tbl_serviceItems";
	private static final String TBL_INCOMING_PAYMENTS = "tbl_payments_received";
	private static final String CMB_TARGETS = "cmbtargets";
	private static final String TXT_QTY = "qty";
	private static final String TXT_TARGET_COST = "txt_TotalAmount";
	private static final String TXT_TARGET_AMOUNT_PAID = "txt_TotalAmountPaid";
	private static String CONFIRM_ACCEPT_SAVE_TARGET = "";
	private static String CONFIRM_ACCEPT_PARSED_DATE = "";
	private DateFormat dateFormat = InternationalisationUtils.getDateFormat();
	
	private Object dialogComponent;
	private UiGeneratorController ui;
	private Target tgt;
	private Object lblClientName;
	private Object lblStartDate;
	private Object txtEndDate;
	private Object tblServiceItemsComponent;
	private Object tblIncomingPaymentsComponent;
	private Object cmbtargets;
	private Object lblTotalTargetCost;
	private Object lblTotalTargetAmountPaid;
	private Object txtQty;
	private Object dialogConfimParsedEndDate;
	private Object dialogConfimUpdateTarget;
	
	private TargetServiceItemDao targetServiceItemDao;
	private TargetDao targetDao;
	private ServiceItemDao serviceItemDao;
	private IncomingPaymentDao incomingPaymentDao;
	private List<TargetServiceItem> selectedTargetServiceItemsLst;
	private List<TargetServiceItem> deletedTargetServiceItemsLst;
	private BigDecimal totalAmountPaid = BigDecimal.ZERO;
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private ServiceItem selectedServiceItem;
	
	private Date startDate;
	private Date endDate;
	private Date tempStartDate;
	private Date tempEndDate;
	private TargetAnalytics targetAnalytics;

	private final CreateSettingsTableHandler createsettingstblhndler;
	private PaymentViewPluginController pluginController;
	
	public EditTargetHandler(PaymentViewPluginController pluginController, Target tgt, CreateSettingsTableHandler createsettingstblhndler) {
		this.ui = pluginController.getUiGeneratorController();
		this.tgt = tgt;
		this.pluginController = pluginController;
		this.createsettingstblhndler = createsettingstblhndler;
		this.targetAnalytics = new TargetAnalytics();
		this.incomingPaymentDao  = pluginController.getIncomingPaymentDao();
		this.targetDao = pluginController.getTargetDao();
		this.targetAnalytics.setIncomingPaymentDao(this.incomingPaymentDao);
		this.targetAnalytics.setTargetDao(this.targetDao);
		this.targetServiceItemDao = pluginController.getTargetServiceItemDao();
		this.serviceItemDao = pluginController.getServiceItemDao();
		init();
		refresh();	
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return this.dialogComponent;
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EDIT_TARGET, this);
		lblClientName = ui.find(dialogComponent, TXT_CLIENT_NAME);
		lblStartDate = ui.find(dialogComponent, TXT_START_DATE);
		lblTotalTargetCost = ui.find(dialogComponent, TXT_TARGET_COST);
		lblTotalTargetAmountPaid = ui.find(dialogComponent, TXT_TARGET_AMOUNT_PAID);
		txtEndDate = ui.find(dialogComponent, TXT_END_DATE);
		cmbtargets = ui.find(dialogComponent, CMB_TARGETS);
		txtQty = ui.find(dialogComponent, TXT_QTY);
		
		tblServiceItemsComponent = ui.find(dialogComponent, TBL_SERVICE_ITEM);
		tblIncomingPaymentsComponent = ui.find(dialogComponent, TBL_INCOMING_PAYMENTS);
		ui.setText(lblClientName, this.tgt.getAccount().getClient().getFullName());
		ui.setText(lblStartDate,  dateFormat.format(this.tgt.getStartDate()));
		ui.setText(txtEndDate,  dateFormat.format(this.tgt.getEndDate()));
		ui.setText(lblTotalTargetCost,  this.tgt.getTotalTargetCost().toString());
		addChoices();
		this.selectedTargetServiceItemsLst = getTargetServiceItemDao().getAllTargetServiceItemByTarget(this.tgt.getId());
		for (TargetServiceItem tsi: selectedTargetServiceItemsLst) {
			ui.add(this.tblServiceItemsComponent, getRow(tsi));
		}
		List<IncomingPayment> ipLst = getIncomingPaymentDao().getActiveIncomingPaymentsByTarget(this.tgt.getId());
 		for (IncomingPayment ip: ipLst) {
			ui.add(this.tblIncomingPaymentsComponent, getRow(ip));
			this.totalAmountPaid = this.totalAmountPaid.add(ip.getAmountPaid());
		}
 		ui.setText(lblTotalTargetAmountPaid, this.totalAmountPaid.toString());
 		
 		this.deletedTargetServiceItemsLst = new ArrayList<TargetServiceItem>();
	}

	private void addChoices() {
		List<ServiceItem> allServiceItems = getServiceItemDao().getAllServiceItems();
		ui.removeAll(cmbtargets);
		for(ServiceItem serviceItem : allServiceItems){
			ui.add(cmbtargets, 
					ui.createComboboxChoice(serviceItem.getTargetName(), serviceItem));
		}
	}
	
	protected Object getRow(TargetServiceItem targeServiceItem) {
		Object row = ui.createTableRow(targeServiceItem);
		ui.add(row, ui.createTableCell(targeServiceItem.getServiceItem().getTargetName()));
		ui.add(row, ui.createTableCell(targeServiceItem.getServiceItemQty()));
		ui.add(row, ui.createTableCell(targeServiceItem.getAmount().toString()));
		return row;
	}
	
	protected Object getRow(IncomingPayment incomingPayment) {
		Object row = ui.createTableRow(incomingPayment);
		ui.add(row, ui.createTableCell(dateFormat.format(new Date(incomingPayment.getTimePaid()))));
		ui.add(row, ui.createTableCell(incomingPayment.getConfirmationCode()));
		ui.add(row, ui.createTableCell(incomingPayment.getPaymentBy()));
		ui.add(row, ui.createTableCell(incomingPayment.getAmountPaid().toString()));
		return row;
	}

	private void refresh() {
	}

	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}

	public void removeField() {
	}

	public List<TargetServiceItem> getSelectedTargetServiceItemsLst() {
		return selectedTargetServiceItemsLst;
	}

	public void setSelectedTargetServiceItemsLst(
			List<TargetServiceItem> selectedTargetServiceItemsLst) {
		this.selectedTargetServiceItemsLst = selectedTargetServiceItemsLst;
	}

	
	public TargetServiceItemDao getTargetServiceItemDao() {
		return targetServiceItemDao;
	}

	public ServiceItemDao getServiceItemDao() {
		return serviceItemDao;
	}

	public IncomingPaymentDao getIncomingPaymentDao() {
		return incomingPaymentDao;
	}

	public Target getTgt() {
		return tgt;
	}
	/* 
	 * This function will just update the selectedTargetServiceItemList.
	 * The update in the DB will be done while clicking on Update Target
	 */
    public void editQty(){
    	TargetServiceItem tgtServiceItem = getSelectedTgtServiceItemInTable();
    	if(tgtServiceItem!=null){
    		ViewEditTargetItemQtyHandler editTargetItemQtyHandler = new ViewEditTargetItemQtyHandler(this.pluginController, tgtServiceItem, this);
			ui.add(editTargetItemQtyHandler.getDialog());
    	} else {
    		ui.alert("No selected Service Items");
    	}
    }
    
    public void deleteServiceItem(){
    	TargetServiceItem tgtServiceItem = getSelectedTgtServiceItemInTable();
    	if(tgtServiceItem!=null){
    		selectedTargetServiceItemsLst.remove(tgtServiceItem);
    		deletedTargetServiceItemsLst.add(tgtServiceItem);
    	}
    	refreshSelectedTheTargetTable();
    }
    
	public void refreshSelectedTheTargetTable(){
		List<TargetServiceItem> lstServiceItem = getSelectedTargetServiceItemsLst();
		ui.removeAll(tblServiceItemsComponent);
		totalAmount = BigDecimal.ZERO;
		ui.setText(lblTotalTargetCost, "0.00");
		for(TargetServiceItem tsi: lstServiceItem){
			totalAmount = totalAmount.add(tsi.getAmount().multiply(new BigDecimal(tsi.getServiceItemQty())));
			ui.add(this.tblServiceItemsComponent, getRow(tsi));
			ui.setText(lblTotalTargetCost, totalAmount.toString());
		}
	}
    
	public Object getSelectedServiceItemRow() {
		return ui.getSelectedItem(tblServiceItemsComponent);
	}
    
	public TargetServiceItem getSelectedTgtServiceItemInTable() {
		Object row = getSelectedServiceItemRow();
		TargetServiceItem targetServiceItem = ui.getAttachedObject(row, TargetServiceItem.class);
		return targetServiceItem;
	}
    
    public void evaluate(){
    	
    }
	
    public void updateTargetAnyway() throws DuplicateKeyException{ 
    	ui.remove(dialogConfimUpdateTarget);
    	incomingPaymentDao = pluginController.getIncomingPaymentDao();
		List<TargetServiceItem> lstServiceItem = getSelectedTargetServiceItemsLst();
		
		totalAmount = BigDecimal.ZERO;
		for(TargetServiceItem tsi: lstServiceItem){
			totalAmount = totalAmount.add(tsi.getAmount().multiply(new BigDecimal(tsi.getServiceItemQty())));
		}
		
		if (totalAmount.compareTo(new BigDecimal(0))==1) {
			removeDialog();
			Target tgt = getTgt();
			tgt.setEndDate(getEndDate());
			tgt.setTotalTargetCost(totalAmount);
			targetDao.updateTarget(tgt); 
			tgt = targetDao.getTargetById(tgt.getId());
			tgt.setStatus(targetAnalytics.getStatus(tgt.getId()).toString());
			targetDao.updateTarget(tgt); 
			
			for (TargetServiceItem deletedTsi : deletedTargetServiceItemsLst){
				if (deletedTsi.getTarget()!=null) {
					targetServiceItemDao.deleteTargetServiceItem(deletedTsi);
				} 
			}
			for(TargetServiceItem tsi: lstServiceItem){
				if (tsi.getTarget()!=null) {
					targetServiceItemDao.updateTargetServiceItem(tsi);
				} else {
					tsi.setTarget(tgt);
					targetServiceItemDao.saveTargetServiceItem(tsi);
				}
			}
			createsettingstblhndler.refreshSettingsHandler();
		} else {
			ui.alert("Please set up at least one target service item.");
		}
    }
    
    public void updateTarget() {
    	if (validateEndDate()) {
			String methodToBeCalled = "updateTargetAnyway";
			CONFIRM_ACCEPT_SAVE_TARGET = "The changes cannot be reversed, do you want to proceed?";
			dialogConfimUpdateTarget = ((UiGeneratorController) ui).showConfirmationDialog(methodToBeCalled, this, CONFIRM_ACCEPT_SAVE_TARGET);
    	}
    }
	
	public void setParsedEndDate() {
		ui.remove(dialogConfimParsedEndDate);
		this.endDate = getTempEndDate();
		this.startDate = getTempStartDate();
		ui.setText(txtEndDate,  dateFormat.format(getEndDate()));
		updateTarget();
	}
	
    private boolean validateEndDate() {
		if (ui.getText(txtEndDate).equals("")) {
			ui.infoMessage("Please enter an end date.");
			return false;
		}
		
		if(parseDateRange(getTgt())){
			return true;
		}
		return false;
    } 
    
	public boolean parseDateRange(Target tgt){
		try {
			String strEndDate = ui.getText(txtEndDate);
			startDate = tgt.getStartDate();
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
    
    public void addServiceItem(String qty) {
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
			    this.selectedTargetServiceItemsLst.add(tsi);
				ui.add(this.tblServiceItemsComponent, getRow(tsi));
				ui.setText(txtQty, "");
				for (TargetServiceItem tempTsi: this.selectedTargetServiceItemsLst) {
					this.totalAmount = this.totalAmount.add(tempTsi.getAmount().multiply(new BigDecimal(qty)));
				}
				ui.setText(lblTotalTargetCost, totalAmount.toString());
			}
		} else {
			ui.alert("Invalid Quantity");
		}
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
    
	private boolean checkIfItemExists(ServiceItem sItem) {
		List<TargetServiceItem> tsiLst = getSelectedTargetServiceItemsLst();
		for (TargetServiceItem tsi: tsiLst) {
			if (tsi.getServiceItem().equals(sItem) && tsi.getAmount().equals(sItem.getAmount())){
				return true;
			}
		}
		return false;
	}
    
	private void readServiceItem() {
		Object selectedItem = ui.getSelectedItem(cmbtargets);
		selectedServiceItem = ui.getAttachedObject(selectedItem, ServiceItem.class);
	}
	
	public ServiceItem getSelectedServiceItem() {
		return selectedServiceItem;
	}
	
	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
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

				CONFIRM_ACCEPT_PARSED_DATE="The selected end date is incorrect. The parsed end date is: "+ PaymentViewUtils.formatDate(calEndDate.getTime()) +". Do you want to proceed?";
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
	
	private int getMonthsDiffFromStart(Calendar calStartDate,
			Calendar calNowDate) {
		return (calStartDate.get(Calendar.YEAR) - calNowDate.get(Calendar.YEAR)) * 12 +
		(calStartDate.get(Calendar.MONTH)- calNowDate.get(Calendar.MONTH)) + 
		(calStartDate.get(Calendar.DAY_OF_MONTH) >= calNowDate.get(Calendar.DAY_OF_MONTH)? 0: -1); 
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
}
