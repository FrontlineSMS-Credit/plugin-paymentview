package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.PaymentServiceStartedNotification;
import net.frontlinesms.payment.PaymentServiceStoppedNotification;
import net.frontlinesms.payment.safaricom.MpesaPayBillService;
import net.frontlinesms.ui.UiDestroyEvent;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

public class OutgoingPaymentsTabHandler extends BaseTabHandler implements EventObserver {

	private static final String XML_OUTGOINGPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/taboutgoingpayments.xml";
	private Object outgoingPaymentsTab;

	private SelectFromClientsTabHandler selectFromClientsTab;
	private SentPaymentsTabHandler sentPaymentsTab;
	private PaymentViewPluginController pluginController;
	private ImportNewPaymentsTabHandler importPaymentsTab;

	public OutgoingPaymentsTabHandler(UiGeneratorController ui, final PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		ui.getFrontlineController().getEventBus().registerObserver(this);
		init();
	}

	@Override
	protected Object initialiseTab() {
		outgoingPaymentsTab = ui.loadComponentFromFile(XML_OUTGOINGPAYMENTS_TAB, this);
		sentPaymentsTab = new SentPaymentsTabHandler(ui, outgoingPaymentsTab,pluginController);
		importPaymentsTab = new ImportNewPaymentsTabHandler(ui, outgoingPaymentsTab,pluginController);
		selectFromClientsTab = new SelectFromClientsTabHandler(ui, outgoingPaymentsTab, pluginController);

		return outgoingPaymentsTab;
	}

	@Override
	public void refresh() {
		sentPaymentsTab.refresh();
		selectFromClientsTab.refresh();
	}
	
	public void notify(final FrontlineEventNotification notification) {
		new FrontlineUiUpateJob() {
			public void run() {
				if (notification instanceof PaymentServiceStartedNotification) {
					OutgoingPaymentsTabHandler.this.refresh();
					if(((PaymentServiceStartedNotification)notification).getPaymentService() instanceof MpesaPayBillService){
						ui.setEnabledRecursively(outgoingPaymentsTab, false);
					}
				}else if (notification instanceof PaymentServiceStoppedNotification) {
					OutgoingPaymentsTabHandler.this.refresh();
					if(((PaymentServiceStoppedNotification)notification).getPaymentService() instanceof MpesaPayBillService){
						ui.setEnabledRecursively(outgoingPaymentsTab, true);
					}
				}else if (notification instanceof UiDestroyEvent) {
					if(((UiDestroyEvent) notification).isFor(ui)) {
						ui.getFrontlineController().getEventBus().unregisterObserver(OutgoingPaymentsTabHandler.this);
					}
				}
			}
		}.execute();
	}

}