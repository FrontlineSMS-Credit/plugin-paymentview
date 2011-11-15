package org.creditsms.plugins.paymentview.ui.handler.base;

import static net.frontlinesms.FrontlineSMSConstants.PROPERTY_FIELD;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.Order;
import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpdateJob;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Client.Field;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;

import thinlet.Thinlet;
import thinlet.ThinletText;

public abstract class BaseClientTableHandler implements PagedComponentItemProvider,
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
	protected List<Client> clientListForAnalytics;
	protected int totalItemCount = 0;
	
	protected final PaymentViewPluginController pluginController;

	public BaseClientTableHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		this.ui = ui;
		this.pluginController = pluginController;
		this.clientDao = pluginController.getClientDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		this.customValueDao = pluginController.getCustomValueDao();
		this.accountDao = pluginController.getAccountDao();
		
		ui.getFrontlineController().getEventBus().registerObserver(this);
		
		this.clientListForAnalytics = new ArrayList<Client>();		
		this.init();
	}

	protected void init() {
		tableClientsPanel = ui.loadComponentFromFile(getClientsPanelFilePath(), this);
		tableClients = ui.find(tableClientsPanel, getClientsTableName());
		clientsTablePager = new ComponentPagingHandler(ui, this, tableClients);
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
		List<Client> clients = new ArrayList<Client>();
		clients = getClients(clientFilter, startIndex, limit);
		Object[] listItems = toThinletComponents(clients);
		return new PagedListDetails(totalItemCount, listItems);

	}

	protected List<Client> getClients(String clientFilter, int startIndex, int limit) {
		if (!clientFilter.trim().isEmpty()) {
			totalItemCount  = this.clientDao.getClientsByFilter(clientFilter).size();
			return this.clientDao.getClientsByFilter(clientFilter, startIndex, limit);
		}else{
			if (clientListForAnalytics.isEmpty()){
				totalItemCount = this.clientDao.getAllActiveClients().size();
				List<Client> activeClientsSorted = this.clientDao.getAllActiveClientsSorted
										(startIndex, limit, getClientsSortField(), getClientsSortOrder());
				return activeClientsSorted;
			} else {
				totalItemCount = clientListForAnalytics.size();
				if (clientsTablePager.getMaxItemsPerPage() < clientListForAnalytics.size()){					
					if( (startIndex+limit) < clientListForAnalytics.size()){
						return clientListForAnalytics.subList(startIndex, startIndex+limit);
					} else {
						return clientListForAnalytics.subList(startIndex, clientListForAnalytics.size());
					}
				}else{
					return clientListForAnalytics;
				}
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
		this.createHeader();
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}

	protected Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		ui.add(row, ui.createTableCell(client.getFullName()));
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
		if (!this.customFieldDao.getAllCustomFields().isEmpty()) {
			for (CustomField cf : this.customFieldDao
					.getAllCustomFields()) {
				if (cf.isUsed() & cf.isActive()){
					for (CustomValue cv : this.customValueDao.getCustomValuesByClientId(client.getId())) {
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

	public String getClientFilter() {
		return clientFilter;
	}
	
	protected void createHeader() {
		ui.removeAll(tableClients);
		
		Object header = ui.createTableHeader();

		Object name = ui.createColumn("Name", "name");
		ui.putProperty(name, PROPERTY_FIELD, Client.Field.FIRST_NAME);
		ui.setWidth(name, 200);
		//ui.setString(name, Thinlet.SORT, Thinlet.DESCENT);
		ui.setIcon(name, Icon.CONTACT);
		ui.add(header, name);

		Object phone = ui.createColumn("Phone", "phone");
		ui.putProperty(phone, PROPERTY_FIELD, Client.Field.PHONE_NUMBER);
		ui.setWidth(phone, 150);
		ui.setIcon(phone, Icon.PHONE_NUMBER);
		ui.add(header, phone);
		
		List<CustomField> allCustomFields = this.customFieldDao
				.getAllActiveUsedCustomFields();
		
		if (!allCustomFields.isEmpty()) {
			for (CustomField cf : allCustomFields) {
				Object column = ui.createColumn(cf.getReadableName(),
						cf.getCamelCaseName());
				ui.putProperty(column, PROPERTY_FIELD, cf.getCamelCaseName());
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
	
	public Object[] getSelectedRows() {
		return ui.getSelectedItems(tableClients);
	}
	
	public List<Client> getSelectedClients() {
		Object[] rows = getSelectedRows();
		List<Client> clients = new ArrayList<Client>(rows.length);
		for (Object o : rows) {
			clients.add(ui.getAttachedObject(o, Client.class));
		}
		return clients;
	}
	
	/**
	 * @param clients the clients to set
	 */
	public void setClients(List<Client> clients) {
		this.clientListForAnalytics = clients;
		this.refresh();
	}
	
	public List<Client> getClients() {
		return clientListForAnalytics;
	}
	
	public void setTotalItemCount(int totalItemCount) {
		this.totalItemCount = totalItemCount;
	}
	
	public void notify(final FrontlineEventNotification notification) {
		if (notification instanceof DatabaseEntityNotification) {
			Object entity = ((DatabaseEntityNotification<?>) notification).getDatabaseEntity();
			if (entity instanceof Client || entity instanceof Account) {
				new FrontlineUiUpdateJob() {
					public void run() {
						refresh();
					}
				}.execute();
			}else if (entity instanceof CustomField) {
				new FrontlineUiUpdateJob() {
					public void run() {
				revalidateTable();
					}
				}.execute();
			}
		}
	}
	
	/** @return the field to sort clients in the client list by */
	protected Field getClientsSortField() {
		Object header = Thinlet.get(this.tableClients, ThinletText.HEADER);
		Object tableColumn = ui.getSelectedItem(header);
		Client.Field field = Client.Field.FIRST_NAME;
		if (tableColumn != null) {
			field = (Client.Field) ui.getProperty(tableColumn, PROPERTY_FIELD);
		}
		
		return field;
	}
	
	/** @return the sorting order for the message list */
	protected Order getClientsSortOrder() {
		Object header = Thinlet.get(this.tableClients, ThinletText.HEADER);
		Object tableColumn = ui.getSelectedItem(header);
		Order order = Order.ASCENDING;
		if (tableColumn != null) {
			order = Thinlet.get(tableColumn, ThinletText.SORT).equals(ThinletText.ASCENT) ? Order.ASCENDING : Order.DESCENDING;
		}
		return order;
	}
	
	
	public void setSelectionMethod(String selection) {
		ui.setChoice(this.tableClients, Thinlet.SELECTION, selection);
	}
}
