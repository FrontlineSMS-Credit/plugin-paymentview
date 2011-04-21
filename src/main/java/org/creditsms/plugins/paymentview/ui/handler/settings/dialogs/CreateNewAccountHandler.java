package org.creditsms.plugins.paymentview.ui.handler.settings.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;

public class CreateNewAccountHandler implements ThinletUiEventHandler {
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep1.xml";

	private AccountDao accountDao;
	private Object compPanelFields;

	private Object dialogComponent;
	private UiGeneratorController ui;

	public CreateNewAccountHandler(UiGeneratorController ui,
			AccountDao accountDao) {
		this.ui = ui;
		this.accountDao = accountDao;
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
		dialogComponent = ui.loadComponentFromFile(XML_CONFIGURE_ACCOUNT, this);
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
}