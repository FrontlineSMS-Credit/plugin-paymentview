package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;

import org.creditsms.plugins.paymentview.data.domain.OtherClientDetails;

/**
 * @author Roy
 * */

public interface OtherClientDetailsDao {
	/**
	 * return all the OtherClientDetails in the system
	 * **/
	public List<OtherClientDetails> getAllOtherDetails();
	
	/** 
	 * @param clientId
	 * @return
	 * return all the OtherClientDetails belonging to a client
	 */
	public List<OtherClientDetails> getOtherDetailsByClientId(long clientId);

	/** 
	 * @param customfieldId and otherdetails value
	 * @return
	 * return OtherClientDetails with CustomField and otherdetails value
	 */
	public List<OtherClientDetails> getOtherDetailsByCustomFieldByValue(long customfieldId, String strValue);
	
	/**
	 * returning a specific OtherClientDetails
	 * */
	public OtherClientDetails getOtherClientDetailsById(long id); 
	
	/**
	 * Deletes a OtherClientDetails from the system
	 * @param OtherClientDetails
	 */
	public void deleteOtherClientDetails(OtherClientDetails otherClientDetails);

	/**
	 * Saves a OtherClientDetails to the system
	 * @param OtherClientDetails
	 */
	public void saveOtherClientDetails(OtherClientDetails otherClientDetails) throws DuplicateKeyException;

	/**
	 * Updates a OtherClientDetails to the system
	 * @param OtherClientDetails
	 */
	public void updateOtherClientDetails(OtherClientDetails otherClientDetails) throws DuplicateKeyException;
}
