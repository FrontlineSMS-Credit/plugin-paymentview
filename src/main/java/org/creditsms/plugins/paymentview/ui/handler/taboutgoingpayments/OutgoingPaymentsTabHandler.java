package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

public class OutgoingPaymentsTabHandler extends BaseTabHandler {

	private static final String TABBED_PANE_MAIN = "tabbedPaneMain";
	private static final String XML_OUTGOINGPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/taboutgoingpayments.xml";

	private ImportNewPaymentsTabHandler importNewPaymentsTab;
	private Object mainTabbedPane;
	private Object outgoingPaymentsTab;

	private SelectFromClientsTabHandler selectFromClientsTab;
	private SendNewPaymentsTabHandler sendNewPaymentsTab;
	private SentPaymentsTabHandler sentPaymentsTab;
	private PaymentViewPluginController pluginController;

	public OutgoingPaymentsTabHandler(UiGeneratorController ui, final PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		init();
	}

	@Override
	protected Object initialiseTab() {
		outgoingPaymentsTab = ui.loadComponentFromFile(
				XML_OUTGOINGPAYMENTS_TAB, this);

		mainTabbedPane = ui.find(outgoingPaymentsTab, TABBED_PANE_MAIN);
		sentPaymentsTab = new SentPaymentsTabHandler(ui, pluginController);
		ui.add(mainTabbedPane, sentPaymentsTab.getTab());

		sendNewPaymentsTab = new SendNewPaymentsTabHandler(ui, pluginController);
		ui.add(mainTabbedPane, sendNewPaymentsTab.getTab());

		importNewPaymentsTab = new ImportNewPaymentsTabHandler(ui);
		ui.add(mainTabbedPane, importNewPaymentsTab.getTab());

		selectFromClientsTab = new SelectFromClientsTabHandler(ui, pluginController);
		selectFromClientsTab.refresh();
		ui.add(mainTabbedPane, selectFromClientsTab.getTab());

		return outgoingPaymentsTab;
	}

	@Override
	public void refresh() {
		sentPaymentsTab.refresh();
		sendNewPaymentsTab.refresh();
		importNewPaymentsTab.refresh();
		selectFromClientsTab.refresh();
	}

}