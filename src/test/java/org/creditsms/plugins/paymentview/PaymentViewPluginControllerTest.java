package org.creditsms.plugins.paymentview;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.plugins.payment.service.PaymentServiceException;
import net.frontlinesms.serviceconfig.ConfigurableService;
import net.frontlinesms.serviceconfig.StructuredProperties;

import static org.mockito.Mockito.*;

public class PaymentViewPluginControllerTest extends BaseTestCase {
	/** {@link PaymentViewPluginController} instance under test */
	PaymentViewPluginController controller;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.controller = new PaymentViewPluginController();
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
}

class MockPaymentService implements PaymentService {
	private boolean started;

	//> Verification methods
	public boolean wasStarted() {
		return started;
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
	}
	public boolean isOutgoingPaymentEnabled() {
		return false;
	}
}
