package net.frontlinesms.payment.safaricom.ui;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.smslib.CService;

import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.safaricom.SafaricomPaymentService;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

public class SafaricomPaymentServiceConfigUiHandler implements ThinletUiEventHandler {
	private static final String DIALOG_XML_FILE = "/ui/plugins/paymentview/services/safaricom/dgConfig.xml";
	private UiGeneratorController ui;
	private Object dialog;
	private Object combo;

	public SafaricomPaymentServiceConfigUiHandler(UiGeneratorController ui) {
		this.ui = ui;
		initDialog();
	}

//> SETUP METHODS
	private void initDialog() {
		this.dialog = ui.loadComponentFromFile(DIALOG_XML_FILE, this);
		initDeviceList();
	}

	private void initDeviceList() {
		combo = getDeviceList();
		for(SmsService s : ui.getFrontlineController().getSmsServiceManager().getAll()) {
			ui.add(combo, getComboChoice(s));
		}
	}

//> UI HELPER METHODS
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
	
	private boolean checkValidityOfPinFields(String pin, String vPin){
		if(pin.trim().length()==0 || pin.trim().length()<4){
			return false;
		}else if(vPin.trim().length()==0 || vPin.trim().length()<4){
			return false;
		}else if(vPin.trim().length()==4 && vPin.trim().length()==4){
			if(vPin.equals(pin)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	private String getPin() {
		return ui.getText(find("tfPin"));
	}
	
	private String getVPin() {
		return ui.getText(find("tfVPin"));
	}
	
//> PUBLIC UI EVENT METHODS
	public void createService() {
		if(ui.getSelectedItem(getDeviceList())!=null){
			String pin = getPin();
			String vPin = getVPin();			
			if(checkValidityOfPinFields(pin, vPin)){
				SmsService s = ui.getAttachedObject(ui.getSelectedItem(getDeviceList()), SmsService.class);
				SafaricomPaymentService sps = new SafaricomPaymentService();
				sps.setPin(pin);
				CService cService = ((SmsModem) s).getCService();
				sps.setCService(cService);
				ui.alert("Created payment service: " + s +" With PIN: " + pin +" Verify PIN: " + vPin + " And CService: " + cService);
	
				// TODO once we are persisting payment service settings, we will just save new (or update)
				// settings here.  For now, we only have one, statically accessed PaymentService and so we
				// can create it directly
				PaymentViewPluginController.setPaymentService(sps);
			} else {
				ui.alert("The Pins are invalid or do not match");
				// TODO set focus on pin 1 field
			}
		}else{
			ui.alert("Please select a connected device.");
			// TODO set focus on combo
		}
	}
	
	public void removeDialog() {
		ui.remove(this.dialog);
	}
	
	public void assertMaxLength(Object component, int maxLength) {
		String text = ui.getText(component);
		if(text.length() > maxLength) {
			ui.setText(component, text.substring(0, maxLength));
		}
	}
}
