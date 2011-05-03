package org.creditsms.plugins.paymentview.ui.handler.tabanalytics;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ConfigureServiceTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class AnalyticsTabHandler implements ThinletUiEventHandler {
	private static final String XML_ANALYTICS_TAB = "/ui/plugins/paymentview/analytics/tabanalytics.xml";

	private Object analyticsTab;
	private ConfigureServiceTabHandler configureServiceTabHandler;
	private AddClientTabHandler createDashBoardHandler;

	private UiGeneratorController ui;
	private ViewDashBoardTabHandler viewDashBoardHandler;
	private PaymentViewThinletTabController paymentViewThinletTabController;

	public AnalyticsTabHandler(UiGeneratorController ui, final PaymentViewThinletTabController paymentViewThinletTabController) {
		this.ui = ui;
		this.paymentViewThinletTabController = paymentViewThinletTabController;

		init();
	}

	public Object getTab() {
		return analyticsTab;
	}

	protected Object init() {
		analyticsTab = ui.loadComponentFromFile(XML_ANALYTICS_TAB, this);
		createDashBoardHandler = new AddClientTabHandler(ui, analyticsTab, paymentViewThinletTabController);
		viewDashBoardHandler = new ViewDashBoardTabHandler(ui, analyticsTab, paymentViewThinletTabController);
		configureServiceTabHandler = new ConfigureServiceTabHandler(ui,	analyticsTab, paymentViewThinletTabController);
		return analyticsTab;
	}

	public void refresh() {
		this.createDashBoardHandler.refresh();
		this.viewDashBoardHandler.refresh();
		this.configureServiceTabHandler.refresh();
	}
}
