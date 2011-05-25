package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.StkInputRequiremnent;
import org.smslib.stk.StkMenu;
import org.smslib.stk.StkRequest;
import org.smslib.stk.StkResponse;

public abstract class MpesaPaymentService implements PaymentService, EventObserver  {
//> REGEX PATTERN CONSTANTS
	private static final String PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN = 
		"[A-Z\\d]+ Confirmed.\n"+
		"Ksh[,|\\d]+ sent to ([A-Za-z ]+) 07[\\d]{8} on " +
		"(([1-2]?[1-9]|3[0-1])/([1-9]|1[0-2])/(1[1-2])) at ([1]?\\d:[0-5]\\d) (AM|PM)"+
		"New M-PESA balance is Ksh([,|\\d]+)";
	
	protected static final String AMOUNT_PATTERN = "Ksh[,|\\d]+";
	protected static final String DATETIME_PATTERN = "d/M/yy hh:mm a";
	protected static final String PHONE_PATTERN = "2547[\\d]{8}";
	protected static final String CONFIRMATION_CODE_PATTERN = "[A-Z0-9]+ Confirmed.";
	protected static final String PAID_BY_PATTERN = "([A-Za-z ]+)";
	protected static final String ACCOUNT_NUMBER_PATTERN = "Account Number [\\d]+";
	protected static final String RECEIVED_FROM = "received from";

//> INSTANCE PROPERTIES
	private final Logger log = FrontlineUtils.getLogger(this.getClass());
	private SmsService smsService;
	private CService cService;
	
//> DAOs
	AccountDao accountDao;
	ClientDao clientDao;
	IncomingPaymentDao incomingPaymentDao;
	OutgoingPaymentDao outgoingPaymentDao;
	
//> FIELDS
	private String pin;
	
	public MpesaPaymentService(EventBus eventBus){
		eventBus.registerObserver(this);
	}
	
	public MpesaPaymentService() {
	}
	
//> STK & PAYMENT ACCOUNT
	public void checkBalance() throws PaymentServiceException {
		try {
			StkMenu mPesaMenu = getMpesaMenu();
			StkMenu myAccountMenu = (StkMenu) cService.stkRequest(mPesaMenu
					.getRequest("My account"));
			StkResponse getBalanceResponse = cService.stkRequest(myAccountMenu
					.getRequest("Show balance"));
			assert getBalanceResponse instanceof StkInputRequiremnent;
			StkInputRequiremnent pinRequired = (StkInputRequiremnent) getBalanceResponse;
			assert pinRequired.getText().contains("Enter PIN");
			//StkResponse finalResponse = cService.stkRequest(
			//pinRequired.getRequest(), this.pin);
			// TODO check finalResponse is OK
			// TODO wait for response...
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

	private StkMenu getMpesaMenu() throws PaymentServiceException {
		try {
			StkMenu rootMenu = (StkMenu) cService
					.stkRequest(StkRequest.GET_ROOT_MENU);
			return (StkMenu) cService.stkRequest(rootMenu.getRequest("M-PESA"));
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

	public void makePayment(Account account, BigDecimal amount)
			throws PaymentServiceException {
		try {
			StkMenu mPesaMenu = getMpesaMenu();
			StkResponse sendMoneyResponse = cService.stkRequest(mPesaMenu.getRequest("Send money"));
			String phoneNumber = account.getClient().getPhoneNumber();

			StkRequest phoneNumberRequest = ((StkInputRequiremnent) sendMoneyResponse).getRequest();
			StkResponse phoneNumberResponse = cService.stkRequest(phoneNumberRequest, phoneNumber);

			StkRequest amountRequest = ((StkInputRequiremnent) phoneNumberResponse).getRequest();
			StkResponse amountResponse = cService.stkRequest(amountRequest,	amount.toString());

			StkRequest pinRequest = ((StkInputRequiremnent) amountResponse).getRequest();
			cService.stkRequest(pinRequest, this.pin);
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

//> EVENTBUS NOTIFY
	@SuppressWarnings("rawtypes")
	public void notify(FrontlineEventNotification notification) {
		//If the notification is of Importance to us
		if (!(notification instanceof EntitySavedNotification)) {
			return;
		}
		
		//And is of a saved message
		Object entity = ((EntitySavedNotification) notification)
				.getDatabaseEntity();
		if (!(entity instanceof FrontlineMessage)) {
			return;
		}
		final FrontlineMessage message = (FrontlineMessage) entity;
		
		if (isValidOutgoingPaymentConfirmation(message)){
			processOutgoingPayment(message);
		}else if (isValidIncomingPaymentConfirmation(message)) {
			processIncomingPayment(message);
		}
	}
 
	private boolean isValidOutgoingPaymentConfirmation(FrontlineMessage message) {
		if (!message.getSenderMsisdn().equals("MPESA")) {
			return false;
		}
		return message.getTextContent().matches(PERSONAL_OUTGOING_PAYMENT_REGEX_PATTERN);
	}

	private void processOutgoingPayment(final FrontlineMessage message) {
		new FrontlineUiUpateJob() {

			// This probably shouldn't be a UI job,
			// but it certainly should be done on a separate thread!
			public void run() {
				try {
					final OutgoingPayment payment = new OutgoingPayment();
					payment.setAccount(getAccount(message));
					payment.setPhoneNumber(getPhoneNumber(message));
					payment.setAmountPaid(getAmount(message));
					payment.setConfirmationCode(getConfirmationCode(message));
					payment.setPaymentTo(getPaymentBy(message));
					payment.setTimePaid(getTimePaid(message));
		
					outgoingPaymentDao.saveOutgoingPayment(payment);
				} catch (IllegalArgumentException ex) {
					log.warn("Message failed to parse; likely incorrect format", ex);
					throw new RuntimeException(ex);
				} catch (Exception ex) {
					log.error("Unexpected exception parsing incoming payment SMS.", ex);
					throw new RuntimeException(ex);
				}
			}
		}.execute();
	}
	
	//> INCOMING MESSAGE PAYMENT PROCESSORS
	private void processIncomingPayment(final FrontlineMessage message) {
		new FrontlineUiUpateJob() {
			// This probably shouldn't be a UI job,
			// but it certainly should be done on a separate thread!
			public void run() {
				try {
					final IncomingPayment payment = new IncomingPayment();
					payment.setAccount(getAccount(message));
					payment.setPhoneNumber(getPhoneNumber(message));
					payment.setAmountPaid(getAmount(message));
					payment.setConfirmationCode(getConfirmationCode(message));
					payment.setPaymentBy(getPaymentBy(message));
					payment.setTimePaid(getTimePaid(message));
		
					incomingPaymentDao.saveIncomingPayment(payment);
				} catch (IllegalArgumentException ex) {
					log.warn("Message failed to parse; likely incorrect format", ex);
					throw new RuntimeException(ex);
				} catch (Exception ex) {
					log.error("Unexpected exception parsing incoming payment SMS.", ex);
					throw new RuntimeException(ex);
				}
			}
		}.execute();
	}

	private boolean isValidIncomingPaymentConfirmation(FrontlineMessage message) {
		if (!message.getSenderMsisdn().equals("MPESA")) {
			return false;
		}
		return isMessageTextValid(message.getTextContent());
	}

	
	abstract Date getTimePaid(FrontlineMessage message);
	abstract boolean isMessageTextValid(String messageText);
	abstract Account getAccount(FrontlineMessage message);
	abstract String getPaymentBy(FrontlineMessage message);

	private BigDecimal getAmount(FrontlineMessage message) {
		String amountWithKsh = getFirstMatch(message, AMOUNT_PATTERN);
		return new BigDecimal(amountWithKsh.substring(3).replaceAll(",", ""));
	}

	String getPhoneNumber(FrontlineMessage message) {
		return "+" + getFirstMatch(message, PHONE_PATTERN);
	}

	private String getConfirmationCode(FrontlineMessage message) {
		String firstMatch = getFirstMatch(message, CONFIRMATION_CODE_PATTERN);
		return firstMatch.replace(" Confirmed.", "").trim();
	}

	protected String getFirstMatch(String string, String regexMatcher) {
		Matcher matcher = Pattern.compile(regexMatcher).matcher(string);
		matcher.find();
		return matcher.group();
	}

	protected String getFirstMatch(FrontlineMessage message, String regexMatcher) {
		return getFirstMatch(message.getTextContent(), regexMatcher);
	}
			
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof PaymentService)){
			return false;
		}
		
		if (!(other instanceof MpesaPaymentService)){
			return false;
		}
		
			
		if (other.getClass().getName().equals(this.getClass().getName())){ 
			return true;
		}
		
		return super.equals(other);
	}
	
//> ACCESSORS AND MUTATORS
	public String getPin() {
		return pin;
	}

	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public void setSmsService(SmsService smsService) {
		this.smsService = smsService;
	}

	public SmsService getSmsService(){
		return smsService;
	}
	
	public void setCService(CService cService) {
		this.cService = cService;
	}

	public CService getCService(){
		if (smsService != null & smsService instanceof SmsModem && cService == null){
			return ((SmsModem)smsService).getCService();
		}
		return cService;
	}
	
	public void setAccountDao(AccountDao accountDao) {
		this.accountDao = accountDao;
	}

	public void setIncomingPaymentDao(IncomingPaymentDao incomingPaymentDao) {
		this.incomingPaymentDao = incomingPaymentDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public void setOutgoingPaymentDao(OutgoingPaymentDao outgoingPaymentDao) {
		this.outgoingPaymentDao = outgoingPaymentDao;
	}
}