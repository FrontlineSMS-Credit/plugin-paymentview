package org.creditsms.plugins.paymentview.data.domain;

import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @Author Roy
 * @author ian
 * */

@Entity
@Table(name = CustomField.TABLE_NAME)
public class CustomField {
	public static final String TABLE_NAME = "CustomField";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = "name", nullable = true, unique = false)
	private String strName;

	@OneToMany
	private Collection<CustomValue> customValue;
	
	public CustomField(String strName) {
		this.strName = strName;
	}

	public CustomField(String strName, Collection<CustomValue> customValue) {
		this.strName = strName;
		this.customValue = customValue;
	}

	public Collection<CustomValue> getCustomValue() {
		return customValue;
	}

	public long getId() {
		return id;
	}

	public String getStrName() {
		return strName;
	}

	public void setCustomValue(Collection<CustomValue> customValue) {
		this.customValue = customValue;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}
}
