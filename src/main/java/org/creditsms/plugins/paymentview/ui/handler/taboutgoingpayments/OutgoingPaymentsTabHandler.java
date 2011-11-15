package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntityUpdatedNotification;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.event.PaymentServiceStartedNotification;
import net.frontlinesms.payment.event.PaymentServiceStoppedNotification;
import net.frontlinesms.ui.HomeTabEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;

public class OutgoingPaymentsTabHandler extends BaseTabHandler {

	private static final String XML_OUTGOINGPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/taboutgoingpayments.xml";
	private Object outgoingPaymentsTab;

	private SelectFromClientsTabHandler selectFromClientsTab;
	private SentPaymentsTabHandler sentPaymentsTab;
	private PaymentViewPluginController pluginController;

	public OutgoingPaymentsTabHandler(UiGeneratorController ui, final PaymentViewPluginController pluginController) {
		super(ui, true);
		this.pluginController = pluginController;
		init();
	}

	@Override
	protected Object initialiseTab() {
		outgoingPaymentsTab = ui.loadComponentFromFile(XML_OUTGOINGPAYMENTS_TAB, this);
		sentPaymentsTab = new SentPaymentsTabHandler(ui, outgoingPaymentsTab,pluginController);
		selectFromClientsTab = new SelectFromClientsTabHandler(ui, outgoingPaymentsTab, pluginController);

		return outgoingPaymentsTab;
	}

	@Override
	public void refresh() {
		sentPaymentsTab.refresh();
		selectFromClientsTab.refresh();
	}
	
	public void notify(final FrontlineEventNotification notification) {
		super.notify(notification);
		if (notification instanceof PaymentServiceStartedNotification) {
			threadSafeRefresh();
		} else if (notification instanceof PaymentServiceStoppedNotification) {
			threadSafeRefresh();
		} else if (notification instanceof EntityUpdatedNotification) {
			Object entity = ((DatabaseEntityNotification<?>) notification).getDatabaseEntity();	
			if (entity instanceof OutgoingPayment) {
				OutgoingPayment outgoingPayment = (OutgoingPayment) entity;
				if (outgoingPayment.getStatus().equals(OutgoingPayment.Status.ERROR)) {
					eventBus.notifyObservers(new HomeTabEventNotification(HomeTabEventNotification.Type.RED, "Error occurred in outgoing payment: " + outgoingPayment.getClient().getFullName()));
				}
			}
		}
	}
}