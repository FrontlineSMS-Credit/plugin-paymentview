/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.FrontlineSMSConstants;
import net.frontlinesms.data.domain.FrontlineMessage;
import net.frontlinesms.data.repository.MessageDao;
import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.messaging.sms.SmsServiceManager;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

/**
 * 
 * @author 
 */
public class PvDebugTabController extends BaseTabHandler implements
		ThinletUiEventHandler {
	
	private static final String COMPONENT_LST_CONNECTED_DEVICES = "lstConnectedDevices";
	private static final String XML_CONNECTED_DEVICE_VIEW_TAB = "/ui/plugins/paymentview/connectedDevicesTab.xml";
	private Object connectedDevicesViewTab;

	private final SmsServiceManager smsServiceManager;
	private MessageDao messageDao;

	public MessageDao getMessageDao() {
		return messageDao;
	}

	public void setMessageDao(MessageDao messageDao) {
		this.messageDao = messageDao;
	}

	/**
	 * 
	 * @param paymentViewController
	 *            value for {@link #controller}
	 * @param uiController
	 *            value for {@linkplain #ui}
	 */
	public PvDebugTabController(UiGeneratorController ui) {
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
		Object connectedDevicesList = getConnectedDevicesList();
		ui.removeAll(connectedDevicesList);
		for(SmsService s : this.smsServiceManager.getAll()) {
			if(shouldDisplay(s)) {
				ui.add(connectedDevicesList, getListItem(s));
			}
		}
	}
	
	private Object getConnectedDevicesList() {
		return find(COMPONENT_LST_CONNECTED_DEVICES);
	}
	
	private Object getListItem(SmsService s) {
		return ui.createListItem(s.toString() + " | " + s.getServiceName() + " | " + s.getServiceIdentification() + " | " + s.getMsisdn(), s);
	}
	
	private boolean shouldDisplay(SmsService s) {
		return true; // eventually this will check if the device is suitable for MPESA
	}
	
	public void saveMessage(String message){
		//ui.alert("The message to be saved is:::"+message);
		FrontlineMessage msg = FrontlineMessage.createIncomingMessage(System.currentTimeMillis(), "MPESA", FrontlineSMSConstants.EMULATOR_MSISDN, message);
		messageDao.saveMessage(msg);
	}
	
	public Object getConnectedDevicesViewTab() {
		return connectedDevicesViewTab;
	}
	public void setConnectedDevicesViewTab(Object connectedDevicesViewTab) {
		this.connectedDevicesViewTab = connectedDevicesViewTab;
	}
}