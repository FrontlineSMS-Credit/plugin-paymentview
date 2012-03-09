package org.creditsms.plugins.paymentview.utils;

import static net.frontlinesms.ui.i18n.InternationalisationUtils.isValidInternationalPhoneNumber;
import static net.frontlinesms.ui.i18n.InternationalisationUtils.isValidLocalPhoneNumber;
import static net.frontlinesms.ui.i18n.InternationalisationUtils.getInternationalPhoneNumber;

/**
 * @author Roy
 */
public class PhoneNumberFormatter {
	private String number;
	
	public String getPhoneNumber() {
		return number;
	}

	public boolean format(String tempPhoneNumber) {
		if(isValidInternationalPhoneNumber(tempPhoneNumber)) {
			number = tempPhoneNumber;
			return true;
		} else if(isValidInternationalPhoneNumber("+" + tempPhoneNumber)) {
			number = "+" + tempPhoneNumber;
			return true;
		} else if(isValidLocalPhoneNumber(tempPhoneNumber)) {
			number = getInternationalPhoneNumber(tempPhoneNumber);
			return true;
		}
		return false;
	}
}
