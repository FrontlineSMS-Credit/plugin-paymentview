package org.creditsms.plugins.paymentview.data.domain;

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

import org.creditsms.plugins.paymentview.utils.StringUtil;

/**
 * @Author Roy
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 * */

@Entity
@Table(name = CustomField.TABLE_NAME)
public class CustomField {
	public static final String TABLE_NAME = "CustomField";
	private static final String FIELD_READABLE_NAME = "readableName";
	private static final String FIELD_USED = "used";
	private static final String FIELD_ACTIVE = "active";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(nullable = false, unique = true)
	private long id;

	@Column(nullable = false, unique = true)
	private String readableName;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<CustomValue> customValue;

	@Column(name = FIELD_USED)
	private boolean used = false;

	@Column(name = FIELD_ACTIVE)
	private boolean active = false;

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

	public Set<CustomValue> getCustomValue() {
		return customValue;
	}

	public long getId() {
		return id;
	}

	public String getReadableName() {
		return readableName;
	}

	public void setCustomValue(Set<CustomValue> customValue) {
		this.customValue = customValue;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setReadableName(String strName) {
		this.readableName = strName;
	}

	public String getCamelCaseName() {
		return StringUtil.toCamelCase(this.readableName);
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
