package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class SentPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_SENTPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sentpayments.xml";
	private Object sentPaymentsTab; 
	
	public SentPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {
	}

	@Override
	protected Object initialiseTab() {
		sentPaymentsTab = ui.loadComponentFromFile(XML_SENTPAYMENTS_TAB, this);
		return sentPaymentsTab;
	}
}
