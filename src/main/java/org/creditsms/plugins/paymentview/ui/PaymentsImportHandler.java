package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.ui.UiGeneratorController;
import org.creditsms.plugins.paymentview.ui.ImportDialogHandler;

public class PaymentsImportHandler extends ImportDialogHandler{
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORTING_SELECTED_CONTACTS = "message.importing.contacts.groups";
	/** i18n Text Key: "Active" */
	private static final String I18N_COMMON_ACTIVE = "common.active";

	public PaymentsImportHandler(UiGeneratorController ui) {
		super(ui);
	}

	@Override
	void doSpecialImport(String dataPath) throws CsvParseException {
		// TODO Auto-generated method stub

	}

	@Override
	protected CsvImporter getImporter() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setImporter(String filename) throws CsvParseException {
		// TODO Auto-generated method stub

	}

	@Override
	protected void appendPreviewHeaderItems(Object header) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object[] getPreviewRows() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWizardTitleI18nKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOptionsFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

}
