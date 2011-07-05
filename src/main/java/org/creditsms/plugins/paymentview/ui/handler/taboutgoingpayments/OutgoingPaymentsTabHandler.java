package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

public class OutgoingPaymentsTabHandler extends BaseTabHandler {

	private static final String XML_OUTGOINGPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/taboutgoingpayments.xml";
	//private ImportNewPaymentsTabHandler importNewPaymentsTab;
	private Object outgoingPaymentsTab;

	private SelectFromClientsTabHandler selectFromClientsTab;
	private SentPaymentsTabHandler sentPaymentsTab;
	private PaymentViewPluginController pluginController;

	public OutgoingPaymentsTabHandler(UiGeneratorController ui, final PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		init();
	}

	@Override
	protected Object initialiseTab() {
		outgoingPaymentsTab = ui.loadComponentFromFile(XML_OUTGOINGPAYMENTS_TAB, this);
		sentPaymentsTab = new SentPaymentsTabHandler(ui, outgoingPaymentsTab,pluginController);
		//importNewPaymentsTab = new ImportNewPaymentsTabHandler(ui, outgoingPaymentsTab, pluginController);
		selectFromClientsTab = new SelectFromClientsTabHandler(ui, outgoingPaymentsTab, pluginController);

		return outgoingPaymentsTab;
	}

	@Override
	public void refresh() {
		sentPaymentsTab.refresh();
		//importNewPaymentsTab.refresh();
		selectFromClientsTab.refresh();
	}

}