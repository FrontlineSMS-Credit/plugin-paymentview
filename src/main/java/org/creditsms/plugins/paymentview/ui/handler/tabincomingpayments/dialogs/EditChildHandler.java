package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;


import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs.DistributeIncomingPaymentDialogHandler.Child;

public class EditChildHandler implements ThinletUiEventHandler {
	private static final String XML_EDIT_CHILD = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgEditChild.xml";
	private static final String TXT_AMOUNT = "txtPopUpAmount";
	private final DistributeIncomingPaymentDialogHandler distributeIncomingPaymentDialogHandler;

	private Object dialogComponent;
	private Object txtAmount;
	private UiGeneratorController ui;
	private Child child;
	private ServiceItemDao serviceItemDao;
	
	public EditChildHandler(PaymentViewPluginController pluginController,
			Child child, DistributeIncomingPaymentDialogHandler distributeIncomingPaymentDialogHandler) {
		this.child = child;
		this.ui = pluginController.getUiGeneratorController();
		this.distributeIncomingPaymentDialogHandler = distributeIncomingPaymentDialogHandler;
		init();
		refresh();
	}
	
	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EDIT_CHILD, this);
		ui.setText(dialogComponent, "Edit "+child.getClient().getFullName()+"'s Amount");
		txtAmount = ui.find(dialogComponent, TXT_AMOUNT);
		ui.setText(txtAmount,child.getAmount().toString());
	}
	
    boolean checkIfBigDecimal(String bdStr) {
        try {
        	BigDecimal bd = new BigDecimal(bdStr);
        } catch (NumberFormatException ex) {
        	ui.alert("Invalid Amount");
            return false;
        }
        return true;
    }
    
	public void persistAmount(String newAmountStr) throws DuplicateKeyException {
		if(checkIfBigDecimal(newAmountStr)){
			BigDecimal newAmount = new BigDecimal(newAmountStr).setScale(2,RoundingMode.HALF_EVEN);
            if(child.getAmount().equals(newAmount)){
            	ui.alert(child.getClient().getFullName() + "'s amount has not been changed.");
            	this.removeDialog();
            } else {
                	List<Child> children = distributeIncomingPaymentDialogHandler.getChildren();
                	for (Child child: children){
                		if (this.child.equals(child)) {
                			child.setAmount(newAmount);
                		}
                	}
                	distributeIncomingPaymentDialogHandler.setChildren(children);
                	distributeIncomingPaymentDialogHandler.refreshSelectedChildrenTable();
        			this.removeDialog();
        			ui.infoMessage("You have successfully  changed " + child.getClient().getFullName() + "'s amount.");
            	 
            }
		} else {
			ui.alert("Invalid Amount");
		}
	}
	

	public static String camelCase(String string) {
		String result = "";
		for (int i = 0; i < string.length(); i++) {
			String next = string.substring(i, i + 1);
			if (i == 0) {
				result += next.toUpperCase();
			} else {
				result += next.toLowerCase();
			}
		}
		return result;
	}

	public Object getDialog() {
		return this.dialogComponent;
	}



	public void refresh() {
	}

	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}

	public ServiceItemDao getServiceItemDao() {
		return serviceItemDao;
	}
	
}
