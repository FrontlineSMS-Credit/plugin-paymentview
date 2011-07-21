package org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.payment.balance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.frontlinesms.resources.UserHomeFilePropertySet;


public class BalanceProperties extends UserHomeFilePropertySet {

	private static final String DATETIME_KEY = "datetime";
	private static final String BALANCE_AMOUNT_KEY = "balance";
	private static final String CONFIRMATION_CODE_KEY = "confirmation_code";
	private static final String DATETIME_PATTERN = "d/M/yy hh:mm a";
	private static final SimpleDateFormat SDF = new SimpleDateFormat(DATETIME_PATTERN);
	private static final BalanceProperties INSTANCE = new BalanceProperties();
	
	protected BalanceProperties() {
		super("paymentservice-balance");
	}
	
	public static BalanceProperties getInstance() {
		return INSTANCE;
	}
	
	public Balance getBalance() throws ParseException{
		if (super.getProperty(BALANCE_AMOUNT_KEY) == null){
			return createBalanceObject("0", "", null);
		}
		return createBalanceObject(getProperty(BALANCE_AMOUNT_KEY), getProperty(DATETIME_KEY), getPropertyAsDate(CONFIRMATION_CODE_KEY));
	}
	
	private Date getPropertyAsDate(String key) throws ParseException {
		return SDF.parse(getProperty(key));
	}

	private Balance createBalanceObject(String balance_amount, String confirmation_code, Date datetime){
		Balance balance = new Balance();
		balance.setBalance(new BigDecimal(balance_amount));
		balance.setConfirmationMessage(confirmation_code);
		balance.setDateTime(datetime);
		return balance;
	}

	public void setBalance(Balance balance) {
		this.setBalanceAmount(balance.getBalance());
		this.setConfirmationCode(balance.getConfirmationMessage());
		this.setDateTime(balance.getDateTime());
		//Now Save to Disk...
		this.saveToDisk();
	}

	private void setBalanceAmount(BigDecimal amount) {
		this.setProperty(BALANCE_AMOUNT_KEY, amount.toString());
	}
	
	private void setConfirmationCode(String confirmationCode) {
		this.setProperty(CONFIRMATION_CODE_KEY, confirmationCode);
	}

	private void setDateTime(Date datetime) {
		this.setProperty(DATETIME_KEY, SDF.format(datetime));
	}
}
