package org.creditsms.plugins.paymentview.ui.handler.tabexport;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;

public class ExportTabHandler extends BaseTabHandler {
	private static final String TABBED_PANE_MAIN = "tabP_MainPane";
	private static final String XML_EXPORT_TAB = "/ui/plugins/paymentview/export/tabexport.xml";

	private ClientDao clientDao;
	private ExportClientHistoryTabHandler exportclientHistoryTab;
	private ExportClientsTabHandler exportclientsTab;
	private Object exportTab;
	private IncomingPaymentDao incomingPaymentDao;
	private Object mainTabbedPane;
	private OutgoingPaymentDao outgoingPaymentDao;
	private ExportPaymentsTabHandler exportpaymentsTab;
	private CustomFieldDao customFieldDao;
	private CustomValueDao customValueDao;

	public ExportTabHandler(UiGeneratorController ui,
			PaymentViewThinletTabController paymentViewThinletTabController) {
		super(ui);
		this.clientDao = paymentViewThinletTabController.getClientDao();
		this.incomingPaymentDao = paymentViewThinletTabController
				.getIncomingPaymentDao();
		this.outgoingPaymentDao = paymentViewThinletTabController
				.getOutgoingPaymentDao();
		this.customFieldDao = paymentViewThinletTabController
				.getCustomFieldDao();
		this.customValueDao = paymentViewThinletTabController
				.getCustomValueDao();
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

		exportclientsTab = new ExportClientsTabHandler(ui, clientDao,
				customFieldDao, customValueDao, outgoingPaymentDao,
				incomingPaymentDao);
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
