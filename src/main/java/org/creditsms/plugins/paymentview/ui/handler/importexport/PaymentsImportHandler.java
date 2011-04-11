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
import org.creditsms.plugins.paymentview.data.importexport.PaymentCsvImporter;

public class PaymentsImportHandler extends ImportDialogHandler {
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORTING_SELECTED_CONTACTS = "message.importing.contacts.groups";
	/** i18n Text Key: "Active" */
	private static final String I18N_COMMON_ACTIVE = "common.active";
	
//> INSTANCE PROPERTIES
	private PaymentCsvImporter importer;
	private int columnCount;
	
	public PaymentsImportHandler(UiGeneratorController ui) {
		super(ui);
	}
	
	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_IMPORTING_SELECTED_CONTACTS;
	}
	
	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CONTACT;
	}
	
	@Override
	protected CsvImporter getImporter() {
		return this.importer;
	}
	
	@Override
	protected void setImporter(String filename) throws CsvParseException {
		this.importer = new PaymentCsvImporter(new File(filename));
	}

	@Override
	protected void doSpecialImport(String dataPath) {
		CsvRowFormat rowFormat = getRowFormatForContact();
		this.importer.importPayments(this.contactDao, this.groupMembershipDao, this.groupDao, rowFormat);
		this.uiController.refreshContactsTab();
		this.uiController.infoMessage(InternationalisationUtils.getI18nString(I18N_IMPORT_SUCCESSFUL));
	}
	
	//TODO: Roy, look at this to help you solve issue CREDIT-30
	
	@Override
	protected void appendPreviewHeaderItems(Object header) {
		int columnCount = 0;
		for (Object checkbox : getCheckboxes()) {
			if (this.uiController.isSelected(checkbox)) {
				String attributeName = this.uiController.getText(checkbox);
				/*if (this.uiController.getName(checkbox).equals(COMPONENT_CB_STATUS)
						&& this.type.equals(EntityType.CONTACTS)) {
					attributeName = InternationalisationUtils.getI18nString(I18N_COMMON_ACTIVE);
				}
				*/
				//TODO: This is what I mean exactly
				this.uiController.add(header, this.uiController.createColumn(attributeName, attributeName));//
				++columnCount;
			}
		}
		this.columnCount = columnCount;
	}
	
	
	@Override
	protected Object[] getPreviewRows() {
		List<Object> previewRows = new ArrayList<Object>();
		for(String[] lineValues : this.importer.getRawValues()) {
			previewRows.add(getRow(lineValues));
		}
		return previewRows.toArray();
	}
	
	protected Object getRow(String[] lineValues) {
		Object row = this.uiController.createTableRow();
		addContactCells(row, lineValues);
		return row;
	}
	
	private void addContactCells(Object row, String[] lineValues) {
		Object iconCell = this.uiController.createTableCell("");
		this.uiController.setIcon(iconCell, Icon.CONTACT);
		this.uiController.add(row, iconCell);

		for (int i = 0; i < columnCount && i < lineValues.length; ++i) {
			Object cell = this.uiController.createTableCell(lineValues[i].replace(
					CsvExporter.GROUPS_DELIMITER, ", "));

			if (lineValues[i].equals(InternationalisationUtils.getI18nString(FrontlineSMSConstants.COMMON_ACTIVE))) {
				lineValues[i] = lineValues[i].toLowerCase();
				if (!lineValues[i].equalsIgnoreCase("false") && !lineValues[i].equals("dormant")) {
					this.uiController.setIcon(cell, Icon.CIRLCE_TICK);
				} else {
					this.uiController.setIcon(cell, Icon.CANCEL);
				}
			}

			this.uiController.add(row, cell);
		}
	}
	
	protected List<Object> getCheckboxes() {
		Object pnCheckboxes = this.uiController.find(this.wizardDialog, COMPONENT_PN_CHECKBOXES);
		return Arrays.asList(this.uiController.getItems(pnCheckboxes));
	}

}
