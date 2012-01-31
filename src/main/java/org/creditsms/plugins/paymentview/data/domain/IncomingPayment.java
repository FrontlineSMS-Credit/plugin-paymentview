package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import net.frontlinesms.data.EntityField;
import net.frontlinesms.data.domain.PersistableSettings;

/**
 * @author Roy
 * @author ian
 */
@Entity
public class IncomingPayment {
	private static final String FIELD_CONFIRMATION_CODE = "confirmationCode";
	private static final String FIELD_TIME_PAID = "timePaid";
	private static final String FIELD_ACCOUNT = "account";
	private static final String FIELD_TARGET ="target";
	private static final String FIELD_SERVICE_SETTINGS = "serviceSettings";
	@Id @Column(unique=true)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private BigDecimal amountPaid;
	@Column(name=FIELD_CONFIRMATION_CODE)
	private String confirmationCode;
	private String paymentBy;
	private String phoneNumber;
	private long timePaid;
	private boolean active = true;
	private String paymentId = "";
	private String notes = "";
	private boolean isChild;
	
	@ManyToOne
	private Account account;
	
	@ManyToOne
	@JoinColumn(name=FIELD_TARGET, nullable=true) //nullable if payment for generic account
	private Target target;
	
	@ManyToOne
	@JoinColumn(name=FIELD_SERVICE_SETTINGS)
	private PersistableSettings serviceSettings;
	
	public enum Field implements EntityField<IncomingPayment> {
		CONFIRMATION_CODE(FIELD_CONFIRMATION_CODE),
		TIME_PAID(FIELD_TIME_PAID),
		ACCOUNT(FIELD_ACCOUNT);
		
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

	/** Empty constructor required for hibernate. */
	public IncomingPayment() {
	}	
	
	public IncomingPayment(String paymentBy, String phoneNumber,
			BigDecimal amountPaid, long timePaid, Account account, Target target) {
		this.paymentBy = paymentBy;
		this.phoneNumber = phoneNumber;
		this.amountPaid = amountPaid;
		this.timePaid = timePaid;
		this.account = account;
		this.target = target;
		this.active = true;
		this.isChild = false;
	}


	public IncomingPayment(String paymentBy, String phoneNumber,
			BigDecimal amountPaid, Date timePaid, Account account, Target target) {
		this(paymentBy,phoneNumber,amountPaid,timePaid.getTime(),account, target);
	}
	
	public Account getAccount() {
		return account;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public long getId() {
		return id;
	}

	public String getPaymentBy() {
		return paymentBy;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Long getTimePaid() {
		return timePaid;
	}


	public void setAccount(Account account) {
		this.account = account;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public Target getTarget() {
		return this.target;
	}

	public void setTarget(Target target) {
		this.target = target;
	}

	
	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setPaymentBy(String paymentBy) {
		this.paymentBy = paymentBy;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setTimePaid(Date timePaid) {
		this.timePaid = timePaid.getTime();
	}

	public void setActive(boolean active) {
		this.active = active;
	}
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public boolean isChildPayment() {
		return isChild;
	}

	public void setChild(boolean isChild) {
		this.isChild = isChild;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((amountPaid == null) ? 0 : amountPaid.hashCode());
		result = prime
				* result
				+ ((confirmationCode == null) ? 0 : confirmationCode.hashCode());
		result = prime * result
				+ ((paymentBy == null) ? 0 : paymentBy.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + (int) (timePaid ^ (timePaid >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IncomingPayment other = (IncomingPayment) obj;
		if (amountPaid == null) {
			if (other.amountPaid != null)
				return false;
		} else if (!amountPaid.equals(other.amountPaid))
			return false;
		if (confirmationCode == null) {
			if (other.confirmationCode != null)
				return false;
		} else if (!confirmationCode.equals(other.confirmationCode))
			return false;
		if (paymentBy == null) {
			if (other.paymentBy != null)
				return false;
		} else if (!paymentBy.equals(other.paymentBy))
			return false;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		if (timePaid != other.timePaid)
			return false;
		if (isChild != other.isChild)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "IncomingPayment [id=" + id + ", amountPaid=" + amountPaid
				+ ", confirmationCode=" + confirmationCode + ", paymentBy="
				+ paymentBy + ", phoneNumber=" + phoneNumber + ", timePaid="
				+ timePaid + "]";
	}
	
	public String toStringForLogs() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm a");
		return "Incoming payment characteristics: Ksh " + this.amountPaid + " received from " + this.paymentBy +" "+ this.phoneNumber 
		          + " on " + dateFormat.format(this.timePaid); 
	}

	public void setServiceSettings(PersistableSettings settings) {
		this.serviceSettings = settings;
	}

	public PersistableSettings getServiceSettings() {
		return serviceSettings;
	}
}