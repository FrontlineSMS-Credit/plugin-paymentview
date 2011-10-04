package org.creditsms.plugins.paymentview.userhomepropeties.payment.balance;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Date;

import net.frontlinesms.junit.BaseTestCase;
import net.frontlinesms.payment.PaymentService;

import org.creditsms.plugins.paymentview.data.domain.PaymentServiceSettings;

public class BalancePropertiesTest extends BaseTestCase {

	private BalanceProperties properties;
	private Balance balance;
	private Date datetime;
	private PaymentService paymentService;
	private PaymentServiceSettings paymentServiceSettings;

	@Override
	protected void setUp() throws Exception {
		properties = BalanceProperties.getInstance();
		balance = new Balance();
		datetime = new Date();
		
		paymentService = mock(PaymentService.class);
		
	    paymentServiceSettings = mock(PaymentServiceSettings.class);
		when(paymentService.getSettings()).thenReturn(paymentServiceSettings);
		when(paymentServiceSettings.getPsSmsModemSerial()).thenReturn("359126030119333@639029400593656");
 
		balance.setPaymentService(paymentService);
		balance.setBalanceAmount(new BigDecimal("200.00"));
		balance.setConfirmationCode("CCVSBB566");
		balance.setDateTime(datetime);
		balance.setBalanceUpdateMethod("enquiry");
	}

	public void testGetBalanceAmount() throws Exception {
		balance.updateBalance();
		assertEquals("Balance Amount not correct", balance.getBalanceAmount(), new BigDecimal("200.00"));
	}

	public void testGetConfirmationCode() throws Exception {
		balance.updateBalance();
		assertEquals("Confirmation Code not correct", balance.getConfirmationCode(), "CCVSBB566");
	}

	public void testDateTime() throws ParseException {
		balance.updateBalance();
		assertEquals("Datetime not correct", properties.getBalance(paymentService).getDateTime(), datetime);
	}
}
