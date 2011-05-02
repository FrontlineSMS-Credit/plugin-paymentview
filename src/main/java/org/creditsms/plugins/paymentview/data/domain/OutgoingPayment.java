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

	@Column(name = FIELD_TIME_PAID, nullable = false, unique = false)
	private long timePaid;
	
	public enum Field implements EntityField<OutgoingPayment> {
		AMOUNT_PAID(FIELD_AMOUNT_PAID),
		CONFIRMATION_CODE(FIELD_CONFIRMATION_CODE),
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

	public long getTimePaid() {
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

	@Override
	public String toString() {
		return "OutgoingPayment [id=" + id + ", amountPaid=" + amountPaid
				+ ", status=" + status + ", confirmationCode="
				+ confirmationCode + ", notes=" + notes + ", phoneNumber="
				+ phoneNumber + ", timePaid=" + timePaid + ", account="
				+ account + "]";
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}
}
