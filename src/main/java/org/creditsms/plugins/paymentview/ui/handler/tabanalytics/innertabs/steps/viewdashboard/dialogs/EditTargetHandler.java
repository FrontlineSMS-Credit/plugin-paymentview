package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.dialogs;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetServiceItemDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient.EditTargetItemQtyHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.CreateSettingsTableHandler;

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
	
	private TargetServiceItemDao targetServiceItemDao;
	private ServiceItemDao serviceItemDao;
	private IncomingPaymentDao incomingPaymentDao;
	private List<TargetServiceItem> selectedTargetServiceItemsLst;
	private BigDecimal totalAmountPaid = BigDecimal.ZERO;
	private BigDecimal totalAmount = BigDecimal.ZERO;
	private ServiceItem selectedServiceItem;

	private final CreateSettingsTableHandler createsettingstblhndler;
	private PaymentViewPluginController pluginController;
	public EditTargetHandler(PaymentViewPluginController pluginController, Target tgt, CreateSettingsTableHandler createsettingstblhndler) {
		this.ui = pluginController.getUiGeneratorController();
		this.tgt = tgt;
		this.createsettingstblhndler = createsettingstblhndler;
		this.targetServiceItemDao = pluginController.getTargetServiceItemDao();
		this.serviceItemDao = pluginController.getServiceItemDao();
		this.incomingPaymentDao  = pluginController.getIncomingPaymentDao();
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
		List<TargetServiceItem> tsiLst = getTargetServiceItemDao().getAllTargetServiceItemByTarget(this.tgt.getId());
		for (TargetServiceItem tsi: tsiLst) {
			ui.add(this.tblServiceItemsComponent, getRow(tsi));
		}
		List<IncomingPayment> ipLst = getIncomingPaymentDao().getActiveIncomingPaymentsByTarget(this.tgt.getId());
 		for (IncomingPayment ip: ipLst) {
			ui.add(this.tblIncomingPaymentsComponent, getRow(ip));
			this.totalAmountPaid = this.totalAmountPaid.add(ip.getAmountPaid());
		}
 		ui.setText(lblTotalTargetAmountPaid, this.totalAmountPaid.toString());
 		
		setSelectedTargetServiceItemsLst(tsiLst);
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

	public void updateServiceItem(String txtServiceItemName, String txtServiceItemAmount) {
		if (!(txtServiceItemAmount.isEmpty() & txtServiceItemName.isEmpty())){
			ServiceItemDao serviceItemDao = pluginController.getServiceItemDao();
			Target target = getTgt();
			//target.setTargetName(txtServiceItemName);
			//target.setAmount(new BigDecimal(txtServiceItemAmount));
//			try {
//				serviceItemDao.updateServiceItem(this.tgt);
//				//createsettingstblhndler.refreshServiceItemTable();
//				ui.alert("Item Updated successfully!");	
//			} catch (DuplicateKeyException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}								
			removeDialog();
		}else{
			ui.alert("Please fill all the fields!");
		}
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
	
    public void editQty(){
//    	TargetServiceItem tgtServiceItem = getSelectedTgtServiceItemInTable();
//    	if(tgtServiceItem!=null){
//			EditTargetItemQtyHandler editTargetItemQtyHandler = new EditTargetItemQtyHandler(pluginController, tgtServiceItem, this);
//			ui.add(editTargetItemQtyHandler.getDialog());
//    	} else {
//    		ui.alert("No selected Service Items");
//    	}
    }
    
    public void evaluate(){
    	
    }
	
    public void updateTarget() {
    	
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
				this.totalAmount = this.totalAmount.add(sItem.getAmount().multiply(new BigDecimal(qty)));
				ui.add(this.tblServiceItemsComponent, getRow(tsi));
				ui.setText(lblTotalTargetCost, totalAmount.toString());
				ui.setText(txtQty, "");
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
}
