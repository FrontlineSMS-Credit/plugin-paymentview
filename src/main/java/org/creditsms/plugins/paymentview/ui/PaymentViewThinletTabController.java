/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.handler.IncomingPaymentsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.AnalyticsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabexport.ExportTabHandler;
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

	private static final String TABP_MAIN_PANE = "tabP_mainPane";
	private static final String XML_PAYMENT_VIEW_TAB = "/ui/plugins/paymentview/paymentViewTab.xml";

	private SettingsTabHandler settingsTab;
	private OutgoingPaymentsTabHandler outgoingPayTab;
	private IncomingPaymentsTabHandler incomingPayTab;
	private ClientsTabHandler clientsTab;
	private ExportTabHandler exportTab;
	private AnalyticsTabHandler analyticsTab;

	private Object mainPane;

	private Object paymentViewTab;

	private AccountDao accountDao;
	private ClientDao clientDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private CustomFieldDao customFieldDao;
	private CustomValueDao customValueDao;
	private ServiceItemDao serviceItemDao;
	private TargetDao targetDao;
	private IncomingPaymentDao incomingPaymentDao;
	private PvDebugTabController cdtController;

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
	}

	public void initTabs() {	
		this.setPaymentViewTab(ui.loadComponentFromFile(XML_PAYMENT_VIEW_TAB, this));
		
		mainPane = ui.find(getPaymentViewTab(), TABP_MAIN_PANE);
		clientsTab = new ClientsTabHandler(ui, this);
		clientsTab.refresh();
		ui.add(mainPane, clientsTab.getTab());

		incomingPayTab = new IncomingPaymentsTabHandler(ui, this);
		incomingPayTab.refresh();
		ui.add(mainPane, incomingPayTab.getTab());

		outgoingPayTab = new OutgoingPaymentsTabHandler(ui, this);
		outgoingPayTab.refresh();
		ui.add(mainPane, outgoingPayTab.getTab());

		exportTab = new ExportTabHandler(ui, this);
		exportTab.refresh();
		ui.add(mainPane, exportTab.getTab());

		analyticsTab = new AnalyticsTabHandler(ui, this);
		analyticsTab.refresh();
		ui.add(mainPane, analyticsTab.getTab());

		settingsTab = new SettingsTabHandler(ui, this);
		settingsTab.refresh();
		ui.add(mainPane, settingsTab.getTab());
		
		cdtController = new PvDebugTabController(ui);
		cdtController.setMessageDao(ui.getFrontlineController().getMessageDao());
		ui.add(mainPane, cdtController.getTab());
	}

	/**
	 * Refreshes the tab display
	 */
	public void refresh() {
	}

	public AccountDao getAccountDao() {
		return accountDao;
	}

	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public ClientDao getClientDao() {
		return clientDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public OutgoingPaymentDao getOutgoingPaymentDao() {
		return outgoingPaymentDao;
	}

	public void setOutgoingPaymentDao(OutgoingPaymentDao outgoingPaymentDao) {
		this.outgoingPaymentDao = outgoingPaymentDao;
	}

	public CustomFieldDao getCustomFieldDao() {
		return customFieldDao;
	}

	public void setCustomFieldDao(CustomFieldDao customFieldDao) {
		this.customFieldDao = customFieldDao;
	}

	public CustomValueDao getCustomValueDao() {
		return customValueDao;
	}

	public void setCustomValueDao(CustomValueDao customValueDao) {
		this.customValueDao = customValueDao;
	}

	public ServiceItemDao getServiceItemDao() {
		return serviceItemDao;
	}

	public void setServiceItemDao(ServiceItemDao serviceItemDao) {
		this.serviceItemDao = serviceItemDao;
	}

	public TargetDao getTargetDao() {
		return targetDao;
	}

	public void setTargetDao(TargetDao targetDao) {
		this.targetDao = targetDao;
	}

	public IncomingPaymentDao getIncomingPaymentDao() {
		return incomingPaymentDao;
	}

	public void setIncomingPaymentDao(IncomingPaymentDao incomingPaymentDao) {
		this.incomingPaymentDao = incomingPaymentDao;
	}

	/**
	 * @param paymentViewTab the paymentViewTab to set
	 */
	public void setPaymentViewTab(Object paymentViewTab) {
		this.paymentViewTab = paymentViewTab;
	}

	/**
	 * @return the paymentViewTab
	 */
	public Object getPaymentViewTab() {
		return paymentViewTab;
	}
}