/**
 * 
 */
package org.creditsms.plugins.paymentview.data.repository.hibernate;

import net.frontlinesms.data.repository.hibernate.BaseHibernateConfigurableServiceSettingsDao;
import net.frontlinesms.payment.PaymentService;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class HibernatePaymentServiceSettingsDao
		extends BaseHibernateConfigurableServiceSettingsDao
		implements PaymentServiceSettingsDao {
	@Override
	public Class<PaymentService> getServiceClass() {
		return PaymentService.class;
	}
}
