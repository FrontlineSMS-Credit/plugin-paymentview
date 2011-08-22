package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;

import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.ui.UiGeneratorController;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.IncomingPaymentsTabHandler;

public class AutoReplyPaymentsDialogHandler extends BaseDialog  {
	private static final String XML_AUTO_REPLY_PAYMENTS_DIALOG = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgAutoReplyPayments.xml";
	private PaymentViewPluginController pluginController;

	public AutoReplyPaymentsDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
		initialise();
		this.refresh();
	}
	
	protected void initialise() {
		dialogComponent = ui.loadComponentFromFile(XML_AUTO_REPLY_PAYMENTS_DIALOG, this);
	}
	
	/** Save auto reply details */
	public void save() {
		
	}
	
	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
}
