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
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.LogMessageDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.userhomepropeties.payment.balance.Balance;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.handler.ATHandler.SynchronizedWorkflow;
import org.smslib.stk.StkConfirmationPrompt;
import org.smslib.stk.StkMenu;
import org.smslib.stk.StkMenuItemNotFoundException;
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
	
	
	private static final int BALANCE_ENQUIRY_CHARGE = 1;
	private static final BigDecimal BD_BALANCE_ENQUIRY_CHARGE = new BigDecimal(BALANCE_ENQUIRY_CHARGE);
	
	private static final String STR_REVERSE_REGEX_PATTERN = 
		"[A-Z0-9]+ Confirmed.\n"
		+"Transaction [A-Z0-9]+\n"
		+"has been reversed. Your\n"
		+"account balance now\n"
		+"[,|\\d]+Ksh";

	private static final Pattern REVERSE_REGEX_PATTERN = Pattern.compile(STR_REVERSE_REGEX_PATTERN);

//> INSTANCE PROPERTIES
	protected Logger pvLog = Logger.getLogger(this.getClass());
	private CService cService;

	//> DAOs
	AccountDao accountDao;
	ClientDao clientDao;
	TargetDao targetDao;
	IncomingPaymentDao incomingPaymentDao;
	OutgoingPaymentDao outgoingPaymentDao;
	LogMessageDao logMessageDao;
	
//> FIELDS
	private String pin;
	protected Balance balance;
	protected EventBus eventBus;
	private TargetAnalytics targetAnalytics;
	
	public void configureModem() throws PaymentServiceException {
		try{
			this.cService.doSynchronized(new SynchronizedWorkflow<Object>() {
				public Object run() throws SMSLibDeviceException, IOException {
					cService.getAtHandler().configureModem();
					return null;
				}
			});
		} catch (final SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		} catch (final IOException e) {
			throw new PaymentServiceException(e);
		} catch (RuntimeException e) {
			throw new PaymentServiceException("PIN rejected");
		}
		
	}	
	
	//configureModem
	
	
//> STK & PAYMENT ACCOUNT
	public void checkBalance() throws PaymentServiceException {
		try {
			final String pin = this.pin;
			this.cService.doSynchronized(new SynchronizedWorkflow<Object>() {
				public Object run() throws SMSLibDeviceException, IOException {
					initIfRequired();
					final StkMenu mPesaMenu = getMpesaMenu();
					final StkMenu myAccountMenu = (StkMenu) cService.stkRequest(mPesaMenu.getRequest("My account"));
					final StkResponse getBalanceResponse = cService.stkRequest(myAccountMenu.getRequest("Show balance"));
					
					final StkResponse enterPinResponse = cService.stkRequest(((StkValuePrompt) getBalanceResponse).getRequest(), pin);
					if(enterPinResponse == StkResponse.ERROR) throw new RuntimeException("PIN rejected");
					
					return null;
				}
			});
			// TODO check finalResponse is OK
			// TODO wait for response...
		} catch (final SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		} catch (final IOException e) {
			throw new PaymentServiceException(e);
		} catch (RuntimeException e) {
			throw new PaymentServiceException("PIN rejected");
		}
	}

	public void makePayment(final Client client, final BigDecimal amount) throws PaymentServiceException {
		try {
			this.cService.doSynchronized(new SynchronizedWorkflow<Object>() {
				public Object run() throws SMSLibDeviceException, IOException {
					initIfRequired();
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
					
					final StkResponse enterPinResponse = cService.stkRequest(((StkValuePrompt) enterAmountResponse).getRequest(), pin);
					if(!(enterPinResponse instanceof StkConfirmationPrompt)) throw new RuntimeException("PIN rejected");
					
					final StkResponse confirmationResponse = cService.stkRequest(((StkConfirmationPrompt) enterPinResponse).getRequest());
					if(confirmationResponse == StkResponse.ERROR) throw new RuntimeException("Payment failed for some reason.");
					
					return null;
				}
			});
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
		//I have overridden this function...
		if (isValidIncomingPaymentConfirmation(message)) {
			processIncomingPayment(message);
		}else if (isValidBalanceMessage(message)){
			processBalance(message);
		}else if (isValidReverseMessage(message)){
			processReversePayment(message);
		} else {
			// Message is invalid
			logMessageDao.saveLogMessage(new LogMessage(LogMessage.LogLevel.ERROR,"Payment Message: Invalid message",message.getTextContent()));
		}
	}

	//> INCOMING MESSAGE PAYMENT PROCESSORS
	private void processIncomingPayment(final FrontlineMessage message) {
		new PaymentJob() {
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
							
							performIncominPaymentFraudCheck(message, payment);
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
						
						performIncominPaymentFraudCheck(message, payment);
						incomingPaymentDao.saveIncomingPayment(payment);
					}
					
					//log the saved incoming payment
					logMessageDao.saveLogMessage(
							new LogMessage(LogMessage.LogLevel.INFO,
										   	"Incoming Payment",
										   	message.getTextContent()));
				} catch (final IllegalArgumentException ex) {
					logMessageDao.saveLogMessage(
							new LogMessage(LogMessage.LogLevel.ERROR,
										   	"Incoming Payment: Message failed to parse; likely incorrect format",
										   	 message.getTextContent()));
					pvLog.warn("Message failed to parse; likely incorrect format", ex);
					throw new RuntimeException(ex);
				} catch (final Exception ex) {
					pvLog.error("Unexpected exception parsing incoming payment SMS.", ex);
					logMessageDao.saveLogMessage(
							new LogMessage(LogMessage.LogLevel.ERROR,
								   	"Incoming Payment: Unexpected exception parsing incoming payment SMS",
								   	message.getTextContent()));
					throw new RuntimeException(ex);
				}
			}
		}.execute();
	}
	
	private void processReversePayment(final FrontlineMessage message) {
		new FrontlineUiUpateJob() {
			public void run() {
				try {
					IncomingPayment incomingPayment = incomingPaymentDao.getByConfirmationCode(getReversedConfirmationCode(message));
					incomingPayment.setActive(false);
					
					performPaymentReversalFraudCheck(
						getConfirmationCode(message),
						incomingPayment.getAmountPaid(), 
						getReversedPaymentBalance(message),
						message
					);
					
					incomingPaymentDao.updateIncomingPayment(incomingPayment);
					logMessageDao.saveLogMessage(
							new LogMessage(LogMessage.LogLevel.INFO,"Reverse Transaction",message.getTextContent()));
				}catch (Exception e) {
					e.printStackTrace();
				}
			}
		}.execute();
	}
	
	protected void processBalance(final FrontlineMessage message){
		new PaymentJob() {
			public void run() {
				performBalanceEnquiryFraudCheck(message);
				logMessageDao.saveLogMessage(
						new LogMessage(LogMessage.LogLevel.INFO,"Check Balance Response",message.getTextContent()));
			}
		}.execute();
	}
	
	private void performPaymentReversalFraudCheck(String confirmationCode, BigDecimal amountPaid, BigDecimal actualBalance, final FrontlineMessage message) {
		BigDecimal expectedBalance = balance.getBalanceAmount().subtract(amountPaid);
		
		performInform(actualBalance, expectedBalance, message.getTextContent());
		
		balance.setBalanceAmount(actualBalance);
		balance.setBalanceUpdateMethod("PaymentReversal");
		balance.setDateTime(new Date());
		balance.setConfirmationCode(confirmationCode);
		
		balance.updateBalance();

	}

	private void performInform(BigDecimal actualBalance, BigDecimal expectedBalance, String messageContent) {
		if (expectedBalance.compareTo(new BigDecimal(0)) >= 0) {//Now we don't want Mathematical embarrassment...
			informUserOnFraud(expectedBalance, actualBalance, !expectedBalance.equals(actualBalance), messageContent);
		}else{
			pvLog.error("Balance is way low: than expected " + actualBalance + " instead of : "+ expectedBalance);
		}
	}

	private BigDecimal getReversedPaymentBalance(FrontlineMessage message) {
		String firstMatch = getFirstMatch(message, "[,|\\d]+Ksh");
		String strBalance = firstMatch.replace("Ksh", "").replace(",", "");
		return new BigDecimal(strBalance);
	}
	
	private String getReversedConfirmationCode(FrontlineMessage message) {
		String firstMatch = getFirstMatch(message, "Transaction [A-Z0-9]+");
		return firstMatch.replace("Transaction ", "").trim();
	}
	
	synchronized void performBalanceEnquiryFraudCheck(final FrontlineMessage message) {
		BigDecimal tempBalance = balance.getBalanceAmount();
		BigDecimal expectedBalance = tempBalance.subtract(BD_BALANCE_ENQUIRY_CHARGE);
		
		BigDecimal actualBalance = getAmount(message);
		performInform(actualBalance, expectedBalance, message.getTextContent());
		
		balance.setBalanceAmount(actualBalance);
		balance.setConfirmationCode(getConfirmationCode(message));
		balance.setDateTime(getTimePaid(message));
		balance.setBalanceUpdateMethod("BalanceEnquiry");
		balance.updateBalance();
	}
	
	synchronized void performIncominPaymentFraudCheck(final FrontlineMessage message,
			final IncomingPayment payment) {
		//check is: Let Previous Balance be p, Current Balance be c and Amount received be a
		final BigDecimal actualBalance = getBalance(message);
		BigDecimal expectedBalance = payment.getAmountPaid().add(balance.getBalanceAmount());
		
		//c == p + a
		performInform(actualBalance, expectedBalance, message.getTextContent());
		
		balance.setBalanceAmount(actualBalance);
		balance.setConfirmationCode(payment.getConfirmationCode());
		balance.setDateTime(new Date(payment.getTimePaid()));
		balance.setBalanceUpdateMethod("IncomingPayment");
		
		balance.updateBalance();
	}
	
	void informUserOnFraud(BigDecimal expected, BigDecimal actual, boolean fraudCommited, String messageContent) {
		if (fraudCommited) {
			String message = "Fraud commited? Was expecting balance as: "+expected+", But was "+actual;

			logMessageDao.saveLogMessage(
					new LogMessage(LogMessage.LogLevel.WARNING,
						   	"Fraud commited? Was expecting balance as: "+expected+", But was "+actual,
						    messageContent));
			pvLog.warn(message);
			this.eventBus.notifyObservers(new BalanceFraudNotification(message));
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
	
	private boolean isValidReverseMessage(FrontlineMessage message) {
		return REVERSE_REGEX_PATTERN.matcher(message.getTextContent()).matches();
	}
	
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

	private StkMenu getMpesaMenu() throws StkMenuItemNotFoundException, SMSLibDeviceException, IOException {
		final StkResponse stkResponse = cService.stkRequest(StkRequest.GET_ROOT_MENU);
		StkMenu rootMenu = null;
		
		if (stkResponse instanceof StkMenu) {
			rootMenu = (StkMenu) stkResponse;
			return (StkMenu) cService.stkRequest(rootMenu.getRequest("M-PESA"));
		} else {
			throw new SMSLibDeviceException("StkResponse Error Returned.");
		}
	}

	private void initIfRequired() throws SMSLibDeviceException, IOException {
		// For now, we assume that init is always required.  If there is a clean way
		// of identifying when it is and is not, we should perhaps implement this.
		this.cService.getAtHandler().stkInit();
	}

//> DESTROY
	public void stop() {
		deinit();
		//Other stuff here
	}
	
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
		this.logMessageDao = pluginController.getLogMessageDao();
		this.balance = Balance.getInstance().getLatest();
		this.registerToEventBus(
			pluginController.getUiGeneratorController()
			.getFrontlineController().getEventBus()
		);
		
		this.balance.setEventBus(this.eventBus);
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
	
	public class BalanceFraudNotification implements FrontlineEventNotification{
		private final String message;
		public BalanceFraudNotification(String message){this.message=message;}
		public String getMessage(){return message;}
	}
}