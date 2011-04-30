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

/**
 * @Author Roy
 * @author ian
 * */

@Entity
@Table(name = CustomField.TABLE_NAME)
public class CustomField {
	public static final String TABLE_NAME = "CustomField";
	private static final String FIELD_READABLE_NAME = "strname";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_USED = "used";
	private static final String FIELD_ACTIVE = "active";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = FIELD_NAME, nullable = false, unique = true)
	private String name;
	
	@Column(name = FIELD_READABLE_NAME, nullable = true, unique = false)
	private String readableName;

	@OneToMany(fetch = FetchType.EAGER)
	private Set<CustomValue> customValue;
	
	@Column(name = FIELD_USED, unique = false)
	private boolean used  = false;
	
	@Column(name = FIELD_ACTIVE, unique = false)
	private boolean active = false;
	
	public enum Field implements EntityField<CustomField> {
		READABLE_NAME(FIELD_READABLE_NAME),
		USED(FIELD_USED),
		ACTIVE(FIELD_ACTIVE),
		NAME(FIELD_NAME);
		
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
	CustomField() {}
	
	public CustomField(String name, String strName) {
		this.name = name;
		this.readableName = strName;
	}
	

	public CustomField(String name, String strName, boolean used, boolean active) {
		this(name, strName);
		this.used = used;
		this.active = active;
	}

	public CustomField(String strName, Set<CustomValue> customValue) {
		this.readableName = strName;
		this.customValue = customValue;
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

	public void setStrName(String strName) {
		this.readableName = strName;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
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
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((readableName == null) ? 0 : readableName.hashCode());
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
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
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
		return "CustomField [id=" + id + ", name=" + name + ", strName="
				+ readableName + ", used=" + used
				+ ", active=" + active + "]";
	}
}
