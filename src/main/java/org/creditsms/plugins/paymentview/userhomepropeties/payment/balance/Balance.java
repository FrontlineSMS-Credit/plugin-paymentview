package org.creditsms.plugins.paymentview.userhomepropeties.payment.balance;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.utils.PvUtils;

public class Balance {
	private String confirmationCode;
	private BigDecimal balanceAmount;
	private Date dateTime;

	private static final Balance INSTANCE = new Balance();
	private UiGeneratorController uiController;
	private String balanceUpdateMethod;
	private BigDecimal nextExpectedBalance;

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

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public synchronized Balance getLatest() {
		try {
			return BalanceProperties.getInstance().getBalance();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error Reading the balance details.");
	}

	public void updateBalance() {
		BalanceProperties.getInstance().updateBalance();
		if (uiController != null){
			this.uiController.getFrontlineController().getEventBus()
				.notifyObservers(new BalanceEventNotification());
		}
	}

	public static Balance getInstance() {
		return INSTANCE;
	}

	public void setUiController(UiGeneratorController uiController) {
		this.uiController = uiController;
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
	
	public void setNextExpectedBalance(BigDecimal nextExpectedBalance) {
		this.nextExpectedBalance = nextExpectedBalance;
	}
	
	public BigDecimal getNextExpectedBalance() {
		return nextExpectedBalance;
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
}
