package org.creditsms.plugins.paymentview.authorizationcode;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import net.frontlinesms.resources.UserHomeFilePropertySet;

import org.smslib.util.HexUtils;

public class AuthCodeProperties extends UserHomeFilePropertySet {
	private static final String KEY_AUTH_CODE = "auth.code";
	private static final AuthCodeProperties INSTANCE = new AuthCodeProperties(); 
	
	private AuthCodeProperties() {
		super("payment-view");
	}
	
	public void setAuthCode(String authCode) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		byte[] hash = hash(authCode);
		String hashAsString = HexUtils.encode(hash);
		setProperty(KEY_AUTH_CODE, hashAsString);
	}
	
	private byte[] hash(String authCode) {
		return AuthorizationCode.getHash(AuthorizationCode.ITERATION_NUMBER, authCode, getSalt());
	}
	
	public byte[] getAuthCode() {
		byte jt[] = getPropertyAsByteArray(KEY_AUTH_CODE, hash("pass"));
		return jt;
	}

	public byte[] getSalt() {
		return getPropertyAsByteArray("auth.salt", new byte[]{0,1,0,1,0,1,0,1,0,1,0,1});
	}
	
	private byte[] getPropertyAsByteArray(String key, byte[] defaultValue) {
		String value = super.getProperty(key);
		if(value != null)
			return HexUtils.decode(value);
		else return defaultValue;
	}

	public static AuthCodeProperties getInstance() {
		return INSTANCE;
	}
}
