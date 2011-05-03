package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.frontlinesms.data.EntityField;

/**
 * @Author Roy
 * */

@Entity
@Table(name = CustomValue.TABLE_NAME)
public class CustomValue {
	public static final String TABLE_NAME = "CustomValue";

	public static final String FIELD_STR_VALUE = "strValue";
	public static final String FIELD_CUSTOM_FIELD = "customFieldId";
	public static final String FIELD_CLIENT = "clientId";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;

	@Column(name = FIELD_STR_VALUE, nullable = true, unique = false)
	private String strValue;

	@ManyToOne
	@JoinColumn(name = FIELD_CUSTOM_FIELD, nullable = false)
	private CustomField customField;

	@ManyToOne
	@JoinColumn(name = FIELD_CLIENT, nullable = true)
	private Client client;
	
	public enum Field implements EntityField<CustomValue> {
		STR_VALUE(FIELD_STR_VALUE),
		CUSTOM_FIELD(FIELD_CUSTOM_FIELD),
		CLIENT(FIELD_CLIENT);
		
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
	public CustomValue() {
	}

	public Client getClient() {
		return client;
	}

	public CustomField getCustomField() {
		return customField;
	}

	public long getId() {
		return id;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}

	/*
	 * 
	 * location; group; branchOffice; representatives;
	 */

	public void setId(long id) {
		this.id = id;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}
}
