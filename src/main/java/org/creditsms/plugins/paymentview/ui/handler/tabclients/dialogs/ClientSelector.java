/**
 * 
 */
package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import static net.frontlinesms.FrontlineSMSConstants.PROPERTY_FIELD;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.domain.Contact;
import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTable;

import thinlet.Thinlet;

public class ClientSelector extends BaseClientTable{
	
//> UI CONSTANTS
	private static final String UI_FILE_CLIENT_SELECTOR = "/ui/core/contacts/dgContactSelecter.xml";
	public static final String COMPONENT_CLIENT_SELECTOR_OK_BUTTON = "contactSelecter_okButton";
	public static final String COMPONENT_CLIENT_SELECTOR_CLIENT_LIST = "contactSelecter_contactList";
	
//> INSTANCE PROPERTIES
	private Object selectorDialog;

//> CONSTRUCTORS
	public ClientSelector(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui, pluginController);
		this.clientDao = pluginController.getClientDao();
	}
	
	/** @see UiGeneratorController#removeDialog(Object) */
	public void removeDialog() {
		ui.removeDialog(this.selectorDialog);
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
	
	@Override
	protected Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		ui.add(row,
				ui.createTableCell(client.getFirstName() + " "
						+ client.getOtherName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));
		return row;

	}

	@Override
	protected String getClientsTableName() {
		return null;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return null;
	}
}
