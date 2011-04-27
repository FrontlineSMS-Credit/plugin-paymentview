package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * @Author Roy
 * */
@Entity
@Table(name = Account.TABLE_NAME)
public class Account {
	public static final String TABLE_NAME = "Account";
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;
	
	@Column(name = "accountNumber", nullable = false, unique = true)
	private String accountNumber;
 
	@ManyToOne
	@JoinColumn(name = "clientId", nullable = true)
	private Client client;

	/* Needed by Hibernate */
	public Account() {}

	public Account(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Account(String accountNumber, Client client) {
		this.accountNumber = accountNumber;
		this.client = client;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((client == null) ? 0 : client.hashCode());
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
		Account other = (Account) obj;
		if (accountNumber != other.accountNumber)
			return false;
		if (client == null) {
			if (other.client != null)
				return false;
		} else if (!client.equals(other.client))
			return false;
		if (id != other.id)
			return false;
		return true;
	}

	public long getAccountId() {
		return id;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public Client getClient() {
		return client;
	}

	public void setAccountId(long id) {
		this.id = id;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	@Override
	public String toString() {
		return "Account [id=" + id + ", accountNumber=" + accountNumber
				+ ", client=" + client + "]";
	}
}
