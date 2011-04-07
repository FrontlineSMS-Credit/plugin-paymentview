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
@Table(name = Account.TABLE_NAME)

public class Account {
	public static final String TABLE_NAME = "Account";
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id",
            nullable=false,
            unique=true)
	private long id;
	
	@Column(name="accountNumber", nullable=false, unique=true)
	private long accountNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clientId", 
			nullable = true)
	private Client client;

	public long getAccountId() {
		return id;
	}

	public void setAccountId(long id) {
		this.id = id;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

}
