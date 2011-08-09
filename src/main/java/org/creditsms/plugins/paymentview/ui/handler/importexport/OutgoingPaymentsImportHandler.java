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
import org.creditsms.plugins.paymentview.data.importexport.OutgoingPaymentCsvImporter;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.ImportNewPaymentsTabHandler;

/**
 * 
 * @author Roy
 *
 */

public class OutgoingPaymentsImportHandler extends ImportDialogHandler {
	private static final String PAYMENT_ID = "cbPaymentId";
	private static final String COMPONENT_CB_AMOUNT_PAID = "cbAmountPaid";
	private static final String COMPONENT_CB_OUTGOING_NOTES = "cbNotes";
	private static final String COMPONENT_CB_PHONE_NUMBER = "cbPhoneNumber";
	private final ImportNewPaymentsTabHandler newPaymentsTabHandler;
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_IMPORTING_SELECTED_CLIENTS = "Import Clients";
	private static final String UI_FILE_OPTIONS_PANEL_CONTACT = "/ui/plugins/paymentview/importexport/pnOutgoingPaymentsDetails.xml";

	private AccountDao accountDao;
	private ClientDao clientDao;
	private int columnCount;


	// > INSTANCE PROPERTIES
	private OutgoingPaymentCsvImporter importer;
	private OutgoingPaymentDao outgoingPaymentDao;
	
	public OutgoingPaymentsImportHandler(UiGeneratorController ui,
			AccountDao accountDao, ClientDao clientDao, ImportNewPaymentsTabHandler newPaymentsTabHandler) {
		super(ui);
		this.accountDao = accountDao;
		this.clientDao = clientDao;
		this.newPaymentsTabHandler = newPaymentsTabHandler;
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

	@Override
	protected void appendPreviewHeaderItems(Object header) {
		int columnCount = 0;
		for (Object checkbox : getCheckboxes()) {
			if (this.uiController.isSelected(checkbox)) {
				String attributeName = this.uiController.getText(checkbox);
				this.uiController.add(header, this.uiController.createColumn(attributeName, attributeName));
				++columnCount;
			}
		}
		this.columnCount = columnCount;
	}

	@Override
	protected void doSpecialImport(String dataPath) {
		CsvRowFormat rowFormat = getRowFormatForIncomingPayment();
		
		this.importer.importOutgoingPayments(this.outgoingPaymentDao,
				this.accountDao,this.clientDao, rowFormat);
		this.newPaymentsTabHandler.refresh(this.importer.getImportedPaymentsLst());
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(I18N_IMPORT_SUCCESSFUL));
		if(this.importer.incorrectCount!=0){
			this.uiController.alert(this.importer.incorrectCount+" payment record(s) had invalid phone number(s) or record(s)");
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
		return UI_FILE_OPTIONS_PANEL_CONTACT;
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

	private CsvRowFormat getRowFormatForIncomingPayment() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_INCOMING_PHONE_NUMBER,
				COMPONENT_CB_PHONE_NUMBER);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_INCOMING_AMOUNT_PAID,
				COMPONENT_CB_AMOUNT_PAID);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_INCOMING_ACCOUNT,
				PAYMENT_ID);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_OUTGOING_NOTES,
				COMPONENT_CB_OUTGOING_NOTES);
		return rowFormat;
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_IMPORTING_SELECTED_CLIENTS;
	}

	@Override
	protected void setImporter(String filename) throws CsvParseException {
		this.importer = new OutgoingPaymentCsvImporter(new File(filename));
	}

}
