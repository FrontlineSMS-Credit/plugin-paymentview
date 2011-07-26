package net.frontlinesms.payment;

import java.awt.EventQueue;

public abstract class PaymentJob implements Runnable {
	public void execute() {
		EventQueue.invokeLater(this);
	}
}