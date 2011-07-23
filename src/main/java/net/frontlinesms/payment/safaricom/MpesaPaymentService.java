package net.frontlinesms.payment.safaricom;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventBus;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.PaymentJob;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.userhomepropeties.payment.balance.Balance;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.handler.ATHandler.SynchronizedWorkflow;
import org.smslib.stk.StkConfirmationPrompt;
import org.smslib.stk.StkMenu;
import org.smslib.stk.StkRequest;
import org.smslib.stk.StkResponse;
import org.smslib.stk.StkValuePrompt;

public abstract class MpesaPaymentService implements PaymentService, EventObserver  {
//> REGEX PATTERN CONSTANTS
	protected static final String AMOUNT_PATTERN = "Ksh[,|.|\\d]+";
	protected static final String SENT_TO = " sent to";
	protected static final String DATETIME_PATTERN = "d/M/yy hh:mm a";
	protected static final String PHONE_PATTERN = "2547[\\d]{8}";
	protected static final String CONFIRMATION_CODE_PATTERN = "[A-Z0-9]+ Confirmed.";
	protected static final String PAID_BY_PATTERN = "([A-Za-z ]+)";
	protected static final String ACCOUNT_NUMBER_PATTERN = "Account Number [\\d]+";
	protected static final String RECEIVED_FROM = "received from";

//> INSTANCE PROPERTIES
	protected Logger pvLog;
	private CService cService;

	//> DAOs
	AccountDao accountDao;
	ClientDao clientDao;
	TargetDao targetDao;
	IncomingPaymentDao incomingPaymentDao;
	OutgoingPaymentDao outgoingPaymentDao;
	
//> FIELDS
	private String pin;
	protected Balance balance;
	private EventBus eventBus;
	private TargetAnalytics targetAnalytics;
	
//> STK & PAYMENT ACCOUNT
	public void checkBalance() throws PaymentServiceException, IOException {
		initIfRequired();
		try {
			final String pin = this.pin;
			this.cService.doSynchronized(new SynchronizedWorkflow<Object>() {
				public Object run() throws SMSLibDeviceException, IOException {
					try {
						final StkMenu mPesaMenu = getMpesaMenu();
						final StkMenu myAccountMenu = (StkMenu) cService.stkRequest(mPesaMenu.getRequest("My account"));
						final StkResponse getBalanceResponse = cService.stkRequest(myAccountMenu.getRequest("Show balance"));
						assert getBalanceResponse instanceof StkValuePrompt;
						final StkValuePrompt pinRequired = (StkValuePrompt) getBalanceResponse;
						assert pinRequired.getPromptText().contains("Enter PIN");
						cService.stkRequest(pinRequired.getRequest(), pin);
						return null;
					} catch(final PaymentServiceException ex) {
						throw new SMSLibDeviceException(ex);
					}
				}
			});
			// TODO check finalResponse is OK
			// TODO wait for response...
		} catch (final SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		} catch (final IOException e) {
			throw new PaymentServiceException(e);
		}
	}

	public void makePayment(final Client client, final BigDecimal amount) throws PaymentServiceException {
		initIfRequired();
		try {
			final StkMenu mPesaMenu = getMpesaMenu();
			final StkResponse sendMoneyResponse = cService.stkRequest(mPesaMenu.getRequest("Send money"));

			StkValuePrompt enterPhoneNumberPrompt;
			if(sendMoneyResponse instanceof StkMenu) {
				enterPhoneNumberPrompt = (StkValuePrompt) cService.stkRequest(((StkMenu) sendMoneyResponse).getRequest("Enter phone no."));
			} else {
				enterPhoneNumberPrompt = (StkValuePrompt) sendMoneyResponse;
			}

			final StkResponse enterPhoneNumberResponse = cService.stkRequest(enterPhoneNumberPrompt.getRequest(), client.getPhoneNumber());
			if(!(enterPhoneNumberResponse instanceof StkValuePrompt)) throw new RuntimeException("Phone number rejected");
			
			final StkResponse enterAmountResponse = cService.stkRequest(((StkValuePrompt) enterPhoneNumberResponse).getRequest(), amount.toString());
			if(!(enterAmountResponse instanceof StkValuePrompt)) throw new RuntimeException("amount rejected");
			
			final StkResponse enterPinResponse = cService.stkRequest(((StkValuePrompt) enterAmountResponse).getRequest(), this.pin);
			if(!(enterPinResponse instanceof StkConfirmationPrompt)) throw new RuntimeException("PIN rejected");
			
			final StkResponse confirmationResponse = cService.stkRequest(((StkConfirmationPrompt) enterPinResponse).getRequest());
			if(confirmationResponse == StkResponse.ERROR) throw new RuntimeException("Payment failed for some reason.");
			//If I got Here, it means that I was successful, Right?
		} catch (final SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		} catch (final IOException e) {
			throw new PaymentServiceException(e);
		}
	}

//> EVENTBUS NOTIFY
	@SuppressWarnings("rawtypes")
	public void notify(final FrontlineEventNotification notification) {
		if (!(notification instanceof EntitySavedNotification)) {
			return;
		}
		
		//And is of a saved message
		final Object entity = ((EntitySavedNotification) notification).getDatabaseEntity();
		if (!(entity instanceof FrontlineMessage)) {
			return;
		}
		final FrontlineMessage message = (FrontlineMessage) entity;
		processMessage(message);
	}

	protected void processMessage(final FrontlineMessage message) {
		//I have overrided this function...
		if (isValidIncomingPaymentConfirmation(message)) {
			processIncomingPayment(message);
		}else if (isValidBalanceMessage(message)){
			processBalance(message);
		} 
	}

//> INCOMING MESSAGE PAYMENT PROCESSORS
	private void processIncomingPayment(final FrontlineMessage message) {
		new PaymentJob() {
			// This probably shouldn't be a UI job,
			// but it certainly should be done on a separate thread!
			public void run() {
				try {
					final IncomingPayment payment = new IncomingPayment();
					
					// retrieve applicable account if the client exists
					Account account = getAccount(message);
					
					if (account != null){
						final Target tgt = targetDao.getActiveTargetByAccount(account.getAccountNumber());
						if (tgt != null){//account is a non generic one
							payment.setAccount(account);
							payment.setTarget(tgt);
							payment.setPhoneNumber(getPhoneNumber(message));
							payment.setAmountPaid(getAmount(message));
							payment.setConfirmationCode(getConfirmationCode(message));
							payment.setPaymentBy(getPaymentBy(message));
							payment.setTimePaid(getTimePaid(message));
							
							incomingPaymentDao.saveIncomingPayment(payment);

							// Check if the client has reached his targeted amount
							if (targetAnalytics.getStatus(tgt.getId()) == TargetAnalytics.Status.COMPLETED){
								//Update target.completedDate
								final Calendar calendar = Calendar.getInstance();
								tgt.setCompletedDate(calendar.getTime());
								targetDao.updateTarget(tgt);
								// Update account.activeAccount
								payment.getAccount().setActiveAccount(false);
								accountDao.updateAccount(payment.getAccount());
							}

						} else {
							//account is a generic one(standard) or a non-generic without any active target(paybill)
							payment.setAccount(account);
							payment.setTarget(null);
							payment.setPhoneNumber(getPhoneNumber(message));
							payment.setAmountPaid(getAmount(message));
							payment.setConfirmationCode(getConfirmationCode(message));
							payment.setPaymentBy(getPaymentBy(message));
							payment.setTimePaid(getTimePaid(message));
							
							performIncominPaymentFraudCheck(message, payment);
							
							incomingPaymentDao.saveIncomingPayment(payment);
						}
					} else {
						// paybill - account does not exist (typing error) but client exists
						if (clientDao.getClientByPhoneNumber(getPhoneNumber(message))!=null){
							//save the incoming payment in generic account
							account = accountDao.getGenericAccountsByClientId(clientDao.getClientByPhoneNumber(getPhoneNumber(message)).getId());
							pvLog.warn("The account does not exist for this client. Incoming payment has been saved in generic account. "+ message.getTextContent());
						} else {
							// client does not exist in the database -> create client and generic account
							final String paymentBy = getPaymentBy(message);
							final String[] names = paymentBy.split(" ");
							String firstName = "";
							String otherName = "";
							if (names.length == 2){
							firstName = paymentBy.split(" ")[0];
							otherName = paymentBy.split(" ")[1];
							} else {
								otherName = paymentBy;
							}
							final Client client = new Client(firstName,otherName,getPhoneNumber(message));
							clientDao.saveClient(client);
							account = new Account(createAccountNumber(),client,false,true);
							accountDao.saveAccount(account);
						}
						
						payment.setAccount(account);
						payment.setTarget(null);
						payment.setPhoneNumber(getPhoneNumber(message));
						payment.setAmountPaid(getAmount(message));
						payment.setConfirmationCode(getConfirmationCode(message));
						payment.setPaymentBy(getPaymentBy(message));
						payment.setTimePaid(getTimePaid(message));
						incomingPaymentDao.saveIncomingPayment(payment);
					}
				} catch (final IllegalArgumentException ex) {
					pvLog.warn("Message failed to parse; likely incorrect format", ex);
					throw new RuntimeException(ex);
				} catch (final Exception ex) {
					pvLog.error("Unexpected exception parsing incoming payment SMS.", ex);
					throw new RuntimeException(ex);
				}
			}
		}.execute();
	}
	
	synchronized void performIncominPaymentFraudCheck(final FrontlineMessage message,
			final IncomingPayment payment) {
		//check is: Let Previous Balance be p, Current Balance be c and Amount received be a
		final BigDecimal currentBalance = getBalance(message);
		BigDecimal expectedBalance = payment.getAmountPaid().add(balance.getBalanceAmount());
		
		//c == p + a
		informUserOnFraud(currentBalance, expectedBalance, !currentBalance.equals(expectedBalance));
		
		balance.setBalanceAmount(currentBalance);
		balance.setConfirmationMessage(payment.getConfirmationCode());
		balance.setDateTime(new Date(payment.getTimePaid()));
		balance.setBalanceUpdateMethod("IncomingPayment");
		
		balance.updateBalance();
	}
	
	void informUserOnFraud(BigDecimal expected, BigDecimal actual, boolean fraudCommited) {
		if (fraudCommited) {
			pvLog.warn("Fraud commited? Was Expecting: "+expected+", But was "+actual);
		}else{
			pvLog.info("No Fraud occured!");
		}
	}
	
	private boolean isValidIncomingPaymentConfirmation(final FrontlineMessage message) {
		if (!message.getSenderMsisdn().equals("MPESA")) {
			return false;
		}
		return isMessageTextValid(message.getTextContent());
	}
	
	abstract void processBalance(FrontlineMessage message);
	abstract Date getTimePaid(FrontlineMessage message);
	abstract boolean isMessageTextValid(String message);
	abstract Account getAccount(FrontlineMessage message);
	abstract String getPaymentBy(FrontlineMessage message);
	protected abstract boolean isValidBalanceMessage(FrontlineMessage message);
	
	BigDecimal getAmount(final FrontlineMessage message) {
		final String amountWithKsh = getFirstMatch(message, AMOUNT_PATTERN);
		return new BigDecimal(amountWithKsh.substring(3).replaceAll(",", ""));
	}

	BigDecimal getBalance(final FrontlineMessage message) {
		try {
	        final String balance_part = getFirstMatch(message, "balance is Ksh[,|.|\\d]+");
	        final String amountWithKsh = balance_part.split("balance is ")[1];
	        return new BigDecimal(amountWithKsh.substring(3).replaceAll(",", ""));
		} catch(final ArrayIndexOutOfBoundsException ex) {
		        throw new IllegalArgumentException(ex);
		}
	}	
	
	String getPhoneNumber(final FrontlineMessage message) {
		return "+" + getFirstMatch(message, PHONE_PATTERN);
	}

	String getConfirmationCode(final FrontlineMessage message) {
		final String firstMatch = getFirstMatch(message, CONFIRMATION_CODE_PATTERN);
		return firstMatch.replace(" Confirmed.", "").trim();
	}

	protected String getFirstMatch(final String string, final String regexMatcher) {
		final Matcher matcher = Pattern.compile(regexMatcher).matcher(string);
		matcher.find();
		return matcher.group();
	}

	protected String getFirstMatch(final FrontlineMessage message, final String regexMatcher) {
		return getFirstMatch(message.getTextContent(), regexMatcher);
	}
			
	@Override
	public boolean equals(final Object other) {
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

	private StkMenu getMpesaMenu() throws PaymentServiceException {
		try {
			final StkResponse stkResponse = cService.stkRequest(StkRequest.GET_ROOT_MENU);
			StkMenu rootMenu = null;
			
			if (stkResponse instanceof StkMenu) {
				rootMenu = (StkMenu) stkResponse;
			} else {
				throw new PaymentServiceException("StkResponse Error Returned.");
			}
			
			return  (StkMenu)cService.stkRequest(rootMenu.getRequest("M-PESA"));
		} catch (final SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		} catch (final IOException e) {
			throw new PaymentServiceException(e);
		}
	}

	
	private void initIfRequired() throws PaymentServiceException {
		// For now, we assume that init is always required.  If there is a clean way
		// of identifying when it is and is not, we should perhaps implement this.
		try {
			this.cService.getAtHandler().stkInit();
		} catch(final Exception ex) {
			throw new PaymentServiceException(ex);
		}
	}
	
//> DESTROY
	public void deinit(){
		eventBus.unregisterObserver(this);
	}
	
//> ACCESSORS
	public String getPin() {
		return pin;
	}
	
	public void registerToEventBus(final EventBus eventBus) {
		if (eventBus != null) {
			this.eventBus = eventBus;
			this.eventBus.registerObserver(this);
		}
	}

	public void setPin(final String pin) {
		this.pin = pin;
	}
	
	public void setCService(final CService cService) {
		this.cService = cService;
	}
	
	public Balance getBalance() {
		return balance;
	}
	
	public void initDaosAndServices(final PaymentViewPluginController pluginController) {
		this.accountDao = pluginController.getAccountDao();
		this.clientDao = pluginController.getClientDao();
		this.outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		this.targetDao = pluginController.getTargetDao();
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.targetAnalytics = pluginController.getTargetAnalytics();
		
		this.balance = Balance.getInstance().getLatest();
		this.balance.setEventBus(pluginController.getUiGeneratorController()
				.getFrontlineController().getEventBus());
		//Would like to test using the log...
		this.pvLog = pluginController.getLogger(this.getClass());
	}
	
	/**
	 * 
	 * @return a generic account number
	 */
	public String createAccountNumber(){
		int accountNumberGenerated = this.accountDao.getAccountCount()+1;
		String accountNumberGeneratedStr = String.format("%05d", accountNumberGenerated);
		while (this.accountDao.getAccountByAccountNumber(accountNumberGeneratedStr) != null){
			accountNumberGeneratedStr = String.format("%05d", ++ accountNumberGenerated);
		}
		return accountNumberGeneratedStr;
	}
}