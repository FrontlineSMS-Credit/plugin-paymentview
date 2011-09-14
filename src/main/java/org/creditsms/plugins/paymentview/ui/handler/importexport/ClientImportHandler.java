package org.creditsms.plugins.paymentview.ui.handler.importexport;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import net.frontlinesms.csv.CsvImporter;
import net.frontlinesms.csv.CsvParseException;
import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.handler.importexport.ImportDialogHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.csv.PaymentViewCsvUtils;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.importexport.ClientCsvImporter;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;
import org.creditsms.plugins.paymentview.utils.PaymentPluginConstants;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

public class ClientImportHandler extends ImportDialogHandler {
	private static final String COMPONENT_CB_NAME = "cbName";
	private static final String COMPONENT_CB_PHONE = "cbPhone";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORTING_SELECTED_CLIENTS = "plugins.paymentview.message.importing.selected.client";
	/** i18n Text Key: "Active" */
	private static final String UI_FILE_OPTIONS_PANEL_CLIENT = "/ui/plugins/paymentview/importexport/pnImportClientDetails.xml";

	private ClientDao clientDao;
// > INSTANCE PROPERTIES
	private final ClientsTabHandler clientsTabHandler;
	private int columnCount;
	private ClientCsvImporter importer;
	private CustomFieldDao customFieldDao;
	private final PaymentViewPluginController pluginController;

	public ClientImportHandler(
			PaymentViewPluginController pluginController, ClientsTabHandler clientsTabHandler) {
		super(pluginController.getUiGeneratorController());
		this.pluginController = pluginController;
		this.clientDao = pluginController.getClientDao();
		this.clientsTabHandler = clientsTabHandler;
		this.customFieldDao = pluginController.getCustomFieldDao();
	}

	@Override
	public void showWizard() {
		List<CustomField> allCustomFields = this.customFieldDao
				.getAllActiveUsedCustomFields();
		Object pnInfo = uiController.find(optionsPanel, "pnInfo");
		if (!allCustomFields.isEmpty()) {
			for (CustomField cf : allCustomFields) {
					Object checkbox = uiController.createCheckbox(cf.getCamelCaseName(),
							cf.getReadableName(), true);
					uiController.add(pnInfo, checkbox);
			}
		}
		super.showWizard();
	}
	
	private void addClientCells(Object row, String[] lineValues) {
		for (int i = 0; i < columnCount && i < lineValues.length; ++i) {
			Object cell = this.uiController.createTableCell(lineValues[i]);

			if (lineValues[i].equals(InternationalisationUtils
					.getI18nString(PaymentPluginConstants.COMMON_FIRST_NAME))
					|| lineValues[i]
							.equals(InternationalisationUtils
									.getI18nString(PaymentPluginConstants.COMMON_OTHER_NAME))) {
			}

			if (lineValues[i].equals(InternationalisationUtils
					.getI18nString(PaymentPluginConstants.COMMON_PHONE))) {
				uiController.setWidth(cell, 100);
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
					this.uiController.add(header, this.uiController.createColumn(InternationalisationUtils
							.getI18nString(PaymentPluginConstants.COMMON_FIRST_NAME), ""));
					this.uiController.add(header, this.uiController.createColumn(InternationalisationUtils
							.getI18nString(PaymentPluginConstants.COMMON_OTHER_NAME), ""));
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
		try{
			this.importer.importClients(this.clientDao, rowFormat, pluginController);
			this.clientsTabHandler.refresh();
			this.uiController.infoMessage(InternationalisationUtils
					.getI18nString(I18N_IMPORT_SUCCESSFUL));
		} catch (DuplicateKeyException e) {
			pluginController.getUiGeneratorController().
			alert(e.getMessage());
		}
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
		this.uiController.add(row, this.uiController.createTableCell(""));
		addClientCells(row, lineValues);		
		return row;
	}

	protected CsvRowFormat getRowFormatForClient() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_FIRST_NAME, COMPONENT_CB_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_OTHER_NAME, COMPONENT_CB_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_PHONE, COMPONENT_CB_PHONE);
		
		for(CustomField cf : customFieldDao.getAllActiveUsedCustomFields()){
			addMarker(rowFormat, PaymentViewUtils.getMarkerFromString(cf.getReadableName()),
					cf.getCamelCaseName());
		}
		
		return rowFormat;
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_IMPORTING_SELECTED_CLIENTS;
	}

	@Override
	protected void setImporter(String filename) throws CsvParseException {
		this.importer = new ClientCsvImporter(new File(filename), pluginController);
	}
}
