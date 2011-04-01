package org.creditsms.plugins.paymentview.data.domain;

import java.util.Date;
import java.util.Calendar;

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
@Table(name = OutGoingpayment.TABLE_NAME)

public class OutGoingpayment {
	public static final String TABLE_NAME = "OutGoingpayment";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="outgoingPaymentId",
            nullable=false,
            unique=true)
	private long outgoingPaymentId;

	@Column(name="phoneNumber",
			nullable=false,
			unique=false)
	private long phoneNumber;
	
	@Column(name="amountPaid",
			nullable=false,
			unique=false)
	private float amountPaid;
	
	@Column(name="datePaid",
			nullable=false,
			unique=false)
	private Calendar datePaid;	
	
	@Column(name="timePaid",
			nullable=false,
			unique=false)
	private Date timePaid;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "accountId", 
			nullable = true)
	private Account account;
	
	@Column(name="notes",
			nullable=false,
			unique=false)
	private String notes;
	
	@Column(name="confirmation",
			nullable=false,
			unique=false)
	private boolean confirmation;
	
	public long getOutgoingPaymentId() {
		return outgoingPaymentId;
	}

	public void setOutgoingPaymentId(long outgoingPaymentId) {
		this.outgoingPaymentId = outgoingPaymentId;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public float getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(float amountPaid) {
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

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public boolean isConfirmation() {
		return confirmation;
	}

	public void setConfirmation(boolean confirmation) {
		this.confirmation = confirmation;
	}
	
}
