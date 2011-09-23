package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;

public class EditTargetItemQtyHandler extends BasePanelHandler implements ThinletUiEventHandler {
	private static final String XML_TARGET_ITEM_EDIT_QTY = "/ui/plugins/paymentview/analytics/addclient/dlgEditTargetItemQty.xml";
	private static final String TXT_QTY = "txtPopUpQty";

	private Object dialogComponent;
	private Object txtQty;
	private UiGeneratorController ui;
	private TargetServiceItem tsi;

	public EditTargetItemQtyHandler(UiGeneratorController ui, PaymentViewPluginController pluginController,
			TargetServiceItem tsi) {
		super(ui);
		this.tsi = tsi;
		this.ui = pluginController.getUiGeneratorController();
		init();
		refresh();
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
    
	public void createField(String newQty) throws DuplicateKeyException {
		if(checkIfInt(newQty)){
            if(tsi.getServiceItemQty()==Integer.parseInt(newQty)){
            	ui.alert(tsi.getServiceItem().getTargetName() + "'s qty has not been changed.");
            	this.removeDialog();
            }
            //todo
			this.removeDialog();
			ui.infoMessage("You have succesfully  changed '" + tsi.getServiceItem().getTargetName() + "'s qty.");
		} else {
			ui.alert("Invalid Quantity");
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

	public void init() {
		txtQty = ui.find(this.getPanelComponent(), TXT_QTY);
		ui.setText(txtQty,  Integer.toString(tsi.getServiceItemQty()));
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
}
