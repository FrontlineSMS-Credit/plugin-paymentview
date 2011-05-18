package org.creditsms.plugins.paymentview.authorizationcode;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AuthorizationCode {
	   private AuthCodeProperties properties = AuthCodeProperties.getInstance();
	   final static int ITERATION_NUMBER = 1;

	   public boolean authenticate(String password) throws NoSuchAlgorithmException, IOException{
		   final byte[] retrievedSalt = properties.getSalt();
		  
		   return Arrays.equals(getHash(ITERATION_NUMBER, password, retrievedSalt), properties.getAuthCode());
	  }
	   
	   public static byte[] getHash(int iterationNb, String authenticationCode, byte[] salt) {
		   MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	       digest.reset();
	       digest.update(salt);
	       byte[] input = digest.digest(authenticationCode.getBytes());
	       for (int i = 0; i < iterationNb; i++) {
	           digest.reset();
	           input = digest.digest(input);
	       }
	       return input;
	   }
}
