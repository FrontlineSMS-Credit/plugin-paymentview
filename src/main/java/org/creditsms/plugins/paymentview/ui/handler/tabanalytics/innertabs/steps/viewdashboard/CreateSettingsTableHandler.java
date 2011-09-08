package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import static net.frontlinesms.FrontlineSMSConstants.PROPERTY_FIELD;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpateJob;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTable;

import thinlet.Thinlet;
import thinlet.ThinletText;

public class CreateSettingsTableHandler extends BaseClientTable{
	private static final String XML_VIEWDASHBOARD_CLIENTS_TABLE = "/ui/plugins/paymentview/analytics/viewdashboard/clientsTable.xml";
	private static final String TBL_CLIENTS = "tbl_clients";
	
	private TargetAnalytics targetAnalytics;
	private TargetDao targetDao;
	private Map<Client,ServiceItem> clients_targets;
	private DateFormat dateFormat = InternationalisationUtils.getDateFormat();
	private ServiceItemDao serviceItemDao;

	public CreateSettingsTableHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController,
			CreateSettingsHandler createSettingsHandler) {
		super(ui, pluginController);
		initDaos();
	}
	
	private void initDaos() {
		serviceItemDao = pluginController.getServiceItemDao();
		targetDao = pluginController.getTargetDao();
		
		targetAnalytics = new TargetAnalytics();
		targetAnalytics.setIncomingPaymentDao(pluginController.getIncomingPaymentDao());
		targetAnalytics.setTargetDao(targetDao);
		
		initSettingsTableForSorting();
	}
	
	private void initSettingsTableForSorting() {
		Object header = Thinlet.get(this.tableClients, ThinletText.HEADER);
		for (Object o : ui.getItems(header)) {
			String text = ui.getText(o);
			if(text != null) {
				if(text.equalsIgnoreCase("Name")) ui.putProperty(o, PROPERTY_FIELD, Client.Field.FIRST_NAME);
			}
		}
	}
	
	@Override
	protected String getClientsTableName() {
		return TBL_CLIENTS;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return XML_VIEWDASHBOARD_CLIENTS_TABLE;
	}
	
	@Override
	public void updateClientsList() {
		clients_targets = new HashMap<Client,ServiceItem>();
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}
	
	protected Object[] toThinletComponents(List<Client> clients) {
		List<Object> rows = new ArrayList<Object>();
		for(Client client: clients){
			for (ServiceItem si : serviceItemDao.getAllServiceItems()) {
				rows.addAll(getRows(client, si));
			}
		}
		return rows.toArray();
	}

	private List<Object> getRows(Client client, ServiceItem sItem) {
		List<Target> targets = targetDao.getActiveTargetsByServiceItemByClient(sItem.getId(), client.getId());
		List<Object> trgtRowLst = new ArrayList<Object>();
		
		for(Target target : targets){
			Object row = ui.createTableRow(client);		
			Object name = ui.createTableCell(client.getFullName());
			
			Object amountSaved = ui.createTableCell(targetAnalytics.getAmountSaved(target.getId()).toString());
			Object daysRemaining = ui.createTableCell(targetAnalytics.getDaysRemaining(target.getId()).toString());
			Object lastAmountPaid = ui.createTableCell(targetAnalytics.getLastAmountPaid(target.getId()).toString());
			Object percentageToGo = ui.createTableCell(targetAnalytics.getPercentageToGo(target.getId()).toString()+" %");
			Object lastDatePaid = "";
			
			if(targetAnalytics.getLastDatePaid(target.getId()) != null){
				lastDatePaid = ui.createTableCell(dateFormat.format(targetAnalytics.getLastDatePaid(target.getId())));
			}else{
				lastDatePaid = ui.createTableCell("No payment done yet");
			}

			String targetStatusStr = targetAnalytics.getStatus(target.getId()).toString();
			
			Object targetStatus = ui.createTableCell(targetStatusStr);
			Object savingsTarget = ui.createTableCell(target.getServiceItem().getAmount().toString());
			Object startDate = ui.createTableCell(dateFormat.format(target.getStartDate()));
			Object endDate = ui.createTableCell(dateFormat.format(target.getEndDate()));
			
			targetAnalytics.computeAnalyticsIntervalDatesAndSavings(target.getId());
		    Object monthlyAmountSaved = ui.createTableCell(targetAnalytics.getMonthlyAmountSaved().toString());
		    Object monthlyAmountDue = ui.createTableCell(targetAnalytics.getMonthlyAmountDue().toString());
		    
		    Object endOfMonthlyInterval = "";
			if(targetAnalytics.getEndMonthInterval() != null){
				endOfMonthlyInterval = ui.createTableCell(dateFormat.format(targetAnalytics.getEndMonthInterval()));
			}else{
				endOfMonthlyInterval = ui.createTableCell("Target end date passed");
			}
		 			 	
		 	targetAnalytics.clearAnalyticsComputations();
			ui.add(row, name);
			ui.add(row, ui.createTableCell(target.getServiceItem().getTargetName()));
			ui.add(row, startDate);
			ui.add(row, endDate);
			ui.add(row, savingsTarget);
			ui.add(row, amountSaved);
			ui.add(row, percentageToGo);
			ui.add(row, lastAmountPaid);
			ui.add(row, lastDatePaid);
			
			ui.add(row, monthlyAmountSaved);
			ui.add(row, monthlyAmountDue);
			ui.add(row, endOfMonthlyInterval);
			
			ui.add(row, daysRemaining);
			ui.add(row, targetStatus);
			
			trgtRowLst.add(row);
		}
		return trgtRowLst;
	}
	
	protected List<Client> getClients(String filter, int startIndex, int limit) {
		if (!filter.trim().isEmpty()) {
			totalItemCount  = this.clientDao.getClientsByFilter(filter).size();
			List<Client> clients = this.clientDao.getClientsByFilter(filter, startIndex, limit);
			
			for (ServiceItem si : this.serviceItemDao.getServiceItemsLikeName(filter)) {
				for(Target t : targetDao.getTargetsByServiceItem(si.getId())){
					Client client = t.getAccount().getClient();
					if (!clients.contains(client)){clients.add(client);}
				}
			}
			
			return clients;
		}else{
			if (clientListForAnalytics.isEmpty()){
				List<Client> activeClientsSorted = this.clientDao.getAllActiveClientsSorted
										(startIndex, limit, getClientsSortField(), getClientsSortOrder());
				totalItemCount = activeClientsSorted.size();
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
	
	public void notify(final FrontlineEventNotification notification) {
		super.notify(notification);
		new FrontlineUiUpateJob() {
			public void run() {
				if (!(notification instanceof DatabaseEntityNotification)) {
					return;
				}
				Object entity = ((DatabaseEntityNotification) notification).getDatabaseEntity();
				if (entity instanceof Target) {
					Target target = (Target) entity;
					if (notification instanceof EntitySavedNotification){
						Account a = target.getAccount();
						if ((a != null) & (a.getClient() != null)) {
							if (a.getClient().isActive()){
								clients_targets.put(a.getClient(), target.getServiceItem());
							}
						}
					}
					CreateSettingsTableHandler.this.refresh();
				}
			}
		}.execute();
	}
}