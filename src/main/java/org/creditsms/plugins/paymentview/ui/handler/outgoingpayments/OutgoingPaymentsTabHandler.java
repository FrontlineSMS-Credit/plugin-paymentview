package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class OutgoingPaymentsTabHandler extends BaseTabHandler{

	private static final String XML_OUTGOINGPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/taboutgoingpayments.xml";
	private Object outgoingPaymentsTab; 
	
	public OutgoingPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {
	}

	@Override
	protected Object initialiseTab() {
		outgoingPaymentsTab = ui.loadComponentFromFile(XML_OUTGOINGPAYMENTS_TAB, this);
		return outgoingPaymentsTab;
	}

	//> EVENTS...
	public void customizeClientDB(){		
	}
	
	public void addClient(){			
	}
	
	public void importClient(){		
	}	
}