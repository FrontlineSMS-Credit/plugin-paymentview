package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard;

import static net.frontlinesms.FrontlineSMSConstants.PROPERTY_FIELD;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpdateJob;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.TargetServiceItem;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.data.repository.TargetServiceItemDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTableHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs.steps.viewdashboard.dialogs.EditTargetHandler;

import thinlet.Thinlet;
import thinlet.ThinletText;

public class CreateSettingsTableHandler extends BaseClientTableHandler implements EventObserver{
	private static final String XML_VIEWDASHBOARD_CLIENTS_TABLE = "/ui/plugins/paymentview/analytics/viewdashboard/clientsTable.xml";
	private static final String TBL_TARGET_ANALYTICS = "tbl_clients_analytics";
	private TargetAnalytics targetAnalytics;
	private TargetDao targetDao;
	private Map<Client,Target> clients_targets;
	private DateFormat dateFormat = InternationalisationUtils.getDateFormat();
	private ServiceItemDao serviceItemDao;
	private TargetServiceItemDao targetServiceItemDao;
	private CreateSettingsHandler createSettingsHandler;

	public CreateSettingsTableHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController,
			CreateSettingsHandler createSettingsHandler) {
		super(ui, pluginController);
		this.createSettingsHandler = createSettingsHandler;
		initDaos();
	}

	private void initDaos() {
		serviceItemDao = pluginController.getServiceItemDao();
		targetDao = pluginController.getTargetDao();
		targetServiceItemDao = pluginController.getTargetServiceItemDao();

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
		return TBL_TARGET_ANALYTICS;
	}

	@Override
	protected String getClientsPanelFilePath() {
		return XML_VIEWDASHBOARD_CLIENTS_TABLE;
	}

	public Target getSelectedTargetInTable() {
		try {
			Object row = super.getSelectedRows()[0];
			Target target = ui.getAttachedObject(row, Target.class);
			return target;
		} catch (ArrayIndexOutOfBoundsException ai){
			return null;
		}
	}

	public void viewEditTargetAnalytics() {
		if(null==getSelectedTargetInTable()){
			ui.alert("No selected Target");
		} else {
	    	Target tgt = getSelectedTargetInTable();
	    	if(tgt!=null){
	    		EditTargetHandler editTargetHandler = new EditTargetHandler(pluginController, tgt, this);
				ui.add(editTargetHandler.getDialog());
	    	} else {
	    		ui.alert("No selected Target");
	    	}			
		}
	}

	@Override
	public void updateClientsList() {
		clients_targets = new HashMap<Client,Target>();
		this.clientsTablePager.setCurrentPage(0);
		this.clientsTablePager.refresh();
	}

	protected Object[] toThinletComponents(List<Client> clients) {
		List<Object> rows = new ArrayList<Object>();

		for(Client client: clients){
			List<Account> lstAccount = accountDao.
			getAccountsByClientId(client.getId());
			for (Account acc: lstAccount) {
				for (Target tgt : targetDao.getTargetsByAccount(acc.getAccountNumber())) {
					rows.addAll(getRows(tgt));
				}
			}
		}
		return rows.toArray();
	}

	private List<Object> getRows(Target target) {	
		List<Object> trgtRowLst = new ArrayList<Object>();

		Object name = ui.createTableCell(target.getAccount().
				getClient().getFullName());

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

		if(!targetAnalytics.getStatus(target.getId()).toString().equals(target.getStatus())) {
			target.setStatus(targetAnalytics.getStatus(target.getId()).toString());
			targetDao.saveTarget(target);
		}
		Object targetStatus = ui.createTableCell(target.getStatus());

		Object savingsTarget = ui.createTableCell(target.getTotalTargetCost().toString());
		Object startDate = ui.createTableCell(dateFormat.format(target.getStartDate()));
		Object endDate = ui.createTableCell(dateFormat.format(target.getEndDate()));

		targetAnalytics.computeAnalyticsIntervalDatesAndSavings(target.getId());
	    Object monthlyAmountSaved = ui.createTableCell(targetAnalytics.getMonthlyAmountSaved().toString());
	    Object remainingAmount = ui.createTableCell(target.getTotalTargetCost().subtract(targetAnalytics.getAmountSaved(target.getId())).toString());
	    Object monthlyAmountDue = ui.createTableCell(targetAnalytics.getMonthlyAmountDue().toString());

	    Object endOfMonthlyInterval = "";
		if(targetAnalytics.getEndMonthInterval() != null){
			endOfMonthlyInterval = ui.createTableCell(dateFormat.format(targetAnalytics.getEndMonthInterval()));
		}else{
			endOfMonthlyInterval = ui.createTableCell("Target end date passed");
		}

	 	targetAnalytics.clearAnalyticsComputations();

		String neededitems = "";
		List<TargetServiceItem> lstTSI = targetServiceItemDao.getAllTargetServiceItemByTarget(target.getId());
		for(TargetServiceItem tsi: lstTSI){
			if (neededitems.length()==0) {
				neededitems = tsi.getServiceItem().getTargetName();
			} else {
				neededitems = neededitems+", "+tsi.getServiceItem().getTargetName();
			}
		}
		Object neededItems = ui.createTableCell(neededitems);
		Object row = ui.createTableRow(target);
		ui.add(row, name);
		ui.add(row, neededItems);
		ui.add(row, startDate);
		ui.add(row, endDate);
		ui.add(row, savingsTarget);
		ui.add(row, amountSaved);
		ui.add(row, remainingAmount);
		ui.add(row, percentageToGo);
		ui.add(row, lastAmountPaid);
		ui.add(row, lastDatePaid);
		/*ui.add(row, monthlyAmountSaved);*/
		ui.add(row, daysRemaining);
		ui.add(row, targetStatus);
		/*ui.add(row, monthlyAmountDue);
		ui.add(row, endOfMonthlyInterval);*/

		trgtRowLst.add(row);

		return trgtRowLst;
	}

	public void refreshSettingsHandler() {
		createSettingsHandler.refresh();
	}

	protected List<Client> getClients(String filter, int startIndex, int limit) {
		if (!filter.trim().isEmpty()) {
			totalItemCount  = this.clientDao.getClientsByNameFilter(filter).size();
			List<Client> clients = this.clientDao.getClientsByFilter(filter, startIndex, limit);

			if(this.serviceItemDao.getServiceItemsLikeName(filter).size()==0) {
				List<Target> lstTgts = targetDao.getTargetsByStatus(filter);
				for (Target t: lstTgts) {
					Client client = t.getAccount().getClient();
					if (!clients.contains(client)){clients.add(client);}
				}
			} else {
				for (ServiceItem si : this.serviceItemDao.getServiceItemsLikeName(filter)) {
					List<TargetServiceItem> targetServiceItems = targetServiceItemDao.
					getAllTargetServiceItemByServiceItemId(si.getId());
					for(int y = 0 ; y<targetServiceItems.size(); y++){
						Target t = targetServiceItems.get(y).getTarget();
						Client client = t.getAccount().getClient();
						if (!clients.contains(client)){clients.add(client);}
					}
					List<Target> lstTgts = targetDao.getTargetsByStatus(filter);
					for (Target t: lstTgts) {
						Client client = t.getAccount().getClient();
						if (!clients.contains(client)){clients.add(client);}
					}
				}	
			}

			return clients;
		}else{
			if (clientListForAnalytics.isEmpty()){
				totalItemCount = this.clientDao.getAllActiveClientsSorted(getClientsSortField(), getClientsSortOrder()).size();
				List<Client> activeClientsSorted = this.clientDao.getAllActiveClientsSorted
										(getClientsSortField(), getClientsSortOrder());
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
		if (notification instanceof DatabaseEntityNotification) {
			final Object entity = ((DatabaseEntityNotification<?>) notification).getDatabaseEntity();
			if (entity instanceof Target) {
				new FrontlineUiUpdateJob() {
					public void run() {
						Target target = (Target) entity;
						if (notification instanceof EntitySavedNotification){
							Account a = target.getAccount();
							if ((a != null) & (a.getClient() != null)) {
								if (a.getClient().isActive()){
									clients_targets.put(a.getClient(), target);
								}
							}
						}
						refresh();
					}
				}.execute();
			} else if (entity instanceof Client || entity instanceof Account) {
				new FrontlineUiUpdateJob() {
					public void run() {
						refresh();
					}
				}.execute();
			}
		}
	}
}