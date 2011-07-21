package org.creditsms.plugins.paymentview.ui.handler;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;

public abstract class BaseClientTable implements PagedComponentItemProvider,
		ThinletUiEventHandler, EventObserver {
	protected ComponentPagingHandler clientsTablePager;
	protected UiGeneratorController ui;
	protected Object tableClients;
	protected String clientFilter = "";
	protected ClientDao clientDao;
	protected CustomFieldDao customFieldDao;
	protected CustomValueDao customValueDao;
	protected AccountDao accountDao;
	protected Object tableClientsPanel;
	protected List<Client> clients;

	public BaseClientTable(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		this.ui = ui;
		this.clientDao = pluginController.getClientDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		this.customValueDao = pluginController.getCustomValueDao();
		this.accountDao = pluginController.getAccountDao();
		
		ui.getFrontlineController().getEventBus().registerObserver(this);
		
		this.clients = new ArrayList<Client>();		
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
		clients = getClients(clientFilter, startIndex, limit);
		return new PagedListDetails(clients.size(), toThinletComponents(clients));
	}

	protected List<Client> getClients(String filter, int startIndex, int limit) {
		if (clients.isEmpty()){
			if (!filter.trim().isEmpty()) {
				return this.clientDao.getClientsByName(filter, startIndex, limit);
			}else{
				return this.clientDao.getAllActiveClients(startIndex, limit);
			}
		}else{
			//FIXME: Make this stable
			if (filter.trim().isEmpty()) {
				if (clientsTablePager.getMaxItemsPerPage() < clients.size()){
					return clients.subList(startIndex, limit);
				}
				return clients;
			}else{
				List<Client> subList = null;
				
				if (clientsTablePager.getMaxItemsPerPage() < clients.size()){
					subList = clients.subList(startIndex, limit);
				}else{
					subList = clients;
				}
				
				List<Client> temp = new ArrayList<Client>(); 
				for (Client c : subList) {
					if (c.getFullName().equalsIgnoreCase(filter)) {
						temp.add(c);
					}
				}
				return temp;
			}
		}
	}

	public void setClientFilter(String clientFilter) {
		this.clientFilter = clientFilter;
	}

	public void filterClients(String clientFilter) {
		setClientFilter(clientFilter);
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

		return addCustomData(client, row);
	}

	protected List<String> getAccounts(Client client) {
		List<String> accountNumbers = new ArrayList<String>();
		for (Account a : this.accountDao.getAccountsByClientId(client.getId())) {
			accountNumbers.add(a.getAccountNumber());
		}
		return accountNumbers;
	}

	protected Object addCustomData(Client client, Object row) {
		List<CustomField> allCustomFields = this.customFieldDao
				.getAllCustomFields();
		List<CustomValue> allCustomValues = this.customValueDao
				.getCustomValuesByClientId(client.getId());

		if (!allCustomFields.isEmpty()) {
			for (CustomField cf : allCustomFields) {
				if (cf.isUsed() & cf.isActive()){
					for (CustomValue cv : allCustomValues) {
						if (cv.getCustomField().equals(cf)) {
							ui.add(row, ui.createTableCell(cv.getStrValue()));
						}
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

	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<Client> clients) {
		this.clients = clients;
		this.refresh();
	}
	
	public void notify(final FrontlineEventNotification notification) {
		new FrontlineUiUpateJob() {
			public void run() {
				if (!(notification instanceof DatabaseEntityNotification)) {
					return;
				}
		
				Object entity = ((DatabaseEntityNotification) notification).getDatabaseEntity();
				if (entity instanceof Client || entity instanceof Account) {
					BaseClientTable.this.refresh();
				}else if (entity instanceof CustomField) {
					revalidateTable();
				}
			}
		}.execute();
	}
}
