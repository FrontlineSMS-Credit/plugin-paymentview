package net.frontlinesms.plugins.payment.monitor;

import org.springframework.context.ApplicationContext;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.plugins.payment.service.PaymentService;

/** Interface to be implemented by {@link PaymentService} providers who need to
 * trigger behaviour depending on external events. */
public interface PaymentServiceMonitor extends EventObserver {
	void init(FrontlineSMS frontlineController, ApplicationContext applicationContext);
}
