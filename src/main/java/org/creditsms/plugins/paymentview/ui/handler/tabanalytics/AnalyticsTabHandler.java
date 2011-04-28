package org.creditsms.plugins.paymentview.ui.handler.tabanalytics;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ConfigureServiceTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class AnalyticsTabHandler implements ThinletUiEventHandler {
	private static final String XML_ANALYTICS_TAB = "/ui/plugins/paymentview/analytics/tabanalytics.xml";

	private AccountDao accountDao;
	private Object analyticsTab;
	private ClientDao clientDao;
	private ConfigureServiceTabHandler configureServiceTabHandler;
	private AddClientTabHandler createDashBoardHandler;

	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private ServiceItemDao serviceItemDao;
	private TargetDao targetDao;
	private UiGeneratorController ui;
	private ViewDashBoardTabHandler viewDashBoardHandler;

	public AnalyticsTabHandler(UiGeneratorController ui, PaymentViewThinletTabController paymentViewThinletTabController) {
		this.ui = ui;
		this.accountDao = paymentViewThinletTabController.getAccountDao();
		this.targetDao = paymentViewThinletTabController.getTargetDao();
		this.clientDao = paymentViewThinletTabController.getClientDao();
		this.incomingPaymentDao = paymentViewThinletTabController.getIncomingPaymentDao();
		this.outgoingPaymentDao = paymentViewThinletTabController.getOutgoingPaymentDao();
		this.serviceItemDao = paymentViewThinletTabController.getServiceItemDao();

		init();
	}

	public Object getTab() {
		return analyticsTab;
	}

	protected Object init() {
		analyticsTab = ui.loadComponentFromFile(XML_ANALYTICS_TAB, this);
		createDashBoardHandler = new AddClientTabHandler(ui,
				analyticsTab, clientDao, accountDao, targetDao, incomingPaymentDao, outgoingPaymentDao);
		viewDashBoardHandler = new ViewDashBoardTabHandler(ui, analyticsTab,
				clientDao);
		configureServiceTabHandler = new ConfigureServiceTabHandler(ui,
				analyticsTab);
		return analyticsTab;
	}

	public void refresh() {
		this.createDashBoardHandler.refresh();
		this.viewDashBoardHandler.refresh();
		this.configureServiceTabHandler.refresh();
	}

	// > EVENTS...

}
