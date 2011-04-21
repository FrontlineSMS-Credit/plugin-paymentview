package org.creditsms.plugins.paymentview.ui.handler.analytics;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;

public class AnalyticsTabHandler implements ThinletUiEventHandler {
	private static final String XML_ANALYTICS_TAB = "/ui/plugins/paymentview/analytics/tabanalytics.xml";

	private Object analyticsTab;
	private CreateDashBoardTabHandler createDashBoardHandler;
	private UiGeneratorController ui;
	private ViewDashBoardTabHandler viewDashBoardHandler;
	private ConfigureServiceTabHandler configureServiceTabHandler;

	private TargetDao targetDao;
	private ClientDao clientDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private IncomingPaymentDao incomingPaymentDao;
	private AccountDao accountDao;
	private ServiceItemDao serviceItemDao;

	public AnalyticsTabHandler(UiGeneratorController ui, ClientDao clientDao,
			AccountDao accountDao, IncomingPaymentDao incomingPaymentDao,
			OutgoingPaymentDao outgoingPaymentDao,
			ServiceItemDao serviceItemDao, TargetDao targetDao) {
		this.ui = ui;
		this.accountDao = accountDao;
		this.targetDao = targetDao;
		this.clientDao = clientDao;
		this.incomingPaymentDao = incomingPaymentDao;
		this.outgoingPaymentDao = outgoingPaymentDao;
		this.serviceItemDao = serviceItemDao;

		init();
	}

	public void refresh() {
		this.createDashBoardHandler.refresh();
		this.viewDashBoardHandler.refresh();
		this.configureServiceTabHandler.refresh();
	}

	protected Object init() {
		analyticsTab = ui.loadComponentFromFile(XML_ANALYTICS_TAB, this);
		createDashBoardHandler = new CreateDashBoardTabHandler(ui, analyticsTab);
		viewDashBoardHandler = new ViewDashBoardTabHandler(ui, analyticsTab);
		configureServiceTabHandler = new ConfigureServiceTabHandler(ui,
				analyticsTab);
		return analyticsTab;
	}

	public Object getTab() {
		return analyticsTab;
	}

	// > EVENTS...

}
