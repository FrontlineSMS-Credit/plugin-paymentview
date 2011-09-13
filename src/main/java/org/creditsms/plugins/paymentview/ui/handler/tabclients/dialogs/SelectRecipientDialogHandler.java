package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseActionDialog;

public class SelectRecipientDialogHandler extends BaseActionDialog  {
	private static final String XML_SELECT_RECIPIENTS_DIALOG = "/ui/plugins/paymentview/clients/dialogs/dlgSelectClients.xml";

	public SelectRecipientDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		init();
	}
	
	@Override
	public void init() {
		super.init();
	}
	
	/** Save auto reply details */
	public void save(String message) {
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
		return XML_SELECT_RECIPIENTS_DIALOG;
	}
	
	public Object getDialog() {
		return this.getDialogComponent();
	}
	
	public Object addClients() {
		return this.getDialogComponent();
	}
}
