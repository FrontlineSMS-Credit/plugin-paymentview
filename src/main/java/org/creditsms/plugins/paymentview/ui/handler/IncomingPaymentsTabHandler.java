package org.creditsms.plugins.paymentview.ui.handler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class IncomingPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_INCOMING_PAYMENTS_TAB = "/ui/plugins/paymentview/incomingpayments/tabincomingpayments.xml";
	private Object incomingPaymentsTab; 
	
	public IncomingPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {		
	}

	@Override
	protected Object initialiseTab() {
		incomingPaymentsTab = ui.loadComponentFromFile(XML_INCOMING_PAYMENTS_TAB, this);
		return incomingPaymentsTab;
	}

	//> EVENTS...
	public void customizeClientDB(){		
	}
	
	public void addClient(){			
	}
	
	public void importClient(){		
	}	

}
