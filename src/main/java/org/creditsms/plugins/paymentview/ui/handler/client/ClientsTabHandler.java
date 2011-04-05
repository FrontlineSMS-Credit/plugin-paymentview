package org.creditsms.plugins.paymentview.ui.handler.client;

import org.creditsms.plugins.paymentview.ui.PaymentsImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.client.dialogs.CustomizeClientHandler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ClientsTabHandler extends BaseTabHandler{
	private static final String XML_CLIENTS_TAB = "/ui/plugins/paymentview/clients/clients.xml";
	private Object clientsTab;
	
	public ClientsTabHandler(UiGeneratorController ui) {
		super(ui);		
		init();
	}

	@Override
	public void refresh() {		
	}

	@Override
	protected Object initialiseTab() {
		clientsTab = ui.loadComponentFromFile(XML_CLIENTS_TAB, this);
		return clientsTab;
	}

	//> EVENTS...
	public void customizeClientDB(){
	}
	
	public void addClient(){		
		ui.add(new CustomizeClientHandler(ui).getDialog());
	}
	
	public void showImportWizard(String typeName){
		new PaymentsImportHandler(ui).showWizard();
	}
}
