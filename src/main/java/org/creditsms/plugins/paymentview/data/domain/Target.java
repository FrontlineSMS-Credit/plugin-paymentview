package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.frontlinesms.data.EntityField;

/**
 * @Author Roy
 */
@Entity
@Table(name = Target.TABLE_NAME)
public class Target {
	public static final String TABLE_NAME = "Target";

	public static final String FIELD_START_DATE = "startDate";
	public static final String FIELD_END_DATE = "endDate";
	public static final String FIELD_COMPLETED_DATE = "completedDate";
	public static final String FIELD_ACCOUNT = "accountId";
	public static final String FIELD_TARGET_SERVICE_ITEM = "targetServiceItemId";
	public static final String FIELD_TOTAL_TARGET_COST = "totalTargetCost";
	public static final String FIELD_INCOMING_PAYMENT ="incomingPaymentId";
	public static final String FIELD_TARGET_STATUS ="status";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	@Column(name = FIELD_START_DATE)
	private long startDate;
	@Column(name = FIELD_END_DATE)
	private long endDate;
	@Column(name = FIELD_COMPLETED_DATE, nullable = true)
	private Long completedDate;
	@Column(name = FIELD_TARGET_STATUS, nullable = true)
	private String status;
	@ManyToOne
	@JoinColumn(name = FIELD_ACCOUNT)
	private Account account;
	@Column(name = FIELD_TOTAL_TARGET_COST, nullable = false, unique = false)
	private BigDecimal totalTargetCost;
	@OneToMany
	@JoinColumn(name = FIELD_TARGET_SERVICE_ITEM)
	private Set<TargetServiceItem> targetServiceItem = new HashSet<TargetServiceItem>();
	@OneToMany
	@JoinColumn(name = FIELD_TARGET_STATUS)
	private Set<IncomingPayment> incomingPayments = new HashSet<IncomingPayment>();

	public enum Field implements EntityField<Target> {
		START_DATE(FIELD_START_DATE),
		END_DATE(FIELD_END_DATE),
		COMPLETED_DATE(FIELD_COMPLETED_DATE),
		ACCOUNT(FIELD_ACCOUNT),
		SERVICE_ITEM(FIELD_TARGET_SERVICE_ITEM),
		INCOMING_PAYMENT(FIELD_TARGET_STATUS);
		
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
	public Target() {
	}
	
	//NEW CONSTRUCTOR
	public Target(Date targetStartDate,Date targetEndDate, Account account, BigDecimal totalTargetCost) {
		this.startDate = targetStartDate.getTime();
		this.endDate = targetEndDate.getTime();
		this.completedDate = null;
		this.account = account;
		this.totalTargetCost = totalTargetCost;
	}

	public Account getAccount() {
		return account;
	}

	public Date getEndDate() {
		return new Date(endDate);
	}
	
	public Date getCompletedDate() {
		return completedDate == null ? null :
				new Date(completedDate);
	}

	public long getId() {
		return id;
	}

	public Date getStartDate() {
		return new Date(startDate);
	}

	public void setAccount(Account account) {
		this.account = account;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate.getTime();
	}
	
	public void setCompletedDate(Date completedDate) {
		if (completedDate != null) {
			this.completedDate = completedDate.getTime();	
		} else {
			this.completedDate = null;
		}
	}

	public void setId(long id) {
		this.id = id;
	}
	
	public void setStartDate(Date startDate) {
		this.startDate = startDate.getTime();
	}

	public Set<IncomingPayment> getIncomingPayments() {
		return incomingPayments;
	}

	public void setIncomingPayments(Set<IncomingPayment> incomingPayments) {
		this.incomingPayments = incomingPayments;
	}
	
	public BigDecimal getTotalTargetCost() {
		return totalTargetCost;
	}

	public void setTotalTargetCost(BigDecimal totalTargetCost) {
		this.totalTargetCost = totalTargetCost;
	}
	
	public Set<TargetServiceItem> getTargetServiceItem() {
		return targetServiceItem;
	}

	public void setTargetServiceItem(Set<TargetServiceItem> targetServiceItem) {
		this.targetServiceItem = targetServiceItem;
	}
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((account == null) ? 0 : account.hashCode());
		result = prime * result + (int) (endDate ^ (endDate >>> 32));
		/*result = prime * result
				+ ((serviceItem == null) ? 0 : serviceItem.hashCode());*/
		result = prime * result + (int) (startDate ^ (startDate >>> 32));
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
		Target other = (Target) obj;
		if (account == null) {
			if (other.account != null)
				return false;
		} else if (!account.equals(other.account))
			return false;
		if (endDate != other.endDate)
			return false;
		/*if (serviceItem == null) {
			if (other.serviceItem != null)
				return false;
		} else if (!serviceItem.equals(other.serviceItem))
			return false;*/
		if (startDate != other.startDate)
			return false;
		// WARNING => NULL?????
		if (completedDate != other.completedDate)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Target [id=" + id + ", startDate=" + startDate + ", endDate="
				+ endDate + ", serviceItem="
				+ totalTargetCost + "]";
	}
}
