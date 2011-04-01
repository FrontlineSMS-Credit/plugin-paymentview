package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
 
public class SendNewPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_SEND_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sendnewpayments.xml";
	private Object sendNewPaymentsTab;  
	
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
}
