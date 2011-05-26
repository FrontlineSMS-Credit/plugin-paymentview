package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateAlertHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class CreateSettingsHandler extends BasePanelHandler {
	private static final String TBL_CLIENTS = "tbl_clients";
	private static final String XML_STEP_VIEW_CLIENTS = "/ui/plugins/paymentview/analytics/viewdashboard/stepviewclients.xml";
	private ViewDashBoardTabHandler viewDashBoardTabHandler;
	private PaymentViewPluginController pluginController;
	private SelectClientsHandler previousSelectClientsHandler;
	private Object tblClients;
	private TargetAnalytics targetAnalytics;

	CreateSettingsHandler(UiGeneratorController ui,
			ViewDashBoardTabHandler viewDashBoardTabHandler,
			PaymentViewPluginController pluginController, SelectClientsHandler selectClientsHandler) {
		super(ui);
		this.viewDashBoardTabHandler = viewDashBoardTabHandler;
		this.pluginController = pluginController;
		this.previousSelectClientsHandler = selectClientsHandler;
		
		targetAnalytics = new TargetAnalytics();
		targetAnalytics.setIncomingPaymentDao(pluginController.getIncomingPaymentDao());
		targetAnalytics.setTargetDao(pluginController.getTargetDao());
		
		init();
	}

	private void init() {
		this.loadPanel(XML_STEP_VIEW_CLIENTS);
		tblClients = ui.find(this.getPanelComponent(), TBL_CLIENTS);
		List<Client> selectedClients = previousSelectClientsHandler.getSelectedClients();
		createRows(selectedClients);
	}

	private void createRows(List<Client> selectedClients) {
		for (Client client : selectedClients) {
			ui.add(tblClients, getRow(client));
		}
	}

	private Object getRow(Client client) {
		DateFormat dateFormat = InternationalisationUtils.getDateFormat();
		
		Object row = ui.createTableRow(client);		
		Object name = ui.createTableCell(client.getName());
		Map<Client, Target> clients_targets = previousSelectClientsHandler.getClients_targets();
		Target target = clients_targets.get(client);
		
		Object amountSaved = ui.createTableCell(targetAnalytics.getAmountSaved(target.getId()).toString());
		Object daysRemaining = ui.createTableCell(targetAnalytics.getDaysRemaining(target.getId()).toString());
		Object lastAmountPaid = ui.createTableCell(targetAnalytics.getLastAmountPaid(target.getId()).toString());
		Object percentageToGo = ui.createTableCell(targetAnalytics.getPercentageToGo(target.getId()).toString()+" %");
		Object lastDatePaid = "";
		if(targetAnalytics.getLastDatePaid(target.getId())!=null){
			lastDatePaid = ui.createTableCell(dateFormat.format(targetAnalytics.getLastDatePaid(target.getId())));
		}else{
			lastDatePaid = ui.createTableCell("No payment done yet");
		}

		String targetStatusStr = "";
		int targetStatusRC =0;
		targetStatusRC = targetAnalytics.isStatusGood(target.getId());
		
		if(targetStatusRC==0){
			targetStatusStr = "delayed";
		}else if(targetStatusRC==1){
			targetStatusStr = "on track";
		}else if(targetStatusRC==2){
			targetStatusStr = "completed";
		}
		Object targetStatus = ui.createTableCell(targetStatusStr);
		Object savingsTarget = ui.createTableCell(target.getServiceItem().getAmount().toString());
		Object startDate = ui.createTableCell(dateFormat.format(target.getStartDate()));
		Object endDate = ui.createTableCell(dateFormat.format(target.getEndDate()));
		
		ui.add(row, name);
		ui.add(row, startDate);
		ui.add(row, endDate);
		ui.add(row, savingsTarget);
		ui.add(row, amountSaved);
		ui.add(row, percentageToGo);
		ui.add(row, lastAmountPaid);
		ui.add(row, lastDatePaid);
		ui.add(row, daysRemaining);
		ui.add(row, targetStatus);
		
		return row;
	}

	public void createAlert() {
		ui.add(new CreateAlertHandler((UiGeneratorController) ui).getDialog());
	}

	public void export() {
		new ClientExportHandler((UiGeneratorController) ui, pluginController).showWizard();
	}

	public void previous() {
		viewDashBoardTabHandler.setCurrentStepPanel(previousSelectClientsHandler.getPanelComponent());
	}
	
	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}
}
