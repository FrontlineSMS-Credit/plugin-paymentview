package org.creditsms.plugins.paymentview.authorizationcode;
import net.frontlinesms.junit.BaseTestCase;


public class AuthorizationCodeIntegrationTest extends BaseTestCase {
   private AuthCodeProperties properties;

   @Override
	protected void setUp() throws Exception {
	   properties = AuthCodeProperties.getInstance();
	}

   public void testGetAuthCode() throws Exception {
	   final String authCode = "passwd";
	   AuthorizationCode authCodeObj = new AuthorizationCode();
	   properties.setAuthCode(authCode);
	   final byte[] retrievedSalt = properties.getSalt();
	   
	   assertEquals("Hashed authCode not returned as expected.",
			   authCodeObj.getHash(authCodeObj.ITERATION_NUMBER, authCode, retrievedSalt), properties.getAuthCode());
   }
}