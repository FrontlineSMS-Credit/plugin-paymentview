package org.creditsms.plugins.paymentview.ui.handler.export;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ExportPaymentsTabHandler extends BaseTabHandler{
	private static final String XML_EXPORT_PAYMENTS_TAB = "/ui/plugins/paymentview/export/innertabs/tabexportpayments.xml";
	private Object paymentsTab;
	
	public ExportPaymentsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {		
	}
  
	@Override
	protected Object initialiseTab() {
		paymentsTab = ui.loadComponentFromFile(XML_EXPORT_PAYMENTS_TAB, this);
		return paymentsTab;
	}
}