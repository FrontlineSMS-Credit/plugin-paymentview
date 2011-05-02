package org.creditsms.plugins.paymentview.ui.handler;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.utils.StringUtil;

public abstract class BaseSelectClientTableHandler extends BaseClientTable {

	private static final String ICONS_CHECKBOX_SELECTED_PNG = "/icons/checkbox-selected.png";
	private static final String ICONS_CHECKBOX_UNSELECTED_PNG = "/icons/checkbox-unselected.png";
	protected List<Client> selectedUsers;

	public BaseSelectClientTableHandler(UiGeneratorController ui,
			ClientDao clientDao, CustomFieldDao customFieldDao,
			CustomValueDao customValueDao) {
		super(ui, clientDao, customFieldDao, customValueDao);
	}

	@Override
	protected void createHeader() {
		ui.removeAll(this.tableClients);
	
		Object header = ui.createTableHeader();
	
		Object select = ui.createColumn("Select", "select");
		ui.setWidth(select, 50);
		ui.add(header, select);
	
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
				.getAllActiveUsedCustomFields();
		if (!allCustomFields.isEmpty()) {
			for (CustomField cf : allCustomFields) {
				Object column = ui.createColumn(cf.getReadableName(),
						cf.getName());
				ui.setWidth(column, 110);
				ui.add(header, column);
			}
		}
		ui.add(this.tableClients, header);
	}

	@Override
	protected Object getRow(Client client) {
		Object row = ui.createTableRow(client);
	
		Object cell = ui.createTableCell(StringUtil.EMPTY);
		ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
		ui.add(row, cell);
	
		ui.add(row,
				ui.createTableCell(client.getFirstName() + StringUtil.SPACE
						+ client.getOtherName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));
		String accountStr = "";
		for (Account a : client.getAccounts()) {
			accountStr += a.getAccountNumber() + StringUtil.SPACE;
		}
		ui.add(row, ui.createTableCell(accountStr));
		ui.setAttachedObject(row, client);
		return addCustomData(client, row);
	}

	public void selectAll() {
		Object[] rows = ui.getItems(this.tableClients);
	
		for (Object row : rows) {
			Object cell = ui.getItem(row, 0);
			ui.remove(cell);
			Client attachedClient = (Client) ui.getAttachedObject(row);
			if (!attachedClient.isSelected()) {
				attachedClient.setSelected(true);
				ui.setIcon(cell, ICONS_CHECKBOX_SELECTED_PNG);
			}
	
			ui.add(row, cell, 0);
		}
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
			selectedUsers.remove(attachedClient);
			ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
		} else {
			attachedClient.setSelected(true);
			selectedUsers.add(attachedClient);
			ui.setIcon(cell, ICONS_CHECKBOX_SELECTED_PNG);
		}
	
		ui.add(row, cell, 0);
	}

	public List<Client> getSelectedUsers() {
		return this.selectedUsers;
	}

}