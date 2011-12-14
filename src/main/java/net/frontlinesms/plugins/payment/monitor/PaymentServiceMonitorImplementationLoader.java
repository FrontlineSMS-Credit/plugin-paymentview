package net.frontlinesms.plugins.payment.monitor;

import java.util.Comparator;

import net.frontlinesms.resources.ImplementationLoader;

public class PaymentServiceMonitorImplementationLoader extends ImplementationLoader<PaymentServiceMonitor> {
	@Override
	protected Class<PaymentServiceMonitor> getEntityClass() {
		return PaymentServiceMonitor.class;
	}

	@Override
	protected Comparator<? super Class<? extends PaymentServiceMonitor>> getSorter() {
		return new PaymentServiceMonitorSorter();
	}
}

class PaymentServiceMonitorSorter implements Comparator<Class<? extends PaymentServiceMonitor>> {
	public int compare(Class<? extends PaymentServiceMonitor> o1, Class<? extends PaymentServiceMonitor> o2) {
		if(o1 == null) return -1;
		else if(o2 == null) return 1;
		else {
			return o1.getSimpleName().compareTo(o2.getSimpleName());
		}
	}
}
