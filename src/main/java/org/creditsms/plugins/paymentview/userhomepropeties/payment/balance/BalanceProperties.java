package org.creditsms.plugins.paymentview.userhomepropeties.payment.balance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import net.frontlinesms.resources.UserHomeFilePropertySet;

import org.creditsms.plugins.paymentview.utils.PvUtils;

public class BalanceProperties extends UserHomeFilePropertySet {

	private static final String BALANCE_UPDATE_METHOD_KEY = "balance_update_method";
	private static final String DATETIME_KEY = "datetime";
	private static final String BALANCE_AMOUNT_KEY = "balance";
	private static final String CONFIRMATION_CODE_KEY = "confirmation_code";
	private static final BalanceProperties INSTANCE = new BalanceProperties();
	
	protected BalanceProperties() {
		super("paymentservice-balance");
	}
	
	public static BalanceProperties getInstance() {
		return INSTANCE;
	}
	
	synchronized Balance getBalance() throws ParseException{
		if (super.getProperty(BALANCE_AMOUNT_KEY) == null){
			return createBalanceObject("0", "", null);
		}
		return createBalanceObject(getProperty(BALANCE_AMOUNT_KEY), getProperty(CONFIRMATION_CODE_KEY), getPropertyAsDate(DATETIME_KEY));
	}
	
	private synchronized Date getPropertyAsDate(String key) throws ParseException {
		return PvUtils.parseDate(getProperty(key));
	}

	private synchronized Balance createBalanceObject(String balance_amount, String confirmation_code, Date datetime){
		Balance balance = Balance.getInstance();
		balance.setBalanceAmount(new BigDecimal(balance_amount));
		balance.setConfirmationMessage(confirmation_code);
		balance.setDateTime(datetime);
		return balance;
	}

	synchronized void updateBalance() {
		Balance balance = Balance.getInstance();
		this.setBalanceAmount(balance.getBalanceAmount());
		this.setConfirmationCode(balance.getConfirmationCode());
		this.setDateTime(balance.getDateTime());
		this.setBalanceUpdateMethod(balance.getBalanceUpdateMethod());
		//Now Save to Disk...
		this.saveToDisk();
	}

	private void setBalanceUpdateMethod(String balanceUpdateMethod) {
		this.setProperty(BALANCE_UPDATE_METHOD_KEY, balanceUpdateMethod);
	}

	private synchronized void setBalanceAmount(BigDecimal amount) {
		this.setProperty(BALANCE_AMOUNT_KEY, amount.toString());
	}
	
	private synchronized void setConfirmationCode(String confirmationCode) {
		this.setProperty(CONFIRMATION_CODE_KEY, confirmationCode);
	}

	private synchronized void setDateTime(Date datetime) {
		this.setProperty(DATETIME_KEY, PvUtils.formatDate(datetime));
	}
}
