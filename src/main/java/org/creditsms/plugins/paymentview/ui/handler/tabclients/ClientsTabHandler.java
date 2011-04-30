package org.creditsms.plugins.paymentview.ui.handler.tabclients;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.BaseTabHandler;
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
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.CustomizeClientDBHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.EditClientHandler;
import org.springframework.util.StringUtils;

public class ClientsTabHandler extends BaseTabHandler implements
		PagedComponentItemProvider {
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clientList";
	private static final String COMPONENT_PANEL_CLIENT_LIST = "pnl_tbl_clientList";
	// > STATIC CONSTANTS
	private static final String XML_CLIENTS_TAB = "/ui/plugins/paymentview/clients/clients.xml";
	// > INSTANCE PROPERTIES
	private ClientDao clientDao;

	private String clientFilter;

	private Object clientsTableComponent;
	private ComponentPagingHandler clientsTablePager;
	private CustomFieldDao customFieldDao;
	private Object pnlClientsList;
	private PaymentViewThinletTabController paymentViewThinletTabController;
	private CustomValueDao customValueDao;

	public ClientsTabHandler(
			UiGeneratorController ui,
			final PaymentViewThinletTabController paymentViewThinletTabController) {
		super(ui);
		this.paymentViewThinletTabController = paymentViewThinletTabController;
		this.clientFilter = "";
		this.clientDao = this.paymentViewThinletTabController.getClientDao();
		this.customFieldDao = this.paymentViewThinletTabController
				.getCustomFieldDao();
		this.customValueDao = this.paymentViewThinletTabController
				.getCustomValueDao();
		init();
	}

	@Override
	protected Object initialiseTab() {
		Object clientsTab = ui.loadComponentFromFile(XML_CLIENTS_TAB, this);
		clientsTableComponent = ui.find(clientsTab, COMPONENT_CLIENT_TABLE);
		this.createHeader();
		clientsTablePager = new ComponentPagingHandler(ui, this,
				clientsTableComponent);
		pnlClientsList = ui.find(clientsTab, COMPONENT_PANEL_CLIENT_LIST);
		this.ui.add(pnlClientsList, this.clientsTablePager.getPanel());
		return clientsTab;
	}

	@Override
	public void refresh() {
		this.updateContactList();
	}

	public void addClient() {
		ui.add(new EditClientHandler(ui, clientDao, this, customValueDao,
				customFieldDao).getDialog());
	}

	public void analyseClient() {
		// TODO Auto-generated method stub
	}

	// > EVENTS...
	public void customizeClientDB() {
		ui.add(new CustomizeClientDBHandler(ui, customFieldDao, this).getDialog());
	}

	public void deleteClient() {
		Object[] selectedClients = this.ui
				.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = ui.getAttachedObject(selectedClient, Client.class);
			clientDao.deleteClient(c);
		}

		ui.removeDialog(ui
				.find(UiGeneratorControllerConstants.COMPONENT_CONFIRM_DIALOG));
		ui.infoMessage("You have succesfully deleted from the client!");
		this.refresh();
	}

	public void editClient() {
		Object[] selectedClients = this.ui
				.getSelectedItems(clientsTableComponent);
		for (Object selectedClient : selectedClients) {
			Client c = (Client) ui.getAttachedObject(selectedClient);
			ui.add(new EditClientHandler(ui, c, clientDao, this,
					customValueDao, customFieldDao).getDialog());
		}
	}

	public void exportClient() {
		new ClientExportHandler(ui, clientDao).showWizard();
		this.refresh();
	}

	public void filterClients() {
		this.updateContactList();
	}

	/**
	 * @return the clientFilter
	 */
	public String getClientFilter() {
		return clientFilter;
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

	// > PAGING METHODS
	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.clientsTableComponent) {
			return getClientListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	public Object getRow(Client client) {
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
	
	public void revalidateTable(){
		createHeader();
		refresh();
	}

	// > CUSTOM DATA
	private void createHeader() {
		ui.removeAll(clientsTableComponent);
		
		Object header = ui.createTableHeader();

		Object name = ui.createColumn("Name", "name");
		ui.setWidth(name, 200);
		ui.setIcon(name, "/icons/user.png");
		ui.add(header, name);

		Object phone = ui.createColumn("Phone", "phone");
		ui.setWidth(phone, 150);
		ui.setIcon(phone, "/icons/phone.png");
		ui.add(header, phone);

		Object accounts = ui.createColumn("Account(s)", "accounts");
		ui.setWidth(accounts, 100);
		ui.add(header, accounts);

		List<CustomField> allCustomFields = this.customFieldDao
				.getAllActiveCustomFields();
		if (!allCustomFields.isEmpty()) {
			for (CustomField cf : allCustomFields) {
				if (cf.isActive() & cf.isUsed()) {
					Object column = ui.createColumn(cf.getReadableName(),
							cf.getName());
					ui.setWidth(column, 110);
					ui.add(header, column);
				}
			}
		}
		ui.add(this.clientsTableComponent, header);
	}

	private Object addCustomData(Client client, Object row) {
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

	// > ACTION HANDLERS
	public void importClient() {
		new ClientImportHandler(ui, this, clientDao, this.customFieldDao,
				this.customValueDao).showWizard();
		this.refresh();
	}

	// > PAGINATION HANDLERS
	/**
	 * @param clientFilter
	 *            the clientFilter to set
	 */
	public void setClientFilter(String clientFilter) {
		this.clientFilter = clientFilter;
		filterClients();
	}

	private Object[] toThinletComponents(List<Client> clients) {
		Object[] components = new Object[clients.size()];
		for (int i = 0; i < components.length; i++) {
			Client c = clients.get(i);
			components[i] = getRow(c);
		}
		return components;
	}

	public void updateContactList() {
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}
}
