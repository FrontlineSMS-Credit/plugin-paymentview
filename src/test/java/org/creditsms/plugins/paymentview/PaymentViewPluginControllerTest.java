package org.creditsms.plugins.paymentview;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.data.events.EntityDeletedNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.data.events.EntityUpdatedNotification;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.plugins.BasePluginControllerTests;
import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.plugins.payment.service.PaymentServiceException;
import net.frontlinesms.plugins.payment.service.PaymentServiceStartRequest;
import net.frontlinesms.plugins.payment.service.ui.PaymentServiceUiActionHandler;
import net.frontlinesms.serviceconfig.ConfigurableService;
import net.frontlinesms.serviceconfig.StructuredProperties;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import static org.mockito.Mockito.*;

public class PaymentViewPluginControllerTest extends BasePluginControllerTests<PaymentViewPluginController> {
	EventBus eventBus;
	PaymentServiceSettingsDao settingsDao;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		settingsDao = mock(PaymentServiceSettingsDao.class);
		inject(controller, "paymentServiceSettingsDao", settingsDao);
		
		eventBus = mock(EventBus.class);
		inject(controller, "eventBus", eventBus);
	}
	
	@Override
	protected Class<PaymentViewPluginController> getControllerClass() {
		return PaymentViewPluginController.class;
	}
	
	/**
	 * TITLE: after saving the payment service settings, FrontlineSMS SHOULD
	 * 	start the payment service
	 * GIVEN settings are not previously saved
	 * WHEN settings are saved
	 * THEN start the corresponding payment service
	 */
	public void testServiceStartsWhenSettingsSaved() {
		// given
		assertEquals(0, controller.getActiveServices().size());
		PersistableSettings mockSettings = mockSettings();

		// when
		controller.notify(new EntitySavedNotification<PersistableSettings>(mockSettings));
		
		// then
		assertEquals(1, controller.getActiveServices().size());
		PaymentService service = controller.getActiveServices().toArray(new PaymentService[0])[0];
		assertTrue(service instanceof MockPaymentService);
		assertTrue(((MockPaymentService) service).wasStarted());
	}

	private PersistableSettings mockSettings() {
		return new PersistableSettings(new MockPaymentService());
	}
	
	/**
	 * TITLE:after updating the payment service settings, FrontlineSMS SHOULD
	 * restart the payment service
	 * GIVEN the service is running
	 * WHEN settings are updated
	 * THEN restart the corresponding payment service
	 */
	public void testRestartServiceWhenSettingsUpdate_running() throws Exception {
		// given
		PersistableSettings mockSettings = mockSettings();
		MockPaymentService service = addActiveService(mockSettings);
		
		// when
		controller.notify(new EntityUpdatedNotification<PersistableSettings>(mockSettings));
		
		// then
		assertEquals(1, controller.getActiveServices().size());
		assertTrue(service.wasStopped());
		assertTrue(service.wasStarted());
	}
	
	/**
	 * TITLE:after updating the payment service settings, FrontlineSMS SHOULD
	 * restart the payment service
	 * GIVEN the service is NOT running
	 * WHEN settings are updated
	 * THEN restart the corresponding payment service
	 */
	public void testRestartServiceWhenSettingsUpdate_notRunning() throws Exception {
		// given
		PersistableSettings mockSettings = mockSettings();
		assertEquals(0, controller.getActiveServices().size());
		
		// when
		controller.notify(new EntityUpdatedNotification<PersistableSettings>(mockSettings));
		
		// then
		assertEquals(0, controller.getActiveServices().size());
	}
	
	/**
	 *	TITLE:when deleting a payment service settings, FrontlineSMS SHOULD
	 *	stop the payment service
	 *	GIVEN the service is running
	 *	WHEN settings are deleted
	 *	THEN the corresponding payment service is stopped
	 */
	public void testStopServiceWhenSettingsDeleted_running() throws Exception {
		// given
		PersistableSettings mockSettings = mockSettings();
		MockPaymentService service = addActiveService(mockSettings);
		
		// when
		controller.notify(new EntityDeletedNotification<PersistableSettings>(mockSettings));
		
		// then
		assertEquals(0, controller.getActiveServices().size());
		assertTrue(service.wasStopped());
	}
	
	/**
	 *	TITLE:when deleting a payment service settings, FrontlineSMS SHOULD
	 *	stop the payment service
	 *	GIVEN the service is NOT running
	 *	WHEN settings are deleted
	 *	THEN no attempt is made to stop the service
	 */
	public void testStopServiceWhenSettingsDeleted_notRunning() throws Exception {
		// given
		PersistableSettings mockSettings = mockSettings();
		assertEquals(0, controller.getActiveServices().size());
		
		// when
		controller.notify(new EntityDeletedNotification<PersistableSettings>(mockSettings));
		
		// then
		assertEquals(0, controller.getActiveServices().size());
	}
	
	public void testRequestForServiceStartHonoured() {
		// given
		assertEquals(0, controller.getActiveServices().size());
		PersistableSettings mockSettings = mockSettings();
		
		// when
		controller.notify(new PaymentServiceStartRequest(mockSettings));
		
		// then
		assertEquals(1, controller.getActiveServices().size());
		assertTrue(getFirstService().wasStarted());
	}
	
	/**
	 * GIVEN there are payment services running
	 * WHEN deinit() is called
	 * THEN running services are stopped
	 * @throws Exception 
	 */
	public void testDeinitStopsServices() throws Exception {
		// given
		inject(controller, "serviceMonitors", new HashSet<Object>());
		addActiveService(mockSettings());
		MockPaymentService service = getFirstService();
		
		// when
		controller.deinit();
		
		// then
		assertTrue(service.wasStopped());
	}
	
	/**
	 * GIVEN there are payment services running
	 * WHEN deinit() is called and one of the services throws a shutdown exception
	 * THEN running services are all still stopped
	 * @throws Exception 
	 */
	public void testDeinitStopsServicesWithShutdownExceptions() throws Exception {
		// given
		inject(controller, "serviceMonitors", new HashSet<Object>());
		PaymentService exceptionableService = mock(PaymentService.class);
		doThrow(new RuntimeException("Service shutdown failed!")).when(exceptionableService).stopService();
		addActiveService(1L, exceptionableService);
		addActiveService(mockSettings());
		MockPaymentService service = getFirstService();
		
		// when
		controller.deinit();
		
		// then
		assertTrue(service.wasStopped());
	}
	
	/**
	 * GIVEN (blank)
	 * WHEN we request payment service start
	 * THEN payment service is initialised before starting
	 * @throws Exception 
	 */
	public void testPaymentServiceStart() throws Exception {
		// given
		PersistableSettings s = mockSettings();
		
		// when
		controller.startService(s);
		
		// then
		MockPaymentService service = getFirstService();
		assertTrue(service.isInitialised());
		assertTrue(service.isSettingsSet());
	}
	
	private MockPaymentService getFirstService() {
		PaymentService service = getActiveService(0);
		assertTrue(service instanceof MockPaymentService);
		return (MockPaymentService) service;
	}
	
	private PaymentService getActiveService(int index) {
		return controller.getActiveServices().toArray(new PaymentService[0])[index];
	}

	private MockPaymentService addActiveService(PersistableSettings settings) throws Exception {
		MockPaymentService service = new MockPaymentService();
		return (MockPaymentService) addActiveService(settings.getId(), service);
	}

	@SuppressWarnings("unchecked")
	private PaymentService addActiveService(Long settingsId, PaymentService service) throws Exception {
		Field field = controller.getClass().getDeclaredField("activeServices");
		field.setAccessible(true);
		Map<Long, PaymentService> activeServices = (Map<Long, PaymentService>) field.get(controller);
		activeServices.put(settingsId, service);
		return service;
	}
}

class MockPaymentService implements PaymentService {
	private final ArrayList<String> methodCalls = new ArrayList<String>();

//> Verification methods
	public boolean wasStarted() {
		return methodCalls.contains("startService");
	}
	public boolean wasStopped() {
		return methodCalls.contains("stopService");
	}
	public boolean isSettingsSet() {
		return methodCalls.contains("setSettings");
	}
	public boolean isInitialised() {
		return methodCalls.contains("init");
	}
	
//> PaymentService methods
	public void init(PaymentViewPluginController pluginController) throws PaymentServiceException {
		methodCalls.add("init");
	}
	public StructuredProperties getPropertiesStructure() {
		return null;
	}
	public PersistableSettings getSettings() {
		return null;
	}
	public void setSettings(PersistableSettings settings) {
		methodCalls.add("setSettings");
	}
	public boolean isRestartRequired(PersistableSettings newSettings) {
		return true;
	}
	public Class<? extends ConfigurableService> getSuperType() {
		return PaymentService.class;
	}
	public void makePayment(OutgoingPayment payment) throws PaymentServiceException {
	}
	public void checkBalance() throws PaymentServiceException {
	}
	public BigDecimal getBalanceAmount() {
		return null;
	}
	public void startService() throws PaymentServiceException {
		methodCalls.add("startService");
	}
	public void stopService() {
		methodCalls.add("stopService");
	}
	public boolean isOutgoingPaymentEnabled() {
		return false;
	}
	public PaymentServiceUiActionHandler getServiceActionUiHandler(UiGeneratorController ui) {
		return null;
	}
	public boolean isCheckBalanceEnabled() {
		return false;
	}
}
