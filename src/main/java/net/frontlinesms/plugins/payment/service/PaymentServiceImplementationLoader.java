package net.frontlinesms.plugins.payment.service;

import java.util.Comparator;

import net.frontlinesms.resources.ImplementationLoader;

public class PaymentServiceImplementationLoader extends ImplementationLoader<PaymentService> {

	@Override
	protected Class<PaymentService> getEntityClass() {
		return PaymentService.class;
	}

	@Override
	protected Comparator<? super Class<? extends PaymentService>> getSorter() {
		return new PaymentServiceSorter();
	}
}

/** Sort {@link SmsInternetService}s alphabetically by name. */
class PaymentServiceSorter implements Comparator<Class<? extends PaymentService>> {
	public int compare(Class<? extends PaymentService> o1, Class<? extends PaymentService> o2) {
		if(o1 == null) return -1;
		else if(o2 == null) return 1;
		else {
			return o1.getSimpleName().compareTo(o2.getSimpleName());
		}
	}
}
