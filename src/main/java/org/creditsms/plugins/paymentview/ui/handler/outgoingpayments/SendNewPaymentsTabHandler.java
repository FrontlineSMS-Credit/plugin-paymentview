package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SendPaymentAuthDialogHandler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
 
public class SendNewPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_SEND_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sendnewpayments.xml";
	private Object sendNewPaymentsTab;  
	private Object sendPaymentAuthDialog;
	private Object schedulePaymentAuthDialog;  
	
	public SendNewPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {
	}

	@Override
	protected Object initialiseTab() {
		sendNewPaymentsTab = ui.loadComponentFromFile(XML_SEND_NEW_PAYMENTS_TAB, this);
		return sendNewPaymentsTab;
	}
	
	public void showSendNewPaymentsAuthDialog(){
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui).getDialog();
		ui.add(sendPaymentAuthDialog);
	}
	
	public void showScheduleNewPaymentsAuthDialog(){
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui).getDialog();
		ui.add(schedulePaymentAuthDialog);
	}
}
