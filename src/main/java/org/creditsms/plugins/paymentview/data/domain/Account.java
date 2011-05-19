package org.creditsms.plugins.paymentview.data.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import net.frontlinesms.data.EntityField;

/**
 * @Author Roy
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 * 
 * */
@Entity
@Table(name = Account.TABLE_NAME)
public class Account {
	public static final String TABLE_NAME = "Account";

	private static final String FIELD_ACCOUNT_NUMBER = "accountNumber";
	private static final String FIELD_CLIENT = "clientId";
	private static final String FIELD_ID = "id";

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = FIELD_ID, nullable = false, unique = true)
	private long id;

	@Column(name = FIELD_ACCOUNT_NUMBER, nullable = false, unique = true)
	private String accountNumber;

	@ManyToOne
	@JoinColumn(name = FIELD_CLIENT, nullable = true)
	private Client client;

	/* Needed by Hibernate */
	public Account() {
	}

	public enum Field implements EntityField<Account> {
		ID(FIELD_ID),
		ACCOUNT_NUMBER(FIELD_ACCOUNT_NUMBER),
		CLIENT(FIELD_CLIENT);
		
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
	
	public Account(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public Account(String accountNumber, Client client) {
		this.accountNumber = accountNumber;
		this.client = client;
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((accountNumber == null) ? 0 : accountNumber.hashCode());
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
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		return true;
	}
}
