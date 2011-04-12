package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.smslib.CService;

import net.frontlinesms.payment.IncomingPaymentProcessor;
import net.frontlinesms.payment.PaymentService;

public class SafaricomPaymentService implements PaymentService {
	private RealCService cService;
	private String pin;
	
	public void checkBalance() {
		StkMenu mPesaMenu = getMpesaMenu();
		StkMenu myAccountMenu = (StkMenu) cService.stkRequest(mPesaMenu.getRequest("My account"));
		StkResponse getBalanceResponse = cService.stkRequest(myAccountMenu.getRequest("Show balance"));
		assert(getBalanceResponse.getText().contains("Enter PIN"));
		StkResponse finalResponse = cService.stkRequest(getBalanceResponse.getIndicatorRequest(), this.pin);
		// TODO check finalResponse is OK
		// TODO wait for response...
	}

	private StkMenu getMpesaMenu() {
		StkMenu rootMenu = (StkMenu) cService.stkRequest(StkRequest.GET_ROOT_MENU);
		return (StkMenu) cService.stkRequest(rootMenu.getRequest("M-PESA"));
	}

	public void makePayment(Account account, BigDecimal amount) {
		StkMenu mPesaMenu = getMpesaMenu();
		StkResponse sendMoneyResponse = cService.stkRequest(mPesaMenu.getRequest("Send money"), Long.toString(account.getAccountNumber()), amount.toString());
		// TODO process sendMoneyResponse
	}

	public void setIncomingPaymentProcessor(
		IncomingPaymentProcessor incomingPaymentProcessor) {
	}
}

class RealCService extends CService {
	RealCService() {
		super("", 0, "", "", "");
	}
	public StkResponse stkRequest(StkRequest request, String... variables) {
		return null;
	}
	StkResponse stkRequest(StkRequest request) {
		return null;
	}
}
class StkMenu extends StkResponse {
	public StkRequest getRequest(String menuOption) {
		return null;
	}
}

class StkRequest {
	public static final StkRequest GET_ROOT_MENU = null;
}

class StkResponse {
	public String getText() {
		return null;
	}
	public StkRequest getIndicatorRequest() {
		return null;
	}
}