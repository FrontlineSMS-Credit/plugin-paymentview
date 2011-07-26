/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright (C) - 2009, 2010
 * 
 * This file is part of FrontlineSMS:Credit
 * 
 */
package org.creditsms.plugins.paymentview;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.frontlinesms.BuildProperties;
import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.sms.events.SmsModemStatusNotification;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.messaging.sms.modem.SmsModemStatus;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceStartedNotification;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.paymentsettings.PaymentSettingsProperties;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.AuthorizationProperties;
import org.creditsms.plugins.paymentview.utils.PvUtils;
import org.springframework.context.ApplicationContext;

/**
 * This is the base class for the FrontlineSMS:Credit PaymentView plugin. The
 * PaymentView plugin is used to process payments transacted via the connected
 * mobile phone. Processing of the payments includes mining the incoming message
 * for specific information and pushing the same to an online/external database
 * or system such as Mifos - http://www.mifos.org
 * 
 * @author Emmanuel Kala
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
@PluginControllerProperties(name = "Payment View", iconPath = "/icons/creditsms.png", i18nKey = "plugins.paymentview", springConfigLocation = "classpath:org/creditsms/plugins/paymentview/paymentview-spring-hibernate.xml", hibernateConfigPath = "classpath:org/creditsms/plugins/paymentview/paymentview.hibernate.cfg.xml")
public class PaymentViewPluginController extends BasePluginController
		implements ThinletUiEventHandler, EventObserver {

//> CONSTANTS
	/** Filename and path of the XML for the PaymentView tab */

	private AccountDao accountDao;
	private ClientDao clientDao;
	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;
	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private TargetDao targetDao;
	private ServiceItemDao serviceItemDao;
	
	private TargetAnalytics targetAnalytics;
	
	private PaymentViewThinletTabController tabController;
	private UiGeneratorController ui;
	
	/** Currently we will allow only one payment service to be configured TO MAKE THINGS SIMPLER */
	private PaymentService paymentService;
	private FrontlineSMS frontlineController;
	
	/** @see net.frontlinesms.plugins.PluginController#deinit() */
	public void deinit() {
		this.frontlineController.getEventBus().unregisterObserver(this);
	}
	
//> CONFIG METHODS
	/**
	 * @see net.frontlinesms.plugins.PluginController#init(FrontlineSMS,
	 *      ApplicationContext)
	 */
	public void init(FrontlineSMS frontlineController,
			ApplicationContext applicationContext)
			throws PluginInitialisationException {
		frontlineController.getEventBus().registerObserver(this);
		
		// Initialize the DAO for the domain objects
		clientDao 			= (ClientDao) applicationContext.getBean("clientDao");
		accountDao 			= (AccountDao) applicationContext.getBean("accountDao");
		customValueDao 		= (CustomValueDao) applicationContext.getBean("customValueDao");
		customFieldDao 		= (CustomFieldDao) applicationContext.getBean("customFieldDao");
		incomingPaymentDao 	= (IncomingPaymentDao) applicationContext.getBean("incomingPaymentDao");
		outgoingPaymentDao 	= (OutgoingPaymentDao) applicationContext.getBean("outgoingPaymentDao");
		serviceItemDao 		= (ServiceItemDao) applicationContext.getBean("serviceItemDao");
		targetDao 			= (TargetDao) applicationContext.getBean("targetDao");
		this.frontlineController = frontlineController;
		
		targetAnalytics = new TargetAnalytics();
		targetAnalytics.setIncomingPaymentDao(incomingPaymentDao);
		targetAnalytics.setTargetDao(targetDao);
		
		// Default authorisation code set up to password if none
		if(!AuthorizationProperties.getInstance().isAuthCodeSet()){
			AuthorizationProperties.getInstance().setAuthCode("password");
		}
		
		// If not a production build, and database is empty, add test data
		if(BuildProperties.getInstance().isSnapshot() && clientDao.getClientCount()==0) {
			try {
				DemoData.createDemoData(applicationContext);
			} catch (DuplicateKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/** @see net.frontlinesms.plugins.BasePluginController#initThinletTab(UiGeneratorController) */
	@Override
	public Object initThinletTab(UiGeneratorController ui) {
		tabController = new PaymentViewThinletTabController(this, ui);
		this.ui = ui;
		return tabController.getPaymentViewTab();
	}
	
//> ACCESSORS
	public AccountDao getAccountDao() {
		return accountDao;
	}
	
	public IncomingPaymentDao getIncomingPaymentDao() {
		return this.incomingPaymentDao;
	}
	
	public OutgoingPaymentDao getOutgoingPaymentDao() {
		return outgoingPaymentDao;
	}
	
	public ClientDao getClientDao() {
		return clientDao;
	}
	
	public CustomFieldDao getCustomFieldDao() {
		return customFieldDao;
	}
	
	public CustomValueDao getCustomValueDao() {
		return customValueDao;
	}

	public TargetDao getTargetDao() {
		return targetDao;
	}

	public ServiceItemDao getServiceItemDao() {
		return serviceItemDao;
	}
	
	public UiGeneratorController getUiGeneratorController() {
		return ui;
	}

	public TargetAnalytics getTargetAnalytics() {
		return targetAnalytics;
	}

	public List<PaymentService> getPaymentServices() {
		if(this.paymentService == null) return Collections.emptyList();
		else {
			return Arrays.asList(new PaymentService[] { this.paymentService });
		}
	}

	public void setPaymentService(PaymentService service) {
		this.paymentService = service;
	}

	public PaymentService getPaymentService() {
		return this.paymentService;
	}

	public void notify(FrontlineEventNotification notification) {
		if(notification instanceof SmsModemStatusNotification &&
				((SmsModemStatusNotification) notification).getStatus() == SmsModemStatus.CONNECTED) {
			// TODO this should be done on a thread other than the UI Event Thread
			final SmsModem connectedModem = ((SmsModemStatusNotification) notification).getService();
			final PaymentViewPluginController pluginController = this;
			new FrontlineUiUpateJob() {
				public void run() {
					PaymentSettingsProperties props = PaymentSettingsProperties.getInstance();
					if(props.getSmsModemSerial()!=null){
						if(props.getSmsModemSerial().equals(connectedModem.getSerial())) {
							// We've just connected the configured device, so start up the payment service...
							//...if it's not already running!
							MpesaPaymentService mpesaPaymentService = (MpesaPaymentService) props.initPaymentService();
							if(mpesaPaymentService != null) {
								if(props.getPin() != null) {
									mpesaPaymentService.setPin(props.getPin());
									mpesaPaymentService.setCService(connectedModem.getCService());
									mpesaPaymentService.initDaosAndServices(pluginController);
									EventBus eventBus = ui.getFrontlineController().getEventBus();
									eventBus.registerObserver(mpesaPaymentService);
									pluginController.setPaymentService(mpesaPaymentService);
									eventBus.notifyObservers(new PaymentServiceStartedNotification(mpesaPaymentService));
								}
								//String propPaymentService = ;
								//String propPin = props.getPin();
								
								// TODO configure the payment service from the properties file
								// TODO set the payment service in the plugin controller
								// TODO start the payment service
							} else {
								ui.alert("Please setup payment service");
							}
						}	
					} else {
						ui.alert("Please setup payment service");
					}
				}
			}.execute();
		}
	}

	public Logger getLogger(Class<?> clazz) {
		return PvUtils.getLogger(clazz);
	}
	
	/*
	public class PaymentServiceStartedNotification implements FrontlineEventNotification{
		private MpesaPaymentService paymentService;
		PaymentServiceStartedNotification(MpesaPaymentService paymentService){
			this.paymentService = paymentService;
		}
	}

	public FrontlineSMS getFrontlineController() { // TODO this method shouldn't really be here :)
		return this.frontlineController;
	}
	*/
}
