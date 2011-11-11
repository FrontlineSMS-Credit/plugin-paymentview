//package org.creditsms.plugins.paymentview.userhomepropeties.payment.balance;
//
//import java.math.BigDecimal;
//import java.util.Date;
//
//import net.frontlinesms.payment.PaymentService;
//import net.frontlinesms.resources.UserHomeFilePropertySet;
//
//public class BalanceProperties extends UserHomeFilePropertySet {
//
//	private static final String BALANCE_UPDATE_METHOD_KEY = "balance_update_method";
//	private static final String DATETIME_KEY = "datetime";
//	private static final String BALANCE_AMOUNT_KEY = "balance";
//	private static final String CONFIRMATION_CODE_KEY = "confirmation_code";
//	private static final BalanceProperties INSTANCE = new BalanceProperties();
//	
//	protected BalanceProperties() {
//		super("paymentservice-balance");
//	}
//	
//	public static BalanceProperties getInstance() {
//		return INSTANCE;
//	}
//	
//	public synchronized Balance getBalance(PaymentService paymentService) throws NumberFormatException{
//		if (super.getProperty(getAmountKeyForPaymentService(paymentService)) == null){
//			return createBalanceObject("0", "", null, paymentService);
//		}
//		return createBalanceObject(
//				getProperty(getAmountKeyForPaymentService(paymentService)), 
//				getProperty(getConfirmationCodeForPaymentService(paymentService)), 
//				getPropertyAsDate(getDateTimeForPaymentService(paymentService)), paymentService);
//	}
//	
//	private String getDateTimeForPaymentService(PaymentService paymentService) {
//		return DATETIME_KEY +"-"+ paymentService.getSettings().getId();
//	}
//	
//	private String getConfirmationCodeForPaymentService(PaymentService paymentService) {
//		return CONFIRMATION_CODE_KEY +"-"+ paymentService.getSettings().getId();
//	}
//	
//	private String getAmountKeyForPaymentService(PaymentService paymentService) {
//		return BALANCE_AMOUNT_KEY +"-"+ paymentService.getSettings().getId();
//	}
//	
//	private String getBalanceUpdateMethodForPaymentService(PaymentService paymentService) {
//		return BALANCE_UPDATE_METHOD_KEY +"-"+ paymentService.getSettings().getId();
//	}
//	
//	private synchronized Date getPropertyAsDate(String key) throws NumberFormatException {
//		return new Date(Long.parseLong(getProperty(key)));
//	}
//	
//	private synchronized Balance createBalanceObject(String balance_amount, String confirmation_code, 
//			Date datetime, PaymentService paymentService){
//		Balance balance = new Balance();
//		balance.setBalanceAmount(new BigDecimal(balance_amount));
//		balance.setConfirmationCode(confirmation_code);
//		balance.setDateTime(datetime);
//		balance.setPaymentService(paymentService);
//		return balance;
//	}
//
//	synchronized void updateBalance(Balance balance) {
//		this.setBalanceAmount(balance);
//		this.setConfirmationCode(balance);
//		this.setDateTime(balance);
//		this.setBalanceUpdateMethod(balance);
//		//Now Save to Disk...
//		this.saveToDisk();
//	}
//
//	private void setBalanceUpdateMethod(Balance balance) {
//		this.setProperty(getBalanceUpdateMethodForPaymentService(balance.getPaymentService()), 
//				balance.getBalanceUpdateMethod());
//	}
//
//	private synchronized void setBalanceAmount(Balance balance) {
//		this.setProperty(getAmountKeyForPaymentService(balance.getPaymentService()), 
//				balance.getBalanceAmount().toString());
//	}
//	
//	private synchronized void setConfirmationCode(Balance balance) {
//		this.setProperty(getConfirmationCodeForPaymentService(balance.getPaymentService()), 
//				balance.getConfirmationCode());
//	}
//	
//	private synchronized void setDateTime(Balance balance) {
//		this.setProperty(getDateTimeForPaymentService(balance.getPaymentService()), 
//				Long.toString(balance.getDateTime().getTime()));
//	}
//}
