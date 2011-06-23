package org.creditsms.plugins.paymentview.authorizationcode;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class AuthorizationChecker {
	public final static int ITERATION_NUMBER = 1;

	public static boolean authenticate(String suppliedAuthCode) throws UnsupportedEncodingException {
		AuthorizationProperties authProp = AuthorizationProperties.getInstance();
		return Arrays.equals(getHash(ITERATION_NUMBER, suppliedAuthCode, getSalt()),
				authProp.getHashedAuthCode());
	}
   
	public static byte[] getSalt() {
		byte[] salt = AuthorizationProperties.getInstance().getSalt();
		if(salt == null) {
			return new byte[]{0,1,0,1,0,1,0,1,0,1,0,1};
		} 
		return salt;
	}

	public static byte[] getHash(int iterationNb, String authenticationCode,
			byte[] salt) throws UnsupportedEncodingException {
		MessageDigest digest = getDigest(salt);
		byte[] input = digest.digest(authenticationCode.getBytes("UTF-8"));
		for (int i = 0; i < iterationNb; i++) {
			digest.reset();
			input = digest.digest(input);
		}
		return input;
	}

	private static MessageDigest getDigest(byte[] salt) {
		MessageDigest digest;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
		digest.reset();
		digest.update(salt);
		return digest;
	}
}
