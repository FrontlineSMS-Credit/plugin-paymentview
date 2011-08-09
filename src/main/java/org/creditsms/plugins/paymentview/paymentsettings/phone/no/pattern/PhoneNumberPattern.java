package org.creditsms.plugins.paymentview.paymentsettings.phone.no.pattern;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 * @author Roy
 *
 */

public class PhoneNumberPattern {
	String newPhoneNumberPattern;
	private static String PHONE_PATTERN = "\\+2547[\\d]{8}";
	
	public String getNewPhoneNumberPattern() {
		return newPhoneNumberPattern;
	}

	public void setNewPhoneNumberPattern(String newPhoneNumberPattern) {
		this.newPhoneNumberPattern = newPhoneNumberPattern;
	}

	public boolean formatPhoneNumber(String tempPhoneNumber){
		Matcher phoneMatcher = null;
		if(tempPhoneNumber.contains("2547")){
			if(tempPhoneNumber.contains("+2547")){
				phoneMatcher = Pattern.compile(PHONE_PATTERN).matcher(tempPhoneNumber);
				if(phoneMatcher.matches()){
					setNewPhoneNumberPattern(tempPhoneNumber);
					return true;
				}
			} else {
				tempPhoneNumber = "+"+tempPhoneNumber;
				phoneMatcher = Pattern.compile(PHONE_PATTERN).matcher(tempPhoneNumber);
				if(phoneMatcher.matches()){
					setNewPhoneNumberPattern(tempPhoneNumber);
					return true;
				}
			}
		} else {
			if(tempPhoneNumber.length()==10){
				String newPhoneNumber = tempPhoneNumber.substring(1, tempPhoneNumber.length());
				tempPhoneNumber = "+254"+newPhoneNumber;
				phoneMatcher = Pattern.compile(PHONE_PATTERN).matcher(tempPhoneNumber);
				if(phoneMatcher.matches()){
					setNewPhoneNumberPattern(tempPhoneNumber);
					return true;
				}
			} else {
				if(tempPhoneNumber.length()==9){
					tempPhoneNumber = "+254"+tempPhoneNumber;
					phoneMatcher = Pattern.compile(PHONE_PATTERN).matcher(tempPhoneNumber);
					if(phoneMatcher.matches()){
						setNewPhoneNumberPattern(tempPhoneNumber);
						return true;
					}
				}
			}
		}
		return false;
	}
}
