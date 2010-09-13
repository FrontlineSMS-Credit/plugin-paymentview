package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.QuickDialCode;
import org.creditsms.plugins.paymentview.data.domain.NetworkOperator;

public interface QuickDialCodeDao {
    
    /** Gets all the quick dial codes in the database */
    public List<QuickDialCode> getAllQuickDialCodes();
    
    /**
     * Returns all quick dial codes from a particular start index with a maximum number of quick dial
     * codes returned in the set
     * 
     * @param startIndex index of the first quick dial code to fetch
     * @param limit No. of quick dial codes to fetch
     * @return
     */
    public List<QuickDialCode> getQuickDialCodes(int startIndex, int limit);
    
    /** Gets the number of quick dial codes in the datasource */
    public int getQuickDialCodeCount();
    
    
    /**
     * Gets the list of quick dial codes for the specified network operator
     * @param operator
     * @return
     */
    public List<QuickDialCode> getQuickDialCodesByNetworkOperator(NetworkOperator operator);
    
    /**
     * Saves the quick dial code in the data store
     * @param quickDialCode
     * @throws DuplicateKeyException
     */
    public void saveQuickDialCode(QuickDialCode quickDialCode) throws DuplicateKeyException;
    
    /**
     * Updates the details of the quick dial code in the data store
     * @param quickDialCode
     * @throws DuplicateKeyException
     */
    public void updateQuickDialCode(QuickDialCode quickDialCode) throws DuplicateKeyException;
    
    /**
     * Deletes/removes the quick dial code from the data store
     * @param quickDialCode
     */
    public void deleteQuickDialCode(QuickDialCode quickDialCode);
}
