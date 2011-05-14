package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs;

import java.math.BigDecimal;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;

public class CreateNewServiceItemHandler implements ThinletUiEventHandler {
	private static final String XML_CREATE_NEW_TARGET = "/ui/plugins/paymentview/analytics/dialogs/dlgCreateNewTarget.xml";

	private Object dialogComponent;
	private UiGeneratorController ui;

	private PaymentViewPluginController pluginController;

	public CreateNewServiceItemHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		this.ui = ui;
		this.pluginController = pluginController;
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
		dialogComponent = ui.loadComponentFromFile(XML_CREATE_NEW_TARGET, this);
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

	public void createServiceItem(String txtServiceItemName, String txtServiceItemAmount) {
		if (!(txtServiceItemAmount.isEmpty() & txtServiceItemName.isEmpty())){
			ServiceItemDao serviceItemDao = pluginController.getServiceItemDao();
			ServiceItem serviceItem = new ServiceItem();
			serviceItem.setTargetName(txtServiceItemName);
			serviceItem.setAmount(new BigDecimal(txtServiceItemAmount));
			
			serviceItemDao.saveServiceItem(serviceItem);
								
			ui.alert("Item Created successfully!");			
			removeDialog();
		}else{
			ui.alert("Please fill all the fields!");
		}
	}
}
