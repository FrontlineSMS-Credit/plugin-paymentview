package org.creditsms.plugins.paymentview.data.repository.hibernate;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.payment.safaricom.MpesaPersonalService;

import org.creditsms.plugins.paymentview.data.domain.PaymentServiceSettings;
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
		assertEquals(0, hibernatePaymentServiceSettingsDao.getPaymentServiceAccounts().size());
	}

	public void testSavingPaymentServiceSettings() throws DuplicateKeyException{
		assertEmptyDatabases();
		MpesaPersonalService service = new MpesaPersonalService();
		PaymentServiceSettings psSettings = new PaymentServiceSettings(service);
		psSettings.setPsPin("1111");
		hibernatePaymentServiceSettingsDao.savePaymentServiceSettings(psSettings);

		assertEquals(1, hibernatePaymentServiceSettingsDao.getPaymentServiceAccounts().size());
	}
	
	public void testUpdatingOutgoingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		MpesaPersonalService service = new MpesaPersonalService();
		PaymentServiceSettings psSettings = new PaymentServiceSettings(service);
		psSettings.setPsPin("1111");
		hibernatePaymentServiceSettingsDao.savePaymentServiceSettings(psSettings);
		
		hibernatePaymentServiceSettingsDao.updatePaymentServiceSettings(psSettings);
		assertEquals(1, hibernatePaymentServiceSettingsDao.getPaymentServiceAccounts().size());
	}
	
	public void testDeletingOutgoingPayment() throws DuplicateKeyException{
		assertEmptyDatabases();
		MpesaPersonalService service = new MpesaPersonalService();
		PaymentServiceSettings psSettings = new PaymentServiceSettings(service);
		psSettings.setPsPin("1111");
		hibernatePaymentServiceSettingsDao.savePaymentServiceSettings(psSettings);
		hibernatePaymentServiceSettingsDao.deletePaymentServiceSettings(psSettings);
		assertEquals(0, hibernatePaymentServiceSettingsDao.getPaymentServiceAccounts().size());
	}
	

}
