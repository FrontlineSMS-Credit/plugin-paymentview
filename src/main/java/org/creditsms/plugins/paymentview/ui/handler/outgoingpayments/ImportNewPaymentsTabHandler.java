package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SendPaymentAuthDialogHandler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.importexport.ImportDialogHandlerFactory;

public class ImportNewPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_IMPORT_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/importnewpayments.xml";
	private Object selectFromClientsTab;
	private Object sendPaymentAuthDialog;
	private Object schedulePaymentAuthDialog;
	
	public ImportNewPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsTab = ui.loadComponentFromFile(XML_IMPORT_NEW_PAYMENTS_TAB, this);
		return selectFromClientsTab;
	}
	
	public void showImportWizard(String typeName){
		new OutgoingPaymentsImportHandler(ui).showWizard();
	}
	
	public void showSendPaymentAuthDialog(){
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui).getDialog();
		ui.add(sendPaymentAuthDialog);
	}
	
	public void showSchedulePaymentAuthDialog(){
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui).getDialog();
		ui.add(schedulePaymentAuthDialog);
	}
} 

