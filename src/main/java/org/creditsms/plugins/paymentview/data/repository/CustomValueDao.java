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
	public List<CustomValue> getAllOtherDetails();
	
	/** 
	 * @param clientId
	 * @return
	 * return all the OtherClientDetails belonging to a client
	 */
	public List<CustomValue> getOtherDetailsByClientId(long clientId);

	/** 
	 * @param customfieldId and otherdetails value
	 * @return
	 * return OtherClientDetails with CustomField and otherdetails value
	 */
	public List<CustomValue> getOtherDetailsByCustomFieldByValue(long customfieldId, String strValue);
	
	/**
	 * returning a specific OtherClientDetails
	 * */
	public CustomValue getOtherClientDetailsById(long id); 
	
	/**
	 * Deletes a OtherClientDetails from the system
	 * @param CustomValue
	 */
	public void deleteOtherClientDetails(CustomValue otherClientDetails);

	/**
	 * Saves a OtherClientDetails to the system
	 * @param CustomValue
	 */
	public void saveOtherClientDetails(CustomValue otherClientDetails) throws DuplicateKeyException;

	/**
	 * Updates a OtherClientDetails to the system
	 * @param CustomValue
	 */
	public void updateOtherClientDetails(CustomValue otherClientDetails) throws DuplicateKeyException;
}
