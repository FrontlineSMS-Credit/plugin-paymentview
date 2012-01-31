/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright (C) - 2009, 2010
 * 
 * This file is part of FrontlineSMS:Credit
 * 
 */
package org.creditsms.plugins.paymentview;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
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
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.plugins.payment.monitor.PaymentServiceMonitor;
import net.frontlinesms.plugins.payment.monitor.PaymentServiceMonitorImplementationLoader;
import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.plugins.payment.service.PaymentServiceException;
import net.frontlinesms.plugins.payment.service.PaymentServiceStartRequest;
import net.frontlinesms.plugins.payment.settings.ui.PaymentViewTing;
import net.frontlinesms.ui.HomeTabEventNotification;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
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
 * mobile phone.
 * 
 * @author Emmanuel Kala
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 * @author Roy Kisaka
 * @author Alex Anderson
 */
@PluginControllerProperties(name = "Payment View", iconPath = "/icons/creditsms.png", i18nKey = "plugins.payment.name", springConfigLocation = "classpath:org/creditsms/plugins/paymentview/paymentview-spring-hibernate.xml", hibernateConfigPath = "classpath:org/creditsms/plugins/paymentview/paymentview.hibernate.cfg.xml")
public class PaymentViewPluginController extends BasePluginController implements
		ThinletUiEventHandler, EventObserver {

	// > CONSTANTS
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

	private EventBus eventBus;

	private PaymentViewThinletTabController paymentViewThinletTabController;
	private UiGeneratorController ui;

	private Map<Long, PaymentService> activeServices = new HashMap<Long, PaymentService>();

	private HashSet<PaymentServiceMonitor> serviceMonitors;

	// > CONFIG METHODS
	/**
	 * @see net.frontlinesms.plugins.PluginController#init(FrontlineSMS,
	 *      ApplicationContext)
	 */
	public void init(FrontlineSMS frontlineController,
			ApplicationContext applicationContext)
			throws PluginInitialisationException {
		eventBus = frontlineController.getEventBus();
		eventBus.registerObserver(this);

		// Initialize the DAO for the domain objects
		clientDao = (ClientDao) applicationContext.getBean("clientDao");
		accountDao = (AccountDao) applicationContext.getBean("accountDao");
		customValueDao = (CustomValueDao) applicationContext
				.getBean("customValueDao");
		customFieldDao = (CustomFieldDao) applicationContext
				.getBean("customFieldDao");
		incomingPaymentDao = (IncomingPaymentDao) applicationContext
				.getBean("incomingPaymentDao");
		outgoingPaymentDao = (OutgoingPaymentDao) applicationContext
				.getBean("outgoingPaymentDao");
		paymentServiceSettingsDao = (PaymentServiceSettingsDao) applicationContext
				.getBean("paymentServiceSettingsDao");
		serviceItemDao = (ServiceItemDao) applicationContext
				.getBean("serviceItemDao");
		targetDao = (TargetDao) applicationContext.getBean("targetDao");
		logMessageDao = (LogMessageDao) applicationContext
				.getBean("logMessageDao");
		thirdPartyResponseDao = (ThirdPartyResponseDao) applicationContext
				.getBean("thirdPartyResponseDao");
		responseRecipientDao = (ResponseRecipientDao) applicationContext
				.getBean("responseRecipientDao");
		targetServiceItemDao = (TargetServiceItemDao) applicationContext
				.getBean("targetServiceItemDao");

		targetAnalytics = new TargetAnalytics();
		targetAnalytics.setIncomingPaymentDao(incomingPaymentDao);
		targetAnalytics.setTargetDao(targetDao);

		// Default authorisation code set up to password if none
		if (!AuthorizationProperties.getInstance().isAuthCodeSet()) {
			AuthorizationProperties.getInstance().setAuthCode("password");
		}

		// If not a production build, and database is empty, add test data
		if (BuildProperties.getInstance().isSnapshot()
				&& clientDao.getClientCount() == 0) {
			try {
				DemoData.createDemoData(applicationContext);
			} catch (DuplicateKeyException e) {
				e.printStackTrace();
			}
		}

		serviceMonitors = new HashSet<PaymentServiceMonitor>();
		for (Class<? extends PaymentServiceMonitor> c : new PaymentServiceMonitorImplementationLoader()
				.getAll()) {
			PaymentServiceMonitor m = null;
			try {
				m = c.newInstance();
				m.init(frontlineController, applicationContext);
				serviceMonitors.add(m);
				eventBus.registerObserver(m);
			} catch (Exception ex) {
				log.warn("Error loading payment service monitor " + c, ex);
				try {
					eventBus.unregisterObserver(m);
				} catch (Exception _) { /* ignore */
				}
				serviceMonitors.remove(m);
			}
		}
	}

	/** @see net.frontlinesms.plugins.PluginController#deinit() */
	public void deinit() {
		if (eventBus == null)
			log.error("Event bus appears to be null so cannot deregister plugin controller or service monitors.");
		else
			eventBus.unregisterObserver(this);

		for (PaymentService s : activeServices.values()) {
			try {
				s.stopService();
			} catch (Throwable t) {
				log.error("Problem shutting down payment service: " + s, t);
			}
		}

		if (eventBus != null) {
			for (PaymentServiceMonitor m : serviceMonitors) {
				eventBus.unregisterObserver(m);
			}
		}

		if (serviceMonitors != null) {
			serviceMonitors.clear();
		}
	}

	/** @see net.frontlinesms.plugins.BasePluginController#initThinletTab(UiGeneratorController) */
	@Override
	public Object initThinletTab(UiGeneratorController ui) {
		paymentViewThinletTabController = new PaymentViewThinletTabController(
				this, ui);
		this.ui = ui;
		return paymentViewThinletTabController.getPaymentViewTab();
	}

	// > ACCESSORS
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

	public PaymentService getActiveService(PersistableSettings settings) {
		return activeServices.get(settings.getId());
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public boolean isActive(PersistableSettings s) {
		return this.activeServices.containsKey(s.getId());
	}

	public PaymentService getPaymentService(PersistableSettings s) {
		return this.activeServices.get(s.getId());
	}

	public void reportPaymentByNewClient(String name, BigDecimal amount) {
		eventBus.notifyObservers(new HomeTabEventNotification(
				HomeTabEventNotification.Type.GREEN,
				"Payment received from new client: " + name + " " + amount));
	}

	public void updateStatusBar(String message) {
		paymentViewThinletTabController.updateStatusBar(message);
	}

	public void clearStatusBar(String message) {
		updateStatusBar("");
	}

	public void notify(final FrontlineEventNotification notification) {
		if (notification instanceof PaymentServiceStartRequest) {
			PersistableSettings settings = ((PaymentServiceStartRequest) notification)
					.getSettings();
			startServiceAndLogExceptions(settings);
		} else if (notification instanceof DatabaseEntityNotification<?>) {
			Object entity = ((DatabaseEntityNotification<?>) notification)
					.getDatabaseEntity();
			if (entity instanceof PersistableSettings) {
				PersistableSettings settings = (PersistableSettings) entity;
				if (settings.getServiceTypeSuperclass() == PaymentService.class) {
					if (notification instanceof EntitySavedNotification<?>) {
						startServiceAndLogExceptions(settings);
					} else if (notification instanceof EntityDeletedNotification<?>) {
						stopService(settings);
					} else if (notification instanceof EntityUpdatedNotification<?>) {
						// Following commented out due to CREDIT-250
						PaymentService service = activeServices.get(settings.getId());
						if(service==null || !service.isRestartRequired(settings)) return;
						service.stopService();
						service.setSettings(settings);
						try {
							service.startService();
						} catch (PaymentServiceException ex) {
							log.warn("Failed to restart PaymentService for settings " + settings.getId(), ex);
						}
					}
				}
			}
		}
	}

	private void startServiceAndLogExceptions(PersistableSettings s) {
		try {
			startService(s);
		} catch (Throwable t) {
			log.warn("Problem starting payment service.", t);
			t.printStackTrace(); // FIXME remove print out
		}
	}

	public synchronized void startService(PersistableSettings settings)
			throws PaymentServiceException {
		PaymentService service;
		try {
			service = (PaymentService) settings.getServiceClass().newInstance();
		} catch (Exception ex) {
			throw new PaymentServiceException(
					"Failed to create payment service with class: "
							+ settings.getServiceClass(), ex);
		}
		service.setSettings(settings);
		service.init(this);
		service.startService();
		activeServices.put(settings.getId(), service);
	}

	public synchronized void stopService(PersistableSettings settings) {
		PaymentService service = activeServices.remove(settings.getId());
		if (service == null)
			return;
		service.stopService();
	}
	
	public PluginSettingsController getSettingsController(
			UiGeneratorController uiController) {
		return new PaymentViewTing(uiController, uiController
				.getFrontlineController().getBean("paymentServiceSettingsDao",
						PaymentServiceSettingsDao.class),
				this.getName(InternationalisationUtils.getCurrentLocale()),
				getIcon(this.getClass()));
	}
}
