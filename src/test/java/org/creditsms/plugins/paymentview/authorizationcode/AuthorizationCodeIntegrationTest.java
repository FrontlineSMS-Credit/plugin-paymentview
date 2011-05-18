package org.creditsms.plugins.paymentview.authorizationcode;
import net.frontlinesms.junit.BaseTestCase;


public class AuthorizationCodeIntegrationTest extends BaseTestCase {
   private AuthorizationProperties properties;

   @Override
	protected void setUp() throws Exception {
	   properties = AuthorizationProperties.getInstance();
	}

   public void testGetAuthCode() throws Exception {
	   final String authCode = "passwd";
	   AuthorizationChecker authCodeObj = new AuthorizationChecker();
	   final byte[] retrievedSalt = authCodeObj.getSalt();
	   properties.setAuthCode(authCodeObj.getHash(authCodeObj.ITERATION_NUMBER, authCode, retrievedSalt));

	   assertEquals("Hashed authCode not returned as expected.",
			   authCodeObj.getHash(authCodeObj.ITERATION_NUMBER, authCode, retrievedSalt), properties.getHashedAuthCode());
   }
}