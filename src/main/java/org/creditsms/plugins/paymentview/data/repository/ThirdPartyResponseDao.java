package org.creditsms.plugins.paymentview.data.repository;

import net.frontlinesms.data.DuplicateKeyException;
import org.creditsms.plugins.paymentview.data.domain.ThirdPartyResponse;


/**
 * @author Roy
 **/

public interface ThirdPartyResponseDao {
	
	/**
	 * Retrieves the ThirdPartyResponse with
	 * 
	 * @param clientId
	 **/
	public ThirdPartyResponse getThirdPartyResponseByClientId(long clientId);

	/**
	 * Deletes a ThirdPartyResponse from the system
	 * 
	 * @param thirdPatyResponse
	 */
	public void deleteThirdPartyResponse(ThirdPartyResponse thirdPatyResponse);
	
	/**
	 * updates a ThirdPartyResponse to the system
	 * 
	 * @param thirdPatyResponse
	 * @throws DuplicateKeyException 
	 */
	public void updateThirdPartyResponse(ThirdPartyResponse thirdPatyResponse) throws DuplicateKeyException;
	
	/**
	 * Saves a ThirdPartyResponse to the system
	 * 
	 * @param thirdPatyResponse
	 */
	public void saveThirdPartyResponse(ThirdPartyResponse thirdPatyResponse) throws DuplicateKeyException;
}
