package org.creditsms.plugins.paymentview.ui.handler;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.springframework.util.StringUtils;

public abstract class BaseClientTable implements PagedComponentItemProvider,
		ThinletUiEventHandler {
	protected ComponentPagingHandler clientsTablePager;
	protected UiGeneratorController ui;
	protected Object tableClients;
	protected String clientFilter = "";
	protected ClientDao clientDao;
	protected CustomFieldDao customFieldDao;
	protected CustomValueDao customValueDao;
	protected Object tableClientsPanel;

	public BaseClientTable(UiGeneratorController ui, ClientDao clientDao,
			CustomFieldDao customFieldDao, CustomValueDao customValueDao) {
		this.ui = ui;
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.customValueDao = customValueDao;

		this.init();
	}

	protected void init() {
		tableClientsPanel = ui.loadComponentFromFile(getClientsPanelFilePath(),
				this);
		tableClients = ui.find(tableClientsPanel, getClientsTableName());
		clientsTablePager = new ComponentPagingHandler(ui, this, tableClients);
		this.createHeader();
		this.ui.add(tableClientsPanel, this.clientsTablePager.getPanel());
	}

	protected abstract String getClientsTableName();

	protected abstract String getClientsPanelFilePath();

	protected Object[] toThinletComponents(List<Client> clients) {
		Object[] components = new Object[clients.size()];
		for (int i = 0; i < components.length; i++) {
			Client c = clients.get(i);
			components[i] = getRow(c);
		}
		return components;
	}

	protected PagedListDetails getClientListDetails(int startIndex, int limit) {
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

	public void setClientFilter(String clientFilter) {
		this.clientFilter = clientFilter;
		filterClients();
	}

	public void filterClients() {
		this.updateClientsList();
	}

	public void updateClientsList() {
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}

	protected Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		ui.add(row,
				ui.createTableCell(client.getFirstName() + " "
						+ client.getOtherName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));

		List<String> accountNumbers = new ArrayList<String>(client
				.getAccounts().size());

		for (Account a : client.getAccounts()) {
			accountNumbers.add(a.getAccountNumber());
		}

		ui.add(row, ui.createTableCell(StringUtils
				.arrayToCommaDelimitedString(accountNumbers.toArray())));
		return addCustomData(client, row);
	}

	protected Object addCustomData(Client client, Object row) {
		List<CustomField> allCustomFields = this.customFieldDao
				.getAllCustomFields();
		List<CustomValue> allCustomValues = this.customValueDao
				.getCustomValuesByClientId(client.getId());

		if (!allCustomFields.isEmpty()) {
			for (CustomField cf : allCustomFields) {
				for (CustomValue cv : allCustomValues) {
					if (cv.getCustomField().equals(cf)) {
						ui.add(row, ui.createTableCell(cv.getStrValue()));
					}
				}
			}
		}
		return row;
	}

	public void revalidateTable() {
		createHeader();
		refresh();
	}

	public void refresh() {
		this.updateClientsList();
	}

	protected String getClientFilter() {
		return clientFilter;
	}

	protected void createHeader() {
		ui.removeAll(tableClients);

		Object header = ui.createTableHeader();

		Object name = ui.createColumn("Name", "name");
		ui.setWidth(name, 200);
		ui.setIcon(name, Icon.CONTACT);
		ui.add(header, name);

		Object phone = ui.createColumn("Phone", "phone");
		ui.setWidth(phone, 150);
		ui.setIcon(phone, Icon.PHONE_NUMBER);
		ui.add(header, phone);

		Object accounts = ui.createColumn("Account(s)", "accounts");
		ui.setWidth(accounts, 100);
		ui.add(header, accounts);

		List<CustomField> allCustomFields = this.customFieldDao
				.getAllActiveUsedCustomFields();
		if (!allCustomFields.isEmpty()) {
			for (CustomField cf : allCustomFields) {
				Object column = ui.createColumn(cf.getReadableName(),
						cf.getCamelCaseName());
				ui.setWidth(column, 110);
				ui.add(header, column);
			}
		}
		ui.add(this.tableClients, header);
	}

	// > PAGING METHODS
	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.tableClients) {
			return getClientListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	public Object getClientsTablePanel() {
		return this.tableClientsPanel;
	}

	public void showConfirmationDialog(String methodToBeCalled) {
		ui.showConfirmationDialog(methodToBeCalled, this);
	}

	public Object showConfirmationDialog(String methodToBeCalled,
			String confirmationMessageKey) {
		return ui.showConfirmationDialog(methodToBeCalled, this,
				confirmationMessageKey);
	}

	public Object getClientsTable() {
		return this.tableClients;
	}
}
