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

/**
 * @Author Roy <roy@credit.frontlinesms.com>
 * @author Ian <ian@credit.frontlinesms.com>
 * */

@Entity
@Table(name = OutgoingPayment.TABLE_NAME)
public class OutgoingPayment {
	public static final String TABLE_NAME = "Outgoingpayment";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;

	@Column(name = "amountPaid", nullable = false, unique = false)
	private BigDecimal amountPaid;

	@Enumerated(EnumType.ORDINAL)
	private Status status;

	@Column(name = "confirmationCode", nullable = true, unique = false)
	private String confirmationCode;

	@Column(name = "notes", nullable = true, unique = false)
	private String notes;

	@Column(name = "phoneNumber", nullable = false, unique = false)
	private String phoneNumber;

	@Column(name = "timePaid", nullable = false, unique = false)
	private Date timePaid;

	@ManyToOne
	@JoinColumn(name = "accountId", nullable = true)
	private Account account;

	/** Empty constructor required for hibernate. */
	public OutgoingPayment() {
	}

	public static enum Status {
		SENT("Sent", "/icons/sms_send.png"),
		RECEIVED("Received","/icons/sms_receive.png"),
		CONFIRMED("Confirmed", "/icons/tick.png"),
		ERROR("Error", "/icons/error.png");

		private String status;
		private String icon;

		private Status(String displayName, String icon) {
			this.status = displayName;
			this.icon = icon;
		}

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
		this.phoneNumber = phoneNumber;
		this.amountPaid = amountPaid;
		this.timePaid = timePaid;
		this.account = account;
		this.notes = notes;
		this.status = status;
	}

	public OutgoingPayment(String phoneNumber, BigDecimal amountPaid,
			long timePaid, Account account, String notes, Status status) {
		this(phoneNumber, amountPaid, new Date(timePaid), account, notes,
				status);
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

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public void setTimePaid(Date timePaid) {
		this.timePaid = timePaid;
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
