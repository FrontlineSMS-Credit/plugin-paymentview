/**
 * 
 */
package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.frontlinesms.messaging.sms.internet.SmsInternetService;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;

/**
 * Class encapsulating settings of a {@link PaymentService}.
 * @author Kim
 */
@Entity
public class PaymentServiceSettings {
//> INSTANCE PROPERTIES
	/** Unique id for this entity.  This is for hibernate usage. */
	@SuppressWarnings("unused")
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY) @Column(unique=true,nullable=false,updatable=false)
	private long id;
	/** The name of the class of the {@link PaymentService} these settings apply to. */
	private String serviceClassName;
	/** The properties for a {@link PaymentService} */
	private String psPin;
	private String psSmsModemSerial;
	private BigDecimal psBalance;
	private String psBalanceConfirmationCode;
	private String psBalanceUpdateMethod;
	private Date psBalanceUpdateDatetime;
//	protected Logger pvLog = Logger.getLogger(this.getClass());
	
//> CONSTRUCTORS
	/** Empty constructor for hibernate */
	PaymentServiceSettings() {}
	
	/**
	 * Create a new instance of service settings for the supplied service.
	 * @param service
	 */
	public PaymentServiceSettings(MpesaPaymentService service) {
		this.serviceClassName = service.getClass().getCanonicalName();
	}
	
//> ACCESSOR METHODS
	/** @return the class name of {@link SmsInternetService} implementation that these settings apply to */
	public String getServiceClassName() {
		return this.serviceClassName;
	}
	
	public long getId() {
		return id;
	}
	
	public String getPsPin() {
		return psPin;
	}
	
	public String getPsSmsModemSerial() {
		return psSmsModemSerial;
	}

	public BigDecimal getPsBalance() {
		return psBalance;
	}

	public String getPsBalanceConfirmationCode() {
		return psBalanceConfirmationCode;
	}

	public void setPsPin(String psPin) {
		this.psPin = psPin;
	}

	public String getPsBalanceUpdateMethod() {
		return psBalanceUpdateMethod;
	}

	public Date getPsBalanceUpdateDatetime() {
		return psBalanceUpdateDatetime;
	}
	
	public void setPsSmsModemSerial(String psSmsModemSerial) {
		this.psSmsModemSerial = psSmsModemSerial;
	}

	public void setPsBalance(BigDecimal psBalance) {
		this.psBalance = psBalance;
	}

	public void setPsBalanceConfirmationCode(String psBalanceConfirmationCode) {
		this.psBalanceConfirmationCode = psBalanceConfirmationCode;
	}

	public void setPsBalanceUpdateMethod(String psBalanceUpdateMethod) {
		this.psBalanceUpdateMethod = psBalanceUpdateMethod;
	}

	public void setPsBalanceUpdateDatetime(Date psBalanceUpdateDatetime) {
		this.psBalanceUpdateDatetime = psBalanceUpdateDatetime;
	}

	public void setServiceClassName(String serviceClassName) {
		this.serviceClassName = serviceClassName;
	}

	public PaymentService initPaymentService() {
		if(this.serviceClassName != null){
			try {
				return (PaymentService) Class.forName(this.serviceClassName).newInstance();
			} catch (Exception ex) {
				ex.printStackTrace();
//				pvLog.warn("Unable to load payment service specified in properties file.", ex);
			}
		}
		return null;
	}

	
//> GENERATED METHODS

	/** @see java.lang.Object#hashCode() */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((serviceClassName == null) ? 0 : serviceClassName.hashCode());
		return result;
	}

	/** @see java.lang.Object#equals(java.lang.Object) */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PaymentServiceSettings other = (PaymentServiceSettings) obj;
		if (serviceClassName == null) {
			if (other.serviceClassName != null)
				return false;
		} else if (!serviceClassName.equals(other.serviceClassName)){
			return false;
		} else if (!psSmsModemSerial.equals(other.psSmsModemSerial))
			return false;
		return true;
	}
}