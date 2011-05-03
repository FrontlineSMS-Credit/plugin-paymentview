package org.creditsms.plugins.paymentview.ui.handler.importexport;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.csv.CsvExporter;
import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.importexport.ImportDialogHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.importexport.ClientCsvImporter;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;
import org.creditsms.plugins.paymentview.utils.PaymentPluginConstants;

public class ClientImportHandler extends ImportDialogHandler {
	private static final String COMPONENT_ACCOUNTS = "cbAccounts";
	private static final String COMPONENT_CB_NAME = "cbName";
	private static final String COMPONENT_CB_PHONE = "cbPhone";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORTING_SELECTED_CLIENTS = "plugins.paymentview.message.importing.selected.client";
	/** i18n Text Key: "Active" */
	private static final String UI_FILE_OPTIONS_PANEL_CLIENT = "/ui/plugins/paymentview/importexport/pnClientDetails.xml";

	private ClientDao clientDao;
	// > INSTANCE PROPERTIES
	private final ClientsTabHandler clientsTabHandler;
	private int columnCount;
	private ClientCsvImporter importer;

	public ClientImportHandler(UiGeneratorController ui,
			ClientsTabHandler clientsTabHandler, ClientDao clientDao) {
		super(ui);
		this.clientDao = clientDao;
		this.clientsTabHandler = clientsTabHandler;
	}

	private void addClientCells(Object row, String[] lineValues) {
		Object iconCell = this.uiController.createTableCell("");
		this.uiController.setIcon(iconCell, Icon.CONTACT);
		this.uiController.add(row, iconCell);

		for (int i = 0; i < columnCount && i < lineValues.length; ++i) {
			Object cell = this.uiController.createTableCell(lineValues[i]
					.replace(CsvExporter.GROUPS_DELIMITER, ", "));

			if (lineValues[i].equals(InternationalisationUtils
					.getI18nString(PaymentPluginConstants.COMMON_FIRST_NAME))
					|| lineValues[i]
							.equals(InternationalisationUtils
									.getI18nString(PaymentPluginConstants.COMMON_OTHER_NAME))) {
				this.uiController.setIcon(cell, Icon.USER_STATUS_ACTIVE);
			}

			if (lineValues[i].equals(InternationalisationUtils
					.getI18nString(PaymentPluginConstants.COMMON_PHONE))) {
				this.uiController.setIcon(cell, Icon.PHONE_NUMBER);
			}

			if (lineValues[i].equals(InternationalisationUtils
					.getI18nString(FrontlineSMSConstants.COMMON_ACTIVE))) {
				lineValues[i] = lineValues[i].toLowerCase();
				if (!lineValues[i].equalsIgnoreCase("false")
						&& !lineValues[i].equals("dormant")) {
					this.uiController.setIcon(cell, Icon.CIRLCE_TICK);
				} else {
					this.uiController.setIcon(cell, Icon.CANCEL);
				}
			}

			this.uiController.add(row, cell);
		}
	}

	@Override
	protected void appendPreviewHeaderItems(Object header) {
		int columnCount = 0;
		for (Object checkbox : getCheckboxes()) {
			if (this.uiController.isSelected(checkbox)) {
				String attributeName = this.uiController.getText(checkbox);

				if (uiController.getName(checkbox).equals(COMPONENT_CB_NAME)) {
					//TODO: take care of this hack... Separate names?
					this.uiController.add(header, this.uiController.createColumn("First Name", "First Name"));
					this.uiController.add(header, this.uiController.createColumn("Second Name", "Second Name"));
				}else{
					this.uiController.add(header, this.uiController.createColumn(attributeName, attributeName));
				}
				
				++columnCount;
			}
		}
		this.columnCount = columnCount;
	}

	@Override
	protected void doSpecialImport(String dataPath) {
		CsvRowFormat rowFormat = getRowFormatForClient();
		this.importer.importClients(this.clientDao, rowFormat);

		this.clientsTabHandler.refresh();
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(I18N_IMPORT_SUCCESSFUL));
	}

	protected List<Object> getCheckboxes() {
		Object pnCheckboxes = this.uiController.find(this.wizardDialog,
				COMPONENT_PN_CHECKBOXES);
		return Arrays.asList(this.uiController.getItems(pnCheckboxes));
	}

	@Override
	protected CsvImporter getImporter() {
		return this.importer;
	}

	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CLIENT;
	}

	@Override
	protected Object[] getPreviewRows() {
		List<Object> previewRows = new ArrayList<Object>();
		for (String[] lineValues : this.importer.getRawValues()) {
			previewRows.add(getRow(lineValues));
		}
		return previewRows.toArray();
	}

	protected Object getRow(String[] lineValues) {
		Object row = this.uiController.createTableRow();
		addClientCells(row, lineValues);
		return row;
	}

	protected CsvRowFormat getRowFormatForClient() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME,
				COMPONENT_CB_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME,
				COMPONENT_CB_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_PHONE,
				COMPONENT_ACCOUNTS);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_ACCOUNTS,
				COMPONENT_CB_PHONE);
		return rowFormat;
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_IMPORTING_SELECTED_CLIENTS;
	}

	@Override
	protected void setImporter(String filename) throws CsvParseException {
		this.importer = new ClientCsvImporter(new File(filename));
	}

}
