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

	public static final String FIELD_STR_NAME = "name";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = FIELD_STR_NAME, nullable = true, unique = false)
	private String strName;

	@OneToMany
	private Set<CustomValue> customValue;
	
	public enum Field implements EntityField<CustomField> {
		STR_NAME(FIELD_STR_NAME);
		
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
	
	public CustomField(String strName) {
		this.strName = strName;
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
}
