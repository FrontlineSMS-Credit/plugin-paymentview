package org.creditsms.plugins.paymentview.data.repository;

import java.util.List;
import org.creditsms.plugins.paymentview.data.domain.OtherClientDetails;

/**
 * @author Roy
 * */

public interface OtherClientDetailsDAO {
	/**
	 * return all the OtherClientDetails belonging to a client
	 * **/
	public List<OtherClientDetails> getOtherDetailsByClientId();
	
	/**
	 * returning a specific OtherClientDetails
	 * */
	public OtherClientDetails getOtherClientDetails(long otherClientDetailsId); 
	
	/**
	 * Deletes a OtherClientDetails from the system
	 * @param OtherClientDetails
	 */
	public void deleteClient(OtherClientDetails otherClientDetails);

	/**
	 * Saves a OtherClientDetails to the system
	 * @param OtherClientDetails
	 */
	public void saveUpdateClient(OtherClientDetails otherClientDetails);
}
