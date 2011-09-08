package org.creditsms.plugins.paymentview.ui.handler.base;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;

public abstract class BaseSelectClientTableHandler extends BaseClientTable {

	protected static final String ICONS_CHECKBOX_SELECTED_PNG = "/icons/checkbox-selected.png";
	protected static final String ICONS_CHECKBOX_UNSELECTED_PNG = "/icons/checkbox-unselected.png";

	protected List<Client> selectedClients;
	
	public BaseSelectClientTableHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui, pluginController);		
		selectedClients = new ArrayList<Client>(); 
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

	@Override
	protected Object getRow(Client client) {
		Object row = ui.createTableRow(client);
	
		Object cell = ui.createTableCell("");
		markCell(cell, false);
		ui.add(row, cell);
	
		ui.add(row,
				ui.createTableCell(client.getFirstName() + " "
						+ client.getOtherName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));
		
		ui.setAttachedObject(row, client);
		return addCustomData(client, row);
	}
	
	public void selectAll(){
		markAllCells(true);
	}
	
	public void unSelectAll() {
		markAllCells(false);
	}

	private void markAllCells(boolean choice) {
		Object[] rows = ui.getItems(this.tableClients);
	
		for (Object row : rows) {
			Object cell = ui.getItem(row, 0);
			ui.remove(cell);
			Client attachedClient = (Client) ui.getAttachedObject(row);
			if (!attachedClient.isSelected()) {
				attachedClient.setSelected(choice);
				markCell(cell, choice);
			}
	
			ui.add(row, cell, 0);
		}
	}
	
	protected void markCell(Object cell, boolean choice){
		if(choice){
			ui.setIcon(cell, ICONS_CHECKBOX_SELECTED_PNG);
		}else{
			ui.setIcon(cell, ICONS_CHECKBOX_UNSELECTED_PNG);
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
			selectedClients.remove(attachedClient);
			markCell(cell, false);
		} else {
			attachedClient.setSelected(true);
			selectedClients.add(attachedClient);
			markCell(cell, true);
		}
	
		ui.add(row, cell, 0);
	}

	public List<Client> getSelectedClients() {
		return this.selectedClients;
	}

}