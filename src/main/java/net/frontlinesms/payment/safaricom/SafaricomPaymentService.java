package net.frontlinesms.payment.safaricom;

import java.math.BigDecimal;

import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.IncomingPaymentProcessor;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.PaymentServiceException;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.smslib.CService;
import org.smslib.SMSLibDeviceException;
import org.smslib.stk.StkInputRequiremnent;
import org.smslib.stk.StkMenu;
import org.smslib.stk.StkRequest;
import org.smslib.stk.StkResponse;

public class SafaricomPaymentService implements PaymentService, EventObserver {
	private CService cService;
	private IncomingPaymentProcessor incomingPaymentProcessor;
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

	public void setIncomingPaymentProcessor(IncomingPaymentProcessor incomingPaymentProcessor) {
		this.incomingPaymentProcessor = incomingPaymentProcessor;
	}

	@SuppressWarnings("unchecked")
	public void notify(FrontlineEventNotification notification) {
		if(!(notification instanceof EntitySavedNotification)) {
			return;
		}
		
		Object entity = ((EntitySavedNotification) notification).getDatabaseEntity();
		if(!(entity instanceof FrontlineMessage)) {
			return;
		}
		
		FrontlineMessage message = (FrontlineMessage) entity;
		if(!message.getSenderMsisdn().equals("MPESA")) {
			return;
		}
		
		try {
			final IncomingPayment payment = new IncomingPayment();
			payment.setAccount(getAccount(message));
			payment.setPaymentBy(getPayer(message));
			payment.setAmountPaid(getAmount(message));
			new FrontlineUiUpateJob() { // This probably shouldn't be a UI job!
				public void run() {
					incomingPaymentProcessor.process(payment);					
				}
			}.execute();
		} catch(IllegalArgumentException ex) {
			// Message failed to parse; likely incorrect format
			return;
		}
	}

	private BigDecimal getAmount(FrontlineMessage message) {
		String[] s = message.getTextContent().split("\\s");
		if(s.length < 2 || !s[s.length-1].equals("KES")) {
			throw new IllegalArgumentException();
		}
		String amountString = s[s.length-2];
		if(!amountString.matches("[0-9]+")) {
			throw new IllegalArgumentException();
		}
		
		return new BigDecimal(amountString);
	}

	private String getPayer(FrontlineMessage message) {
		String payerNumber = message.getTextContent().split("\\s", 2)[0];
		if(payerNumber.matches("07[0-9]{8}")) {
			return payerNumber;
		} else {
			throw new IllegalArgumentException();
		}
	}

	private Account getAccount(FrontlineMessage message) {
		// TODO Auto-generated method stub
		return null;
	}
}