package net.frontlinesms.payment.safaricom;

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
	}
	
	public void testCheckBalance() throws PaymentServiceException, SMSLibDeviceException {
		// given
		StkMenuItem mpesaMenuItem = mockMenuItem("M-PESA", 129, 21);
		when(c.stkRequest(StkRequest.GET_ROOT_MENU)).thenReturn(
				new StkMenu("Safaricom", "Safaricom+", mpesaMenuItem));
		StkMenuItem myAccountMenuItem = mockMenuItem("My account");
		when(c.stkRequest(mpesaMenuItem.getRequest())).thenReturn(new StkMenu("M-PESA",
				"Send money", "Withdraw cash", "Buy airtime",
				"Pay Bill", "Buy Goods", "ATM Withdrawal", myAccountMenuItem));
		StkMenuItem showBalanceMenuItem = mockMenuItem("Show balance");
		when(c.stkRequest(myAccountMenuItem.getRequest())).thenReturn(
				new StkMenu("My account", showBalanceMenuItem, "Call support",
						"Change PIN", "Secret word", "Language", "Update menu"));
		StkInputRequiremnent pinRequest = mockInputRequirement("Enter PIN", 0, 0, 4, 4, 0);
		when(c.stkRequest(showBalanceMenuItem.getRequest())).thenReturn(pinRequest);
		
		// when
		s.checkBalance();
		
		// then
		verify(c).stkRequest(StkRequest.GET_ROOT_MENU);
		verify(c).stkRequest(mpesaMenuItem.getRequest());
		verify(c).stkRequest(myAccountMenuItem.getRequest());
		verify(c).stkRequest(showBalanceMenuItem.getRequest());
		verify(c).stkRequest(pinRequest.getRequest(), "1234");
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
