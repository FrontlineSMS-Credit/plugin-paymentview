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
import javax.persistence.Transient;

import net.frontlinesms.data.EntityField;

import org.hibernate.annotations.IndexColumn;

/**
 * @Author Roy
 */
@Entity
@Table(name = Client.TABLE_NAME)
public class Client {
	public static final String TABLE_NAME = "Client";

	private static final String FIELD_ID = "id";
	private static final String FIELD_FIRST_NAME = "firstName";
	private static final String FIELD_OTHER_NAME = "otherName";
	private static final String FIELD_PHONE_NUMBER = "phoneNumber";
	private static final String FIELD_ACTIVE = "active";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@IndexColumn(name = FIELD_ID)
	@Column(name = FIELD_ID, nullable = false, unique = true)
	private long id;

	@Column(name = FIELD_FIRST_NAME)
	private String firstName;

	@Column(name = FIELD_OTHER_NAME)
	private String otherName;

	@Column(name = FIELD_PHONE_NUMBER, nullable = false, unique = true)
	private String phoneNumber;
	
	@Column(name = FIELD_ACTIVE, nullable = false)
	private boolean active = true;

	@OneToMany
	private Set<CustomValue> customValues;
	
	@OneToMany(fetch = FetchType.EAGER)
	private Set<Target> targets;
	
	public enum Field implements EntityField<Client> {
		ID(FIELD_ID),
		FIRST_NAME(FIELD_FIRST_NAME),
		OTHER_NAME(FIELD_OTHER_NAME),
		PHONE_NUMBER(FIELD_PHONE_NUMBER),
		ACTIVE(FIELD_ACTIVE);
		
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
	public Client() {
	}

	public Client(String firstName, String otherName, String phoneNumber) {
		this.firstName = firstName;
		this.otherName = otherName;
		this.phoneNumber = phoneNumber;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Client other = (Client) obj;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}

	public Set<CustomValue> getCustomValues() {
		return customValues;
	}

	public String getFirstName() {
		return firstName;
	}

	public long getId() {
		return id;
	}

	public String getOtherName() {
		return otherName;
	}

	public String getPhoneNumber() {
		return phoneNumber;
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
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	public void setCustomValue(Set<CustomValue> customValue) {
		this.customValues = customValue;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", firstName=" + firstName + ", otherName="
				+ otherName + ", phoneNumber=" + phoneNumber;
	}

	// > Helper Methods
	public String getFullName() {
		return "" + this.firstName + " " + this.otherName;
	}
	
	//> Used by the UI; an illusion mimmick show selection ;-)
	@Transient
	private boolean selected = false;
	
	/**
	 * @param selected the selected to set
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return the selected
	 */
	public boolean isSelected() {
		return selected;
	}

	public void setTargets(Set<Target> targets) {
		this.targets = targets;
	}

	public Set<Target> getTargets() {
		return targets;
	}
}
