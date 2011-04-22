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
 */
@Entity
@Table(name = Client.TABLE_NAME)
public class Client {
	public static final String TABLE_NAME = "Client";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = "firstName", nullable = true, unique = false)
	private String firstName;

	@Column(name = "otherName", nullable = true, unique = false)
	private String otherName;

	@Column(name = "phoneNumber", nullable = false, unique = true)
	private String phoneNumber;
	
	@OneToMany
	private Collection<Account> accounts;

	@OneToMany
	private Collection<CustomValue> customData;
	
	/** Empty constructor required for hibernate. */
	public Client() {}

	public Client(String firstName, String otherName, String phoneNumber,
			Collection<Account> accountsFromString) {
		this.firstName = firstName;
		this.otherName = otherName;
		this.phoneNumber = phoneNumber;
		this.firstName = firstName;
	}

	public void addAccount(Account account) {
		this.accounts.add(account);
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

	public Collection<Account> getAccounts() {
		return accounts;
	}

	public Collection<CustomValue> getCustomData() {
		return customData;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((phoneNumber == null) ? 0 : phoneNumber.hashCode());
		return result;
	}

	void setAccounts(Collection<Account> accounts) {
		this.accounts = accounts;
	}

	public void setCustomData(Collection<CustomValue> customData) {
		this.customData = customData;
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
				+ otherName + ", phoneNumber=" + phoneNumber + ", accounts="
				+ accounts + "]";
	}

}
