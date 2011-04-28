package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;

public class OutgoingPaymentsTabHandler extends BaseTabHandler {

	private static final String TABBED_PANE_MAIN = "tabbedPaneMain";
	private static final String XML_OUTGOINGPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/taboutgoingpayments.xml";

	private AccountDao accountDao;
	private ClientDao clientDao;
	private ImportNewPaymentsTabHandler importNewPaymentsTab;
	private Object mainTabbedPane;
	private OutgoingPaymentDao outgoingPaymentDao;
	private Object outgoingPaymentsTab;

	private SelectFromClientsTabHandler selectFromClientsTab;
	private SendNewPaymentsTabHandler sendNewPaymentsTab;
	private SentPaymentsTabHandler sentPaymentsTab;
	private PaymentViewThinletTabController paymentViewThinletTabController;

	public OutgoingPaymentsTabHandler(UiGeneratorController ui, final PaymentViewThinletTabController paymentViewThinletTabController) {
		super(ui);
		this.paymentViewThinletTabController = paymentViewThinletTabController;
		this.outgoingPaymentDao = paymentViewThinletTabController.getOutgoingPaymentDao();
		this.clientDao = paymentViewThinletTabController.getClientDao();
		init();
	}

	@Override
	protected Object initialiseTab() {
		outgoingPaymentsTab = ui.loadComponentFromFile(
				XML_OUTGOINGPAYMENTS_TAB, this);

		mainTabbedPane = ui.find(outgoingPaymentsTab, TABBED_PANE_MAIN);
		sentPaymentsTab = new SentPaymentsTabHandler(ui, clientDao,
				outgoingPaymentDao, accountDao);
		ui.add(mainTabbedPane, sentPaymentsTab.getTab());

		sendNewPaymentsTab = new SendNewPaymentsTabHandler(ui);
		ui.add(mainTabbedPane, sendNewPaymentsTab.getTab());

		importNewPaymentsTab = new ImportNewPaymentsTabHandler(ui);
		ui.add(mainTabbedPane, importNewPaymentsTab.getTab());

		selectFromClientsTab = new SelectFromClientsTabHandler(ui, clientDao);
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