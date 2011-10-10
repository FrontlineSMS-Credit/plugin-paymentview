package org.creditsms.plugins.paymentview.userhomepropeties.payment.balance;

import java.math.BigDecimal;
import java.util.Date;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.PaymentService;

import org.creditsms.plugins.paymentview.utils.PvUtils;

public class Balance {
	private String confirmationCode;
	private BigDecimal balanceAmount;
	private Date dateTime;

	private EventBus eventBus;
	private String balanceUpdateMethod;
	
	private PaymentService paymentService;

	public Balance(){}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public BigDecimal getBalanceAmount() {
		return balanceAmount;
	}

	public void setBalanceAmount(BigDecimal balanceAmount) {
		this.balanceAmount = balanceAmount;
	}
	
	public void setBalanceAmount(String balanceAmount) {
		setBalanceAmount(new BigDecimal(balanceAmount));
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public synchronized Balance getLatest() {
		try {
			return BalanceProperties.getInstance().getBalance(this.paymentService);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error Reading the balance details.");
	}

	public void updateBalance() {
		BalanceProperties.getInstance().updateBalance(this);
		if (eventBus != null){
			eventBus.notifyObservers(new BalanceEventNotification(this.paymentService));
		}
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
		private final PaymentService _paymentService;
		public BalanceEventNotification(PaymentService paymentService) {
			_paymentService = paymentService;
		}
		public String getMessage() {
			return String.format("%s New Balance is: %s", Balance.this.getConfirmationCode(), 
					Balance.this.getBalanceAmount().toString());
		}
		
		public PaymentService getPaymentService() {
			return _paymentService;
		}
	}

	public void reset() {
		setBalanceAmount("0");
		setDateTime(new Date(0));
		setConfirmationCode("");
		setBalanceUpdateMethod("");
		
		updateBalance();
	}

	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	public PaymentService getPaymentService() {
		return paymentService;
	}
}
