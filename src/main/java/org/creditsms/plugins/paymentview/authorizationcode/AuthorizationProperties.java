package org.creditsms.plugins.paymentview.authorizationcode;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import net.frontlinesms.resources.UserHomeFilePropertySet;

import org.smslib.util.HexUtils;

final class AuthorizationProperties extends UserHomeFilePropertySet {
	private static final String KEY_AUTH_CODE = "auth.code";
	private static final AuthorizationProperties INSTANCE = new AuthorizationProperties(); 
	
	AuthorizationProperties() {
		super("payment-view");
	}
	
	void setAuthCode(byte[] hashedAuthCode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		setProperty(KEY_AUTH_CODE, HexUtils.encode(hashedAuthCode));
	}
	
	byte[] getHashedAuthCode() {
		byte jt[] = getPropertyAsByteArray(KEY_AUTH_CODE);
		return jt;
	}

	byte[] getSalt() {
		return getPropertyAsByteArray("auth.salt");
	}
	
	private byte[] getPropertyAsByteArray(String key) {
		String value = super.getProperty(key);
		if(value != null){
			return HexUtils.decode(value);
		}
		return null;
	}

	public static AuthorizationProperties getInstance() {
		return INSTANCE;
	}
}
