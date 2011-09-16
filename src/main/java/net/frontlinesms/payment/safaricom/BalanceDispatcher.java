package net.frontlinesms.payment.safaricom;

import java.util.concurrent.LinkedBlockingQueue;

import net.frontlinesms.data.domain.FrontlineMessage;

public class BalanceDispatcher {
	private LinkedBlockingQueue<MpesaPaymentService> queue = new LinkedBlockingQueue<MpesaPaymentService>();
	
	private static BalanceDispatcher INSTANCE = new BalanceDispatcher();
	public static BalanceDispatcher getInstance() {return INSTANCE;}
	private BalanceDispatcher(){}
	
	public void queuePaymentService(MpesaPaymentService paymentService) {
		queue.add(paymentService);
	}
	
	public void notify(FrontlineMessage message) {
		try {
			//Only notify the first PS to enquire
			queue.take().finaliseBalanceProcessing(message);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
