package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import java.text.SimpleDateFormat;
import java.util.List;

import net.frontlinesms.ui.Icon;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTableHandler;

public class ReviewClientTableHandler extends BaseClientTableHandler{
	private static final String LBL_END_DATE = "endDate";
	private static final String LBL_START_DATE = "startDate";
	private static final String LBL_TO_SAVE = "toSave";
	private static final String LBL_CLIENT_NAME = "clientName";
	
	private static final String ENDING_ON = "Ending on: ";
	private static final String STARTING_ON = "Starting on: ";
	private static final String TO_SAVE = "To save: ";
	private static final String NAME = "Client Name: ";
	
	private static final String PNL_TBL_CLIENT_LIST = "tbl_clientList";
	private static final String XML_CLIENTS_PANEL = "/ui/plugins/paymentview/analytics/addclient/reviewclienttable.xml";
	private final ReviewHandler reviewHandler;
	
	public ReviewClientTableHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController,
			ReviewHandler reviewHandler) {
		super(ui, pluginController);
		this.reviewHandler = reviewHandler;
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
		
		Object products = ui.createColumn("Product(s)", "product");
		ui.setWidth(products, 450);
		ui.add(header, products);
		
		//ui.add(header, ui.createColumn("Product(s)", "product"));
		
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
	
	protected Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		ui.add(row, ui.createTableCell(client.getFullName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));
		
		String neededitems = "";
		
		for(TargetServiceItem tsi: reviewHandler.getSelectedServiceItems()){
			if (neededitems.length()==0) {
				neededitems = tsi.getServiceItem().getTargetName();
			} else {
				neededitems = neededitems+", "+tsi.getServiceItem().getTargetName();
			}
		}
		
		ui.add(row, ui.createTableCell(neededitems));
		return addCustomData(client, row);
	}
	
	@Override
	protected String getClientsTableName() {
		return PNL_TBL_CLIENT_LIST;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return XML_CLIENTS_PANEL;
	}
	
	private static final SimpleDateFormat sdf =  new SimpleDateFormat("d/M/yy hh:mm a");
	
	public void showFooter() {
		if (ui.getSelectedIndex(this.tableClients) >= 0){
			Object selectedItem = ui.getSelectedItem(this.tableClients);
			Client attachedClient = ui.getAttachedObject(selectedItem, Client.class);
			
			ui.setText(ui.find(LBL_CLIENT_NAME), NAME + attachedClient.getFullName());
			ui.setText(ui.find(LBL_TO_SAVE) , TO_SAVE + reviewHandler.getTotalAmount());
			ui.setText(ui.find(LBL_START_DATE) , STARTING_ON + sdf.format(reviewHandler.getStartDate()));
			ui.setText(ui.find(LBL_END_DATE) , ENDING_ON + sdf.format(reviewHandler.getEndDate()));
			
		}else{
			Client fstClient = reviewHandler.getSelectedClients().get(0);
			ui.setText(ui.find(LBL_CLIENT_NAME), NAME + fstClient.getFirstName());
			ui.setText(ui.find(LBL_TO_SAVE) , TO_SAVE + reviewHandler.getTotalAmount());
			ui.setText(ui.find(LBL_START_DATE) , STARTING_ON + sdf.format(reviewHandler.getStartDate()));
			ui.setText(ui.find(LBL_END_DATE) , ENDING_ON + sdf.format(reviewHandler.getEndDate()));
		}
	}
}
