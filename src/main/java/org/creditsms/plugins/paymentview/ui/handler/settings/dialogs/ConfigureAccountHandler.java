package org.creditsms.plugins.paymentview.ui.handler.settings.dialogs;

import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;
import org.creditsms.plugins.paymentview.data.repository.NetworkOperatorDao;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class ConfigureAccountHandler implements ThinletUiEventHandler {
	private static final String XML_CONFIGURE_ACCOUNT = "/ui/plugins/paymentview/settings/dialogs/dlgConfigureAccount.xml";

	private UiGeneratorController ui;
	private Object dialogComponent;

	private Object compPanelFields;

	public ConfigureAccountHandler(UiGeneratorController ui) {
		this.ui = ui;
		init();
		refresh();
	}

	public ConfigureAccountHandler(UiGeneratorController ui,
			NetworkOperator networkOperator, NetworkOperatorDao networkOperatorDao) {
		this(ui);
	}

	private void refresh() {		
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CONFIGURE_ACCOUNT,
				this);
	}

	public void updateAccPIN() {
		// TODO Auto-generated method stub

	}
	
	public void updateAuthSettings() {
		// TODO Auto-generated method stub

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

	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
}