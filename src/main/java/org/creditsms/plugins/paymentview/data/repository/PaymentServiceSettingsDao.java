/**
 * 
 */
package org.creditsms.plugins.paymentview.data.repository;

import java.util.Collection;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.PaymentServiceSettings;

/**
 * Data Access Object interface for {@link PaymentServiceSettings}.
 * @author Kim
 */
public interface PaymentServiceSettingsDao {
	/**
	 * Saves {@link PaymentServiceSettings} to the data source 
	 * @param settings settings to save
	 * @throws DuplicateKeyException
	 */
	public void savePaymentServiceSettings(PaymentServiceSettings settings) throws DuplicateKeyException;
	
	/**
	 * Updates {@link PaymentServiceSettings} to the data source 
	 * @param settings settings to update
	 */
	public void updatePaymentServiceSettings(PaymentServiceSettings settings);

	/** @return all {@link PaymentServiceSettings} */
	public Collection<PaymentServiceSettings> getPaymentServiceAccounts();
	
	/**
	 * Deletes {@link PaymentServiceSettings} from the data source
	 * @param settings settings to delete
	 */
	public void deletePaymentServiceSettings(PaymentServiceSettings settings);
}
