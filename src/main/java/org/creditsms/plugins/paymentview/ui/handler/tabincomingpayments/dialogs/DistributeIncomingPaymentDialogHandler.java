package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;

public class DistributeIncomingPaymentDialogHandler extends BaseDialog{
//> CONSTANTS
	private static final String COMPONENT_TEXT_PARENT_NAME = "fldName";
	private static final String COMPONENT_TEXT_PARENT_PHONE_NUMBER = "fldPhoneNumber";
	private static final String COMPONENT_TEXT_PARENT_AMOUNT = "fldAmount";
	private static final String TBL_CHILDREN = "tbl_children";
	private static String XML_DISTRIBUTE_INCOMING = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgDistributeIncomingPayment.xml";

//>DAOs
	private IncomingPaymentDao incomingPaymentDao;
	
//> UI FIELDS
	private Object fieldName;
	private Object fieldPhoneNumber;
	private Object fieldAmount;

//UI HELPERS	
	private IncomingPayment parentIncomingPayment;
	private List<Child> children;
	private ClientDao clientDao;
	private Object tblChildrenComponent;
	


	
	public DistributeIncomingPaymentDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
			IncomingPayment parentIncomingPayment, List<Client> clientList) {
		super(ui);
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.clientDao = pluginController.getClientDao();
		this.parentIncomingPayment = parentIncomingPayment;
		this.children = new ArrayList<Child>(clientList.size());
		for (Client c:clientList){
			children.add(new Child(c,new BigDecimal(0)));
		}
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
		
		for(Child child: children) {
			ui.add(this.tblChildrenComponent, getRow(child));
		}
	}
	
	protected Object getRow(Child child) {
		Object row = ui.createTableRow(child);
		ui.add(row, ui.createTableCell(child.getClient().getPhoneNumber()));
		ui.add(row, ui.createTableCell(child.getClient().getFullName()));
		ui.add(row, ui.createTableCell(child.getAmount().toString()));
		return row;
	}

	public void editAmount(){
		
	}
	
	@Override
	public void refresh() {
	}

	public void next() throws DuplicateKeyException {
//			parentIncomingPayment.setPaymentId(ui.getText(fieldPaymentId));
//			parentIncomingPayment.setNotes(ui.getText(fieldNotes));
//			incomingPaymentDao.updateIncomingPayment(this.parentIncomingPayment);
//			removeDialog();
	}
	
	@Override
	public void showDialog() {
		ui.add(this.dialogComponent);
	}
	
	private class Child {
		private Client client;
		private BigDecimal amount;
		
		Child(Client client,BigDecimal amount){
			this.client = client;
			this.amount = amount;
		}
		
		Client getClient(){
			return client;
		}
		BigDecimal getAmount(){
			return amount;
		}
	}


}
