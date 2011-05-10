package org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs;

import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;

public class ExportByClientXticsStep2Handler extends ClientExportHandler {
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_EXPORT_WIZARD_FORM = "/ui/plugins/paymentview/export/dialogs/dlgExportByClientXtics1.xml";
	private static final String UI_FILE_OPTIONS_PANEL_CLIENT = "/ui/plugins/paymentview/importexport/pnClientDetails.xml";

	private String exportfilepath;
	private final ExportByClientXticsStep1Handler exportByClientXticsStep1Handler;

	public ExportByClientXticsStep2Handler(
			final ExportByClientXticsStep1Handler exportByClientXticsStep1Handler) {
		super(exportByClientXticsStep1Handler.getUi(),
				exportByClientXticsStep1Handler.getClientDao(),
				exportByClientXticsStep1Handler.getCustomFieldDao(),
				exportByClientXticsStep1Handler.getCustomValueDao());
		
		this.exportByClientXticsStep1Handler = exportByClientXticsStep1Handler;
	}

	@Override
	protected String getDialogFile() {
		return UI_FILE_EXPORT_WIZARD_FORM;
	}

	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CLIENT;
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_EXPORTING_SELECTED_CONTACTS;
	}

	public void handleDoExport() {
		super.handleDoExport(this.exportfilepath);
	}

	public void next(String exportfilepath) {
		this.exportfilepath = exportfilepath;
		ExportByClientXticsStep3Handler step3Handler = new ExportByClientXticsStep3Handler(uiController, this);
		uiController.add(step3Handler.getDialog());
		removeDialog();
	}
	
	public void previous(){
		uiController.add(exportByClientXticsStep1Handler.getDialog());
		removeDialog();
	}
}
