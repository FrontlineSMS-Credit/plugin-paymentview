package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class SelectClientsHandler extends BasePanelHandler implements
		PagedComponentItemProvider {
	private static final String ICONS_CHECKBOX_SELECTED_PNG = "/icons/checkbox-selected.png";
	private static final String ICONS_CHECKBOX_UNSELECTED_PNG = "/icons/checkbox-unselected.png";
	private static final String COMPONENT_TABLE_CLIENTS = "tbl_clients";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/viewdashboard/stepselectclients.xml";
	private static final String COMPONENT_PANEL_CLIENTS = "pnl_clients";
	private ClientDao clientDao;
	private ViewDashBoardTabHandler viewDashBoardTabHandler;
	private Object tblClients;
	private Object pnlClients;
	private ComponentPagingHandler clientsTablePager;
	private String clientFilter = "";
	private CustomFieldDao customFieldDao;
	private CustomValueDao customDataDao;

	protected SelectClientsHandler(UiGeneratorController ui,
			ClientDao clientDao, CustomFieldDao customFieldDao,
			CustomValueDao customDataDao,
			ViewDashBoardTabHandler viewDashBoardTabHandler) {
		super(ui);
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.customDataDao = customDataDao;
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.loadPanel(XML_STEP_SELECT_CLIENT);
		this.init();
		refresh();
	}

	private void init() {
		pnlClients = ui.find(this.getPanelComponent(), COMPONENT_PANEL_CLIENTS);
		tblClients = ui.find(this.getPanelComponent(), COMPONENT_TABLE_CLIENTS);
		clientsTablePager = new ComponentPagingHandler(
				(UiGeneratorController) ui, this, tblClients);

		this.ui.add(pnlClients, this.clientsTablePager.getPanel());
	}

	private Object createRow(Client c) {
		Object row = ui.createTableRow();
		ui.add(row,
				ui.createTableCell(c.getFirstName() + " " + c.getOtherName()));
		ui.add(row, ui.createTableCell(c.getPhoneNumber()));
		String accountStr = "";
		for (Account a : c.getAccounts()) {
			accountStr += (a.getAccountNumber()) + ", ";
		}
		ui.add(row, ui.createTableCell(accountStr));

		return row;
	}

	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		viewDashBoardTabHandler.setCurrentStepPanel(new CreateSettingsHandler(
				(UiGeneratorController) ui, clientDao, viewDashBoardTabHandler,
				customFieldDao, customDataDao).getPanelComponent());
	}

	public void previous() {
		viewDashBoardTabHandler
				.setCurrentStepPanel(new SelectTargetSavingsHandler(
						(UiGeneratorController) ui, clientDao, customFieldDao,
						customDataDao, viewDashBoardTabHandler).getPanelComponent());
	}

	public void refresh() {
		this.updateClientList();
	}

	private Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		Object cell = ui.createTableCell("");
		ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
		ui.add(row, cell);

		ui.add(row,
				ui.createTableCell(client.getFirstName() + " "
						+ client.getOtherName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));
		String accountStr = "";
		for (Account a : client.getAccounts()) {
			accountStr += a.getAccountNumber() + " ";
		}
		ui.add(row, ui.createTableCell(accountStr));
		ui.setAttachedObject(row, client);
		return row;
	}

	public void selectUsers(Object tbl_clients) {
		Object row = ui.getSelectedItem(tbl_clients);

		// TODO: Only Working partially with single selection;
		// Algo is not worth cracking the head

		Object cell = ui.getItem(row, 0);
		ui.remove(cell);
		Client attachedClient = (Client) ui.getAttachedObject(row);
		if (attachedClient.isSelected()) {
			attachedClient.setSelected(false);
			ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
		} else {
			attachedClient.setSelected(true);
			ui.setIcon(cell, ICONS_CHECKBOX_SELECTED_PNG);
		}

		ui.add(row, cell, 0);
	}

	public void updateClientList() {
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.tblClients) {
			return getClientListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	private PagedListDetails getClientListDetails(int startIndex, int limit) {
		List<Client> clients = null;
		if (this.clientFilter.equals("")) {
			clients = this.clientDao.getAllClients();
		} else {
			clients = this.clientDao.getClientsByName(clientFilter);
		}

		int totalItemCount = this.clientDao.getClientCount();
		Object[] listItems = toThinletComponents(clients);

		return new PagedListDetails(totalItemCount, listItems);
	}

	private Object[] toThinletComponents(List<Client> clients) {
		Object[] components = new Object[clients.size()];
		for (int i = 0; i < components.length; i++) {
			Client c = clients.get(i);
			components[i] = getRow(c);
		}
		return components;
	}
}
