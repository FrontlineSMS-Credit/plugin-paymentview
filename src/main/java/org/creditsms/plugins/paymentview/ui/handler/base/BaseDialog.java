package org.creditsms.plugins.paymentview.ui.handler.base;

import thinlet.Thinlet;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class BaseDialog implements ThinletUiEventHandler{

	protected UiGeneratorController ui;
	protected Object dialogComponent;

	public BaseDialog(UiGeneratorController ui) {
		this.ui = ui;
	}
	
	protected void refresh() {}
	
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
	
	/** Remove a dialog from view. */
	public Object find(String object) {
		return Thinlet.find(this.dialogComponent, object);
	}
	
	public void showDialog() {
		ui.add(this.dialogComponent);
	}
}