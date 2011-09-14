package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTableHandler;

public class SelectClientsTableHandler extends BaseClientTableHandler {
	private static final String TBL_CLIENTS = "tbl_clients";
	private static final String XML_CLIENTS_TABLE = "/ui/plugins/paymentview/outgoingpayments/innertabs/clientsTable.xml";
	private SelectFromClientsTabHandler selectFromClientTab;
	
	public SelectClientsTableHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, SelectFromClientsTabHandler selectFromClientTab) {
		super(ui, pluginController);		
		this.selectFromClientTab = selectFromClientTab;
	}

	@Override
	protected String getClientsTableName() {
		return TBL_CLIENTS;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return XML_CLIENTS_TABLE;
	}	
	
	public void sendPaymentForClient() {
		selectFromClientTab.sendPaymentForClient();
	}
}
