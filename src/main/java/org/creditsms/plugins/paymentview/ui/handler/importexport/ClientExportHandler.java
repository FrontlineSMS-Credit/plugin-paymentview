package org.creditsms.plugins.paymentview.ui.handler.importexport;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.importexport.ExportDialogHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.importexport.PaymentViewCsvExporter;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

public class ClientExportHandler extends ExportDialogHandler<Client> {
	private static final String COMPONENT_ACCOUNTS = "cbAccounts";
	private static final String COMPONENT_CB_NAME = "cbName";
	private static final String COMPONENT_CB_PHONE = "cbPhone";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_OPTIONS_PANEL_CLIENT = "/ui/plugins/paymentview/importexport/pnClientDetails.xml";

	private ClientDao clientDao;
	private CustomFieldDao customFieldDao;
	private AccountDao accountDao;
	private CustomValueDao customValueDao;
	

	public ClientExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(Client.class, ui);
		this.clientDao = pluginController.getClientDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		this.customValueDao = pluginController.getCustomValueDao();
		this.accountDao = pluginController.getAccountDao();
	}

	@Override
	public void doSpecialExport(String dataPath) throws IOException {
		log.debug("Exporting all clients..");
		exportClients(this.clientDao.getAllClients(), dataPath);
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
				customFieldDao, customValueDao, rowFormat, accountDao);
		uiController.setStatus(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
	}

	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CLIENT;
	}

	@Override
	public void showWizard() {
		List<CustomField> allCustomFields = this.customFieldDao
				.getAllActiveUsedCustomFields();
		if (!allCustomFields.isEmpty()) {
			for (CustomField cf : allCustomFields) {
					Object checkbox = uiController.createCheckbox(cf.getCamelCaseName(),
							cf.getReadableName(), true);
					uiController.add(optionsPanel, checkbox);
			}
		}
		super.showWizard();
	}

	protected CsvRowFormat getRowFormatForClient() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME,
				COMPONENT_CB_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME,
				COMPONENT_CB_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_PHONE,
				COMPONENT_CB_PHONE);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_ACCOUNTS,
				COMPONENT_ACCOUNTS);
		
		for(CustomField cf : customFieldDao.getAllActiveUsedCustomFields()){
			addMarker(rowFormat, PaymentViewUtils.getMarkerFromString(cf.getReadableName()),
					cf.getCamelCaseName());
		}
		return rowFormat;
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_EXPORTING_SELECTED_CONTACTS;
	}
	
	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.wizardDialog);
	}

	/** Remove a dialog from view. */
	@Override
	public void removeDialog(Object dialog) {
		this.uiController.removeDialog(dialog);
	}
}
