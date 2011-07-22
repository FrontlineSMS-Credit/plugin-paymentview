package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.frontlinesms.data.EntityField;

/**
 * @Author Roy
 * @author ian
 * */

@Entity
@Table(name = IncomingPayment.TABLE_NAME)
public class IncomingPayment {
	public static final String TABLE_NAME = "IncomingPayment";
	public static final String FIELD_AMOUNT_PAID = "amountPaid";
	public static final String FIELD_CONFIRMATION_CODE = "confirmationCode";
	public static final String FIELD_PAYMENT_BY = "paymentBy";
	public static final String FIELD_PHONE_NUMBER = "phoneNumber";
	public static final String FIELD_TIME_PAID = "timePaid";
	public static final String FIELD_ACCOUNT = "account";
	public static final String FIELD_TARGET ="target";
	public static final String FIELD_ACTIVE ="active";
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = FIELD_AMOUNT_PAID, nullable = false)
	private BigDecimal amountPaid;

	@Column(name = FIELD_CONFIRMATION_CODE)
	private String confirmationCode;

	@Column(name = FIELD_PAYMENT_BY, nullable = false)
	private String paymentBy;

	@Column(name = FIELD_PHONE_NUMBER, nullable = false)
	private String phoneNumber;

	@Column(name = FIELD_TIME_PAID)
	private long timePaid;
	
	@Column(name = FIELD_ACTIVE, nullable = false)
	private boolean active;
	
	@ManyToOne
	@JoinColumn(name = FIELD_ACCOUNT)
	private Account account;
	
	@ManyToOne
	@JoinColumn(name = FIELD_TARGET, nullable = true) //nullable if payment for generic account
	private Target target;

	public enum Field implements EntityField<IncomingPayment> {
		AMOUNT_PAID(FIELD_AMOUNT_PAID),
		CONFIRMATION_CODE(FIELD_CONFIRMATION_CODE),
		PAYMENT_BY(FIELD_PAYMENT_BY),
		PHONE_NUMBER(FIELD_PHONE_NUMBER),
		TIME_PAID(FIELD_TIME_PAID),
		ACCOUNT(FIELD_ACCOUNT),
		TARGET(FIELD_TARGET),
		ACTIVE(FIELD_ACTIVE);
		
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
	
	public boolean isActive() {
		return active;
	}

	public void setAccount(Account account) {
		this.account = account;
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
		return true;
	}

	@Override
	public String toString() {
		return "IncomingPayment [id=" + id + ", amountPaid=" + amountPaid
				+ ", confirmationCode=" + confirmationCode + ", paymentBy="
				+ paymentBy + ", phoneNumber=" + phoneNumber + ", timePaid="
				+ timePaid + "]";
	}	
}