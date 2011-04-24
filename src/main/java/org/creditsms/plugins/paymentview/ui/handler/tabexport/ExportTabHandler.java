package org.creditsms.plugins.paymentview.ui.handler.tabexport;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

public class ExportTabHandler extends BaseTabHandler {
	private static final String TABBED_PANE_MAIN = "tabP_MainPane";
	private static final String XML_EXPORT_TAB = "/ui/plugins/paymentview/export/tabexport.xml";

	private ClientDao clientDao;
	private ExportClientHistoryTabHandler clientHistoryTab;
	private ExportClientsTabHandler clientsTab;
	private Object exportTab;
	private IncomingPaymentDao incomingPaymentDao;
	private Object mainTabbedPane;
	private OutgoingPaymentDao outgoingPaymentDao;
	private ExportPaymentsTabHandler paymentsTab;

	public ExportTabHandler(UiGeneratorController ui, ClientDao clientDao,
			IncomingPaymentDao incomingPaymentDao,
			OutgoingPaymentDao outgoingPaymentDao) {
		super(ui);
		this.clientDao = clientDao;
		this.incomingPaymentDao = incomingPaymentDao;
		this.outgoingPaymentDao = outgoingPaymentDao;

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

		clientsTab = new ExportClientsTabHandler(ui, clientDao);
		ui.add(mainTabbedPane, clientsTab.getTab());
		clientHistoryTab = new ExportClientHistoryTabHandler(ui);
		ui.add(mainTabbedPane, clientHistoryTab.getTab());
		paymentsTab = new ExportPaymentsTabHandler(ui);
		ui.add(mainTabbedPane, paymentsTab.getTab());

		return exportTab;
	}

	@Override
	public void refresh() {
	}

}
