package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseActionDialog;

public class AutoReplyPaymentsDialogHandler extends BaseActionDialog  {
	private static final String XML_AUTO_REPLY_PAYMENTS_DIALOG = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgAutoReplyPayments.xml";

	public AutoReplyPaymentsDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		init();
	}
	
	/** Save auto reply details */
	public void save() {
		
	}
	
	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}

	@Override
	protected void _init() {
		// TODO Auto-generated method stub
	}
	
	public void addConstantToDialog(String type) {
		Object messageTextArea = ui.find(this.getDialogComponent(), "tfMessage");
		addConstantToCommand(ui.getText(messageTextArea), messageTextArea, type);
	}

	@Override
	protected void handleRemoved() {
		
	}

	@Override
	protected String getLayoutFilePath() {
		return XML_AUTO_REPLY_PAYMENTS_DIALOG;
	}
	
	private void refresh() {
	}

	public Object getDialog() {
		return this.getDialogComponent();
	}
}
