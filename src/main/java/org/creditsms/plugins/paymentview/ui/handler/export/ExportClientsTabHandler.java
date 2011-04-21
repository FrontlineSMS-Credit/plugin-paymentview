package org.creditsms.plugins.paymentview.ui.handler.export;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.export.dialogs.ExportByClientXticsStep1Handler;

public class ExportClientsTabHandler extends BaseTabHandler {
	private static final String XML_EXPORT_CLIENTS_TAB = "/ui/plugins/paymentview/export/innertabs/tabexportclients.xml";
	private ClientDao clientDao;
	private Object clientsTab;

	public ExportClientsTabHandler(UiGeneratorController ui, ClientDao clientDao) {
		super(ui);
		this.clientDao = clientDao;
		init();
	}

	public void exportSelectedClients() {
		ui.add(new ExportByClientXticsStep1Handler(ui, clientDao).getDialog());
	}

	@Override
	protected Object initialiseTab() {
		clientsTab = ui.loadComponentFromFile(XML_EXPORT_CLIENTS_TAB, this);
		return clientsTab;
	}

	@Override
	public void refresh() {
	}
}
