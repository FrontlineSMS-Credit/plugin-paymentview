/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import org.creditsms.plugins.paymentview.ui.handler.connecteddevices.ConnectedDevicesTabHandler;

import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.messaging.sms.SmsServiceManager;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

/**
 * 
 * @author 
 */
public class ConnectedDeviceThinletTabController extends BaseTabHandler implements
		ThinletUiEventHandler {
	
	private static final String TABP_MAIN_PANE = "tabP_mainPane";
	private static final String XML_CONNECTED_DEVICE_VIEW_TAB = "/ui/plugins/paymentview/connectedDevicesTab.xml";
	private Object connectedDevicesViewTab;

	private final SmsServiceManager smsServiceManager;
	private ConnectedDevicesTabHandler connectedDevicesTab;
	private Object mainPane;

	/**
	 * 
	 * @param paymentViewController
	 *            value for {@link #controller}
	 * @param uiController
	 *            value for {@linkplain #ui}
	 */
	public ConnectedDeviceThinletTabController(UiGeneratorController ui) {
		super(ui);
		this.smsServiceManager = ui.getFrontlineController().getSmsServiceManager();
		init();		
	}
	
	@Override
	protected Object initialiseTab() {
		connectedDevicesViewTab = ui.loadComponentFromFile(
				XML_CONNECTED_DEVICE_VIEW_TAB, this);
		
		return connectedDevicesViewTab;
	}
	
	/**
	 * Refreshes the tab display
	 */
	@Override
	public void refresh() {
		System.out.println("refresh()");
		
		Object connectedDevicesList = getConnectedDevicesList();
		ui.removeAll(connectedDevicesList);
		for(SmsService s : this.smsServiceManager.getAll()) {
			if(shouldDisplay(s)) {
				ui.add(connectedDevicesList, getListItem(s));
			}
		}
	}
	
	private Object getConnectedDevicesList() {
		return find("lstConnectedDevices");
	}
	
	private Object getListItem(SmsService s) {
		return ui.createListItem(s.toString() + " | " + s.getServiceName() + " | " + s.getServiceIdentification() + " | " + s.getMsisdn(), s);
	}
	
	private boolean shouldDisplay(SmsService s) {
		return true; // eventually this will check if the device is suitable for MPESA
	}
	
	public Object getConnectedDevicesViewTab() {
		return connectedDevicesViewTab;
	}
	public void setConnectedDevicesViewTab(Object connectedDevicesViewTab) {
		this.connectedDevicesViewTab = connectedDevicesViewTab;
	}
}