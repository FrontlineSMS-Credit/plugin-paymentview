package org.creditsms.plugins.paymentview.data.domain;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @Author Roy
 * */

@Entity
@Table(name = CustomField.TABLE_NAME)

public class CustomField {
	public static final String TABLE_NAME = "CustomField";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",
            nullable=false,
            unique=true)
	private long id;

	@Column(name="name",
			nullable=true,
			unique=false)
	private String strName;
	
	@OneToMany
	private Set<CustomValue> customValue = new HashSet<CustomValue>();
	
	public CustomField(String strName) {
		this.strName = strName;
	}
	
	public CustomField(String strName, Set<CustomValue> customValue) {
		this.strName = strName;
		this.customValue = customValue;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStrName() {
		return strName;
	}

	public void setStrName(String strName) {
		this.strName = strName;
	}

	public Set<CustomValue> getCustomValue() {
		return customValue;
	}

	public void setCustomValue(Set<CustomValue> customValue) {
		this.customValue = customValue;
	}
}
