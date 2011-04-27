package org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;

public class ExportByClientXticsStep1Handler implements ThinletUiEventHandler {
	private static final String XML_EXPORT_BY_CLIENT_XTICS = "/ui/plugins/paymentview/export/dialogs/dlgExportByClientXtics.xml";

	private ClientDao clientDao;
	private Object dialogComponent;

	private UiGeneratorController ui;

	public ExportByClientXticsStep1Handler(UiGeneratorController ui,
			ClientDao clientDao) {
		this.ui = ui;
		this.clientDao = clientDao;
		init();
		refresh();
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
		new ExportByClientXticsStep2Handler(ui, clientDao).showWizard();
		removeDialog();
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
