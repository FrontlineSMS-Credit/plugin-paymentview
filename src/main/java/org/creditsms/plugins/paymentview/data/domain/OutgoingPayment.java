package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.frontlinesms.data.EntityField;

/**
 * @Author Roy <roy@credit.frontlinesms.com>
 * @author Ian <ian@credit.frontlinesms.com>
 * */

@Entity
@Table(name = OutgoingPayment.TABLE_NAME)
public class OutgoingPayment {
	public static final String TABLE_NAME = "Outgoingpayment";
	public static final String FIELD_TIME_PAID = "timePaid";
	public static final String FIELD_PAYMENT_TO = "paymentTo";
	public static final String FIELD_PHONE_NUMBER = "phoneNumber";
	public static final String FIELD_AMOUNT_PAID = "amountPaid";
	public static final String FIELD_CONFIRMATION_CODE = "confirmationCode";
	public static final String FIELD_NOTES = "notes";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;

	@Column(name = FIELD_AMOUNT_PAID, nullable = false, unique = false)
	private BigDecimal amountPaid;

	@Enumerated(EnumType.ORDINAL)
	private Status status;

	@Column(name = FIELD_CONFIRMATION_CODE, nullable = true, unique = false)
	private String confirmationCode;

	@Column(name = FIELD_NOTES, nullable = true, unique = false)
	private String notes;

	@Column(name = FIELD_PHONE_NUMBER, nullable = false, unique = false)
	private String phoneNumber;
	
	@Column(name = FIELD_PAYMENT_TO, nullable = true)
	private String paymentTo;

	@Column(name = FIELD_TIME_PAID, nullable = false, unique = false)
	private long timePaid;
	
	public enum Field implements EntityField<OutgoingPayment> {
		AMOUNT_PAID(FIELD_AMOUNT_PAID),
		CONFIRMATION_CODE(FIELD_CONFIRMATION_CODE),
		PAYMENT_TO(FIELD_PAYMENT_TO),
		PHONE_NUMBER(FIELD_PHONE_NUMBER),
		TIME_PAID(FIELD_TIME_PAID),
		NOTES(FIELD_NOTES);
		
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

	@ManyToOne
	@JoinColumn(name = "accountId", nullable = true)
	private Account account;

	/** Empty constructor required for hibernate. */
	public OutgoingPayment() {
	}

	public static enum Status {
		UNSENT("Unsent","/icons/sms_receive.png"),
		SENT("Sent", "/icons/sms_send.png"),
		ERROR("Error", "/icons/error.png"),
		CONFIRMED("Confirmed", "/icons/tick.png");

		private String status;
		private String icon;

		private Status(String displayName, String icon) {
			this.status = displayName;
			this.icon = icon;
		}

		@Override
		public String toString() {
			return status;
		}

		public String getIconPath() {
			return icon;
		}
		
		public static Status getStatusFromString(String status){
			for(Status s:Status.values()){
				if(status.equalsIgnoreCase(s.toString())){
					return s;
				}
			}
			return null;
		}
	}

	public OutgoingPayment(String phoneNumber, BigDecimal amountPaid,
			Date timePaid, Account account, String notes, Status status) {
		this(phoneNumber, amountPaid, timePaid.getTime(), account, notes,
				status);
	}

	public OutgoingPayment(String phoneNumber, BigDecimal amountPaid, 
			Account account, String notes) {
		this(phoneNumber, amountPaid, null, account, notes,null);
	}
	
	public OutgoingPayment(String phoneNumber, BigDecimal amountPaid,
			long timePaid, Account account, String notes, Status status) {
		
		this.phoneNumber = phoneNumber;
		this.amountPaid = amountPaid;
		this.timePaid = timePaid;
		this.account = account;
		this.notes = notes;
		this.status = status;
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

	public String getNotes() {
		return notes;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public Long getTimePaid() {
		return this.timePaid;
	}

	public void setAccount(Account account) {
		this.account = account;
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

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setTimePaid(Date timePaid) {
		this.timePaid = timePaid.getTime();
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	/**
	 * @param paymentTo the paymentTo to set
	 */
	public void setPaymentTo(String paymentTo) {
		this.paymentTo = paymentTo;
	}

	/**
	 * @return the paymentTo
	 */
	public String getPaymentTo() {
		return paymentTo;
	}
	
	@Override
	public String toString() {
		return "OutgoingPayment [id=" + id + ", amountPaid=" + amountPaid
				+ ", status=" + status + ", confirmationCode="
				+ confirmationCode + ", notes=" + notes + ", phoneNumber="
				+ phoneNumber + ", timePaid=" + timePaid + ", paymentTo="+ paymentTo + ", account="
				+ account + "]";
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
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
				+ ((paymentTo == null) ? 0 : paymentTo.hashCode());
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + (int) (timePaid ^ (timePaid >>> 32));
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if ((!(obj instanceof OutgoingPayment))||(obj == null)) {
			return false;
		}
		OutgoingPayment other = (OutgoingPayment) obj;
		if (amountPaid == null) {
			if (other.amountPaid != null) {
				return false;
			}
		} else if (!amountPaid.equals(other.amountPaid)) {
			return false;
		}
		if (confirmationCode == null) {
			if (other.confirmationCode != null) {
				return false;
			}
		} else if (!confirmationCode.equals(other.confirmationCode)) {
			return false;
		}
		if (paymentTo == null) {
			if (other.paymentTo != null) {
				return false;
			}
		} else if (!paymentTo.equals(other.paymentTo)) {
			return false;
		}
		if (phoneNumber == null) {
			if (other.phoneNumber != null) {
				return false;
			}
		} else if (!phoneNumber.equals(other.phoneNumber)) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		if (timePaid != other.timePaid) {
			return false;
		}
		return true;
	}
	
	
}
