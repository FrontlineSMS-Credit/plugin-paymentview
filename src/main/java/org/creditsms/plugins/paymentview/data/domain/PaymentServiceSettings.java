/**
 * 
 */
package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import net.frontlinesms.data.EntityField;
import net.frontlinesms.messaging.sms.internet.SmsInternetService;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.safaricom.AbstractPaymentService;

import org.hibernate.annotations.IndexColumn;

/**
 * Class encapsulating settings of a {@link PaymentService}.
 * @author Kim
 */
@Entity
@Table(name = PaymentServiceSettings.TABLE_NAME)
public class PaymentServiceSettings {
	public static final String TABLE_NAME = "PaymentServiceSettings";

	private static final String FIELD_ID = "id";
	private static final String FIELD_SERVICE_CLASS = "serviceClassName";
	private static final String FIELD_PS_PIN = "psPin";
	private static final String FIELD_PS_SMS_MODEM_SERIAL = "psSmsModemSerial";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@IndexColumn(name = FIELD_ID)
	@Column(name = FIELD_ID, nullable = false, unique = true)
	private long id;

	
	@Column(name = FIELD_SERVICE_CLASS)
	private String serviceClassName;

	@Column(name = FIELD_PS_PIN)
	private String psPin;

	@Column(name = FIELD_PS_SMS_MODEM_SERIAL)
	private String psSmsModemSerial;
	
	public enum Field implements EntityField<Client> {
		ID(FIELD_ID),
		SERVICE_CLASS(FIELD_SERVICE_CLASS),
		PS_PIN(FIELD_PS_PIN),
		PS_SMS_MODEM_SERIAL(FIELD_PS_SMS_MODEM_SERIAL);
		
		/** name of a field */
		private final String fieldName;
		/**
		 * Creates a new {@link Field}
		 * @param fieldName name of the field
		 */
		Field(String fieldName) { this.fieldName = fieldName; }
		/** @see EntityField#getFieldName() */
		public String getFieldName() { return this.fieldName; }
	}

	
	
//> CONSTRUCTORS
	/** Empty constructor for hibernate */
	PaymentServiceSettings() {}
	
	/**
	 * Create a new instance of service settings for the supplied service.
	 * @param service
	 */
	public PaymentServiceSettings(AbstractPaymentService service) {
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

	public void setPsPin(String psPin) {
		this.psPin = psPin;
	}

	public void setPsSmsModemSerial(String psSmsModemSerial) {
		this.psSmsModemSerial = psSmsModemSerial;
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