package org.creditsms.plugins.paymentview.ui.handler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ExceptionsTabHandler extends BaseTabHandler {
	private static final String XML_EXCEPTIONS_TAB = "/ui/plugins/paymentview/exceptions/tabexceptions.xml";
	private Object exceptionsTab;

	public ExceptionsTabHandler(UiGeneratorController ui) {
		super(ui);
		init();
	}

	public void addClient() {
	}

	// > EVENTS...
	public void customizeClientDB() {
	}

	public void importClient() {
	}

	@Override
	protected Object initialiseTab() {
		exceptionsTab = ui.loadComponentFromFile(XML_EXCEPTIONS_TAB, this);
		return exceptionsTab;
	}

	@Override
	public void refresh() {
	}

}
