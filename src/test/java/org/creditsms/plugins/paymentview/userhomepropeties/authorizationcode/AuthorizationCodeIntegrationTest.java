package org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode;

import net.frontlinesms.junit.BaseTestCase;

public class AuthorizationCodeIntegrationTest extends BaseTestCase {
   private AuthorizationProperties properties;

   @Override
	protected void setUp() throws Exception {
	   properties = AuthorizationProperties.getInstance();
	}

   public void testGetAuthCode() throws Exception {
	   final String authCode = "passwd";
	   final byte[] retrievedSalt = AuthorizationChecker.getSalt();
	   properties.setAuthCode(authCode);

	   assertEquals("Hashed authCode not returned as expected.",
			   AuthorizationChecker.getHash(AuthorizationChecker.ITERATION_NUMBER, authCode, retrievedSalt), properties.getHashedAuthCode());
   }
}