package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Author Roy
 * */

@Entity
@Table(name = IncomingPayment.TABLE_NAME)
public class IncomingPayment {
	public static final String TABLE_NAME = "IncomingPayment";
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountId", nullable = true)
	private Account account;

	@Column(name = "amountPaid", nullable = false, unique = false)
	private BigDecimal amountPaid;

	@Column(name = "confirmationCode", nullable = true, unique = false)
	private String confirmationCode;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;

	@Column(name = "paymentBy", nullable = false, unique = false)
	private String paymentBy;

	@Column(name = "phoneNumber", nullable = false, unique = false)
	private String phoneNumber;

	@Column(name = "timePaid", nullable = true, unique = false)
	private Date timePaid;

	// For Dummy Purposes...
	public IncomingPayment() {
	}

	public IncomingPayment(long incomingPaymentId, String paymentBy,
			String phoneNumber, BigDecimal amountPaid, long timePaid,
			Account account) {
		this.id = incomingPaymentId;
		this.paymentBy = paymentBy;
		this.phoneNumber = phoneNumber;
		this.amountPaid = amountPaid;
		this.timePaid = new Date(timePaid);
		this.account = account;
	}

	public IncomingPayment(String paymentBy, String phoneNumber,
			BigDecimal amountPaid, Date timePaid, Account account) {
		super();
		this.paymentBy = paymentBy;
		this.phoneNumber = phoneNumber;
		this.amountPaid = amountPaid;
		this.timePaid = timePaid;
		this.account = account;
	}

	public IncomingPayment(String paymentBy, String phoneNumber,
			BigDecimal amountPaid, long timePaid, Account account) {
		super();
		this.paymentBy = paymentBy;
		this.phoneNumber = phoneNumber;
		this.amountPaid = amountPaid;
		this.timePaid = new Date(timePaid);
		this.account = account;
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

	public Date getTimePaid() {
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
		this.timePaid = timePaid;
	}

}