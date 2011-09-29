/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.BuildProperties;
import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.AnalyticsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.IncomingPaymentsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tablog.LogTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.OutgoingPaymentsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.SettingsTabHandler;

/**
 * 
 * @author Emmanuel Kala
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
public class PaymentViewThinletTabController extends
		BasePluginThinletTabController<PaymentViewPluginController> implements
		ThinletUiEventHandler {

	private static final String STATUS_BAR = "statusBar";
	private static final String TABP_MAIN_PANE = "tabP_mainPane";
	private static final String XML_PAYMENT_VIEW_TAB = "/ui/plugins/paymentview/paymentViewTab.xml";

	private SettingsTabHandler settingsTab;
	private OutgoingPaymentsTabHandler outgoingPayTab;
	private IncomingPaymentsTabHandler incomingPayTab;
	private ClientsTabHandler clientsTab;
	private AnalyticsTabHandler analyticsTab;
	
	private Object mainPane;
	private Object paymentViewTab;

	private PvDebugTabController cdtController;
//	private ExportTabHandler exportTab;
	private LogTabHandler logsTab;
	private Object statusBar;

	/**
	 * 
	 * @param paymentViewController
	 *            value for {@link #controller}
	 * @param uiController
	 *            value for {@linkplain #ui}
	 */
	public PaymentViewThinletTabController(
			PaymentViewPluginController controller, UiGeneratorController ui) {
		super(controller, ui);
		this.initTabs();
		this.refresh();
	}

	public void initTabs() {	
		this.paymentViewTab = ui.loadComponentFromFile(XML_PAYMENT_VIEW_TAB, this);
			
		statusBar = ui.find(this.paymentViewTab, STATUS_BAR);
		
		mainPane = ui.find(this.paymentViewTab, TABP_MAIN_PANE);
		clientsTab = new ClientsTabHandler(ui, getPluginController());
		clientsTab.refresh();
		ui.add(mainPane, clientsTab.getTab());

		incomingPayTab = new IncomingPaymentsTabHandler(ui, getPluginController());
		incomingPayTab.refresh();
		ui.add(mainPane, incomingPayTab.getTab());

		outgoingPayTab = new OutgoingPaymentsTabHandler(ui, getPluginController());
		outgoingPayTab.refresh();
		ui.add(mainPane, outgoingPayTab.getTab());

		analyticsTab = new AnalyticsTabHandler(ui, getPluginController());
		analyticsTab.refresh();
		ui.add(mainPane, analyticsTab.getTab());

		settingsTab = new SettingsTabHandler(ui, getPluginController());
		//settingsTab.refresh();
		ui.add(mainPane, settingsTab.getTab());
		
		logsTab = new LogTabHandler(ui, getPluginController());
		logsTab.refresh();
		ui.add(mainPane, logsTab.getTab());
		
		//For Tests Only
		if(BuildProperties.getInstance().isSnapshot()) {
			cdtController = new PvDebugTabController(ui);
			cdtController.setMessageDao(ui.getFrontlineController().getMessageDao());
			ui.add(mainPane, cdtController.getTab());
		}
	}
	
	public void updateStatusBar(String message){
		ui.setText(statusBar, message);
	}

	/**
	 * Refreshes the tab display
	 */
	public void refresh() {
	}

	/**
	 * @return the paymentViewTab
	 */
	public Object getPaymentViewTab() {
		return paymentViewTab;
	}

	public void deinit() {
		// TODO de-register with EventBus
	}

}