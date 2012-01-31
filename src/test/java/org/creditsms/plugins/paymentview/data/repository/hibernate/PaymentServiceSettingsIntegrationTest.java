package org.creditsms.plugins.paymentview.data.repository.hibernate;

import org.springframework.beans.factory.annotation.Autowired;

import net.frontlinesms.data.domain.PersistableSettingValue;
import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.junit.HibernateTestCase;
import net.frontlinesms.plugins.payment.service.PaymentService;

public class PaymentServiceSettingsIntegrationTest extends HibernateTestCase {
	@Autowired
	private HibernatePaymentServiceSettingsDao dao;
	
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
		return dao.getHibernateTemplate().loadAll(PersistableSettingValue.class).size();
	}
}

abstract class TestPaymentService implements PaymentService {
}
