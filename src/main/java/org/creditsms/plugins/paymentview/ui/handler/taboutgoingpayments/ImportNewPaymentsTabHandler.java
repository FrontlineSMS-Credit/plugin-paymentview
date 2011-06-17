package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SendPaymentAuthDialogHandler;

public class ImportNewPaymentsTabHandler extends BaseTabHandler {
	private static final String XML_IMPORT_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/importnewpayments.xml";
	private AccountDao accountDao;
	private Object schedulePaymentAuthDialog;
	private Object selectFromClientsPanel;
	private Object sendPaymentAuthDialog;
	
	//KIM
	private static final String TAB_IMPORTNEWPAYMENTS = "tab_importNewOutgoingPayments";
	private Object importPaymentsTab;

	public ImportNewPaymentsTabHandler(UiGeneratorController ui, Object tabOutgoingPayments) {
		super(ui);
		importPaymentsTab = ui.find(tabOutgoingPayments, TAB_IMPORTNEWPAYMENTS);//KIM
		init();
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsPanel = ui.loadComponentFromFile(XML_IMPORT_NEW_PAYMENTS_TAB, this);
		this.ui.add(importPaymentsTab, selectFromClientsPanel);
		return importPaymentsTab;
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
		//TODO construct a list of outgoingpayments
		
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui,this)
				.getDialog();
		ui.add(sendPaymentAuthDialog);
	}
}
