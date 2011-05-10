package org.creditsms.plugins.paymentview.ui.handler.connecteddevices;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;

public class ConnectedDevicesTabHandler extends BaseTabHandler{
	private static final String COMPONENT_PANEL_CONNECTEDDEVICES_LIST = "pnl_tbl_connectedDevicesList";
	// > STATIC CONSTANTS
	private static final String XML_CONNECTEDDEVICES_TAB = "/ui/plugins/paymentview/connecteddevices/connecteddevices.xml";
	// > INSTANCE PROPERTIES
	
	private Object pnlConnectedDevicesList;
	private ComponentPagingHandler connectedDevicesTablePager;
	
	public ConnectedDevicesTabHandler(UiGeneratorController ui) {
		super(ui);
		init();
	}
	
	@Override
	protected Object initialiseTab() {
		Object connectedDevicesTab = ui.loadComponentFromFile(XML_CONNECTEDDEVICES_TAB, this);
		pnlConnectedDevicesList = ui.find(connectedDevicesTab, COMPONENT_PANEL_CONNECTEDDEVICES_LIST);
		this.ui.add(pnlConnectedDevicesList, this.connectedDevicesTablePager.getPanel());
		return connectedDevicesTab;
	}

	@Override
	public void refresh() {
		// TODO Auto-generated method stub
		
	}
}
