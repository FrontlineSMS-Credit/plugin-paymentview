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

import org.creditsms.plugins.paymentview.csv.CsvUtils;
import org.creditsms.plugins.paymentview.data.importexport.IncomingPaymentCsvImporter;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;

/**
 * @author ian <ian@credit.frontlinesms.com>
 * 
 */
public class IncomingPaymentsImportHandler extends ImportDialogHandler {
	private static final String UI_FILE_OPTIONS_PANEL_CONTACT = "/ui/plugins/paymentview/importexport/pnIncomingPaymentsDetails.xml";
	/** I18n Text Key: TODO document */
	private static final String UI_FILE_OPTIONS_PANEL_INCOMING_PAYMENT = "Import Clients";
	/** i18n Text Key: "Active" */
	private static final String I18N_COMMON_ACTIVE = "common.active";
	private static final String COMPONENT_CB_PAYMENT_BY = "cbPaymentBy";
	private static final String COMPONENT_CB_PHONE_NUMBER = "cbPhoneNumber";
	private static final String COMPONENT_CB_ACCOUNT = "cbAccount";
	private static final String COMPONENT_CB_AMOUNT_PAID = "cbAmountPaid";
	private static final String COMPONENT_CB_TIME_PAID = "cbTimePaid";
	private static final String COMPONENT_CB_DATE_PAID = "cbDatePaid";

	// > INSTANCE PROPERTIES
	private IncomingPaymentCsvImporter importer;
	private int columnCount;
	private ClientDao clientDao;
	private IncomingPaymentDao incomingPaymentDao;
	private AccountDao accountDao;

	public IncomingPaymentsImportHandler(UiGeneratorController ui) {
		super(ui);
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return UI_FILE_OPTIONS_PANEL_INCOMING_PAYMENT; 
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
		this.importer = new IncomingPaymentCsvImporter(new File(filename));
	}

	@Override
	protected void doSpecialImport(String dataPath) {
		CsvRowFormat rowFormat = getRowFormatForIncomingPayment();
		this.importer.importIncomingPayments(this.incomingPaymentDao,
				this.accountDao, rowFormat);
		// this.uiController.refreshContactsTab();
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(I18N_IMPORT_SUCCESSFUL));
	}

	private CsvRowFormat getRowFormatForIncomingPayment() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, CsvUtils.MARKER_INCOMING_PAYMENT_BY,
				COMPONENT_CB_PAYMENT_BY);
		addMarker(rowFormat, CsvUtils.MARKER_INCOMING_PHONE_NUMBER,
				COMPONENT_CB_PHONE_NUMBER);
		addMarker(rowFormat, CsvUtils.MARKER_INCOMING_ACCOUNT,
				COMPONENT_CB_ACCOUNT);
		addMarker(rowFormat, CsvUtils.MARKER_INCOMING_AMOUNT_PAID,
				COMPONENT_CB_AMOUNT_PAID);
		addMarker(rowFormat, CsvUtils.MARKER_INCOMING_TIME_PAID,
				COMPONENT_CB_TIME_PAID);
		addMarker(rowFormat, CsvUtils.MARKER_INCOMING_DATE_PAID,
				COMPONENT_CB_DATE_PAID);
		return rowFormat;
	}

	@Override
	protected void appendPreviewHeaderItems(Object header) {
		int columnCount = 0;
		for (Object checkbox : getCheckboxes()) {
			if (this.uiController.isSelected(checkbox)) {
				String attributeName = this.uiController.getText(checkbox);
				if (this.uiController.getName(checkbox).equals(
						COMPONENT_CB_STATUS)) {
					attributeName = InternationalisationUtils
							.getI18nString(I18N_COMMON_ACTIVE);
				}
				this.uiController.add(header, this.uiController.createColumn(
						attributeName, attributeName));//
				++columnCount;
			}
		}
		this.columnCount = columnCount;
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
		addIncomingPaymentCells(row, lineValues);
		return row;
	}

	private void addIncomingPaymentCells(Object row, String[] lineValues) {
		Object iconCell = this.uiController.createTableCell("");
		this.uiController.setIcon(iconCell, Icon.CONTACT);
		this.uiController.add(row, iconCell);

		for (int i = 0; i < columnCount && i < lineValues.length; ++i) {
			Object cell = this.uiController.createTableCell(lineValues[i]
					.replace(CsvExporter.GROUPS_DELIMITER, ", "));

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

	protected List<Object> getCheckboxes() {
		Object pnCheckboxes = this.uiController.find(this.wizardDialog,
				COMPONENT_PN_CHECKBOXES);
		return Arrays.asList(this.uiController.getItems(pnCheckboxes));
	}

}
