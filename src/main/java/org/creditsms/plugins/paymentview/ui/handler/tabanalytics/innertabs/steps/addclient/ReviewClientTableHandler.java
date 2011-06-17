package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.addclient;

import java.text.SimpleDateFormat;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.BaseClientTable;

public class ReviewClientTableHandler extends BaseClientTable{
	private static final String LBL_END_DATE = "endDate";
	private static final String LBL_START_DATE = "startDate";
	private static final String LBL_TO_SAVE = "toSave";
	private static final String LBL_CLIENT_NAME = "clientName";
	
	private static final String EMPTY = "";
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
			
			ui.setText(ui.find(LBL_CLIENT_NAME), NAME + attachedClient.getName());
			ui.setText(ui.find(LBL_TO_SAVE) , TO_SAVE + reviewHandler.getSelectedServiceItem().getAmount().toString());
			ui.setText(ui.find(LBL_START_DATE) , STARTING_ON + sdf.format(reviewHandler.getStartDate()));
			ui.setText(ui.find(LBL_END_DATE) , ENDING_ON + sdf.format(reviewHandler.getEndDate()));
			
		}else{
			ui.setText(ui.find(LBL_CLIENT_NAME), EMPTY);
			ui.setText(ui.find(LBL_TO_SAVE) , EMPTY);
			ui.setText(ui.find(LBL_START_DATE) , EMPTY);
			ui.setText(ui.find(LBL_END_DATE) , EMPTY);
		}
		
	}
}
