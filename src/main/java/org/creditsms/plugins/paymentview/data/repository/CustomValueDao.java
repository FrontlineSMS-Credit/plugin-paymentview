package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.CustomValue;

/**
 * @author Roy
 * */

public interface CustomValueDao {
	/**
	 * return all the OtherClientDetails in the system
	 * **/
	public List<CustomValue> getAllCustomValues();
	
	/** 
	 * @param clientId
	 * @return
	 * return all the OtherClientDetails belonging to a client
	 */
	public List<CustomValue> getCustomValuesByClientId(long clientId);

	/** 
	 * @param customfieldId and otherdetails value
	 * @return
	 * return OtherClientDetails with CustomField and otherdetails value
	 */
	public List<CustomValue> getCustomValuesByCustomFieldByValue(long customfieldId, String strValue);
	
	/**
	 * returning a specific OtherClientDetails
	 * */
	public CustomValue getCustomValueById(long id); 
	
	/**
	 * Deletes a OtherClientDetails from the system
	 * @param CustomValue
	 */
	public void deleteCustomValue(CustomValue customValue);

	/**
	 * Saves a OtherClientDetails to the system
	 * @param CustomValue
	 */
	public void saveCustomValue(CustomValue customValue) throws DuplicateKeyException;

	/**
	 * Updates a OtherClientDetails to the system
	 * @param CustomValue
	 */
	public void updateCustomValue(CustomValue customValue) throws DuplicateKeyException;
}
