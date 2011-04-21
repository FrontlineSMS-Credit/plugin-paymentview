package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.dialogs.SendPaymentAuthDialogHandler;

public class SendNewPaymentsTabHandler extends BaseTabHandler {
	private static final String XML_SEND_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sendnewpayments.xml";
	private Object schedulePaymentAuthDialog;
	private Object sendNewPaymentsTab;
	private Object sendPaymentAuthDialog;

	public SendNewPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);
		init();
	}

	@Override
	protected Object initialiseTab() {
		sendNewPaymentsTab = ui.loadComponentFromFile(
				XML_SEND_NEW_PAYMENTS_TAB, this);
		return sendNewPaymentsTab;
	}

	@Override
	public void refresh() {
	}

	public void showScheduleNewPaymentsAuthDialog() {
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(schedulePaymentAuthDialog);
	}

	public void showSendNewPaymentsAuthDialog() {
		sendPaymentAuthDialog = new SendPaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(sendPaymentAuthDialog);
	}
}
