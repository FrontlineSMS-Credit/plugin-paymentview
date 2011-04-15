package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Author Roy
 * */

@Entity
@Table(name = CustomValue.TABLE_NAME)

public class CustomValue {
	public static final String TABLE_NAME = "OtherClientDetails";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",
            nullable=false,
            unique=true)
	private long id;

	@Column(name="strValue",
			nullable=true,
			unique=false)
	private String strValue;	

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clientId", 
			nullable = true)
	private Client client;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "customDataId", 
			nullable = false)
	private CustomField customField;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getStrValue() {
		return strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public CustomField getCustomField() {
		return customField;
	}

	public void setCustomField(CustomField customField) {
		this.customField = customField;
	}
/*
 * 	
	location;
	group;
	branchOffice;
	representatives;	
 * */
}
