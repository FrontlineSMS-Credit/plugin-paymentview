package org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class ExportByClientXticsStep3Handler implements ThinletUiEventHandler {
	private static final String XML_EXPORT_BY_CLIENT_XTICS = "/ui/plugins/paymentview/export/dialogs/dlgExportByClientXtics2.xml";

	private Object dialogComponent;
	private ExportByClientXticsStep2Handler previousObj;
	private UiGeneratorController ui;

	public ExportByClientXticsStep3Handler(UiGeneratorController ui,
			ExportByClientXticsStep2Handler previousObj) {
		this.ui = ui;
		init();
		refresh();
		this.previousObj = previousObj;
	}

	public void export() {
		previousObj.handleDoExport();
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EXPORT_BY_CLIENT_XTICS,
				this);
	}

	public void next() {
	}

	public void refresh() {
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
}
