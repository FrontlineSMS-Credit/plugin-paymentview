package org.creditsms.plugins.paymentview.ui.handler.tabanalytics;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;
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
	private final PaymentViewPluginController pluginController;

	public AnalyticsTabHandler(UiGeneratorController ui, final PaymentViewPluginController pluginController) {
		this.ui = ui;
		this.pluginController = pluginController;
		init();
	}

	public Object getTab() {
		return analyticsTab;
	}

	protected Object init() {
		analyticsTab = ui.loadComponentFromFile(XML_ANALYTICS_TAB, this);
		createDashBoardHandler = new AddClientTabHandler(ui, analyticsTab, pluginController);
		viewDashBoardHandler = new ViewDashBoardTabHandler(ui, analyticsTab, pluginController);
		configureServiceTabHandler = new ConfigureServiceTabHandler(ui,	analyticsTab, pluginController);
		return analyticsTab;
	}

	public void refresh() {
		this.createDashBoardHandler.refresh();
		this.viewDashBoardHandler.refresh();
		this.configureServiceTabHandler.refresh();
	}
	
}
