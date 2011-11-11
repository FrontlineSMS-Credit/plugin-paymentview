package org.creditsms.plugins.paymentview.data.repository.hibernate;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.domain.PaymentServiceSettings;
import org.creditsms.plugins.paymentview.userhomepropeties.payment.balance.Balance;
import org.springframework.beans.factory.annotation.Autowired;
/**
 * 
 * @author Roy
 *
 */
public class PaymentServiceSettingsIntegrationTest extends HibernateTestCase{
	@Autowired                     
	HibernatePaymentServiceSettingsDao hibernatePaymentServiceSettingsDao;
	
	public void testSetup() {
		assertNotNull(hibernatePaymentServiceSettingsDao);
	}
	
	private void assertEmptyDatabases(){
		assertEquals(0, hibernatePaymentServiceSettingsDao.getServiceAccounts().size());
	}

	public void testSavingPaymentServiceSettings() throws DuplicateKeyException{
		assertEmptyDatabases();
		PaymentService service = new StubbedPaymentService();
		PersistableSettings psSettings = new PersistableSettings(service);
		psSettings.setPsPin("1111");
		hibernatePaymentServiceSettingsDao.savePaymentServiceSettings(psSettings);

		assertEquals(1, hibernatePaymentServiceSettingsDao.getServiceAccounts().size());
	}
	
	public void testUpdatingOutgoingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		PaymentService service = new StubbedPaymentService();
		PersistableSettings psSettings = new PaymentServiceSettings(service);
		psSettings.setPsPin("1111");
		hibernatePaymentServiceSettingsDao.savePaymentServiceSettings(psSettings);
		
		hibernatePaymentServiceSettingsDao.updatePaymentServiceSettings(psSettings);
		assertEquals(1, hibernatePaymentServiceSettingsDao.getPaymentServiceAccounts().size());
	}
	
	public void testDeletingOutgoingPayment() throws DuplicateKeyException{
		// given
		assertEmptyDatabases();
		PaymentService service = new StubbedPaymentService();
		PaymentServiceSettings psSettings = new PaymentServiceSettings(service);
		psSettings.setPsPin("1111");
		
		// when
		hibernatePaymentServiceSettingsDao.savePaymentServiceSettings(psSettings);
		// then
		assertEquals(1, hibernatePaymentServiceSettingsDao.getPaymentServiceAccounts().size());
		
		// when
		hibernatePaymentServiceSettingsDao.deletePaymentServiceSettings(psSettings);
		// then
		assertEquals(0, hibernatePaymentServiceSettingsDao.getPaymentServiceAccounts().size());
	}
}

class StubbedPaymentService implements PaymentService {
	public String getPin() {
		return null;
	}

	public void setPin(String pin) {
	}

	public void makePayment(Client client, OutgoingPayment outgoingPayment) throws PaymentServiceException {
	}

	public void checkBalance() throws PaymentServiceException {
	}

	public void configureModem() throws PaymentServiceException {
	}

	public void stopService() {
	}

	public PaymentServiceSettings getSettings() {
		return null;
	}

	public void initSettings(PaymentServiceSettings settings) {
	}

	public Balance getBalance() {
		return null;
	}

	public void startService() throws PaymentServiceException {
	}

	public boolean isOutgoingPaymentEnabled() {
		return false;
	}
}