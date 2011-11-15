package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.innertabs;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.ServiceItem;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.CreateNewServiceItemHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs.EditServiceItemHandler;

public class ConfigureServiceTabHandler extends BaseTabHandler implements PagedComponentItemProvider {

	private static final String COMPONENT_SERVICE_ITEM_TABLE = "tbl_serviceItem";
	private static final String COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE = "pnl_tbl_serviceItem_holder";
	private static final String TAB_CONFIGURE_SERVICE = "tab_configureService";
	private static final String XML_CONFIGURE_SERVICE = "/ui/plugins/paymentview/analytics/configureservice/configurelayaway.xml";

	private final ServiceItemDao serviceItemDao;
	private ComponentPagingHandler serviceItemTablePager;
	
	private Object serviceItemPanel;
	private Object serviceItemTableComponent;
	private Object pnlServiceItemTableComponent;
	private Object configureServiceTab;
	private PaymentViewPluginController pluginController;
	

	public ConfigureServiceTabHandler(UiGeneratorController ui, Object tabAnalytics, PaymentViewPluginController pluginController) {
		super(ui, true);
		this.pluginController = pluginController;
		this.serviceItemDao = pluginController.getServiceItemDao();
		configureServiceTab = ui.find(tabAnalytics, TAB_CONFIGURE_SERVICE);
		this.init();
	}

	public void createNew() {
		ui.add(new CreateNewServiceItemHandler(ui, pluginController).getDialog());
	}

	public void editserviceItem() {
		ui.add(new EditServiceItemHandler(ui, pluginController, getSelectedServiceItemInTable(), this).getDialog());
	}
	
 	public void refreshServiceItemTable(){
		List<ServiceItem> lstServiceItem = serviceItemDao.getAllServiceItems();
		ui.removeAll(serviceItemTableComponent);
		for(ServiceItem si: lstServiceItem){
			ui.add(this.serviceItemTableComponent, getRow(si));
		}
	}
	
	public Object getSelectedServiceItemRow() {
		return ui.getSelectedItem(serviceItemTableComponent);
	}
    
	public ServiceItem getSelectedServiceItemInTable() {
		Object row = getSelectedServiceItemRow();
		ServiceItem serviceItem = ui.getAttachedObject(row, ServiceItem.class);
		return serviceItem;
	}

	
	public void deinit() {
	}

	@Override
	protected Object initialiseTab() {
		serviceItemPanel = ui.loadComponentFromFile(XML_CONFIGURE_SERVICE, this);
		serviceItemTableComponent = ui.find(serviceItemPanel, COMPONENT_SERVICE_ITEM_TABLE);
		serviceItemTablePager = new ComponentPagingHandler(ui, this, serviceItemTableComponent);
		pnlServiceItemTableComponent = ui.find(serviceItemPanel, COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE);
		
		this.ui.add(pnlServiceItemTableComponent, this.serviceItemTablePager.getPanel());
		this.ui.add(configureServiceTab, serviceItemPanel);
		return configureServiceTab;
	}

	@Override
	public void refresh() {
		this.updateServiceItemList();
	}
	
	public void updateServiceItemList() {
		this.serviceItemTablePager.setCurrentPage(0);
		this.serviceItemTablePager.refresh();
	}
	
	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.serviceItemTableComponent) {
			return getServiceItemListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	private PagedListDetails getServiceItemListDetails(int startIndex,int limit) {
		List<ServiceItem> serviceItems = new ArrayList<ServiceItem>();

		serviceItems = this.serviceItemDao.getAllServiceItems(startIndex, limit);
		int totalItemCount = serviceItemDao.getServiceItemCount();
		Object[] listItems = toThinletComponents(serviceItems);

		return new PagedListDetails(totalItemCount, listItems);
	}
	
	private Object[] toThinletComponents(List<ServiceItem> serviceItems) {
		Object[] components = new Object[serviceItems.size()];
		for (int i = 0; i < components.length; i++) {
			ServiceItem in = serviceItems.get(i);
			components[i] = getRow(in);
		}
		return components;
	}
	
	public Object getRow(ServiceItem serviceItem) {
		Object row = ui.createTableRow(serviceItem);

		ui.add(row, ui.createTableCell(serviceItem.getTargetName()));
		ui.add(row, ui.createTableCell(serviceItem.getAmount().toString()));
		return row;
	}

	public void notify(FrontlineEventNotification notification) {
		super.notify(notification);
		if (notification instanceof EntitySavedNotification) {
			Object entity = ((EntitySavedNotification<?>) notification).getDatabaseEntity();
			if (entity instanceof ServiceItem) {
				threadSafeRefresh();
			}
		}
	}

}
