package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;

import java.math.BigDecimal;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.IncomingPaymentsTabHandler.Child;

public class DistributeIncomingPaymentDialogHandler extends BaseDialog{
//> CONSTANTS
	private static final String COMPONENT_TEXT_PARENT_NAME = "fldName";
	private static final String COMPONENT_TEXT_PARENT_PHONE_NUMBER = "fldPhoneNumber";
	private static final String COMPONENT_TEXT_PARENT_AMOUNT = "fldAmount";
	private static final String TBL_CHILDREN = "tbl_children";
	private static String XML_DISTRIBUTE_INCOMING = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgDistributeIncomingPayment.xml";

	
//> UI FIELDS
	private Object fieldName;
	private Object fieldPhoneNumber;
	private Object fieldAmount;

//UI HELPERS	
	private IncomingPayment parentIncomingPayment;
	private List<Child> children;
	private ClientDao clientDao;
	private Object tblChildrenComponent;
	private PaymentViewPluginController pluginController;
	private BigDecimal totalAmount;
	


	
//	public DistributeIncomingPaymentDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
//			IncomingPayment parentIncomingPayment, List<Client> clientList) {
//		super(ui);
//		this.pluginController = pluginController;
//		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
//		this.clientDao = pluginController.getClientDao();
//		this.parentIncomingPayment = parentIncomingPayment;
//		this.children = new ArrayList<Child>(clientList.size());
//		for (Client c:clientList){
//			children.add(new Child(c,new BigDecimal("0.00")));
//		}
//		this.totalAmount = new BigDecimal("0.00");
//		init();
//		refresh();
//	}
	
	public DistributeIncomingPaymentDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
			IncomingPayment parentIncomingPayment, List<Child> children) {
		super(ui);
		this.pluginController = pluginController;
		this.clientDao = pluginController.getClientDao();
		this.parentIncomingPayment = parentIncomingPayment;
		this.children =children;

		init();
		refresh();
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_DISTRIBUTE_INCOMING, this);
		tblChildrenComponent = ui.find(dialogComponent, TBL_CHILDREN);
		
		fieldName = ui.find(dialogComponent, COMPONENT_TEXT_PARENT_NAME);
		fieldPhoneNumber = ui.find(dialogComponent, COMPONENT_TEXT_PARENT_PHONE_NUMBER);
		fieldAmount = ui.find(dialogComponent, COMPONENT_TEXT_PARENT_AMOUNT);
		ui.setText(fieldName, clientDao.getClientByPhoneNumber(this.parentIncomingPayment.getPhoneNumber()).getFullName());			
		ui.setText(fieldPhoneNumber, this.parentIncomingPayment.getPhoneNumber());
		ui.setText(fieldAmount, this.parentIncomingPayment.getAmountPaid().toPlainString());
		
		totalAmount = BigDecimal.ZERO;
		for(Child child: children){
			totalAmount = totalAmount.add(child.getAmount());
			ui.add(this.tblChildrenComponent, getRow(child));
		}
	}
	
	protected Object getRow(Child child) {
		Object row = ui.createTableRow(child);
		ui.add(row, ui.createTableCell(child.getClient().getPhoneNumber()));
		ui.add(row, ui.createTableCell(child.getClient().getFullName()));
		ui.add(row, ui.createTableCell(child.getAmount().toPlainString()));
		return row;
	}
	
	public Child getSelectedChild() {
		Object row =  ui.getSelectedItem(tblChildrenComponent);
		Child child = ui.getAttachedObject(row, Child.class);
		return child;
	}

	public void editAmount(){
    	Child child = getSelectedChild();
    	if(child!=null){
    		EditChildHandler editChildHandler = new EditChildHandler(this.pluginController, child, this);
			ui.add(editChildHandler.getDialog());
    	} else {
    		ui.alert("No selected client");
    	}
	}
	
	public void refreshSelectedChildrenTable(){
		List<Child> children = getChildren();
		ui.removeAll(tblChildrenComponent);
		totalAmount = BigDecimal.ZERO;
		for(Child child: children){
			totalAmount = totalAmount.add(child.getAmount());
			ui.add(this.tblChildrenComponent, getRow(child));
		}
	}
	
	@Override
	public void refresh() {
	}

	public void next() throws DuplicateKeyException {
		//validate total
		if (totalAmount.equals(parentIncomingPayment.getAmountPaid())){
			new DistributeConfirmationDialogHandler(ui, pluginController, this.parentIncomingPayment, this.children,this).showDialog();
			this.removeDialog();
		} else {
			ui.alert("The selected amounts do not add up to the amount of the group payment.");
		}
	}
	
	public List<Child> getChildren() {
		return children;
	}
	
	public void setChildren(
			List<Child> children) {
		this.children = children;
	}
	
	@Override
	public void showDialog() {
		ui.add(this.dialogComponent);
	}
	



}
