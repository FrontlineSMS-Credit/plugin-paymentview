package org.creditsms.plugins.paymentview.data.repository.hibernate;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ThirdPartyResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Roy
 *
 */
public class ThirdPartyResponseIntergrationTest extends HibernateTestCase{
	@Autowired    
	HibernateClientDao hibernateClientDao;
	@Autowired    
	HibernateThirdPartyResponseDao hibernateThirdPartyResponseDao;
	
	public void testSetup() {
		assertNotNull(hibernateClientDao);
		assertNotNull(hibernateThirdPartyResponseDao);
	}

	public void testCreateThirdPartyResponse() throws DuplicateKeyException{
		ThirdPartyResponse tpr = createThirdPartyResponse("0721987534", "Payment successful");
		hibernateThirdPartyResponseDao.saveThirdPartyResponse(tpr);
		assertEquals(1, hibernateThirdPartyResponseDao.getAllThirdPartyResponses().size());
	}
	
	public void testUpdateThirdPartyResponse() throws DuplicateKeyException{
		ThirdPartyResponse tpr = createThirdPartyResponse("0721987534", "Payment successful");
		hibernateThirdPartyResponseDao.saveThirdPartyResponse(tpr);
		assertEquals(1, hibernateThirdPartyResponseDao.getAllThirdPartyResponses().size());
		tpr.setMessage("Payment successful..");
		hibernateThirdPartyResponseDao.updateThirdPartyResponse(tpr);
		assertEquals("Payment successful..", hibernateThirdPartyResponseDao.getAllThirdPartyResponses().get(0).getMessage());
	}
	
	public void testGetThirdPartyResponseByClientId() throws DuplicateKeyException{
		long clientId = 0;
		
		ThirdPartyResponse tpr = createThirdPartyResponse("0721987534", "Payment successful");
		hibernateThirdPartyResponseDao.saveThirdPartyResponse(tpr);

		ThirdPartyResponse tpr1 = createThirdPartyResponse("0721987999", "Payment successful...");
		hibernateThirdPartyResponseDao.saveThirdPartyResponse(tpr1);
		clientId = tpr1.getClient().getId();
		
		ThirdPartyResponse tpr3 = hibernateThirdPartyResponseDao.getThirdPartyResponseByClientId(clientId);
		assertEquals("Payment successful...", tpr3.getMessage());
	}
	
	public void testDeleteThirdPartyResponse() throws DuplicateKeyException{
		ThirdPartyResponse tpr = createThirdPartyResponse("0721987534", "Payment successful");
		hibernateThirdPartyResponseDao.deleteThirdPartyResponse(tpr);
		assertEquals(0, hibernateThirdPartyResponseDao.getAllThirdPartyResponses().size());
	}
	
	private Client createClient(String phoneNumber){
		Client client = new Client();
		client.setPhoneNumber(phoneNumber);
		hibernateClientDao.saveClient(client);
		client = hibernateClientDao.getClientByPhoneNumber(phoneNumber);
		return client;
	} 
	
	private ThirdPartyResponse createThirdPartyResponse(String phoneNumber, String message){
		ThirdPartyResponse tpr = new ThirdPartyResponse();
		tpr.setClient(createClient(phoneNumber));
		tpr.setMessage(message);
		return tpr;
	}
}
