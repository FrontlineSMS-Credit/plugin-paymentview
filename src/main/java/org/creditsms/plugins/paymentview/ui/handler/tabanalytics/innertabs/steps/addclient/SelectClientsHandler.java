package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.AddClientTabHandler;

public class SelectClientsHandler extends BasePanelHandler implements
		PagedComponentItemProvider {
	private static final String ICONS_CHECKBOX_SELECTED_PNG = "/icons/checkbox-selected.png";
	private static final String ICONS_CHECKBOX_UNSELECTED_PNG = "/icons/checkbox-unselected.png";
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
	private static final String XML_STEP_SELECT_CLIENT = "/ui/plugins/paymentview/analytics/addclient/stepselectclients.xml";
	private static final String COMPONENT_PANEL_CLIENT_LIST = "pnl_clients";
	private Object clientsTableComponent;
	private String clientFilter = "";
	private ClientDao clientDao;
	private final AddClientTabHandler addClientTabHandler;
	private ComponentPagingHandler clientsTablePager;
	private Object pnlClientsList;

	protected SelectClientsHandler(UiGeneratorController ui,
			ClientDao clientDao, AddClientTabHandler addClientTabHandler) {
		super(ui);
		this.addClientTabHandler = addClientTabHandler;
		this.clientDao = clientDao;
		this.loadPanel(XML_STEP_SELECT_CLIENT);
		initialise();
		refresh();
	}

	private void initialise() {
		clientsTableComponent = ui.find(this.getPanelComponent(),
				COMPONENT_CLIENT_TABLE);
		clientsTablePager = new ComponentPagingHandler(
				(UiGeneratorController) ui, this, clientsTableComponent);
		pnlClientsList = ui.find(this.getPanelComponent(),
				COMPONENT_PANEL_CLIENT_LIST);
		this.ui.add(pnlClientsList, this.clientsTablePager.getPanel());
	}

	public void selectUsers(Object tbl_clients) {
		Object[] items = ui.getSelectedItems(tbl_clients);

		// TODO: Change Logic to:
		// Get a list of users (selected), so that on unselection,
		// do a set operation and show only those that have had a union
		// or intersection, or some sort of
		if (items.length > 1) {
			for (Object item : items) {
				Object cell = ui.getItem(item, 0);
				ui.remove(cell);
				Client attachedClient = (Client) ui.getAttachedObject(item);
				if (attachedClient.isSelected()) {
					attachedClient.setSelected(false);
					ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
				} else {
					attachedClient.setSelected(true);
					ui.setIcon(cell, ICONS_CHECKBOX_SELECTED_PNG);
				}

				ui.add(item, cell, 0);
			}
		}else if (items.length == 1) {
			Object item = items[0];
			Object cell = ui.getItem(item, 0);
			ui.remove(cell);
			Client attachedClient = (Client) ui.getAttachedObject(item);
			if (attachedClient.isSelected()) {
				attachedClient.setSelected(false);
				ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
			} else {
				attachedClient.setSelected(true);
				ui.setIcon(cell, ICONS_CHECKBOX_SELECTED_PNG);
			}

			ui.add(item, cell, 0);
		}
	}

	private void refresh() {
		this.updateClientList();
	}

	public void updateClientList() {
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
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

	@Override
	public Object getPanelComponent() {
		return super.getPanelComponent();
	}

	public void next() {
		addClientTabHandler.setCurrentStepPanel(new CreateSettingsHandler(
				(UiGeneratorController) ui, clientDao, addClientTabHandler)
				.getPanelComponent());
	}

	public void previous() {
		addClientTabHandler.setCurrentStepPanel(new SelectTargetSavingsHandler(
				(UiGeneratorController) ui, clientDao, addClientTabHandler)
				.getPanelComponent());
	}

	public void selectService() {
		previous();
	}

	public void targetedSavings() {
		previous();
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.clientsTableComponent) {
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
