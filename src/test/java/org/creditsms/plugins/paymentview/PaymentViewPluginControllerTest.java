package org.creditsms.plugins.paymentview;

import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Map;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.data.events.EntityDeletedNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.data.events.EntityUpdatedNotification;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.messaging.sms.events.SmsModemStatusNotification;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.messaging.sms.modem.SmsModemStatus;
import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.plugins.payment.service.PaymentServiceException;
import net.frontlinesms.serviceconfig.ConfigurableService;
import net.frontlinesms.serviceconfig.StructuredProperties;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

public class PaymentViewPluginControllerTest extends BaseTestCase {
	/** {@link PaymentViewPluginController} instance under test */
	PaymentViewPluginController controller;
    private SmsModem smsModem;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.controller = new PaymentViewPluginController();
		this.smsModem = mock(SmsModem.class);
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
	 * rWHEN settings are updated
	 * THEN restart the corresponding payment service
	*/
	public void testRestartServiceWhenSettingsUpdate() throws Exception {
		// given
		PersistableSettings mockSettings = mockSettings();
		MockPaymentService service = addActiveService(controller, mockSettings);
		
		//when
		controller.notify(new EntityUpdatedNotification<PersistableSettings>(mockSettings));
		
		//then
		assertEquals(1, controller.getActiveServices().size());
		assertTrue(service.wasStopped());
	}
	
	/**
	*	TITLE:when deleting a payment service settings, FrontlineSMS SHOULD
	*	stop the payment service
	*	GIVEN the service is running
	*	WHEN settings are deleted
	*	THEN the corresponding payment service is stopped
	*/
	public void testStopServiceWhenSettingsDeleted() throws Exception {
		// given
		PersistableSettings mockSettings = mockSettings();
		MockPaymentService service = addActiveService(controller, mockSettings);
		
		//when
		controller.notify(new EntityDeletedNotification<PersistableSettings>(mockSettings));
		
		//then
		assertEquals(0, controller.getActiveServices().size());
		assertTrue(service.wasStopped());
	}

	@SuppressWarnings("unchecked")
	private MockPaymentService addActiveService(PaymentViewPluginController controller, PersistableSettings settings) throws Exception {
		Field field = controller.getClass().getDeclaredField("activeServices");
		field.setAccessible(true);
		Map<Long, PaymentService> activeServices = (Map<Long, PaymentService>) field.get(controller);
		MockPaymentService service = new MockPaymentService();
		activeServices.put(settings.getId(), service);
		return service;
	}

	/**
	 * TITLE:when a modem whose settings have been saved to the database is connected
	 * GIVEN the modem settings exists in the database
	 * WHEN modem is connected
	 * THEN the corresponding payment service is started
	 **/
	public void testStartingServiceWhenModemConnected() {
		String serial = "";
		String Pin = "";
		String ImsiNumber = "";
		String serviceClass = "";
		
		//given
		createServiceSettingsForModem(serial, Pin, ImsiNumber, serviceClass);

		//when
		controller.notify(new SmsModemStatusNotification(smsModem, SmsModemStatus.CONNECTED));
		
		//then
		startPaymetService();
		assertEquals(1, controller.getActiveServices().size());
	}

	private void startPaymetService() {
		// TODO Auto-generated method stub
		
	}

	private void createServiceSettingsForModem(String serial, String Pin, String ImsiNumber, String serviceClass) {
		// TODO Auto-generated method stub
	}
}

class MockPaymentService implements PaymentService {
	private boolean started;
	private boolean stoped;

	//> Verification methods
	public boolean wasStarted() {
		return started;
	}
	public boolean wasStopped() {
		return stoped;
	}
	
//> PaymentService methods
	public StructuredProperties getPropertiesStructure() {
		return null;
	}
	public PersistableSettings getSettings() {
		return null;
	}
	public void setSettings(PersistableSettings settings) {
	}
	public Class<? extends ConfigurableService> getSuperType() {
		return PaymentService.class;
	}
	public void makePayment(OutgoingPayment payment)
			throws PaymentServiceException {
	}
	public void checkBalance() throws PaymentServiceException {
	}
	public BigDecimal getBalanceAmount() {
		return null;
	}
	public void startService() throws PaymentServiceException {
		started = true;
	}
	public void stopService() {
		stoped  = true;
	}
	public boolean isOutgoingPaymentEnabled() {
		return false;
	}
}
