package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseSelectClientTableHandler;

public class SelectClientsTableHandler extends BaseSelectClientTableHandler {
	private static final String TBL_CLIENTS = "tbl_clients";
	private static final String XML_CLIENTS_TABLE = "/ui/plugins/paymentview/analytics/viewdashboard/clientsTable.xml";
	private final ServiceItem serviceItem;
	private final TargetDao targetDao;
	
	public SelectClientsTableHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, ServiceItem serviceItem) {
		super(ui, pluginController);
		this.serviceItem = serviceItem;	
		this.targetDao = pluginController.getTargetDao();
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
	
	@Override
	protected List<String> getAccounts(Client client) {
		List<String> accountNumbers = new ArrayList<String>();
		List<Target> tgtLst = this.targetDao.getTargetsByServiceItemByClient(serviceItem.getId(), client.getId());
		
		for(int i=0; i<tgtLst.size(); i++){
			for(int j=0; j<accountNumbers.size();j++){
				if(tgtLst.get(i).getAccount().getAccountNumber().equals(accountNumbers.get(j))){
					accountNumbers.add(tgtLst.get(i).getAccount().getAccountNumber());
					break;
				}
			}
		}
		return accountNumbers;
	}
}
