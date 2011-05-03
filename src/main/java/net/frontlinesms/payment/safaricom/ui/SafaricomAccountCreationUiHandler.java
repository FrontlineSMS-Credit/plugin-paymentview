package net.frontlinesms.payment.safaricom.ui;

import org.smslib.CService;

import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.safaricom.SafaricomPaymentService;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class SafaricomAccountCreationUiHandler implements ThinletUiEventHandler {
	private static final String DIALOG_XML_FILE = "/ui/plugins/paymentview/services/safaricom/dgConfig.xml";
	private UiGeneratorController ui;
	private Object dialog;
	
	public SafaricomAccountCreationUiHandler(UiGeneratorController ui) {
		this.ui = ui;
		initDialog();
	}

	private void initDialog() {
		this.dialog = ui.loadComponentFromFile(DIALOG_XML_FILE, this);
		initDeviceList();
	}

	private void initDeviceList() {
		Object combo = getDeviceList();
		for(SmsService s : ui.getFrontlineController().getSmsServiceManager().getAll()) {
			ui.add(combo, getComboChoice(s));
		}
	}

	private Object getDeviceList() {
		return find("cbDevices");
	}

	private Object find(String componentName) {
		return ui.find(this.dialog, componentName);
	}

	private Object getComboChoice(SmsService s) {
		return ui.createComboboxChoice(s.getServiceName(), s);
	}

	public Object getDialog() {
		return this.dialog;
	}

	private String getPin() {
		return ui.getText(find("tfPin"));
	}
	
	private String getVPin() {
		return ui.getText(find("tfVPin"));
	}
	
//> PUBLIC UI EVENT METHODS
	public void createService() {
		System.out.println("createService()");
		//ui.alert("aaaaaaaaaaaaaaaaaaaaaaaaagh!!!!!!");
		SmsService s = ui.getAttachedObject(ui.getSelectedItem(getDeviceList()), SmsService.class);
		String pin = getPin();
		String vPin = getVPin();
		SafaricomPaymentService sps = new SafaricomPaymentService();
		sps.setPin(pin);
		CService cService = ((SmsModem) s).getCService();
		sps.setCService(cService);
		System.out.println("Created payment service: " + s);
		System.out.println("With PIN: " + pin);
		System.out.println("Verify PIN: " + vPin);
		System.out.println("And CService: " + cService);
	}
	
	public void removeDialog() {
		ui.remove(this.dialog);
	}
}
