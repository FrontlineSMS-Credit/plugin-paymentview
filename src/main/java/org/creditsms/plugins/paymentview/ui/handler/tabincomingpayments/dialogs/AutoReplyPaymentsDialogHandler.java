package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseActionDialog;
import org.creditsms.plugins.paymentview.userhomepropeties.incomingpayments.AutoReplyProperties;

public class AutoReplyPaymentsDialogHandler extends BaseActionDialog  {
	private static final String XML_AUTO_REPLY_PAYMENTS_DIALOG = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgAutoReplyPayments.xml";
	private AutoReplyProperties autoReplyProperties = AutoReplyProperties.getInstance();
	
	public AutoReplyPaymentsDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		init();
	}
	
	@Override
	public void init() {
		super.init();
		ui.setText(ui.find(this.getDialogComponent(), "replyContent"), autoReplyProperties.getMessage());
	}
	
	/** Save auto reply details */
	public void save(String message) {
		autoReplyProperties.setMessage(message);
		this.removeDialog();
	}
	
	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}

	public void addConstantToDialog(String text, Object object, String type) {
		addConstantToCommand(text, object, type);
	}

	@Override
	protected void handleRemoved() {
		
	}

	@Override
	protected String getLayoutFilePath() {
		return XML_AUTO_REPLY_PAYMENTS_DIALOG;
	}
	
	public Object getDialog() {
		return this.getDialogComponent();
	}
}
