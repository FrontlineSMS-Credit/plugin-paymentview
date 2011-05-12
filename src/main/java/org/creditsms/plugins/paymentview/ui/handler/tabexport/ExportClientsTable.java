package org.creditsms.plugins.paymentview.ui.handler.tabexport;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseSelectClientTableHandler;

public class ExportClientsTable extends BaseSelectClientTableHandler {
	private static final String TBL_CLIENTS = "tbl_clients";
	private static final String XML_CLIENTS_TABLE = "/ui/plugins/paymentview/export/innertabs/clientsTable.xml";
	
	public ExportClientsTable(UiGeneratorController ui, PaymentViewPluginController pluginController) {
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
