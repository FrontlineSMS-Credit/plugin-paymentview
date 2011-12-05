/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright (C) - 2009, 2010
 * 
 * This file is part of FrontlineSMS:Credit
 * 
 */
package org.creditsms.plugins.paymentview;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import net.frontlinesms.BuildProperties;
import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntityDeletedNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.data.events.EntityUpdatedNotification;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.sms.events.SmsModemStatusNotification;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.messaging.sms.modem.SmsModemStatus;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.plugins.payment.settings.ui.PaymentServiceSettingsHandler;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpdateJob;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.LogMessageDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;
import org.creditsms.plugins.paymentview.data.repository.ResponseRecipientDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.data.repository.TargetServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.ThirdPartyResponseDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.AuthorizationProperties;
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
@PluginControllerProperties(name="Payment View", iconPath="/icons/creditsms.png", i18nKey="plugins.payment.name",
		springConfigLocation="classpath:org/creditsms/plugins/paymentview/paymentview-spring-hibernate.xml",
		hibernateConfigPath="classpath:org/creditsms/plugins/paymentview/paymentview.hibernate.cfg.xml")
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
	private PaymentServiceSettingsDao paymentServiceSettingsDao;
	private TargetDao targetDao;
	private ServiceItemDao serviceItemDao;
	private LogMessageDao logMessageDao;
	private ThirdPartyResponseDao thirdPartyResponseDao;
	private ResponseRecipientDao responseRecipientDao;
	private TargetServiceItemDao targetServiceItemDao;
	
	private TargetAnalytics targetAnalytics;
	
	private PaymentViewThinletTabController paymentViewThinletTabController;
	private UiGeneratorController ui;
	
	private Map<Long, PaymentService> activeServices = new HashMap<Long, PaymentService>();
	private FrontlineSMS frontlineController;
	
	/** @see net.frontlinesms.plugins.PluginController#deinit() */
	public void deinit() {
		if(this.frontlineController != null) {
			EventBus eventBus = this.frontlineController.getEventBus();
			if(eventBus != null) {
				eventBus.unregisterObserver(this);
			}
		}
	}
	
//> CONFIG METHODS
	/**
	 * @see net.frontlinesms.plugins.PluginController#init(FrontlineSMS,
	 *      ApplicationContext)
	 */
	public void init(FrontlineSMS frontlineController,
			ApplicationContext applicationContext)
			throws PluginInitialisationException {
		this.frontlineController = frontlineController;
		frontlineController.getEventBus().registerObserver(this);
		
		// Initialize the DAO for the domain objects
		clientDao 			= (ClientDao) applicationContext.getBean("clientDao");
		accountDao 			= (AccountDao) applicationContext.getBean("accountDao");
		customValueDao 		= (CustomValueDao) applicationContext.getBean("customValueDao");
		customFieldDao 		= (CustomFieldDao) applicationContext.getBean("customFieldDao");
		incomingPaymentDao 	= (IncomingPaymentDao) applicationContext.getBean("incomingPaymentDao");
		outgoingPaymentDao 	= (OutgoingPaymentDao) applicationContext.getBean("outgoingPaymentDao");
		paymentServiceSettingsDao 	= (PaymentServiceSettingsDao) applicationContext.getBean("paymentServiceSettingsDao");
		serviceItemDao 		= (ServiceItemDao) applicationContext.getBean("serviceItemDao");
		targetDao 			= (TargetDao) applicationContext.getBean("targetDao");
		logMessageDao       = (LogMessageDao) applicationContext.getBean("logMessageDao");
		thirdPartyResponseDao  = (ThirdPartyResponseDao) applicationContext.getBean("thirdPartyResponseDao");
		responseRecipientDao  = (ResponseRecipientDao) applicationContext.getBean("responseRecipientDao");
		targetServiceItemDao  = (TargetServiceItemDao) applicationContext.getBean("targetServiceItemDao");
		
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
		paymentViewThinletTabController = new PaymentViewThinletTabController(this, ui);
		this.ui = ui;
		return paymentViewThinletTabController.getPaymentViewTab();
	}
	
//> ACCESSORS
	public TargetServiceItemDao getTargetServiceItemDao() {
		return targetServiceItemDao;
	}

	public AccountDao getAccountDao() {
		return accountDao;
	}
	
	public IncomingPaymentDao getIncomingPaymentDao() {
		return this.incomingPaymentDao;
	}
	
	public OutgoingPaymentDao getOutgoingPaymentDao() {
		return outgoingPaymentDao;
	}
	
	public PaymentServiceSettingsDao getPaymentServiceSettingsDao() {
		return paymentServiceSettingsDao;
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
	
	public LogMessageDao getLogMessageDao() {
		return logMessageDao;
	}
	
	public ThirdPartyResponseDao getThirdPartyResponseDao() {
		return thirdPartyResponseDao;
	}

	public ResponseRecipientDao getResponseRecipientDao() {
		return responseRecipientDao;
	}

	
	public UiGeneratorController getUiGeneratorController() {
		return ui;
	}

	public TargetAnalytics getTargetAnalytics() {
		return targetAnalytics;
	}

	public Collection<PaymentService> getActiveServices() {
		return Collections.unmodifiableCollection(activeServices.values());
	}

	public void updateStatusBar(String message) {
		paymentViewThinletTabController.updateStatusBar(message);
	}
	
	public void clearStatusBar(String message) {
		updateStatusBar("");
	}

	public void notify(final FrontlineEventNotification notification) {
//		if (notification instanceof PaymentServiceStartedNotification) {
//			new FrontlineUiUpdateJob() {
//				public void run() {
//					activeServices.add(((PaymentServiceStartedNotification) notification)
//							.getPaymentService());
//				}
//			}.execute();
//		} else if (notification instanceof PaymentServiceStoppedNotification) {
//			new FrontlineUiUpdateJob() {
//				public void run() {
//					activeServices.remove(((PaymentServiceStoppedNotification) notification)
//							.getPaymentService());
//				}
//			}.execute();
		/*} else */
		
		if(notification instanceof SmsModemStatusNotification) {
			new FrontlineUiUpdateJob() {
				public void run() {


					if (((SmsModemStatusNotification) notification).getStatus() == SmsModemStatus.CONNECTED) {
						final SmsModem connectedModem = ((SmsModemStatusNotification) notification).getService();
//						Collection<PaymentServiceSettings> paymentServiceSettingsList = paymentServiceSettingsDao.getPaymentServiceAccounts();
//						if (!paymentServiceSettingsList.isEmpty()){
//							for (PaymentServiceSettings psSettings : paymentServiceSettingsList){
//								if (psSettings.getPsSmsModemSerial().equals(connectedModem.getSerial()+"@"+connectedModem.getImsiNumber())) {
//									
//								}
//							}
//						}			
					}
					
					
				}
			}.execute();
		}

		
		if(notification instanceof DatabaseEntityNotification<?>) {
			Object entity = ((DatabaseEntityNotification<?>) notification).getDatabaseEntity();
			if(entity instanceof PersistableSettings) {
				PersistableSettings settings = (PersistableSettings) entity;
				if(settings.getServiceTypeSuperclass() == PaymentService.class) {
					if (notification instanceof EntitySavedNotification<?>) {
						try {
							PaymentService service = (PaymentService) settings.getServiceClass().newInstance();
							activeServices.put(settings.getId(), service);
							service.startService();
						} catch (final Exception ex) {
							log.warn("Failed to start PaymentService for settings " + settings.getId(), ex);
							new FrontlineUiUpdateJob() {
								public void run() {
									throw new RuntimeException(ex);
								}
							}.execute();
						}	
					} else if (notification instanceof EntityDeletedNotification<?>) {
						try {
							PaymentService service = activeServices.get(settings.getId());
							activeServices.remove(settings.getId());
							service.stopService();
						} catch (final Exception ex) {
							log.warn("Failed to stop PaymentService for settings " + settings.getId(), ex);
							new FrontlineUiUpdateJob() {
								public void run() {
									throw new RuntimeException(ex);
								}
							}.execute();
						}	
					} else if (notification instanceof EntityUpdatedNotification<?>) {
						try {
							PaymentService service = activeServices.get(settings.getId());
							activeServices.remove(settings.getId());
							service.stopService();
							activeServices.put(settings.getId(), service);
							service.startService();
						} catch (final Exception ex) {
							log.warn("Failed to restart PaymentService for settings " + settings.getId(), ex);
							new FrontlineUiUpdateJob() {
								public void run() {
									throw new RuntimeException(ex);
								}
							}.execute();
						}	
					}
				}
			}
		}
	}
	
	public PluginSettingsController getSettingsController(UiGeneratorController uiController) {
		return new PaymentServiceSettingsHandler(uiController,
				uiController.getFrontlineController().getBean("paymentServiceSettingsDao", PaymentServiceSettingsDao.class),
				this.getName(InternationalisationUtils.getCurrentLocale()),
				getIcon(this.getClass()));
	}
}
