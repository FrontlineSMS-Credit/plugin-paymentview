package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.IncomingPaymentProcessor;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.StkInputRequiremnent;
import org.smslib.stk.StkMenu;
import org.smslib.stk.StkRequest;
import org.smslib.stk.StkResponse;

public abstract class MpesaPaymentService implements PaymentService, EventObserver {
	private CService cService;
	private IncomingPaymentProcessor incomingPaymentProcessor;
	private String pin;
	AccountDao accountDao;
	ClientDao clientDao;
	
	public void setCService(CService cService) {
		this.cService = cService;
	}
	
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}
	
	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}
	
	public void checkBalance() throws PaymentServiceException {
		try {
			StkMenu mPesaMenu = getMpesaMenu();
			StkMenu myAccountMenu = (StkMenu) cService.stkRequest(mPesaMenu.getRequest("My account"));
			StkResponse getBalanceResponse = cService.stkRequest(myAccountMenu.getRequest("Show balance"));
			assert getBalanceResponse instanceof StkInputRequiremnent;
			StkInputRequiremnent pinRequired = (StkInputRequiremnent) getBalanceResponse;
			assert pinRequired.getText().contains("Enter PIN");
			StkResponse finalResponse = cService.stkRequest(pinRequired.getRequest(), this.pin);
			// TODO check finalResponse is OK
			// TODO wait for response...
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

	private StkMenu getMpesaMenu() throws PaymentServiceException {
		try {
			StkMenu rootMenu = (StkMenu) cService.stkRequest(StkRequest.GET_ROOT_MENU);
			return (StkMenu) cService.stkRequest(rootMenu.getRequest("M-PESA"));
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

	public void makePayment(Account account, BigDecimal amount) throws PaymentServiceException {
		try {
			StkMenu mPesaMenu = getMpesaMenu();
			StkResponse sendMoneyResponse = cService.stkRequest(mPesaMenu.getRequest("Send money"));
			String phoneNumber = account.getClient().getPhoneNumber();
			
			StkRequest phoneNumberRequest = ((StkInputRequiremnent) sendMoneyResponse).getRequest();
			StkResponse phoneNumberResponse = cService.stkRequest(phoneNumberRequest, phoneNumber);
			
			StkRequest amountRequest = ((StkInputRequiremnent) phoneNumberResponse).getRequest();
			StkResponse amountResponse = cService.stkRequest(amountRequest, amount.toString());
			
			StkRequest pinRequest = ((StkInputRequiremnent) amountResponse).getRequest();
			cService.stkRequest(pinRequest, this.pin);
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

	public void setIncomingPaymentProcessor(IncomingPaymentProcessor incomingPaymentProcessor) {
		this.incomingPaymentProcessor = incomingPaymentProcessor;
	}

	@SuppressWarnings("unchecked")
	public void notify(FrontlineEventNotification notification) {
		if(!(notification instanceof EntitySavedNotification)) {
			return;
		}
		
		Object entity = ((EntitySavedNotification) notification).getDatabaseEntity();
		if(!(entity instanceof FrontlineMessage)) {
			return;
		}
		
		final FrontlineMessage message = (FrontlineMessage) entity;
		if(!message.getSenderMsisdn().equals("MPESA")) {
			return;
		}
		
		if(!messageIsValid(message)){
			return;
		}
		
		try {
			new FrontlineUiUpateJob() { // This probably shouldn't be a UI job, but it certainly should be done on a separate thread!
				public void run() {
					final IncomingPayment payment = new IncomingPayment();
					try {
						payment.setAccount(getAccount(message));
						payment.setPhoneNumber(getPhoneNumber(message));
						payment.setAmountPaid(getAmount(message));
						payment.setConfirmationCode(getConfirmationCode(message));
						payment.setPaymentBy(getPaymentBy(message));
						payment.setTimePaid(getTimePaid(message));
					} catch (Exception e) {
						e.printStackTrace();
					}
					incomingPaymentProcessor.process(payment);
				}
			}.execute();
		} catch(IllegalArgumentException ex) {
			// Message failed to parse; likely incorrect format
			return;
		}
	}
	
	private boolean messageIsValid(FrontlineMessage message) {
		String txt = message.getTextContent();
		
		try{
			String confirmationCode = getConfirmationCode(message);
			if (confirmationCode.isEmpty()){
				return false;
			}
		}catch(Exception ae){
			return false;
		}		
		//FIXME: have a more robust way of checking the message.
		return true;
	}

	abstract Date getTimePaid(FrontlineMessage message);

	private BigDecimal getAmount(FrontlineMessage message) {
		String amountWithKsh = getFirstMatch(message, "Ksh[0-9,]+");
		return new BigDecimal(amountWithKsh.substring(3).replaceAll(",", ""));
	}

	String getPhoneNumber(FrontlineMessage message) {
		return "+" + getFirstMatch(message, "2547[0-9]{8}");
	}

	private String getConfirmationCode(FrontlineMessage message) {
		String firstMatch = getFirstMatch(message, "[A-Z0-9]+ Confirmed.");
		return firstMatch.replace(" Confirmed.", "").trim();
	}

	abstract Account getAccount(FrontlineMessage message);

	protected String getFirstMatch(FrontlineMessage message, String regexMatcher) {
		String messageText = message.getTextContent();
		Matcher matcher = Pattern.compile(regexMatcher).matcher(messageText);
		matcher.find();
		return matcher.group();
	}

	private String getPaymentBy(FrontlineMessage message) {
		try {
	        String nameAndPhone = getFirstMatch(message, "[A-Z ]+ 2547[0-9]{8}");
	        String names = nameAndPhone.split("2547[0-9]{8}")[0].trim();
	        return names;
		} catch(ArrayIndexOutOfBoundsException ex) {
		        throw new IllegalArgumentException(ex);
		}
	}
}