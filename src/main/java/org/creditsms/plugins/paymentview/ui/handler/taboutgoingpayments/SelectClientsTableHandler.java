package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseClientTable;

public class SelectClientsTableHandler extends BaseClientTable {
	private static final String TBL_CLIENTS = "tbl_clients";
	private static final String XML_CLIENTS_TABLE = "/ui/plugins/paymentview/outgoingpayments/innertabs/clientsTable.xml";
	
	public SelectClientsTableHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui, pluginController);		
	}

	@Override
	protected String getClientsTableName() {
		return TBL_CLIENTS;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return XML_CLIENTS_TABLE;
	}	
}
