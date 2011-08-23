package org.creditsms.plugins.paymentview.data.domain;

import java.math.BigDecimal;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import net.frontlinesms.data.EntityField;

import org.hibernate.annotations.IndexColumn;

/**
 * @Author Roy
 * @author ian
 */
@Entity
@Table(name = ServiceItem.TABLE_NAME)
public class ServiceItem {
	public static final String TABLE_NAME = "ServiceItem";

	private static final String FIELD_AMOUNT = "amount";
	private static final String FIELD_TARGET_NAME = "targetName";
	
	@Id
	@IndexColumn(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = FIELD_AMOUNT, nullable = false, unique = false)
	private BigDecimal amount;

	@OneToMany(fetch=FetchType.EAGER)
	private Set<Target> targets;

	@Column(name = FIELD_TARGET_NAME, nullable = false, unique = false)
	private String targetName;
	
	public enum Field implements EntityField<ServiceItem> {
		AMOUNT(FIELD_AMOUNT),
		TARGET_NAME(FIELD_TARGET_NAME);
		
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
	public ServiceItem() {}
	
	public BigDecimal getAmount() {
		return amount;
	}

	public long getId() {
		return id;
	}

	public Set<Target> getTargets() {
		return targets;
	}

	public String getTargetName() {
		return targetName;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setTarget(Set<Target> targets) {
		this.targets = targets;
	}

	public void setTargetName(String targetName) {
		this.targetName = targetName;
	}

	@Override
	public String toString() {
		return "ServiceItem [id=" + id + ", targetName=" + targetName
				+ ", amount=" + amount + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result
				+ ((targetName == null) ? 0 : targetName.hashCode());
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
		ServiceItem other = (ServiceItem) obj;
		if (amount == null) {
			if (other.amount != null)
				return false;
		} else if (!amount.equals(other.amount))
			return false;
		if (targetName == null) {
			if (other.targetName != null)
				return false;
		} else if (!targetName.equals(other.targetName))
			return false;
		return true;
	}
}
