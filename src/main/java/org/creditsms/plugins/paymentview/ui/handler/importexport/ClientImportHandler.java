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
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.importexport.ClientCsvImporter;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;
import org.creditsms.plugins.paymentview.utils.PhoneNumberPattern;
import org.creditsms.plugins.paymentview.utils.PvUtils;

import thinlet.Thinlet;

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
	private List<String> tableHeaderList;
	private List<CustomValue> cvLst = new ArrayList<CustomValue>();
	private List<CustomField> cfLst = new ArrayList<CustomField>();
	private List<Client> clntLst = new ArrayList<Client>();
	PhoneNumberPattern phonePattern = new PhoneNumberPattern();
	private boolean hasIncorrectlyFormatedPhoneNo = false;

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
				uiController.setMethod(checkbox, Thinlet.ATTRIBUTE_ACTION, "columnCheckboxChanged", checkbox, this);
				uiController.add(pnInfo, checkbox);
			}
		}
		super.showWizard();
	}

	private void addClientCells(Object row, String[] lineValues, String[] headerValues) {
		Object cell = null;
		String cellValue = "";
		String name = "";
		String leanName = "";
		String firstName  = "";
		String otherName  = "";
		String phonenumber = "";
		List<String> strCustomFld = new ArrayList<String>(); 
		List<String> strCustomVal = new ArrayList<String>(); 

		int t = 0;
		for(int y = 0; y<tableHeaderList.size(); y++) {
			CustomField customField = null;
			CustomValue customValue = null;
			for(int r = 0; r<headerValues.length; r++) {
				if(tableHeaderList.get(y).equals(headerValues[r])) {
					t = r;
					cellValue = getCellValue(lineValues, t);
					cell = this.uiController.createTableCell(cellValue);
					if (tableHeaderList.get(y).equals("Name") || tableHeaderList.get(y).equals("Phone")) {
						if (tableHeaderList.get(y).equals("Name")) {
							name = cellValue;
							leanName = name.replaceAll("\\s+", " ");
							firstName  = leanName.split(" ")[0];
							otherName  = leanName.split(" ")[1];
						} else {
							phonenumber = PvUtils.parsePhoneFromExcel(cellValue);
							if(phonePattern.formatPhoneNumber(phonenumber)) {
								phonenumber = phonePattern.getNewPhoneNumberPattern();
							}else{
								hasIncorrectlyFormatedPhoneNo = true;
							}
						}
						this.uiController.add(row, cell);
					} else {
						strCustomFld.add(tableHeaderList.get(y)); 
						strCustomVal.add(cellValue); 
						customField = customFieldDao.getCustomFieldsByReadableName(tableHeaderList.get(y)).get(0);
						customValue = new CustomValue(cellValue, customField, new Client(firstName, otherName, phonenumber));
						cvLst.add(customValue);
						cfLst.add(customField);
						this.uiController.add(row, cell);
					}
				}
			}
	    }
		
		Client clnt = new Client(firstName, otherName, phonenumber);
		clntLst.add(clnt);
		if(cell!=null){
			this.uiController.add(row, cell);
		}
	}
	
	private String getCellValue(String[] lineValues, int t) {
		String cellValue = "";
		try {
			cellValue = lineValues[t];
		} catch (ArrayIndexOutOfBoundsException e) {
			cellValue = "";
		}		
		return cellValue;
	}

	@Override
	protected void appendPreviewHeaderItems(Object header) {
		int columnCount = 0;
		tableHeaderList = new ArrayList<String>();
		if(tableHeaderList!=null){
			tableHeaderList.clear();
		}
		for (Object checkbox : getCheckboxes()) {
			if (this.uiController.isSelected(checkbox)) {
				String attributeName = this.uiController.getText(checkbox);
				this.uiController.add(header, this.uiController.createColumn(attributeName, attributeName));
				tableHeaderList.add(attributeName);	
				++columnCount;
			}
		}
		this.columnCount = columnCount;
	}
	
	@Override
	protected void doSpecialImport(String dataPath) {
		CsvRowFormat rowFormat = getRowFormatForClient();
		try{
			if(hasIncorrectlyFormatedPhoneNo) {
				this.uiController.alert("There is one or more records with" +
						" incorrectly formatted phone numbers, " +
						"please edit the .csv file and try to import the file again.");
			} else {
				this.importer.setSelectedClientLst(clntLst);
				this.importer.setSelectedCustomFieldlst(cfLst);
				this.importer.setSelectedCustomValueslst(cvLst);
				this.importer.importClients(this.clientDao, rowFormat, pluginController);
				this.clientsTabHandler.refresh();

				this.uiController.infoMessage(InternationalisationUtils
						.getI18nString(I18N_IMPORT_SUCCESSFUL));	
			}
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
		
		if(hasIncorrectlyFormatedPhoneNo){
			this.uiController.alert("There is one or more records with" +
					" incorrectly formatted phone numbers, " +
					"please edit the .csv file and import the file again.");
		}
		return previewRows.toArray();
	}

	protected Object getRow(String[] lineValues) {
		Object row = this.uiController.createTableRow();
		this.uiController.add(row, this.uiController.createTableCell(""));
		addClientCells(row, lineValues, this.importer.getRawFirstLine());
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
		if(cvLst!=null){
			cvLst.clear();
		}
		if(cfLst!=null){
			cfLst.clear();
		}
		if(clntLst!=null){
			clntLst.clear();
		}
		this.importer = new ClientCsvImporter(new File(filename), pluginController);
	}
}
