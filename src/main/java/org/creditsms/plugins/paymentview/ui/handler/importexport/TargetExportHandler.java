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
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.importexport.TargetViewCsvExporter;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;

public class TargetExportHandler extends ExportDialogHandler<Target> {

	private static final String COMPONENT_CB_NAME = "cbName";
	private static final String COMPONENT_CB_PRODUCTS = "cbProducts";
	private static final String COMPONENT_CB_START_DATE = "cbStartDate";
	private static final String COMPONENT_CB_END_DATE = "cbEndDate";
	private static final String COMPONENT_CB_SAVINGS_TARGET = "cbSavingsTarget";
	private static final String COMPONENT_CB_AMOUNT_SAVED = "cbAmountSaved";
	private static final String COMPONENT_CB_PERCENTAGE_SAVED = "cbPercentageSaved";
	/** i18n Text Key: "Active" */
	private static final String COMPONENT_CB_LAST_PAYMENT_DATE = "cbLastPaymentDate";
	private static final String COMPONENT_CB_LAST_PAYMENT = "cbLastPayment";
	private static final String COMPONENT_CB_PAID_THIS_MONTH = "cbPaidThisMonth";
	private static final String COMPONENT_CB_CURRENT_AMOUNT_DUE = "cbAmountDue";
	private static final String COMPONENT_CB_CURRENT_DUE_DATE = "cbCurrentDueDate";
	private static final String COMPONENT_CB_DAYS_REMAINING = "cbDaysRemaining";
	private static final String COMPONENT_CB_STATUS = "cbStatus";
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_OPTIONS_PANEL_TARGET = "/ui/plugins/paymentview/importexport/pnTargetExportDetails.xml";
	
	private TargetDao tgtDao;
	private List<Target> targetList;
	private PaymentViewPluginController pluginController;
	
	public TargetExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(Target.class, ui);
		this.tgtDao = pluginController.getTargetDao();
		this.pluginController = pluginController;
	}
	
	public TargetExportHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, List<Target> selected) {
		super(Target.class, ui);
		this.tgtDao = pluginController.getTargetDao();
		this.targetList = selected;
	}

	@Override
	protected void doSpecialExport(String dataPath) throws IOException {
		if (targetList == null) {
			exportTarget(this.tgtDao.getAllTargets(), dataPath);
		}else{
			exportTarget(targetList, dataPath);
		}
	}

	@Override
	protected void doSpecialExport(String dataPath, List<Target> targetList)
			throws IOException {
		exportTarget(targetList, dataPath);
	}

	private void exportTarget(List<Target> allTargets, String dataPath) throws IOException {
		CsvRowFormat rowFormat = getRowFormatForTarget();

		if (!rowFormat.hasMarkers()) {
			uiController.alert(InternationalisationUtils
					.getI18nString(MESSAGE_NO_FIELD_SELECTED));
			log.trace("EXIT");
			return;
		}

		log.debug("Row Format [" + rowFormat + "]");

		TargetViewCsvExporter.exportTarget(new File(dataPath),
				allTargets, rowFormat, pluginController);
		uiController.setStatus(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
		this.uiController.infoMessage(InternationalisationUtils
				.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
	}
	
	private CsvRowFormat getRowFormatForTarget() {
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_CLIENT_NAME,
				COMPONENT_CB_NAME);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_PRODUCTS,
				COMPONENT_CB_PRODUCTS);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_STARTDATE,
				COMPONENT_CB_START_DATE);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_ENDDATE,
				COMPONENT_CB_END_DATE);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_AMOUNT,
				COMPONENT_CB_SAVINGS_TARGET);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_AMOUNT_PAID,
				COMPONENT_CB_AMOUNT_SAVED);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_PERCENTAGE,
				COMPONENT_CB_PERCENTAGE_SAVED);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_DATE_PAID,
				COMPONENT_CB_LAST_PAYMENT_DATE);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_LAST_AMOUNT_PAID,
				COMPONENT_CB_LAST_PAYMENT);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_MONTHLY_SAVINGS,
				COMPONENT_CB_PAID_THIS_MONTH);
		addMarker(rowFormat, PaymentViewCsvUtils.MONTHLY_DUE,
				COMPONENT_CB_CURRENT_AMOUNT_DUE);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_CURRENT_DUE_DATE,
				COMPONENT_CB_CURRENT_DUE_DATE);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_DAYS_REMAINING,
				COMPONENT_CB_DAYS_REMAINING);
		addMarker(rowFormat, PaymentViewCsvUtils.TARGET_STATUS,
				COMPONENT_CB_STATUS);
		
		
		
		return rowFormat;
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_EXPORTING_SELECTED_CONTACTS;
	}

	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_TARGET;
	}
	
}
