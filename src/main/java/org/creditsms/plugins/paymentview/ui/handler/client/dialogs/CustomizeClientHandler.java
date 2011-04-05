package org.creditsms.plugins.paymentview.ui.handler.client.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class CustomizeClientHandler implements ThinletUiEventHandler {
	private UiGeneratorController ui;
	private String XML_CUSTOMIZE_CLIENT = "/ui/plugins/paymentview/clients/dialogs/dlgCustomizeClient.xml";
	private Object dialogComponent; 
	
	public CustomizeClientHandler(UiGeneratorController ui){
		this.ui = ui;
		init();
	}
	
	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CUSTOMIZE_CLIENT, this);
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent; 
	}	
	
	public void saveClient(){		
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
