package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

//import java.util.ArrayList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.BaseSelectClientTableHandler;
import org.springframework.util.StringUtils;

public class SelectClientsTableHandler extends BaseSelectClientTableHandler {
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
	
	@Override
	protected List<Client> getClients(String filter, int startIndex, int limit) {
		if (clients.isEmpty()){
			return Collections.emptyList();
		}else{
			if (clientsTablePager.getMaxItemsPerPage() < clients.size()){
				return clients.subList(startIndex, limit);
			} else {
				return clients;
			}
		}
	}
	
	protected List<String> getAccounts(Client client) {
		List<String> accountNumbers = new ArrayList<String>();
		for (Account a : this.accountDao.getAccountsByClientId(client.getId())) {
			accountNumbers.add(a.getAccountNumber());
		}
		return accountNumbers;
	}
}
