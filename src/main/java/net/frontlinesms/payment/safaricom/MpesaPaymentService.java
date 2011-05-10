package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.StkInputRequiremnent;
import org.smslib.stk.StkMenu;
import org.smslib.stk.StkRequest;
import org.smslib.stk.StkResponse;

public abstract class MpesaPaymentService implements PaymentService,
		EventObserver {
	// > CONSTANTS
	protected static final String AMOUNT_PATTERN = "Ksh[,|[0-9]]+";
	protected static final String DATETIME_PATTERN = "d/M/yy hh:mm a";
	protected static final String PHONE_PATTERN = "2547[0-9]{8}";
	protected static final String CONFIRMATION_CODE_PATTERN = "[A-Z0-9]+ Confirmed.";
	protected static final String PAID_BY_PATTERN = "([A-Za-z ]+)";
	protected static final String ACCOUNT_NUMBER_PATTERN = "Account Number [0-9]+";
	protected static final String RECEIVED_FROM = "received from";

	// > INSTANCE PROPERTIES
	private final Logger log = FrontlineUtils.getLogger(this.getClass());
	private CService cService;
	private String pin;
	AccountDao accountDao;
	ClientDao clientDao;
	IncomingPaymentDao incomingPaymentDao;

	public void setCService(CService cService) {
		this.cService = cService;
	}

	public void setPin(String pin) {
		this.pin = pin;
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
			StkResponse finalResponse = cService.stkRequest(
					pinRequired.getRequest(), this.pin);
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
	
	@SuppressWarnings("unchecked")
	public void notify(FrontlineEventNotification notification) {
		if (!(notification instanceof EntitySavedNotification)) {
			return;
		}

		Object entity = ((EntitySavedNotification) notification)
				.getDatabaseEntity();
		if (!(entity instanceof FrontlineMessage)) {
			return;
		}

		final FrontlineMessage message = (FrontlineMessage) entity;

		if (!messageIsValid(message)) {
			return;
		}

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

	private boolean messageIsValid(FrontlineMessage message) {
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
	
	public String toString(){
		return "MPesa Safaricom - Kenya: Abstract Payment Service";
	}
}