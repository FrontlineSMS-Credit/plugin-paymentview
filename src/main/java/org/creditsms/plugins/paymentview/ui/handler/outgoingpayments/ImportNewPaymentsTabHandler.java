package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SendPaymentAuthDialogHandler;

public class ImportNewPaymentsTabHandler extends BaseTabHandler {
	private static final String XML_IMPORT_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/importnewpayments.xml";
	private AccountDao accountDao;
	private Object schedulePaymentAuthDialog;
	private Object selectFromClientsTab;
	private Object sendPaymentAuthDialog;

	public ImportNewPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);
		init();
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsTab = ui.loadComponentFromFile(
				XML_IMPORT_NEW_PAYMENTS_TAB, this);
		return selectFromClientsTab;
	}

	@Override
	public void refresh() {
	}

	public void showImportWizard(String typeName) {
		new OutgoingPaymentsImportHandler(ui, accountDao).showWizard();
	}

	public void showSchedulePaymentAuthDialog() {
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(schedulePaymentAuthDialog);
	}

	public void showSendPaymentAuthDialog() {
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(sendPaymentAuthDialog);
	}
}
