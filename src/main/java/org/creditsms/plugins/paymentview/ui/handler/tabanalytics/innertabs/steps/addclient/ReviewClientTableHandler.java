package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseClientTable;

public class ReviewClientTableHandler extends BaseClientTable{
	private static final String PNL_TBL_CLIENT_LIST = "tbl_clientList";
	private static final String XML_CLIENTS_PANEL = "/ui/plugins/paymentview/analytics/addclient/reviewclienttable.xml";
	
	public ReviewClientTableHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController,
			ReviewHandler reviewHandler) {
		super(ui, pluginController);
	}
	
	@Override
	protected String getClientsTableName() {
		return PNL_TBL_CLIENT_LIST;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return XML_CLIENTS_PANEL;
	}
}
