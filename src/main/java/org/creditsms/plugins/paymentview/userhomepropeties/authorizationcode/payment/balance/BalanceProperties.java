package org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.payment.balance;

import java.math.BigDecimal;
import java.util.Date;

import net.frontlinesms.resources.UserHomeFilePropertySet;

public class BalanceProperties extends UserHomeFilePropertySet {

	private static final BalanceProperties INSTANCE = new BalanceProperties();
	
	protected BalanceProperties() {
		super("paymentservice-balance");
	}
	
	public static BalanceProperties getInstance() {
		return INSTANCE;
	}
	
	public BigDecimal getBalance(){
		String balance = super.getProperty("balance");
		if (balance == null){
			return new BigDecimal("0");
		}
		return new BigDecimal(balance);
	}

	public void setBalance(BigDecimal balance) {
		this.setProperty("balance", balance.toString());
		this.saveToDisk();
	}

	public void setConfirmationCode(String confirmationCode) {
		this.setProperty("confirmation_code", confirmationCode);
		this.saveToDisk();
	}

	public void setDateTime(Date datetime) {
		this.setProperty("datetime", datetime.toString());
		this.saveToDisk();
	}

}
