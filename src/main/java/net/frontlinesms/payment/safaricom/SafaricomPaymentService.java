package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.*;

import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.IncomingPaymentProcessor;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;

public class SafaricomPaymentService implements PaymentService, EventObserver {
	private CService cService;
	private String pin;
	
	public void setCService(CService cService) {
		this.cService = cService;
	}
	
	public void setPin(String pin) {
		this.pin = pin;
	}
	
	public void checkBalance() throws PaymentServiceException {
		try {
			StkMenu mPesaMenu = getMpesaMenu();
			StkMenu myAccountMenu = (StkMenu) cService.stkRequest(mPesaMenu.getRequest("My account"));
			StkResponse getBalanceResponse = cService.stkRequest(myAccountMenu.getRequest("Show balance"));
			assert getBalanceResponse instanceof StkInputRequiremnent;
			StkInputRequiremnent pinRequired = (StkInputRequiremnent) getBalanceResponse;
			assert pinRequired.getText().contains("Enter PIN");
			StkResponse finalResponse = cService.stkRequest(pinRequired.getRequest(), this.pin);
			// TODO check finalResponse is OK
			// TODO wait for response...
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

	private StkMenu getMpesaMenu() throws PaymentServiceException {
		try {
			StkMenu rootMenu = (StkMenu) cService.stkRequest(StkRequest.GET_ROOT_MENU);
			return (StkMenu) cService.stkRequest(rootMenu.getRequest("M-PESA"));
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

	public void makePayment(Account account, BigDecimal amount) throws PaymentServiceException {
		try {
			StkMenu mPesaMenu = getMpesaMenu();
			StkResponse sendMoneyResponse = cService.stkRequest(mPesaMenu.getRequest("Send money"));
			String phoneNumber = account.getClient().getPhoneNumber();
			
			StkRequest phoneNumberRequest = ((StkInputRequiremnent) sendMoneyResponse).getRequest();
			StkResponse phoneNumberResponse = cService.stkRequest(phoneNumberRequest, phoneNumber);
			
			StkRequest amountRequest = ((StkInputRequiremnent) phoneNumberResponse).getRequest();
			StkResponse amountResponse = cService.stkRequest(amountRequest, amount.toString());
			
			StkRequest pinRequest = ((StkInputRequiremnent) amountResponse).getRequest();
			cService.stkRequest(pinRequest, this.pin);
		} catch (SMSLibDeviceException ex) {
			throw new PaymentServiceException(ex);
		}
	}

	public void setIncomingPaymentProcessor(
		IncomingPaymentProcessor incomingPaymentProcessor) {
	}

	public void notify(FrontlineEventNotification notification) {
	}
}