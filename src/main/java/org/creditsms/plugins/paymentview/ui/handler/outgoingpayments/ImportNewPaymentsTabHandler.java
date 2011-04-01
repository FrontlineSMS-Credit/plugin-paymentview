package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ImportNewPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_IMPORT_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/importnewpayments.xml";
	private Object selectFromClientsTab;
	
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
}

