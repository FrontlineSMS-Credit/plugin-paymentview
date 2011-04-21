package org.creditsms.plugins.paymentview.ui.handler.settings.dialogs;

import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class CreateNewAccountHandler implements ThinletUiEventHandler {
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep1.xml";

	private UiGeneratorController ui;
	private Object dialogComponent;

	private Object compPanelFields;

	public CreateNewAccountHandler(UiGeneratorController ui, NetworkOperatorDao networkOperatorDao) {
		this.ui = ui;
		init();
		refresh();
	}
	
	private void refresh() {		
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CONFIGURE_ACCOUNT,this);
	}
	
	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
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