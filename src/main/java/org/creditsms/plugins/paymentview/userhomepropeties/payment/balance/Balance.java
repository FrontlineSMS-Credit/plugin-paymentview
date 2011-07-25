package org.creditsms.plugins.paymentview.userhomepropeties.payment.balance;

import java.math.BigDecimal;
import java.util.Date;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.FrontlineEventNotification;

import org.creditsms.plugins.paymentview.utils.PvUtils;

public class Balance {
	private String confirmationCode;
	private BigDecimal balanceAmount;
	private Date dateTime;

	private static final Balance INSTANCE = new Balance();
	private EventBus eventBus;
	private String balanceUpdateMethod;

	private Balance() {
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationMessage(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	
	public void setBalanceAmount(String balanceAmount) {
		this.balanceAmount = new BigDecimal(balanceAmount);
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public synchronized Balance getLatest() {
		try {
			return BalanceProperties.getInstance().getBalance();
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error Reading the balance details.");
	}

	public void updateBalance() {
		BalanceProperties.getInstance().updateBalance();
		if (eventBus != null){
			eventBus.notifyObservers(new BalanceEventNotification());
		}
	}

	public static Balance getInstance() {
		return INSTANCE;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
	
	@Override
	public String toString() {
		return "["+ this.confirmationCode + "] " +this.balanceAmount + " @ "+ PvUtils.formatDate(dateTime);
	}

	public void setBalanceUpdateMethod(String balanceUpdateMethod) {
		this.balanceUpdateMethod = balanceUpdateMethod; 
	}
	
	public String getBalanceUpdateMethod() {
		return balanceUpdateMethod;
	}
	
	public class BalanceEventNotification implements FrontlineEventNotification {
		public BalanceEventNotification() {
		}

		public String getMessage() {
			return String.format("%s New Balance is: %s", Balance.getInstance()
					.getConfirmationCode(), Balance.getInstance()
					.getBalanceAmount().toString());
		}
	}

	public void reset() {
		setBalanceAmount(new BigDecimal(0));
		setDateTime(new Date(0));
		setConfirmationMessage("");
		setBalanceUpdateMethod("");
		
		updateBalance();
	}
}
