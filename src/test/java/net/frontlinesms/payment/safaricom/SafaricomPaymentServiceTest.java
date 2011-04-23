package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.mockito.InOrder;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.*;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.payment.IncomingPaymentProcessor;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;

import static org.mockito.Mockito.*;

public class SafaricomPaymentServiceTest extends BaseTestCase {
	private PaymentService s;
	private CService c;
	
	private StkMenuItem myAccountMenuItem;
	private StkRequest mpesaMenuItemRequest;
	private StkMenuItem sendMoneyMenuItem;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.s = new SafaricomPaymentService();
		this.c = mock(CService.class);
		((SafaricomPaymentService) s).setCService(c);

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
		((SafaricomPaymentService) s).setPin("1234");
		
		// when
		s.checkBalance();
		
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
		when(c.stkRequest(phoneNumberRequest, "071234567")).thenReturn(amountRequired);
		StkRequest amountRequest = amountRequired.getRequest();
		StkInputRequiremnent pinRequired = mockInputRequirement("Enter PIN");
		when(c.stkRequest(amountRequest, "500")).thenReturn(pinRequired);
		StkRequest pinRequiredRequest = pinRequired.getRequest();
		
		// given
		((SafaricomPaymentService) s).setPin("1234");
		
		// when
		s.makePayment(mockAccount(), new BigDecimal("500"));
		
		// then
		InOrder inOrder = inOrder(c);
		inOrder.verify(c).stkRequest(StkRequest.GET_ROOT_MENU);
		inOrder.verify(c).stkRequest(mpesaMenuItemRequest);
		inOrder.verify(c).stkRequest(sendMoneyMenuItemRequest);
		inOrder.verify(c).stkRequest(phoneNumberRequest , "071234567");
		inOrder.verify(c).stkRequest(amountRequest , "500");
		inOrder.verify(c).stkRequest(pinRequiredRequest , "1234");
	}

	public void testIncomingPaymentProcessing() {
		// setup
		SafaricomPaymentService s = (SafaricomPaymentService) this.s;
		IncomingPaymentProcessor incomingPaymentProcessor = mock(IncomingPaymentProcessor.class);
		
		// then
		assertTrue(s instanceof EventObserver);
		
		// when
		s.notify(mockMessageNotification("Safaricom", "Here is the text to process - this should be valid confirmation text."));
		
		// then
		verify(incomingPaymentProcessor).process(any(IncomingPayment.class));
		
		fail("Not implemented");
	}

	public void testIncomingPaymentProcessorIgnoresIrrelevantMessages() {
		// setup
		SafaricomPaymentService s = (SafaricomPaymentService) this.s;
		IncomingPaymentProcessor incomingPaymentProcessor = mock(IncomingPaymentProcessor.class);
		
		// when
		s.notify(mockMessageNotification("Safaricom", "Some random text..."));
		s.notify(mockMessageNotification("0798765432", "... and some more random text."));
		
		// then
		verify(incomingPaymentProcessor).process(any(IncomingPayment.class));
		
		fail("Not implemented");
	}
	
	public void testFakedIncomingPayment() {
		// setup
		SafaricomPaymentService s = (SafaricomPaymentService) this.s;
		IncomingPaymentProcessor incomingPaymentProcessor = mock(IncomingPaymentProcessor.class);
		
		// when
		s.notify(mockMessageNotification("0798765432", "Here is the text to process - this should be valid confirmation text."));
		
		// then
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
		StkRequest mockRequest = mock(StkRequest.class);
		when(ir.getRequest()).thenReturn(mockRequest);
		return ir;
	}
	
	private Account mockAccount() {
		Account a = mock(Account.class);
		when(a.getAccountId()).thenReturn(Long.valueOf(123456789));
		
		Client c = mock(Client.class);
		when(c.getPhoneNumber()).thenReturn("071234567");
		
		when(a.getClient()).thenReturn(c);
		
		return a;
	}
	
	private EntitySavedNotification<FrontlineMessage> mockMessageNotification(String from, String text) {
		FrontlineMessage m = mock(FrontlineMessage.class);
		when(m.getSenderMsisdn()).thenReturn(from);
		when(m.getTextContent()).thenReturn(text);
		return new EntitySavedNotification<FrontlineMessage>(m);
	}
}
