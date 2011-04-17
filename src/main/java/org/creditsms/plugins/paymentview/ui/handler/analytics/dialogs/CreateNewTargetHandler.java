package org.creditsms.plugins.paymentview.ui.handler.analytics.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class CreateNewTargetHandler implements ThinletUiEventHandler {
	private static final String XML_CREATE_NEW_TARGET = "/ui/plugins/paymentview/analytics/dialogs/dlgCreateNewTarget.xml";

	private UiGeneratorController ui;
	private Object dialogComponent;

	public CreateNewTargetHandler(UiGeneratorController ui) {
		this.ui = ui;
		init();
		refresh();
	}

	private void refresh() {		
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CREATE_NEW_TARGET,
				this);
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

	public void removeField() {
		// TODO Auto-generated method stub

	}

}
