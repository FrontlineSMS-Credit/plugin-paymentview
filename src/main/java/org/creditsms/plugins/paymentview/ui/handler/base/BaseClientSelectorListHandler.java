package org.creditsms.plugins.paymentview.ui.handler.base;

import net.frontlinesms.data.Order;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Client.Field;

public abstract class BaseClientSelectorListHandler extends BaseClientTableHandler {

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
	
	public void notify(final FrontlineEventNotification notification) {}
	
}