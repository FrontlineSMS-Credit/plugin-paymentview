package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
 
public class SelectFromClientsTabHandler extends BaseTabHandler{
	private static final String XML_SELECT_FROM_CLIENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/selectfromclients.xml";
	private Object selectFromClientsTab;   
	
	public SelectFromClientsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {
	}

	@Override
	protected Object initialiseTab() {
		selectFromClientsTab = ui.loadComponentFromFile(XML_SELECT_FROM_CLIENTS_TAB, this);
		return selectFromClientsTab;
	}
}

