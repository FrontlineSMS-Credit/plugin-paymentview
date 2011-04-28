/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

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

	private static final String XML_CONNECTED_DEVICE_VIEW_TAB = "/ui/plugins/paymentview/connectedDevicesTab.xml";
	private Object connectedDevicesViewTab;
	private final SmsServiceManager smsServiceManager;

	/**
	 * 
	 * @param paymentViewController
	 *            value for {@link #controller}
	 * @param uiController
	 *            value for {@linkplain #ui}
	 */
	public ConnectedDeviceThinletTabController(UiGeneratorController ui) {
		super(ui);
		init();
		this.smsServiceManager = ui.getFrontlineController().getSmsServiceManager();
	}
	/**
	 * Refreshes the tab display
	 */
	public void refresh() {
		Object connectedDevicesList = getConnectedDevicesList();
		ui.removeAll(connectedDevicesList);
		for(SmsService s : this.smsServiceManager.getAll()) {
			if(shouldDisplay(s)) {
				ui.add(connectedDevicesList, getListItem(s));
			}
		}
	}
	
	private Object getConnectedDevicesList() {
		return find("the name of the list component from the XML file.");
	}
	private Object getListItem(SmsService s) {
		return ui.createListItem("I am a list item", s);
	}
	private boolean shouldDisplay(SmsService s) {
		return true; // eventually this will check if the device is suiotable for MPESA
	}
	@Override
	protected Object initialiseTab() {
		connectedDevicesViewTab = ui.loadComponentFromFile(
				XML_CONNECTED_DEVICE_VIEW_TAB, this);
		
		return connectedDevicesViewTab;
	}
}