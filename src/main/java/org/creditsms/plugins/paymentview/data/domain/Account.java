package org.creditsms.plugins.paymentview.data.domain;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.JoinColumn;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
	@Column(name="accountId",
            nullable=false,
            unique=true)
	private long accountId;
	
	@Column(name="accountNumber",
			nullable=false,
			unique=true)
	private long accountNumber;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "clientId", 
			nullable = true)
	private ClientNew clientNew;
	
	@OneToMany(mappedBy="account",
			cascade = CascadeType.ALL,
			fetch = FetchType.LAZY)
	private List<IncomingPayment> incomingpayment;
	
	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	public long getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(long accountNumber) {
		this.accountNumber = accountNumber;
	}

	public ClientNew getClientNew() {
		return clientNew;
	}

	public void setClientNew(ClientNew clientNew) {
		this.clientNew = clientNew;
	}

	public List<IncomingPayment> getIncomingpayment() {
		return incomingpayment;
	}

	public void setIncomingpayment(List<IncomingPayment> incomingpayment) {
		this.incomingpayment = incomingpayment;
	}
	
}
