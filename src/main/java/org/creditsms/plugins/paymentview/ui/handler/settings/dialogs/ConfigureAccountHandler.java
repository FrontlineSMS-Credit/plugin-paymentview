package org.creditsms.plugins.paymentview.ui.handler.settings.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;

public class ConfigureAccountHandler implements ThinletUiEventHandler {
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/dlgConfigureAccount.xml";

	private Account account;
	private AccountDao accountDao;
	private Object dialogComponent;
	private UiGeneratorController ui;

	public ConfigureAccountHandler(UiGeneratorController ui) {
		this.ui = ui;
		init();
		refresh();
	}

	public ConfigureAccountHandler(UiGeneratorController ui, Account account,
			AccountDao accountDao) {
		this(ui);
		this.account = account;
		this.accountDao = accountDao;
	}

	public void deteteAccount() {
		// TODO Auto-generated method stub

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

	public void updateAccPIN() {
		// TODO Auto-generated method stub

	}

	public void updateAuthSettings() {
		// TODO Auto-generated method stub

	}
}