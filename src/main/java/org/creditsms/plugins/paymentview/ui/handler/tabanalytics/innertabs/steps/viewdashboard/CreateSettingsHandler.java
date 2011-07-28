package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;
import net.frontlinesms.ui.handler.BasePanelHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateAlertHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.ViewDashBoardTabHandler;

public class CreateSettingsHandler extends BasePanelHandler implements EventObserver  {
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
		ui.getFrontlineController().getEventBus().registerObserver(this);
		init();
	}

	private void init() {
		this.loadPanel(XML_STEP_VIEW_CLIENTS);
		tblClients = ui.find(this.getPanelComponent(), TBL_CLIENTS);
		refresh();
	}
	
	public void refresh() {
		ui.removeAll(tblClients);
		List<Client> selectedClients = previousSelectClientsHandler.getSelectedClients();
		createRows(selectedClients);
	}

	private void createRows(List<Client> selectedClients) {
		for (Client client : selectedClients) {
			List<Object> analytic_rows = getRow(client);
			for(int y = 0; y<analytic_rows.size();y++){
				ui.add(tblClients, analytic_rows.get(y));
			}
		}
	}

	private List<Object> getRow(Client client) {
		DateFormat dateFormat = InternationalisationUtils.getDateFormat();
		
		Map<Client, ServiceItem> clients_targets = previousSelectClientsHandler.getClients_targets();
		ServiceItem sItem = clients_targets.get(client);
		List<Target> targetLst = pluginController.getTargetDao().
			getTargetsByServiceItemByClient(sItem.getId(), client.getId());
		
		List<Object> trgtRowLst = new ArrayList<Object>();
		
		for(Target target : targetLst){
			
			Object row = ui.createTableRow(client);		
			Object name = ui.createTableCell(client.getFullName());
			
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

			String targetStatusStr = targetAnalytics.getStatus(target.getId()).toString();
			
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
			
			trgtRowLst.add(row);
		}
		return trgtRowLst;
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
	
	public void notify(final FrontlineEventNotification notification) {
		new FrontlineUiUpateJob() {
			public void run() {
				if (!(notification instanceof DatabaseEntityNotification)) {
					return;
				} else {
					if (notification instanceof DatabaseEntityNotification){
						Object entity = ((DatabaseEntityNotification) notification).getDatabaseEntity();
						if (entity instanceof IncomingPayment ) {
							CreateSettingsHandler.this.refresh();
						}
					}
				}
			}
		}.execute();
	}
}
