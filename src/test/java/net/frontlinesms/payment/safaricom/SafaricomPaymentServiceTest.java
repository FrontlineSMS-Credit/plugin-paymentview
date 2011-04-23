package net.frontlinesms.payment.safaricom;

import org.mockito.InOrder;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.*;

import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;

import static org.mockito.Mockito.*;

public class SafaricomPaymentServiceTest extends BaseTestCase {
	private PaymentService s;
	private CService c;
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.s = new SafaricomPaymentService();
		this.c = mock(CService.class);
		((SafaricomPaymentService) s).setCService(c);
	}
	
	public void testCheckBalance() throws PaymentServiceException, SMSLibDeviceException {
		// given
		((SafaricomPaymentService) s).setPin("1234");
		
		StkMenuItem mpesaMenuItem = mockMenuItem("M-PESA", 129, 21);
		when(c.stkRequest(StkRequest.GET_ROOT_MENU)).thenReturn(
				new StkMenu("Safaricom", "Safaricom+", mpesaMenuItem));
		
		StkMenuItem myAccountMenuItem = mockMenuItem("My account");
		StkRequest mpesaMenuItemRequest = mpesaMenuItem.getRequest();
		when(c.stkRequest(mpesaMenuItemRequest)).thenReturn(new StkMenu("M-PESA",
				"Send money", "Withdraw cash", "Buy airtime",
				"Pay Bill", "Buy Goods", "ATM Withdrawal", myAccountMenuItem));

		StkRequest myAccountMenuItemRequest = myAccountMenuItem.getRequest();
		StkMenuItem showBalanceMenuItem = mockMenuItem("Show balance");
		when(c.stkRequest(myAccountMenuItemRequest)).thenReturn(
				new StkMenu("My account", showBalanceMenuItem, "Call support",
						"Change PIN", "Secret word", "Language", "Update menu"));
		StkInputRequiremnent pinRequired = mockInputRequirement("Enter PIN", 0, 0, 4, 4, 0);
		StkRequest showBalanceMenuItemRequest = showBalanceMenuItem.getRequest();
		when(c.stkRequest(showBalanceMenuItemRequest)).thenReturn(pinRequired);
		
		// when
		s.checkBalance();
		
		// then
		InOrder inOrder = inOrder(c);
		inOrder.verify(c).stkRequest(StkRequest.GET_ROOT_MENU);
		inOrder.verify(c).stkRequest(mpesaMenuItemRequest);
		inOrder.verify(c).stkRequest(myAccountMenuItemRequest);
		inOrder.verify(c).stkRequest(showBalanceMenuItemRequest);
		StkRequest pinRequiredRequest = pinRequired.getRequest();
		inOrder.verify(c).stkRequest(pinRequiredRequest, "1234");
	}
	
	public void testMakePayment() {
		fail("Not implemented");
	}
	
	public void testIncomingPaymentProcessing() {
		fail("Not implemented");
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
}
