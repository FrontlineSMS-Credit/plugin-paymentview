package org.creditsms.plugins.paymentview.data.domain;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * @Author Roy
 * */

@Entity
@Table(name = ClientNew.TABLE_NAME)

public class ClientNew {
	public static final String TABLE_NAME = "ClientNew";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="clientId",
            nullable=false,
            unique=true)
	private long clientId;
	
	@Column(name="firstName",
			nullable=true,
			unique=false)
	private String firstName;
	
	@Column(name="otherName",
			nullable=true,
			unique=false)
	private String otherName;
	
	@Column(name="phoneNumber",
			nullable=false,
			unique=true)
	private long phoneNumber;
	
	@OneToMany(mappedBy="clientNew",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	private List<OtherClientDetails> otherClientDetailsList;

	@OneToMany(mappedBy="clientNew",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	private List<Account> account;

	public long getClientId() {
		return clientId;
	}

	public void setClientId(long clientId) {
		this.clientId = clientId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getOtherName() {
		return otherName;
	}

	public void setOtherName(String otherName) {
		this.otherName = otherName;
	}

	public long getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(long phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<OtherClientDetails> getOtherClientDetailsList() {
		return otherClientDetailsList;
	}

	public void setOtherClientDetailsList(
			List<OtherClientDetails> otherClientDetailsList) {
		this.otherClientDetailsList = otherClientDetailsList;
	}

	public List<Account> getAccount() {
		return account;
	}

	public void setAccount(List<Account> account) {
		this.account = account;
	}
	
}
