/**
 * 
 */
package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import net.frontlinesms.data.Order;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Client.Field;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTableHandler;

public class ClientSelectorTableHandler extends BaseClientTableHandler {

	// > UI CONSTANTS
	private static final String CLIENT_SELECTOR_TABLE_XML = "/ui/plugins/paymentview/clients/dialogs/clientSelectorTable.xml";
	private static final String TBL_CLIENT_LIST = "clientSelectorList";
	public static final String COMPONENT_CLIENT_SELECTOR_OK_BUTTON = "contactSelecter_okButton";
	public static final String COMPONENT_CLIENT_SELECTOR_CLIENT_LIST = "contactSelecter_contactList";

	// > CONSTRUCTORS
	public ClientSelectorTableHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui, pluginController);
		this.clientDao = pluginController.getClientDao();
	}
	
	protected void createHeader(){}
	protected Order getClientsSortOrder() {return Order.ASCENDING;}
	protected Field getClientsSortField(){return Client.Field.FIRST_NAME;}

	@Override
	protected Object getRow(Client client) {
		return ui.createListItem(client.getFullName(), client);
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
