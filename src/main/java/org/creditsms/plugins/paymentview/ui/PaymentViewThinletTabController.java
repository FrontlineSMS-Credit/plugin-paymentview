/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.BuildProperties;
import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.plugins.payment.ui.PaymentPluginTabHandler;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.AnalyticsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.IncomingPaymentsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tablog.LogTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.OutgoingPaymentsTabHandler;

/**
 * @author Emmanuel Kala
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
public class PaymentViewThinletTabController
		extends BasePluginThinletTabController<PaymentViewPluginController>
		implements ThinletUiEventHandler {
	private static final String STATUS_BAR = "statusBar";
	private static final String TABP_MAIN_PANE = "tabP_mainPane";
	private static final String XML_PAYMENT_VIEW_TAB = "/ui/plugins/paymentview/paymentViewTab.xml";
	
	private Object paymentViewTab;

	private PvDebugTabController cdtController;

	public PaymentViewThinletTabController(PaymentViewPluginController controller, UiGeneratorController ui) {
		super(controller, ui);
		this.initTabs();
		this.refresh();
	}

	public void initTabs() {	
		this.paymentViewTab = ui.loadComponentFromFile(XML_PAYMENT_VIEW_TAB, this);
			
		addTab(new ClientsTabHandler(ui, getPluginController()));
		addTab(new IncomingPaymentsTabHandler(ui, getPluginController()));
		addTab(new OutgoingPaymentsTabHandler(ui, getPluginController()));
		addTab(new AnalyticsTabHandler(ui, getPluginController()));
		addTab(new LogTabHandler(ui, getPluginController()));
		addTab(new WalletTabHander(ui, getPluginController()));
		
		//For Tests Only
		if(BuildProperties.getInstance().isSnapshot()) {
			cdtController = new PvDebugTabController(ui);
			cdtController.setMessageDao(ui.getFrontlineController().getMessageDao());
			ui.add(ui.find(this.paymentViewTab, TABP_MAIN_PANE), cdtController.getTab());
		}
	}
	
	private void addTab(PaymentPluginTabHandler handler) {
		handler.refresh();
		Object mainPane = ui.find(this.paymentViewTab, TABP_MAIN_PANE);
		ui.add(mainPane, handler.getTab());
	}
	
	public void updateStatusBar(String message) {
		ui.setText(ui.find(this.paymentViewTab, STATUS_BAR), message);
	}

	/**
	 * Refreshes the tab display
	 */
	public void refresh() {}

	/**
	 * @return the paymentViewTab
	 */
	public Object getPaymentViewTab() {
		return paymentViewTab;
	}

	public void deinit() {}
}