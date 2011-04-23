/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright (C) - 2009, 2010
 * 
 * This file is part of FrontlineSMS:Credit
 * 
 */
package org.creditsms.plugins.paymentview;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.listener.IncomingMessageListener;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.ui.handler.IncomingPaymentsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.analytics.AnalyticsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.client.ClientsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.export.ExportTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.OutgoingPaymentsTabHandler;
import org.creditsms.plugins.paymentview.ui.handler.settings.SettingsTabHandler;
import org.springframework.context.ApplicationContext;

/**
 * This is the base class for the FrontlineSMS:Credit PaymentView plugin. The
 * PaymentView plugin is used to process payments transacted via the connected
 * mobile phone. Processing of the payments includes mining the incoming message
 * for specific information and pushing the same to an online/external database
 * or system such as Mifos - http://www.mifos.org
 * 
 * @author Emmanuel Kala
 */
@PluginControllerProperties(name = "Payment View", iconPath = "/icons/creditsms.png", i18nKey = "plugins.paymentview", springConfigLocation = "classpath:org/creditsms/plugins/paymentview/paymentview-spring-hibernate.xml", hibernateConfigPath = "classpath:org/creditsms/plugins/paymentview/paymentview.hibernate.cfg.xml")
public class PaymentViewPluginController extends BasePluginController implements
		IncomingMessageListener, ThinletUiEventHandler {

	private static final String TABP_MAIN_PANE = "tabP_mainPane";
	// > CONSTANTS
	/** Filename and path of the XML for the PaymentView tab */
	private static final String XML_PAYMENT_VIEW_TAB = "/ui/plugins/paymentview/paymentViewTab.xml";
	/** DAO for accounts */
	private AccountDao accountDao;
	private AnalyticsTabHandler analyticsTab;
	// > INSTANCE PROPERTIES
	/** DAO for clients */
	private ClientDao clientDao;
	// > THE OFFICIAL TABS FOR PAYMENTVIEW
	private ClientsTabHandler clientsTab;
	/** DAO for custom fields */
	private CustomFieldDao customFieldDao;
	/** DAO for quick dial codes (USSD requests) */
	private CustomValueDao customValueDao;

	private ExportTabHandler exportTab;
	private FrontlineSMS frontlineController;

	/** DAO for incoming payments */
	private IncomingPaymentDao incomingPaymentDao;
	private IncomingPaymentsTabHandler incomingPayTab;
	private Object mainPane;

	/** DAO for payment view errors */
	private OutgoingPaymentDao outgoingPaymentDao;
	private OutgoingPaymentsTabHandler outgoingPayTab;
	Object paymentViewTab;
	/** DAO for payment view errors */
	private ServiceItemDao serviceItemDao;
	private SettingsTabHandler settingsTab;
	private PaymentViewThinletTabController tabController;
	/** DAO for payment view errors */
	private TargetDao targetDao;

	/**
	 * @see net.frontlinesms.plugins.PluginController#deinit()
	 */
	public void deinit() {
		// this.frontlineController.removeIncomingMessageListener(this);
	}

	/**
	 * Ensures that the incoming message is trapped and the necessary
	 * information is extracted i.e. transaction type, amount, sender and
	 * transaction code (if any) The above parameters may vary amongst service
	 * providers
	 */
	public void incomingMessageEvent(FrontlineMessage message) {
	}

	// > CONFIG METHODS
	/**
	 * @see net.frontlinesms.plugins.PluginController#init(FrontlineSMS,
	 *      ApplicationContext)
	 */
	public void init(FrontlineSMS frontlineController,
			ApplicationContext applicationContext)
			throws PluginInitialisationException {
		this.frontlineController = frontlineController;
		this.frontlineController.addIncomingMessageListener(this);

		// Initialize the DAO for the domain objects
		clientDao = (ClientDao) applicationContext.getBean("clientDao");
		customValueDao = (CustomValueDao) applicationContext
				.getBean("customValueDao");
		customFieldDao = (CustomFieldDao) applicationContext
				.getBean("customFieldDao");
		incomingPaymentDao = (IncomingPaymentDao) applicationContext
				.getBean("incomingPaymentDao");
		outgoingPaymentDao = (OutgoingPaymentDao) applicationContext
				.getBean("outgoingPaymentDao");
		serviceItemDao = (ServiceItemDao) applicationContext
				.getBean("serviceItemDao");
		targetDao = (TargetDao) applicationContext.getBean("targetDao");
		accountDao = (AccountDao) applicationContext.getBean("accountDao");
		
		new DummyData(accountDao, clientDao, customFieldDao, incomingPaymentDao, outgoingPaymentDao); 
	}

	/** @see net.frontlinesms.plugins.BasePluginController#initThinletTab(UiGeneratorController) */
	@Override
	public Object initThinletTab(UiGeneratorController uiController) {
		tabController = new PaymentViewThinletTabController(this, uiController);
		paymentViewTab = uiController.loadComponentFromFile(
				XML_PAYMENT_VIEW_TAB, tabController);

		tabController.setTabComponent(paymentViewTab);
		tabController.refresh();

		mainPane = uiController.find(paymentViewTab, TABP_MAIN_PANE);

		clientsTab = new ClientsTabHandler(uiController, clientDao, accountDao,
				customFieldDao, customValueDao);
		clientsTab.refresh();
		uiController.add(mainPane, clientsTab.getTab());

		incomingPayTab = new IncomingPaymentsTabHandler(uiController,
				incomingPaymentDao);
		incomingPayTab.refresh();
		uiController.add(mainPane, incomingPayTab.getTab());

		outgoingPayTab = new OutgoingPaymentsTabHandler(uiController,
				outgoingPaymentDao, clientDao);
		outgoingPayTab.refresh();
		uiController.add(mainPane, outgoingPayTab.getTab());

		exportTab = new ExportTabHandler(uiController, clientDao,
				incomingPaymentDao, outgoingPaymentDao);
		exportTab.refresh();
		uiController.add(mainPane, exportTab.getTab());

		analyticsTab = new AnalyticsTabHandler(uiController, clientDao,
				accountDao, incomingPaymentDao, outgoingPaymentDao,
				serviceItemDao, targetDao);

		analyticsTab.refresh();
		uiController.add(mainPane, analyticsTab.getTab());

		settingsTab = new SettingsTabHandler(uiController, incomingPaymentDao,
				outgoingPaymentDao, accountDao, clientDao);
		settingsTab.refresh();
		uiController.add(mainPane, settingsTab.getTab());

		return paymentViewTab;
	}
}
