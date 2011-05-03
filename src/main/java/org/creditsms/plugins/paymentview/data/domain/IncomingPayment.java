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
	
	@ManyToOne
	@JoinColumn(name = FIELD_ACCOUNT)
	private Account account;
	
	public enum Field implements EntityField<IncomingPayment> {
		AMOUNT_PAID(FIELD_AMOUNT_PAID),
		CONFIRMATION_CODE(FIELD_CONFIRMATION_CODE),
		PAYMENT_BY(FIELD_PAYMENT_BY),
		PHONE_NUMBER(FIELD_PHONE_NUMBER),
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
			BigDecimal amountPaid, long timePaid, Account account) {
		this.paymentBy = paymentBy;
		this.phoneNumber = phoneNumber;
		this.amountPaid = amountPaid;
		this.timePaid = timePaid;
		this.account = account;
	}

	public IncomingPayment(String paymentBy, String phoneNumber,
			BigDecimal amountPaid, Date timePaid, Account account) {
		this(paymentBy,phoneNumber,amountPaid,timePaid.getTime(),account);
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

	public long getTimePaid() {
		return timePaid;
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

	public void setPaymentBy(String paymentBy) {
		this.paymentBy = paymentBy;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setTimePaid(Date timePaid) {
		this.timePaid = timePaid.getTime();
	}
	
}