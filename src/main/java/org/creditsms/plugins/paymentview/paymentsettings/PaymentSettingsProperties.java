package org.creditsms.plugins.paymentview.paymentsettings;
import net.frontlinesms.resources.UserHomeFilePropertySet;

public class PaymentSettingsProperties extends UserHomeFilePropertySet {
	private static final String SMS_MODEM = "sms.modem";
	private static final String PAYMENT_SERVICE = "payment.service";
	private static final String PIN = "ps.pin";
	
	private static final String TEMP_SMS_MODEM = "tmp.sms.modem";
	private static final String TEMP_PAYMENT_SERVICE = "tmp.payment.service";

	private static final PaymentSettingsProperties INSTANCE = new PaymentSettingsProperties(); 
	
	private PaymentSettingsProperties() {
		super("payment-view");
	}
	
	public void setSmsModem(String val)  {
		setProperty(SMS_MODEM, val);
	}
	
	public String getSmsModem() {
		String value = super.getProperty(SMS_MODEM);
		if(value != null){
			return value;
		}
		return null;
	}
	
	public void setPaymentService(String val)  {
		setProperty(PAYMENT_SERVICE, val);
	}
	
	public String getPaymentService() {
		String value = super.getProperty(PAYMENT_SERVICE);
		if(value != null){
			return value;
		}
		return null;
	}
	
	public void setPin(String val)  {
		setProperty(PIN, val);
	}
	
	public String getPin() {
		String value = super.getProperty(PIN);
		if(value != null){
			return value;
		}
		return null;
	}
	
	public void setTempSmsModem(String val)  {
		setProperty(TEMP_SMS_MODEM, val);
	}
	
	public String getTempSmsModem() {
		String value = super.getProperty(TEMP_SMS_MODEM);
		if(value != null){
			return value;
		}
		return null;
	}
	
	public void setTempPaymentService(String val)  {
		setProperty(TEMP_PAYMENT_SERVICE, val);
	}
	
	public String getTempPaymentService() {
		String value = super.getProperty(TEMP_PAYMENT_SERVICE);
		if(value != null){
			return value;
		}
		return null;
	}
	
	public static PaymentSettingsProperties getInstance() {
		return INSTANCE;
	}
}
