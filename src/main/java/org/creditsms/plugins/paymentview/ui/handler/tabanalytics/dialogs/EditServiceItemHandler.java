package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs;

import java.math.BigDecimal;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.plugins.payment.service.PaymentServiceException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ConfigureServiceTabHandler;

public class EditServiceItemHandler implements ThinletUiEventHandler {
	private static final String XML_EDIT_TARGET = "/ui/plugins/paymentview/analytics/dialogs/dlgEditTarget.xml";
	private static final String TXT_TARGET_ITEM_NAME = "txtServiceItemName";
	private static final String TXT_TARGET_COST = "txtServiceItemAmount";

	private Object dialogComponent;
	private UiGeneratorController ui;
	private ServiceItem si;
	private Object txtTargetName;
	private Object txtTargetCost;
	private final ConfigureServiceTabHandler confSItem;
	private PaymentViewPluginController pluginController;

	public EditServiceItemHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController, ServiceItem si,
			ConfigureServiceTabHandler confSItem) {
		this.ui = ui;
		this.pluginController = pluginController;
		this.si = si;
		this.confSItem = confSItem;
		init();
		refresh();
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EDIT_TARGET, this);
		txtTargetName = ui.find(dialogComponent, TXT_TARGET_ITEM_NAME);
		txtTargetCost = ui.find(dialogComponent, TXT_TARGET_COST);
		ui.setText(txtTargetName, this.si.getTargetName());
		ui.setText(txtTargetCost, this.si.getAmount().toString());
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
		boolean validAmount = true;
		if (!(txtServiceItemAmount.isEmpty() & txtServiceItemName.isEmpty())){
			try {
				if (new BigDecimal(txtServiceItemAmount) != null) {
				} else {
					validAmount = false;
				}
			} catch (Exception ex) {
				validAmount = false;
			}
			if(validAmount){
				ServiceItemDao serviceItemDao = pluginController.getServiceItemDao();
				ServiceItem serviceItem = getSi();
				serviceItem.setTargetName(txtServiceItemName);
				serviceItem.setAmount(new BigDecimal(txtServiceItemAmount));
				try {
					serviceItemDao.updateServiceItem(this.si);
					confSItem.refreshServiceItemTable();
					ui.alert("Item Updated successfully!");	
				} catch (DuplicateKeyException e) {
					e.printStackTrace();
				}			
				removeDialog();
			} else {
				ui.alert("Invalid Target Amount");
			}
		} else {
			ui.alert("Please fill all the fields!");
		}
	}

	public ServiceItem getSi() {
		return si;
	}
}
