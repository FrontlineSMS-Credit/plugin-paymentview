package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

//import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.BaseSelectClientTableHandler;

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
//			if (filter.trim().isEmpty()) {
				if (clientsTablePager.getMaxItemsPerPage() < clients.size()){
					return clients.subList(startIndex, limit);
				} else {
					return clients;
				}

//			}else{
//				List<Client> subList = null;
//				
//				if (clientsTablePager.getMaxItemsPerPage() < clients.size()){
//					subList = clients.subList(startIndex, limit);
//				}else{
//					subList = clients;
//				}
//				
//				List<Client> temp = new ArrayList<Client>(); 
//				for (Client c : subList) {
//					if (c.getName().equalsIgnoreCase(filter)) {
//						temp.add(c);
//					}
//				}
//				return temp;
//			}
		}
	}
}
