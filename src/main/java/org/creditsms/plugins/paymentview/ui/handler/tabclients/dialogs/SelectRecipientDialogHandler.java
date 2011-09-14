package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientTableHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;

public class SelectRecipientDialogHandler extends ClientTableHandler  {
	private static final String XML_SELECT_RECIPIENTS_DIALOG = "/ui/plugins/paymentview/clients/dialogs/dlgSelectClients.xml";
	private ClientsTabHandler clientsTabHandler;
	
	public SelectRecipientDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, ClientsTabHandler clientsTabHandler) {
		super(ui, pluginController, clientsTabHandler);
		this.clientsTabHandler = clientsTabHandler;
	}
	
	@Override
	public void init() {
		super.init();
	}
	

	protected String getXMLFile() {
		return XML_SELECT_RECIPIENTS_DIALOG;
	}
	
	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
}
