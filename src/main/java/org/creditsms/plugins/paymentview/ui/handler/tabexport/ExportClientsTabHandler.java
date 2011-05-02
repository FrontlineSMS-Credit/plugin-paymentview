package org.creditsms.plugins.paymentview.ui.handler.tabexport;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseSelectClientTableHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs.ExportByClientXticsStep1Handler;

public class ExportClientsTabHandler extends BaseTabHandler {
	private static final String PNL_CLIENTS_TABLE_HOLDER = "pnlClientsTableHolder";
	private static final String XML_EXPORT_CLIENTS_TAB = "/ui/plugins/paymentview/export/innertabs/tabexportclients.xml";
	private ClientDao clientDao;
	private Object clientsTab;
	private BaseSelectClientTableHandler clientsTableHandler;	

	private CustomFieldDao customFieldDao;
	private CustomValueDao customValueDao;
	private Object pnlClientsTableHolder;

	public ExportClientsTabHandler(UiGeneratorController ui,
			ClientDao clientDao, CustomFieldDao customFieldDao,
			CustomValueDao customValueDao) {
		super(ui);
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.customValueDao = customValueDao;
		
		init();
		refresh();
	}

	@Override
	protected Object initialiseTab() {
		clientsTab = ui.loadComponentFromFile(XML_EXPORT_CLIENTS_TAB, this);

		clientsTableHandler = new ExportClientsTable(ui, clientDao,
				customFieldDao, customValueDao);
		
		pnlClientsTableHolder = ui.find(clientsTab, PNL_CLIENTS_TABLE_HOLDER);
		this.ui.add(pnlClientsTableHolder, clientsTableHandler.getClientsTablePanel());
		
		return clientsTab;
	}

	@Override
	public void refresh() {
		this.clientsTableHandler.updateClientsList();
	}

	public void exportSelectedClients() {
		ui.add(new ExportByClientXticsStep1Handler(ui, clientDao,
				customFieldDao, customValueDao, clientsTableHandler.getSelectedUsers()).getDialog());
	}

	public void selectAll() {
		clientsTableHandler.selectAll();
	}
	
	
}
