package org.creditsms.plugins.paymentview.ui.handler.tabexport;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseSelectClientTableHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs.ExportByClientXticsStep1Handler;

public class ExportClientsTabHandler extends BaseTabHandler {
	private static final String PNL_CLIENTS_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_EXPORT_CLIENTS_TAB = "/ui/plugins/paymentview/export/innertabs/tabexportclients.xml";
	private Object clientsTab;
	private BaseSelectClientTableHandler clientsTableHandler;

	private Object pnlClientsTableHolder;
	private PaymentViewPluginController pluginController;

	public ExportClientsTabHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;

		init();
		refresh();
	}

	@Override
	protected Object initialiseTab() {
		clientsTab = ui.loadComponentFromFile(XML_EXPORT_CLIENTS_TAB, this);
		clientsTableHandler = new ExportClientsTable(ui, pluginController);
		pnlClientsTableHolder = ui.find(clientsTab, PNL_CLIENTS_TABLE_HOLDER);
		this.ui.add(pnlClientsTableHolder,
				clientsTableHandler.getClientsTablePanel());

		return clientsTab;
	}

	@Override
	public void refresh() {
		this.clientsTableHandler.refresh();
	}

	public void exportSelectedClients() {
		ui.add(new ExportByClientXticsStep1Handler(ui, clientsTableHandler
						.getSelectedClients(), pluginController).getDialog());
	}

	public void selectAll() {
		clientsTableHandler.selectAll();
	}

}
