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
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.importexport.PaymentViewCsvExporter;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;

public class IncomingPaymentsExportHandler extends ExportDialogHandler<IncomingPayment> {

	private static final String COMPONENT_CB_CONFIRMATION_CODE = "cbConfirmationCode";
	private static final String COMPONENT_CB_NAME = "cbPaymentBy";
	private static final String COMPONENT_CB_AMOUNT_PAID = "cbAmountPaid";
	/** i18n Text Key: "Active" */
	private static final String COMPONENT_CB_PHONE_NUMBER = "cbPhoneNumber";
	private static final String COMPONENT_CB_TIME_PAID = "cbTimePaid";
	private static final String COMPONENT_CB_PAYMENT_ID = "cbPaymentId";
	private static final String COMPONENT_CB_NOTES = "cbNotes";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_OPTIONS_PANEL_INCOMING_PAYMENT = "/ui/plugins/paymentview/importexport/pnIncomingPaymentsDetails.xml";

	private IncomingPaymentDao incomingPaymentDao;
	private List<IncomingPayment> selected;

	public IncomingPaymentsExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(IncomingPayment.class, ui);
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
	}
	
	public IncomingPaymentsExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, List<IncomingPayment> selected) {
		super(IncomingPayment.class, ui);
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.selected = selected;
	}

	@Override
	public void doSpecialExport(String dataPath) throws IOException {
		log.debug("Exporting all contacts..");
		if (selected == null) {
			exportIncomingPayment(this.incomingPaymentDao.getActiveIncomingPayments(), dataPath);
		}else{
			exportIncomingPayment(selected, dataPath);
		}
	}

	@Override
	public void doSpecialExport(String dataPath, List<IncomingPayment> selected)
			throws IOException {
		exportIncomingPayment(selected, dataPath);
	}

	/**
	 * Export the supplied contacts using settings set in {@link #wizardDialog}.
	 * 
	 * @param incomingPayment
	 *            The contacts to export
	 * @param filename
	 *            The file to export the contacts to
	 * @throws IOException
	 */
	private void exportIncomingPayment(List<IncomingPayment> incomingPayment,
			String filename) throws IOException {
		CsvRowFormat rowFormat = getRowFormatForIncomingPayment();

		if (!rowFormat.hasMarkers()) {
			uiController.alert(InternationalisationUtils
					.getI18nString(MESSAGE_NO_FIELD_SELECTED));
			log.trace("EXIT");
			return;
		}

		log.debug("Row Format [" + rowFormat + "]");

		PaymentViewCsvExporter.exportIncomingPayment(new File(filename),
				incomingPayment, rowFormat);
		uiController.setStatus(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
	}

	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_INCOMING_PAYMENT;
	}

	protected CsvRowFormat getRowFormatForIncomingPayment() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_INCOMING_CONFIRMATION_CODE,
				COMPONENT_CB_CONFIRMATION_CODE);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_NAME,
				COMPONENT_CB_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_PHONE_NUMBER,
				COMPONENT_CB_PHONE_NUMBER);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_AMOUNT_PAID,
				COMPONENT_CB_AMOUNT_PAID);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_TIME_PAID,
				COMPONENT_CB_TIME_PAID);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_PAYMENT_ID,
				COMPONENT_CB_PAYMENT_ID);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_NOTES,
				COMPONENT_CB_NOTES);
		return rowFormat;
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_EXPORTING_SELECTED_CONTACTS;
	}
}