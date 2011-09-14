/**
 * 
 */
package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientSelectorListHandler;

public class ClientSelectorTableHandler extends BaseClientSelectorListHandler {
	// > UI CONSTANTS
	private static final String CLIENT_SELECTOR_TABLE_XML = "/ui/plugins/paymentview/clients/dialogs/clientSelectorTable.xml";
	private static final String TBL_CLIENT_LIST = "clientSelectorList";

	// > CONSTRUCTORS
	public ClientSelectorTableHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui, pluginController);
	}
	
	@Override
	protected String getClientsTableName() {
		return TBL_CLIENT_LIST;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return CLIENT_SELECTOR_TABLE_XML;
	}
}
