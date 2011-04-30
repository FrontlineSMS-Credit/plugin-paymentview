package org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.importexport.ExportDialogHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.importexport.PaymentViewCsvExporter;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.utils.PaymentType;

public class ExportByClientXticsStep2Handler extends
		ExportDialogHandler<Client> {
	private static final String COMPONENT_ACCOUNTS = "cbAccounts";

	private static final String COMPONENT_CB_FIRSTNAME = "cbFirstName";
	private static final String COMPONENT_CB_OTHERNAME = "cbOtherName";
	private static final String COMPONENT_CB_PHONE = "cbPhone";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_EXPORT_WIZARD_FORM = "/ui/plugins/paymentview/export/dialogs/dlgExportByClientXtics1.xml";
	private static final String UI_FILE_OPTIONS_PANEL_CLIENT = "/ui/plugins/paymentview/importexport/pnClientDetails.xml";

	private ClientDao clientDao;
	private String exportfilepath;
	private List<Client> selectedUsers;

	private PaymentType paymentType;

	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;

	public ExportByClientXticsStep2Handler(UiGeneratorController ui,
			CustomValueDao customValueDao, CustomFieldDao customFieldDao,
			final List<Client> selectedUsers, final PaymentType paymentType) {
		super(Client.class, ui);
		this.selectedUsers = selectedUsers;
		this.paymentType = paymentType;
	}

	@Override
	public void doSpecialExport(String dataPath) throws IOException {
		log.debug("Exporting selected clients..");
		exportClients(selectedUsers, dataPath);
	}

	@Override
	public void doSpecialExport(String dataPath, List<Client> selected)
			throws IOException {
		exportClients(selected, dataPath);
	}

	/**
	 * Export the supplied contacts using settings set in {@link #wizardDialog}.
	 * 
	 * @param clients
	 *            The contacts to export
	 * @param filename
	 *            The file to export the contacts to
	 * @throws IOException
	 */
	private void exportClients(List<Client> clients, String filename)
			throws IOException {
		CsvRowFormat rowFormat = getRowFormatForClient();

		if (!rowFormat.hasMarkers()) {
			uiController.alert(InternationalisationUtils
					.getI18nString(MESSAGE_NO_FIELD_SELECTED));
			log.trace("EXIT");
			return;
		}

		log.debug("Row Format [" + rowFormat + "]");

		PaymentViewCsvExporter.exportClients(new File(filename), clients,
				customFieldDao, customValueDao, rowFormat);
		uiController.setStatus(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
	}

	@Override
	protected String getDialogFile() {
		return UI_FILE_EXPORT_WIZARD_FORM;
	}

	/**
	 * @return the exportfilepath
	 */
	public String getExportfilepath() {
		return exportfilepath;
	}

	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CLIENT;
	}

	protected CsvRowFormat getRowFormatForClient() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME,
				COMPONENT_CB_FIRSTNAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME,
				COMPONENT_CB_OTHERNAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_PHONE,
				COMPONENT_CB_PHONE);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_ACCOUNTS,
				COMPONENT_ACCOUNTS);
		return rowFormat;
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
		uiController.add(new ExportByClientXticsStep3Handler(uiController,
				this, exportfilepath, selectedUsers).getDialog());
		removeDialog();
	}

	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.wizardDialog);
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.uiController.removeDialog(dialog);
	}
}
