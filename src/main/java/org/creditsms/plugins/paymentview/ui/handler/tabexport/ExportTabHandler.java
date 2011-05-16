package org.creditsms.plugins.paymentview.ui.handler.tabexport;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

public class ExportTabHandler extends BaseTabHandler {
	private static final String TABBED_PANE_MAIN = "tabP_MainPane";
	private static final String XML_EXPORT_TAB = "/ui/plugins/paymentview/export/tabexport.xml";

	private ExportClientHistoryTabHandler exportclientHistoryTab;
	private ExportClientsTabHandler exportclientsTab;
	private Object exportTab;
	private Object mainTabbedPane;
	private ExportPaymentsTabHandler exportpaymentsTab;
	private PaymentViewPluginController pluginController;

	public ExportTabHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		
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
		exportTab = ui.loadComponentFromFile(XML_EXPORT_TAB, this);
		mainTabbedPane = ui.find(exportTab, TABBED_PANE_MAIN);

		exportclientsTab = new ExportClientsTabHandler(ui, pluginController);
		ui.add(mainTabbedPane, exportclientsTab.getTab());
		exportclientHistoryTab = new ExportClientHistoryTabHandler(ui);
		ui.add(mainTabbedPane, exportclientHistoryTab.getTab());
		exportpaymentsTab = new ExportPaymentsTabHandler(ui);
		ui.add(mainTabbedPane, exportpaymentsTab.getTab());

		return exportTab;
	}

	@Override
	public void refresh() {
	}

}
