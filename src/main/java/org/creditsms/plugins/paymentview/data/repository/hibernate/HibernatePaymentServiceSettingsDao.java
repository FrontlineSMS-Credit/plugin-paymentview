/**
 * 
 */
package org.creditsms.plugins.paymentview.data.repository.hibernate;

import java.util.Collection;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.repository.hibernate.BaseHibernateDao;

import org.creditsms.plugins.paymentview.data.domain.PaymentServiceSettings;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

/**
 * @author Alex Anderson <alex@frontlinesms.com>
 */
public class HibernatePaymentServiceSettingsDao extends BaseHibernateDao<PaymentServiceSettings> implements PaymentServiceSettingsDao {
	/** Create instance of this class */
	public HibernatePaymentServiceSettingsDao() {
		super(PaymentServiceSettings.class);
	}

	/** @see PaymentServiceSettingsDao#deletePaymentServiceSettings(PaymentServiceSettings) */
	public void deletePaymentServiceSettings(PaymentServiceSettings settings) {
		super.delete(settings);
	}

	/** @see PaymentServiceSettingsDao#getPaymentServiceAccounts() */
	public Collection<PaymentServiceSettings> getPaymentServiceAccounts() {
		return super.getAll();
	}

	/** @see PaymentServiceSettingsDao#savePaymentServiceSettings(PaymentServiceSettings) */
	public void savePaymentServiceSettings(PaymentServiceSettings settings) throws DuplicateKeyException {
		super.save(settings);
	}

	/** @see PaymentServiceSettingsDao#updatePaymentServiceSettings(PaymentServiceSettings) */
	public void updatePaymentServiceSettings(PaymentServiceSettings settings) {
		super.updateWithoutDuplicateHandling(settings);
	}
}
