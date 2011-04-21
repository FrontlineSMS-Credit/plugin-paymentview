package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
	@Column(name = "accountNumber", nullable = false, unique = true)
	private long accountNumber;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clientId", nullable = true)
	private Client client;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, unique = true)
	private long id;

	// Some constructors Created to be used by the DummyData Class
	public Account() {
	}

	public Account(long accountNumber) {
		super();
		this.accountNumber = accountNumber;
	}

	public Account(long accountNumber, Client client) {
		super();
		this.accountNumber = accountNumber;
		this.client = client;
	}

	public Account(long id, long accountNumber) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
	}

	public Account(long id, long accountNumber, Client client) {
		super();
		this.id = id;
		this.accountNumber = accountNumber;
		this.client = client;
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

	public long getAccountNumber() {
		return accountNumber;
	}

	public Client getClient() {
		return client;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (accountNumber ^ (accountNumber >>> 32));
		result = prime * result + ((client == null) ? 0 : client.hashCode());
		result = prime * result + (int) (id ^ (id >>> 32));
		return result;
	}

	public void setAccountId(long id) {
		this.id = id;
	}

	public void setAccountNumber(long accountNumber) {
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
