package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseSelectClientTableHandler;

public class SelectClientsTableHandler extends BaseSelectClientTableHandler {
	private static final String TBL_CLIENTS = "tbl_clients";
	private static final String XML_CLIENTS_TABLE = "/ui/plugins/paymentview/analytics/addclient/clientsTable.xml";
	
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
