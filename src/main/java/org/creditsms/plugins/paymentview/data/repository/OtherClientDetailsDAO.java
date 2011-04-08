package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;
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
	 * return all the OtherClientDetails belonging to a client
	 * **/
	public List<OtherClientDetails> getOtherDetailsByClientId(long clientId);
	
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
	public void saveUpdateOtherClientDetails(OtherClientDetails otherClientDetails);
}
