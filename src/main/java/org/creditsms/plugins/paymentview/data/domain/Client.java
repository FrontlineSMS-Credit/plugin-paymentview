package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.OneToMany;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @Author Roy
 */
@Entity
@Table(name = Client.TABLE_NAME)
public class Client {
	public static final String TABLE_NAME = "Client";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",
            nullable=false,
            unique=true)
	private long id;
	
	@Column(name="firstName",
			nullable=true,
			unique=false)
	private String firstName;
	
	@Column(name="otherName",
			nullable=true,
			unique=false)
	private String otherName;
	
	@Column(name="phoneNumber", nullable=false, unique=true)
	private String phoneNumber;
	
	@OneToMany
	private Set<Account> accounts = new HashSet<Account>();
	
	@OneToMany
	private Set<CustomValue> customData = new HashSet<CustomValue>();

	public Client(String firstName, String otherName, String phoneNumber,
			Set<Account> accountsFromString) {
		this.firstName = firstName;
		this.otherName = otherName;
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
	}
	// Created for DummyData
	public Client() {
	}

	public Collection<CustomValue> getCustomData() {
		return customData;
	}

	public void setCustomData(Set<CustomValue> customData) {
		this.customData = customData;
	}

	public Collection<Account> getAccounts() {
		return accounts;
	}

	void setAccounts(Set<Account> accounts) {
		this.accounts = accounts;
	}
	
	public void addAccount(Account account) {
		this.accounts.add(account);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
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
		Client other = (Client) obj;
		if (phoneNumber == null) {
			if (other.phoneNumber != null)
				return false;
		} else if (!phoneNumber.equals(other.phoneNumber))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Client [id=" + id + ", firstName=" + firstName + ", otherName="
				+ otherName + ", phoneNumber=" + phoneNumber + ", accounts="
				+ accounts + "]";
	}
	
	

}
