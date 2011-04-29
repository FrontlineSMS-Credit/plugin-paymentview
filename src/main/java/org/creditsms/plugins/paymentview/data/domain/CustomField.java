package org.creditsms.plugins.paymentview.data.domain;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
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
	private static final String FIELD_STR_NAME = "strname";
	private static final String FIELD_NAME = "name";
	private static final String FIELD_USED = "used";
	private static final String FIELD_ACTIVE = "active";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = FIELD_NAME, nullable = false, unique = true)
	private String name;
	
	@Column(name = FIELD_STR_NAME, nullable = true, unique = false)
	private String strName;

	@OneToMany
	private Set<CustomValue> customValue;
	
	@Column(name = FIELD_USED, unique = false)
	private boolean used  = false;
	
	@Column(name = FIELD_ACTIVE, unique = false)
	private boolean active = false;
	
	public enum Field implements EntityField<CustomField> {
		STR_NAME(FIELD_STR_NAME),
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
		this.strName = strName;
	}
	

	public CustomField(String name, String strName, boolean used, boolean active) {
		this(name, strName);
		this.used = used;
		this.active = active;
	}

	public CustomField(String strName, Set<CustomValue> customValue) {
		this.strName = strName;
		this.customValue = customValue;
	}

	public Set<CustomValue> getCustomValue() {
		return customValue;
	}

	public long getId() {
		return id;
	}

	public String getStrName() {
		return strName;
	}

	public void setCustomValue(Set<CustomValue> customValue) {
		this.customValue = customValue;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setStrName(String strName) {
		this.strName = strName;
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
}
