package org.creditsms.plugins.paymentview.ui.handler.export.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class ExportByClientXticsStep1Handler implements ThinletUiEventHandler {
	private static final String XML_EXPORT_BY_CLIENT_XTICS = "/ui/plugins/paymentview/export/dialogs/dlgExportByClientXtics.xml";
	
	private UiGeneratorController ui;	
	private Object dialogComponent;

	public ExportByClientXticsStep1Handler(UiGeneratorController ui) {
		this.ui = ui;
		init();
		refresh();
	}
	
	public void refresh() {
	}
	
	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EXPORT_BY_CLIENT_XTICS, this);	
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
	
	public void showDateSelecter(Object textField) {
		this.ui.showDateSelecter(textField);
	}
	
	public void next() {
		new ExportByClientXticsStep2Handler(ui).showWizard();
		removeDialog();
	}
}
