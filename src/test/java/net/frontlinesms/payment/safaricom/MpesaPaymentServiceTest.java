package net.frontlinesms.payment.safaricom;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.payment.IncomingPaymentProcessor;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.mockito.InOrder;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.StkInputRequiremnent;
import org.smslib.stk.StkMenu;
import org.smslib.stk.StkMenuItem;
import org.smslib.stk.StkRequest;

/** Unit tests for {@link MpesaPaymentService} */
public abstract class MpesaPaymentServiceTest<E extends MpesaPaymentService> extends BaseTestCase {
	private static final String PHONENUMBER_0 = "+254723908000";
	protected static final String PHONENUMBER_1 = "+254723908001";
	protected static final String PHONENUMBER_2 = "+254723908002";
	protected static final String ACCOUNTNUMBER_1_1 = "0700000011";
	protected static final String ACCOUNTNUMBER_2_1 = "0700000021";
	protected static final String ACCOUNTNUMBER_2_2 = "0700000022";
	
	private E safaricomPaymentService;
	private CService c;
	private IncomingPaymentProcessor incomingPaymentProcessor;
	
	private StkMenuItem myAccountMenuItem;
	private StkRequest mpesaMenuItemRequest;
	private StkMenuItem sendMoneyMenuItem;
	private ClientDao clientDao;
	private AccountDao accountDao;
	
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		
		this.safaricomPaymentService = createNewTestClass();
		this.c = mock(CService.class);
		safaricomPaymentService.setCService(c);
		incomingPaymentProcessor = mock(IncomingPaymentProcessor.class);
		safaricomPaymentService.setIncomingPaymentProcessor(incomingPaymentProcessor);
		
		setUpDaos();

		StkMenuItem mpesaMenuItem = mockMenuItem("M-PESA", 129, 21);
		when(c.stkRequest(StkRequest.GET_ROOT_MENU)).thenReturn(
				new StkMenu("Safaricom", "Safaricom+", mpesaMenuItem));
		
		myAccountMenuItem = mockMenuItem("My account");
		mpesaMenuItemRequest = mpesaMenuItem.getRequest();
		sendMoneyMenuItem = mockMenuItem("Send money");
		when(c.stkRequest(mpesaMenuItemRequest)).thenReturn(new StkMenu("M-PESA",
				sendMoneyMenuItem , "Withdraw cash", "Buy airtime",
				"Pay Bill", "Buy Goods", "ATM Withdrawal", myAccountMenuItem));
	}
	
	protected abstract E createNewTestClass();

	@SuppressWarnings("unchecked")
	private void setUpDaos() {
		clientDao = mock(ClientDao.class);
		accountDao = mock(AccountDao.class);

		List<Account> accounts1 = mockAccounts(ACCOUNTNUMBER_1_1);
		List<Account> accounts2 = mockAccounts(ACCOUNTNUMBER_2_1, ACCOUNTNUMBER_2_2);
		
		mockClient(0, PHONENUMBER_0, Collections.EMPTY_LIST);
		mockClient(1, PHONENUMBER_1, accounts1);
		mockClient(2, PHONENUMBER_2, accounts2);
		
		this.safaricomPaymentService.setClientDao(clientDao);
		this.safaricomPaymentService.setAccountDao(accountDao);
	}
	
	private void mockClient(long id, String phoneNumber, List<Account> accounts) {
		Client c = mock(Client.class);
		when(c.getId()).thenReturn(id);
		when(clientDao.getClientByPhoneNumber(phoneNumber)).thenReturn(c);
		when(accountDao.getAccountsByClientId(id)).thenReturn(accounts);
	}

	public void testCheckBalance() throws PaymentServiceException, SMSLibDeviceException {
		// setup
		StkRequest myAccountMenuItemRequest = myAccountMenuItem.getRequest();
		StkMenuItem showBalanceMenuItem = mockMenuItem("Show balance");
		when(c.stkRequest(myAccountMenuItemRequest)).thenReturn(
				new StkMenu("My account", showBalanceMenuItem, "Call support",
						"Change PIN", "Secret word", "Language", "Update menu"));
		StkInputRequiremnent pinRequired = mockInputRequirement("Enter PIN", 0, 0, 4, 4, 0);
		StkRequest pinRequiredRequest = pinRequired.getRequest();
		StkRequest showBalanceMenuItemRequest = showBalanceMenuItem.getRequest();
		when(c.stkRequest(showBalanceMenuItemRequest)).thenReturn(pinRequired);
		
		// given
		safaricomPaymentService.setPin("1234");
		
		// when
		safaricomPaymentService.checkBalance();
		
		// then
		InOrder inOrder = inOrder(c);
		inOrder.verify(c).stkRequest(StkRequest.GET_ROOT_MENU);
		inOrder.verify(c).stkRequest(mpesaMenuItemRequest);
		inOrder.verify(c).stkRequest(myAccountMenuItemRequest);
		inOrder.verify(c).stkRequest(showBalanceMenuItemRequest);
		inOrder.verify(c).stkRequest(pinRequiredRequest, "1234");
	}
	
	public void testMakePayment() throws PaymentServiceException, SMSLibDeviceException {
		// setup
		StkRequest sendMoneyMenuItemRequest = sendMoneyMenuItem.getRequest();
		StkInputRequiremnent phoneNumberRequired = mockInputRequirement("Enter phone no.");
		when(c.stkRequest(sendMoneyMenuItemRequest)).thenReturn(phoneNumberRequired);
		StkRequest phoneNumberRequest = phoneNumberRequired.getRequest();
		StkInputRequiremnent amountRequired = mockInputRequirement("Enter amount");
		when(c.stkRequest(phoneNumberRequest, "0712345678")).thenReturn(amountRequired);
		StkRequest amountRequest = amountRequired.getRequest();
		StkInputRequiremnent pinRequired = mockInputRequirement("Enter PIN");
		when(c.stkRequest(amountRequest, "500")).thenReturn(pinRequired);
		StkRequest pinRequiredRequest = pinRequired.getRequest();
		
		// given
		safaricomPaymentService.setPin("1234");
		
		// when
		safaricomPaymentService.makePayment(mockAccount(), new BigDecimal("500"));
		
		// then
		InOrder inOrder = inOrder(c);
		inOrder.verify(c).stkRequest(StkRequest.GET_ROOT_MENU);
		inOrder.verify(c).stkRequest(mpesaMenuItemRequest);
		inOrder.verify(c).stkRequest(sendMoneyMenuItemRequest);
		inOrder.verify(c).stkRequest(phoneNumberRequest , "0712345678");
		inOrder.verify(c).stkRequest(amountRequest , "500");
		inOrder.verify(c).stkRequest(pinRequiredRequest , "1234");
	}
	
	protected void testIncomingPaymentProcessing(String messageText,
			final String phoneNo, final String accountNumber, final String amount,
			final String confirmationCode, final String payedBy, final long datetime) {
		// then
		assertTrue(safaricomPaymentService instanceof EventObserver);
		
		// when
		safaricomPaymentService.notify(mockMessageNotification("MPESA", messageText));
		
		// then
		WaitingJob.waitForEvent();
		verify(incomingPaymentProcessor).process(new IncomingPayment() {
			@Override
			public boolean equals(Object that) {
				
				if(!(that instanceof IncomingPayment)) return false;
				IncomingPayment other = (IncomingPayment) that;
				
				
				return other.getPhoneNumber().equals(phoneNo) &&
						other.getAmountPaid().equals(new BigDecimal(amount)) &&
						other.getAccount().getAccountNumber().equals(accountNumber) &&
						other.getConfirmationCode().equals(confirmationCode) &&
						other.getTimePaid().equals(datetime) &&
						other.getPaymentBy().equals(payedBy);
						
			}
		});
	}

	public void testIncomingPaymentProcessorIgnoresIrrelevantMessages() {
		// when
		testIncomingPaymentProcessorIgnoresMessage("MPESA", "Some random text...");
		testIncomingPaymentProcessorIgnoresMessage("0798765432", "... and some more random text.");
	}
	
	protected void testIncomingPaymentProcessorIgnoresMessage(String fromNumber, String messageText) {
		safaricomPaymentService.notify(mockMessageNotification(fromNumber, messageText));
		
		// then
		WaitingJob.waitForEvent();
		verify(incomingPaymentProcessor, never()).process(any(IncomingPayment.class));
	}
	
	public void testFakedIncomingPayment() {
		// Message text which looks reasonable, but is wrong format
		testFakedIncomingPayment("0798765432", "0712345678 sent payment of 500 KES");
		
		// Genuine MPESA message text, with bad number
		testFakedIncomingPayment("0798765432", "BI94HR849 Confirmed.\n" +
				"You have received Ksh1,235 JOHN KIU 254723908653 on 3/5/11 at 10:35 PM\n" +
				"New M-PESA balance Ksh1,236");
		
		// Genuine PayBill message text, with bad number
		testFakedIncomingPayment("0798765432", "BH45UU225 Confirmed.\n" +
				"on 5/4/11 at 2:45 PM\n" +
				"Ksh950 received from ELLY ASAKHULU 254713698227.\n" +
				"Account Number 0713698227\n" +
				"New Utility balance is Ksh50,802\n" +
				"Time: 05/04/2011 14:45:34");
	}
	
	private void testFakedIncomingPayment(String from, String messageText) {
		// setup
		MpesaPaymentService s = this.safaricomPaymentService;
		IncomingPaymentProcessor incomingPaymentProcessor = mock(IncomingPaymentProcessor.class);
		
		// when
		s.notify(mockMessageNotification(from, messageText));
		
		// then
		WaitingJob.waitForEvent();
		verify(incomingPaymentProcessor, never()).process(any(IncomingPayment.class));
	}
	
	private StkMenuItem mockMenuItem(String title, int... numbers) {
		StkMenuItem i = mock(StkMenuItem.class);
		when(i.getText()).thenReturn(title);
		StkRequest mockRequest = mock(StkRequest.class);
		when(i.getRequest()).thenReturn(mockRequest);
		return i;
	}
	
	private StkInputRequiremnent mockInputRequirement(String title, int... nums) {
		StkInputRequiremnent ir = mock(StkInputRequiremnent.class);
		when(ir.getText()).thenReturn(title);
		
		StkRequest mockRequest = mock(StkRequest.class);
		when(ir.getRequest()).thenReturn(mockRequest);
		return ir;
	}
	
	private Account mockAccount() {
		Account a = mock(Account.class);
		when(a.getAccountId()).thenReturn(Long.valueOf(123456789));
		
		Client c = mock(Client.class);
		when(c.getPhoneNumber()).thenReturn("0712345678");
		
		when(a.getClient()).thenReturn(c);
		
		return a;
	}
	
	private EntitySavedNotification<FrontlineMessage> mockMessageNotification(String from, String text) {
		FrontlineMessage m = mock(FrontlineMessage.class);
		when(m.getSenderMsisdn()).thenReturn(from);
		when(m.getTextContent()).thenReturn(text);
		return new EntitySavedNotification<FrontlineMessage>(m);
	}
	
	private List<Account> mockAccounts(String... accountNumbers) {
		ArrayList<Account> accounts = new ArrayList<Account>();
		for(String accountNumber : accountNumbers) {
			Account account = mock(Account.class);
			when(account.getAccountNumber()).thenReturn(accountNumber);
			accounts.add(account);
			when(accountDao.getAccountByAccountNumber(accountNumber)).thenReturn(account);
		}
		return accounts;
	}
}

class WaitingJob extends FrontlineUiUpateJob {
	private boolean running;
	private void block() {
		running = true;
		execute();
		while(running) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				running = false;
			}
		}
	}
	
	public void run() {
		running = false;
	}
	
	/** Put a job on the UI event queue, and block until it has been run. */
	public static void waitForEvent() {
		new WaitingJob().block();
	}
}