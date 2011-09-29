package org.creditsms.plugins.paymentview.ui.handler.base;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.Order;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Client.Field;

public abstract class BaseClientSelectorListHandler extends BaseClientTableHandler {

	private List<Client> exclusionList = new ArrayList<Client>(0);

	public BaseClientSelectorListHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui, pluginController);
	}

	protected void createHeader(){}
	protected Order getClientsSortOrder() {return Order.ASCENDING;}
	protected Field getClientsSortField(){return Client.Field.FIRST_NAME;}
	
	@Override
	protected Object getRow(Client client) {
		return ui.createListItem(client.getFullName() + " ("+client.getPhoneNumber()+")", client);
	}
	
	public void setExclusionList(List<Client> exclusionList) {
		this.exclusionList = exclusionList;
	}
	
	public List<Client> getExclusionList() {
		return exclusionList;
	}
	
	@Override
	public List<Client> getClients() {
		List<Client> clients = super.getClients();
		for (Client c : exclusionList){
			clients.remove(c);
		}
		return clients;
	}
	
	@Override
	protected List<Client> getClients(String clientFilter, int startIndex,
			int limit) {
		List<Client> clients = super.getClients(clientFilter, startIndex, limit);
		for (Client c : exclusionList){
			clients.remove(c);
		}
		return clients;
	}
	
	public void notify(final FrontlineEventNotification notification) {}
	
}