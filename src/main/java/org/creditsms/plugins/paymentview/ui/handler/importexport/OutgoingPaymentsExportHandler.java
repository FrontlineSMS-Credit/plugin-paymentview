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
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.importexport.PaymentViewCsvExporter;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

/**
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
public class OutgoingPaymentsExportHandler extends ExportDialogHandler<OutgoingPayment> {

	private static final String COMPONENT_CB_CLIENT_NAME = "cbClient";
	private static final String COMPONENT_CB_PHONE_NUMBER = "cbPhoneNumber";
	private static final String COMPONENT_CB_AMOUNT_PAID = "cbAmountPaid";
	private static final String COMPONENT_CB_TIME_PAID = "cbTimePaid";
	private static final String COMPONENT_CB_STATUS = "cbStatus";
	private static final String COMPONENT_CB_CONFIRMATION_CODE = "cbConfirmationCode";
	private static final String COMPONENT_CB_PAYMENT_ID = "cbPaymentId";
	private static final String COMPONENT_CB_NOTES = "cbNotes";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_OPTIONS_PANEL_OUTGOING_PAYMENT = "/ui/plugins/paymentview/importexport/pnOutgoingPaymentsExportDetails.xml";

	private OutgoingPaymentDao outgoingPaymentDao;
	private List<OutgoingPayment> selected;

	public OutgoingPaymentsExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(OutgoingPayment.class, ui);
		this.outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
	}

	public OutgoingPaymentsExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, List<OutgoingPayment> selected) {
		super(OutgoingPayment.class, ui);
		this.outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		this.selected = selected;
	}
	
	@Override
	public void doSpecialExport(String dataPath) throws IOException {
		log.debug("Exporting all outgoing payments..");
		if (selected == null) {
			exportOutgoingPayment(this.outgoingPaymentDao.getAllOutgoingPayments(), dataPath);
		}else{
			exportOutgoingPayment(selected, dataPath);
		}		
	}

	@Override
	public void doSpecialExport(String dataPath, List<OutgoingPayment> selected)
			throws IOException {
		exportOutgoingPayment(selected, dataPath);
	}

	/**
	 * Export the supplied contacts using settings set in {@link #wizardDialog}.
	 * 
	 * @param outgoingPayment
	 *            The contacts to export
	 * @param filename
	 *            The file to export the contacts to
	 * @throws IOException
	 */
	private void exportOutgoingPayment(List<OutgoingPayment> outgoingPayment,
			String filename) throws IOException {
		CsvRowFormat rowFormat = getRowFormatForOutgoingPayment();

		if (!rowFormat.hasMarkers()) {
			uiController.alert(InternationalisationUtils
					.getI18nString(MESSAGE_NO_FIELD_SELECTED));
			log.trace("EXIT");
			return;
		}

		log.debug("Row Format [" + rowFormat + "]");

		PaymentViewCsvExporter.exportOutgoingPayment(new File(filename),
				outgoingPayment, rowFormat);
		uiController.setStatus(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
	}

	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_OUTGOING_PAYMENT;
	}

	protected CsvRowFormat getRowFormatForOutgoingPayment() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_CLIENT_NAME, COMPONENT_CB_CLIENT_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_PHONE_NUMBER, COMPONENT_CB_PHONE_NUMBER);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_AMOUNT_PAID, COMPONENT_CB_AMOUNT_PAID);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_TIME_PAID, COMPONENT_CB_TIME_PAID);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_OUTGOING_STATUS, COMPONENT_CB_STATUS);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_OUTGOING_CONFIRMATION_CODE, COMPONENT_CB_CONFIRMATION_CODE);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_PAYMENT_ID, COMPONENT_CB_PAYMENT_ID);
		addMarker(rowFormat, PaymentViewCsvUtils.MARKER_NOTES,	COMPONENT_CB_NOTES);

		return rowFormat;
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_EXPORTING_SELECTED_CONTACTS;
	}
}