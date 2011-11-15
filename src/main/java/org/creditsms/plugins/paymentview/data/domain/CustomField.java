package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import net.frontlinesms.data.EntityField;

import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

/**
 * @Author Roy
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 * */

@Entity
public class CustomField {
	private static final String FIELD_READABLE_NAME = "readableName";
	private static final String FIELD_USED = "used";
	private static final String FIELD_ACTIVE = "active";

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private long id;
	@Column(nullable = false, unique = true)
	private String readableName;
	private boolean used;
	private boolean active;

	public enum Field implements EntityField<CustomField> {
		READABLE_NAME(FIELD_READABLE_NAME), 
		USED(FIELD_USED), 
		ACTIVE(FIELD_ACTIVE);

		/** name of a field */
		private final String fieldName;

		/**
		 * Creates a new {@link Field}
		 * 
		 * @param fieldName
		 *            name of the field
		 */
		Field(String fieldName) {
			this.fieldName = fieldName;
		}

		/** @see EntityField#getFieldName() */
		public String getFieldName() {
			return this.fieldName;
		}
	}

	/** Empty constructor required for hibernate. */
	public CustomField() {
	}

	public long getId() {
		return id;
	}

	public String getReadableName() {
		return readableName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setReadableName(String strName) {
		this.readableName = strName;
	}

	public String getCamelCaseName() {
		return PaymentViewUtils.toCamelCase(this.readableName);
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

	public boolean isUsed() {
		return used;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (active ? 1231 : 1237);
		result = prime * result
				+ ((readableName == null) ? 0 : readableName.hashCode());
		result = prime * result + (used ? 1231 : 1237);
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
		CustomField other = (CustomField) obj;
		if (active != other.active)
			return false;
		if (readableName == null) {
			if (other.readableName != null)
				return false;
		} else if (!readableName.equals(other.readableName))
			return false;
		if (used != other.used)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "CustomField [id=" + id + ", strName="
				+ readableName + ", used=" + used + ", active=" + active + "]";
	}
}
