package org.creditsms.plugins.paymentview.data.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
	public static final String FIELD_ACCOUNT = "accountId";
	public static final String FIELD_SERVICE_ITEM = "serviceItemId";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	@Column(name = FIELD_START_DATE)
	private long startDate;
	@Column(name = FIELD_END_DATE)
	private long endDate;
	@OneToOne
	@JoinColumn(name = FIELD_ACCOUNT)
	private Account account;
	@ManyToOne
	@JoinColumn(name = FIELD_SERVICE_ITEM)
	private ServiceItem serviceItem;
	
	public enum Field implements EntityField<Target> {
		START_DATE(FIELD_START_DATE),
		END_DATE(FIELD_END_DATE),
		ACCOUNT(FIELD_ACCOUNT),
		SERVICE_ITEM(FIELD_SERVICE_ITEM);
		
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

	public Account getAccount() {
		return account;
	}

	public Date getEndDate() {
		return new Date(endDate);
	}

	public long getId() {
		return id;
	}

	public ServiceItem getServiceItem() {
		return serviceItem;
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

	public void setId(long id) {
		this.id = id;
	}

	public void setServiceItem(ServiceItem targetItem) {
		this.serviceItem = targetItem;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate.getTime();
	}
}
