package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.domain.PersistableSettingValue;
import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.payment.service.PaymentService;

public class PaymentServiceSettingsIntegrationTest extends HibernateTestCase {
	@Autowired
	private HibernatePaymentServiceSettingsDao dao;
	@Autowired
	private HibernateIncomingPaymentDao incomingPaymentDao;
	
	public void testSettingsValuesShouldNotBeDuplicatedOnUpdate() throws Exception {
		// given
		final String KEY = "key";
		PersistableSettings s = new PersistableSettings(PaymentService.class, TestPaymentService.class);
		s.set(KEY, 1);
		dao.saveServiceSettings(s);
		assertEquals(1, countPersistedValues());
		
		// when
		s.set(KEY, 2);
		dao.updateServiceSettings(s);
		s.set(KEY, 3);
		dao.updateServiceSettings(s);
		
		// then
		assertEquals(1, countPersistedValues());
	}

	private int countPersistedValues() {
		return countPersistedValues(PersistableSettingValue.class);
	}
	
	private int countPersistedValues(Class<?> entityClass) {
		return dao.getHibernateTemplate().loadAll(entityClass).size();
	}
	
	public void testDeletingServiceWithAssociatedIncomingPaymentsWillComplete() throws Exception {
		//given
		PersistableSettings s = new PersistableSettings(PaymentService.class, TestPaymentService.class);
		s.set("key", 1);
		assertEquals(0, countPersistedValues());
		dao.saveServiceSettings(s);
		assertEquals(1, countPersistedValues());
		assertEquals(0, countPersistedValues(IncomingPayment.class));
		incomingPaymentDao.saveIncomingPayment(createIncomingPayment(s));
		assertEquals(1, countPersistedValues(IncomingPayment.class));
		
		// when
		dao.deleteServiceSettings(s);
		
		// then
		assertEquals(0, countPersistedValues());
		assertEquals(1, countPersistedValues(IncomingPayment.class));
		assertNull(incomingPaymentDao.getActiveIncomingPayments().get(0).getServiceSettings());
	}

	private IncomingPayment createIncomingPayment(PersistableSettings s) {
		IncomingPayment ip = new IncomingPayment("bob", "+234567", new BigDecimal("2.50"), 0, null, null);
		ip.setServiceSettings(s);
		ip.setActive(true);
		return ip;
	}
}

abstract class TestPaymentService implements PaymentService {
}
