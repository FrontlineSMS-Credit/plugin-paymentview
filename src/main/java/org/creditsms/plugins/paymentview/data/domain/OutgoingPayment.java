package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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

import org.hibernate.annotations.IndexColumn;

/**
 * @Author Roy <roy@credit.frontlinesms.com>
 * @author Ian <ian@credit.frontlinesms.com>
 * */

@Entity
@Table(name = OutgoingPayment.TABLE_NAME)
public class OutgoingPayment {
	public static final String TABLE_NAME = "Outgoingpayment";
	public static final String FIELD_ID = "id";
	public static final String FIELD_TIME_PAID = "timePaid";
	public static final String FIELD_TIME_CONFIRMED = "timeConfirmed";
	public static final String FIELD_CLIENT = "client";
	public static final String FIELD_AMOUNT_PAID = "amountPaid";
	public static final String FIELD_CONFIRMATION_CODE = "confirmationCode";
	public static final String FIELD_NOTES = "notes";
	public static final String FIELD_PAYMENT_ID = "paymentId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@IndexColumn(name = FIELD_ID)
	@Column(name = FIELD_ID, nullable = false, unique = true)
	private long id;

	@Column(name = FIELD_AMOUNT_PAID, nullable = false, unique = false)
	private BigDecimal amountPaid;

	@Enumerated(EnumType.ORDINAL)
	private Status status;

	@Column(name = FIELD_CONFIRMATION_CODE, nullable = true, unique = false)
	private String confirmationCode;

	@Column(name = FIELD_NOTES, nullable = true, unique = false)
	private String notes;

	@Column(name = FIELD_PAYMENT_ID, nullable = true, unique = false)
	private String paymentId;	

	@ManyToOne
	@JoinColumn(name = FIELD_CLIENT, nullable = false, unique = false)
	private Client client;
	
	
	@Column(name = FIELD_TIME_PAID, nullable = false, unique = false)
	private Long timePaid;
	
	@Column(name = FIELD_TIME_CONFIRMED, nullable = true, unique = false)
	private Long timeConfirmed;

	public enum Field implements EntityField<OutgoingPayment> {
		AMOUNT_PAID(FIELD_AMOUNT_PAID),
		CONFIRMATION_CODE(FIELD_CONFIRMATION_CODE),
		CLIENT(FIELD_CLIENT),
		TIME_PAID(FIELD_TIME_PAID),
		TIME_CONFIRMED(FIELD_TIME_CONFIRMED),
		NOTES(FIELD_NOTES),
		PAYMENT_ID(FIELD_PAYMENT_ID);
		
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
	@JoinColumn(name = "accountId", nullable = true)//TBC - outgoing payment only for standard one(not for paybill -> so only one account is set per user)
	private Account account;//TBC

	/** Empty constructor required for hibernate. */
	public OutgoingPayment() {
	}

	public static enum Status {
		CREATED("Created","/icons/sms_created.png"),
		UNCONFIRMED("Unconfirmed", "/icons/sms_unconfirmed.png"),
		CONFIRMED("Confirmed", "/icons/sms_confirmed.png"),
		ERROR("Error", "/icons/error.png");

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

	public OutgoingPayment(Client client, BigDecimal amountPaid,
	Date timePaid, Account account, String notes, Status status, String paymentId) {
		this(client, amountPaid, timePaid.getTime(), account, notes,
				status, paymentId);
	}
	
	public OutgoingPayment(Client client, BigDecimal amountPaid, 
		Account account, String notes) {
		this(client, amountPaid, null, account, notes,null,"");
	}

	public OutgoingPayment(Client client, BigDecimal amountPaid,
		long timePaid, Account account, String notes, Status status, String paymentId) {
	
		this.client = client;
		this.amountPaid = amountPaid;
		this.timePaid = timePaid;
		this.timeConfirmed = null;
		this.account = account;
		this.notes = notes;
		this.status = status;
		this.paymentId = paymentId;
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

	public Client getClient() {
		return client;
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

	public void setClient(Client client) {
		this.client = client;
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
	
	public Long getTimeConfirmed() {
		return timeConfirmed;
	}

	public void setTimeConfirmed(Long timeConfirmed) {
		this.timeConfirmed = timeConfirmed;
	}
	
	public String getPaymentId() {
		return paymentId;
	}

	public void setPaymentId(String paymentId) {
		this.paymentId = paymentId;
	}
	
	@Override
	public String toString() {
		return "OutgoingPayment [id=" + id + ", amountPaid=" + amountPaid
				+ ", status=" + status + ", confirmationCode="
				+ confirmationCode + ", notes=" + notes + ", phoneNumber="
				+ client.getPhoneNumber() + ", timePaid=" + timePaid + ", account="
				+ account + ", paymentId=" + paymentId + "]";
	}
	
	public String toStringForLogs() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm a");
		return "Outgoing payment caracteristics:\nKsh " + this.amountPaid + " sent to " + this.client.getFullName() + " " + this.client.getPhoneNumber() 
		          + " on " + dateFormat.format(this.timePaid); 
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
				+ ((client.getPhoneNumber() == null) ? 0 : client.getPhoneNumber().hashCode());
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
		if (paymentId == null) {
			if (other.paymentId != null) {
				return false;
			}
		} else if (!paymentId.equals(other.paymentId)) {
			return false;
		}
		if (client == null) {
			if (other.client != null) {
				return false;
			}
		} else if (client.getId()!=(other.client.getId())) {
			return false;
		}
		if (status != other.status) {
			return false;
		}
		
		if (!timeConfirmed.equals(other.timeConfirmed)) {
			return false;
		}
		return true;
	}
	
	
}
