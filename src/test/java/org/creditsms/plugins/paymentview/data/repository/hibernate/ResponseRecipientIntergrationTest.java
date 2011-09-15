package org.creditsms.plugins.paymentview.data.repository.hibernate;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.junit.HibernateTestCase;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ResponseRecipient;
import org.creditsms.plugins.paymentview.data.domain.ThirdPartyResponse;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * 
 * @author Roy
 *
 */
public class ResponseRecipientIntergrationTest extends HibernateTestCase{
	@Autowired    
	HibernateClientDao hibernateClientDao;
	@Autowired    
	HibernateThirdPartyResponseDao hibernateThirdPartyResponseDao;
	@Autowired    
	HibernateResponseRecipientDao hibernateResponseRecipientDao;
	
	public void testSetup() {
		assertNotNull(hibernateClientDao);
		assertNotNull(hibernateThirdPartyResponseDao);
		assertNotNull(hibernateResponseRecipientDao);
	}
	
	public void testCreateResponseRecipient() throws DuplicateKeyException{
		ResponseRecipient responseRecipient = createResponseRecipient("0723987098", "Payment received today", "0721876425");
		hibernateResponseRecipientDao.saveResponseRecipient(responseRecipient);
		assertEquals(1, hibernateResponseRecipientDao.getAllResponseRecipients().size());
	}
	
	public void testUpdateResponseRecipient() throws DuplicateKeyException{
		ResponseRecipient responseRecipient = createResponseRecipient("0723987098", "Payment received today", "0721876425");
		hibernateResponseRecipientDao.saveResponseRecipient(responseRecipient);
		responseRecipient = hibernateResponseRecipientDao.getAllResponseRecipients().get(0);
		responseRecipient.getThirdPartyResponse().setMessage("Payment received today!");
		hibernateResponseRecipientDao.updateResponseRecipient(responseRecipient);
		assertEquals("Payment received today!", hibernateResponseRecipientDao.getAllResponseRecipients().get(0).getThirdPartyResponse().getMessage());
	}	
	
	public void testDeleteResponseRecipient() throws DuplicateKeyException{
		ResponseRecipient responseRecipient = createResponseRecipient("0723987098", "Payment received today", "0721876425");
		hibernateResponseRecipientDao.saveResponseRecipient(responseRecipient);
		responseRecipient = hibernateResponseRecipientDao.getAllResponseRecipients().get(0);
		hibernateResponseRecipientDao.deleteResponseRecipient(responseRecipient);
		assertEquals(0, hibernateResponseRecipientDao.getAllResponseRecipients().size());
	}	
	
	public void testGetResponseRecipientIdByThirdPartyResponse() throws DuplicateKeyException{
		ResponseRecipient responseRecipient = createResponseRecipient("0723987098", "Payment received today", "0721876425");
		hibernateResponseRecipientDao.saveResponseRecipient(responseRecipient);
		long tpr = hibernateThirdPartyResponseDao.getAllThirdPartyResponses().get(0).getId();
		responseRecipient = hibernateResponseRecipientDao.getResponseRecipientByThirdPartyResponseId(tpr).get(0);
		assertEquals("0721876425", responseRecipient.getClient().getPhoneNumber());
	}	
	
	private Client createClient(String phoneNumber){
		Client client = new Client();
		client.setPhoneNumber(phoneNumber);
		hibernateClientDao.saveClient(client);
		client = hibernateClientDao.getClientByPhoneNumber(phoneNumber);
		return client;
	} 
	
	private ThirdPartyResponse createThirdPartyResponse(String phoneNumber, String responseMessage) throws DuplicateKeyException{
		ThirdPartyResponse tpr = new ThirdPartyResponse();
		tpr.setClient(createClient(phoneNumber));
		tpr.setMessage(responseMessage);
		hibernateThirdPartyResponseDao.saveThirdPartyResponse(tpr);
		tpr = hibernateThirdPartyResponseDao.getAllThirdPartyResponses().get(0);
		return tpr;
	}
	
	private ResponseRecipient createResponseRecipient(String phoneNumber, String responseMessage, String recipientPhoneNumber) throws DuplicateKeyException{
		ResponseRecipient responseRecipient = new ResponseRecipient();
		responseRecipient.setThirdPartyResponse(createThirdPartyResponse(phoneNumber, responseMessage));
		responseRecipient.setClient(createClient(recipientPhoneNumber));
		return responseRecipient;
	}
}
