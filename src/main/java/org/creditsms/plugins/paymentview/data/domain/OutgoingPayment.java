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
 * @author Roy <roy@credit.frontlinesms.com>
 * @author Ian <ian@credit.frontlinesms.com>
 */
@Entity
public class OutgoingPayment {
	private static final String FIELD_TIME_PAID = "timePaid";
	private static final String FIELD_CLIENT = "client";
	private static final String FIELD_SERVICE_SETTINGS = "serviceSettings";

	@Id @Column(unique=true)
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	private BigDecimal amountPaid;
	private Status status;
	@Column(nullable=true)
	private String confirmationCode;
	@Column(nullable=true)
	private String notes;
	@Column(nullable=true)
	private String paymentId;	
	@ManyToOne @JoinColumn(name=FIELD_CLIENT)
	private Client client;
	@Column(name=FIELD_TIME_PAID)
	private long timePaid;
	@Column(nullable=true)
	private Long timeConfirmed;
	@ManyToOne @JoinColumn(name=FIELD_SERVICE_SETTINGS)
	private PersistableSettings serviceSettings;
	/** Special value for use by 3rd party payment services for whatever they choose. */
	@Column(nullable=true)
	private String special;

	public enum Field implements EntityField<OutgoingPayment> {
		TIME_PAID(FIELD_TIME_PAID);
		
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

	public String getSpecial() {
		return special;
	}
	
	public void setSpecial(String special) {
		this.special = special;
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
	
	public void setPaymentServiceSettings(PersistableSettings paymentServiceSettings) {
		this.serviceSettings = paymentServiceSettings;
	}

	public PersistableSettings getPaymentServiceSettings() {
		return serviceSettings;
	}
	
	@Override
	public String toString() {
		return "OutgoingPayment [id=" + id + ", amountPaid=" + amountPaid
				+ ", status=" + status + ", confirmationCode="
				+ confirmationCode + ", notes=" + notes	+ ", phoneNumber=" 
				+ client.getPhoneNumber() + ", timePaid=" + timePaid + ", account="
				+ account + ", paymentId=" + paymentId + "]";
	}
	
	public String toStringForLogs() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy 'at' hh:mm a");
		return "Outgoing payment characteristics: Ksh " + this.amountPaid + " sent to " + 
			 this.client.getFullName() + " " + this.client.getPhoneNumber() 
		          + " on " + dateFormat.format(this.timePaid); 
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result
				+ ((amountPaid == null) ? 0 : amountPaid.hashCode());
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime
				* result
				+ ((confirmationCode == null) ? 0 : confirmationCode.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result
				+ ((paymentId == null) ? 0 : paymentId.hashCode());
		result = prime * result
				+ ((serviceSettings == null) ? 0 : serviceSettings.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result
				+ ((timeConfirmed == null) ? 0 : timeConfirmed.hashCode());
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
		OutgoingPayment other = (OutgoingPayment) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (amountPaid == null) {
			if (other.amountPaid != null)
				return false;
		} else if (!amountPaid.equals(other.amountPaid))
			return false;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (confirmationCode == null) {
			if (other.confirmationCode != null)
				return false;
		} else if (!confirmationCode.equals(other.confirmationCode))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (paymentId == null) {
			if (other.paymentId != null)
				return false;
		} else if (!paymentId.equals(other.paymentId))
			return false;
		if (serviceSettings == null) {
			if (other.serviceSettings != null)
				return false;
		} else if (!serviceSettings.equals(other.serviceSettings))
			return false;
		if (status != other.status)
			return false;
		if (timeConfirmed == null) {
			if (other.timeConfirmed != null)
				return false;
		} else if (!timeConfirmed.equals(other.timeConfirmed))
			return false;
		if (timePaid != other.timePaid)
			return false;
		return true;
	}
}
