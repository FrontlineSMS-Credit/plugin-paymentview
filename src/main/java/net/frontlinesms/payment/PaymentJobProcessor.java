package net.frontlinesms.payment;

import java.util.concurrent.LinkedBlockingQueue;
public class PaymentJobProcessor implements Runnable {
	private static final PaymentJob POISON = new PaymentJob() { public void run() {} };
	
	private Thread t;
	private LinkedBlockingQueue<PaymentJob> q = new LinkedBlockingQueue<PaymentJob>();
	private static final PaymentJobProcessor pJobProcessor = new PaymentJobProcessor();
	
	public static PaymentJobProcessor getPjobprocessor() {
		return pJobProcessor;
	}

	public PaymentJobProcessor(PaymentService service) {
		t = new Thread(this, "PaymnentJobProcessor: " + service.toString());
	}

	public PaymentJobProcessor() {
	}

	public void run() {
		try {
			while(true) {
				PaymentJob job = q.take();
				if(job.equals(POISON)) {
					break;
				} else {
					try {
						job.run();
					} catch(Exception ex) {
						// TODO log this properly
						ex.printStackTrace();
					}
				}
			}
		} catch(InterruptedException ex) {
			ex.printStackTrace(); // TODO log this properly
		}
	}

	public void start() {
		t.start();
	}

	public void stop() {
		this.q.add(POISON);
	}

	public void queue(PaymentJob job) {
		this.q.add(job);
	}
	
}
