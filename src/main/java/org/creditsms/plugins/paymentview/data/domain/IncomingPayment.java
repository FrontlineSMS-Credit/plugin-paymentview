package org.creditsms.plugins.paymentview.data.domain;

import java.util.Date;
import java.util.Calendar;
import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Author Roy
 * */

@Entity
@Table(name = IncomingPayment.TABLE_NAME)

public class IncomingPayment {
	public static final String TABLE_NAME = "IncomingPayment";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",
            nullable=false,
            unique=true)
	private long id;
	
	@Column(name="paymentBy",
			nullable=true,
			unique=false)
	private String paymentBy;
	
	@Column(name="phoneNumber",
			nullable=false,
			unique=false)
	private String phoneNumber;
	
	@Column(name="amountPaid",
			nullable=false,
			unique=false)
	private BigDecimal amountPaid;
	
	@Column(name="datePaid",
			nullable=true,
			unique=false)
	private Calendar datePaid;	
	
	@Column(name="timePaid",
			nullable=true,
			unique=false)
	private Date timePaid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountId", 
			nullable = true)
	private Account account;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getPaymentBy() {
		return paymentBy;
	}

	public void setPaymentBy(String paymentBy) {
		this.paymentBy = paymentBy;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public BigDecimal getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(BigDecimal amountPaid) {
		this.amountPaid = amountPaid;
	}

	public Calendar getDatePaid() {
		return datePaid;
	}

	public void setDatePaid(Calendar datePaid) {
		this.datePaid = datePaid;
	}

	public Date getTimePaid() {
		return timePaid;
	}

	public void setTimePaid(Date timePaid) {
		this.timePaid = timePaid;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}