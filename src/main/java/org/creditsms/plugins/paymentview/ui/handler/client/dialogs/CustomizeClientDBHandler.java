package org.creditsms.plugins.paymentview.ui.handler.client.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class CustomizeClientDBHandler implements ThinletUiEventHandler {
	private static final String XML_CUSTOMIZE_CLIENT_DB = "/ui/plugins/paymentview/clients/dialogs/dlgCustomizeClient.xml";
	
	private UiGeneratorController ui;	
	private Object dialogComponent;
	
	public CustomizeClientDBHandler(UiGeneratorController ui) {
		this.ui = ui;
		init();
		refresh();
	}

	private void refresh() {
		// TODO Auto-generated method stub
		
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CUSTOMIZE_CLIENT_DB, this);		
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
	
	public void addField() {
		// TODO Auto-generated method stub

	}
	
	public void removeField() {
		// TODO Auto-generated method stub

	}

}
