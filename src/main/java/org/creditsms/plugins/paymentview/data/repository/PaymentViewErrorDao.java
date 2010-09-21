package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.PaymentViewError;
import org.creditsms.plugins.paymentview.data.domain.PaymentViewError.ErrorType;

/**
 * Data access interface {@link PaymentViewError}
 * @author Emmanuel Kala
 *
 */
public interface PaymentViewErrorDao {
    /**
     * Gets all the payment view errors from the database
     * @return
     */
    public List<PaymentViewError> getAllErrors();
    
    /**
     * Gets all the payment view errors from a particular index up to a specified maximum in the result set
     * @param startIndex Index of the first record to fetch
     * @param limit Number of records to fetch
     * @return
     */
    public List<PaymentViewError> getAllErrors(int startIndex, int limit);
    
    /**
     * Gets all the errors of a specific type from the database
     * @param type Type of error to be be fetched
     * @return
     */
    public List<PaymentViewError> getAllErrors(ErrorType type);
    
    /**
     * Gets all the errors of a specified type from a particular index (record # in the db) up to a 
     * specified maximum
     * 
     * @param type Type of error
     * @param startIndex Index of the first record
     * @param limit Number of records to fetch
     * @return
     */
    public List<PaymentViewError> getAllErrors(ErrorType type, int startIndex, int limit);
    
    /**
     * Gets the total number of errors in the system
     * @return
     */
    public int getErrorCount();
    
    /**
     * Gets the total number of errors of the specified type
     * @param type
     * @return
     */
    public int getErrorCount(ErrorType type);
    
    /**
     * Saves an error to the database
     * @param error
     * @throws DuplicateKeyException
     */
    public void save(PaymentViewError error);

    /**
     * Updates an error in the database
     * @param error
     */
    public void update(PaymentViewError error);
    
    /**
     * Deletes an error from the database
     * @param error
     */
    public void delete(PaymentViewError error);
    
    /**
     * Deletes all the errors from the database
     */
    public void deleteAll();
}
