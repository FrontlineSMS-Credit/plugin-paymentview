package org.creditsms.plugins.paymentview.ui.handler.connecteddevices;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.UiGeneratorControllerConstants;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.ui.ConnectedDeviceThinletTabController;

public class ConnectedDevicesTabHandler extends BaseTabHandler{
	private static final String COMPONENT_CONNECTEDDEVICES_TABLE = "tbl_connectedDevicesList";
	private static final String COMPONENT_PANEL_CONNECTEDDEVICES_LIST = "pnl_tbl_connectedDevicesList";
	// > STATIC CONSTANTS
	private static final String XML_CONNECTEDDEVICES_TAB = "/ui/plugins/paymentview/connecteddevices/connecteddevices.xml";
	// > INSTANCE PROPERTIES
	
	private Object pnlConnectedDevicesList;
	private Object connectedDevicesTableComponent;
	private ComponentPagingHandler connectedDevicesTablePager;
	
	private ConnectedDeviceThinletTabController connectedDevicesViewThinletTabController; 

	public ConnectedDevicesTabHandler(UiGeneratorController ui, final ConnectedDeviceThinletTabController connectedDevicesViewThinletTabController) {
		super(ui);
		this.connectedDevicesViewThinletTabController = connectedDevicesViewThinletTabController;
		init();
	}
	
	@Override
	protected Object initialiseTab() {
		Object connectedDevicesTab = ui.loadComponentFromFile(XML_CONNECTEDDEVICES_TAB, this);
		connectedDevicesTableComponent = ui.find(connectedDevicesTab, COMPONENT_CONNECTEDDEVICES_TABLE);
		pnlConnectedDevicesList = ui.find(connectedDevicesTab, COMPONENT_PANEL_CONNECTEDDEVICES_LIST);
		this.ui.add(pnlConnectedDevicesList, this.connectedDevicesTablePager.getPanel());
		return connectedDevicesTab;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
