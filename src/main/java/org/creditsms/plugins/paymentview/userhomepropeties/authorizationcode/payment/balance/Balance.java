package org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.payment.balance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

public class Balance {
	private String confirmationMessage;
	private BigDecimal balance;
	private Date dateTime;
	
	public Balance() {
		// TODO Auto-generated constructor stub
	}

	public String getConfirmationMessage() {
		return confirmationMessage;
	}

	public void setConfirmationMessage(String confirmationMessage) {
		this.confirmationMessage = confirmationMessage;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	public Balance getLatest(){
		try {
			return BalanceProperties.getInstance().getBalance();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error Reading the balance details.");
	}
	
	public void updateBalance() {
		BalanceProperties.getInstance().setBalance(this);
	}
	
}
