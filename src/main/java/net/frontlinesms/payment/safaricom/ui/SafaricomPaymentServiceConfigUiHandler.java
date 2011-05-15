package net.frontlinesms.payment.safaricom.ui;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.payment.safaricom.MpesaPersonalService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class SafaricomPaymentServiceConfigUiHandler extends BaseDialog {
	private static final String DIALOG_XML_FILE = "/ui/plugins/paymentview/services/safaricom/dgConfig.xml";
	private Object combo;
	
	/** TODO this should not be referenced here - remove this once we are saving a properties object and allowing
	 * the payment service manager to manage service lifecycles.
	 */
	private final EventBus eventBus;
	private IncomingPaymentDao incomingPaymentDao;
	private ClientDao clientDao;
	private AccountDao accountDao;

	public SafaricomPaymentServiceConfigUiHandler(UiGeneratorController ui, IncomingPaymentDao incomingPaymentDao, ClientDao clientDao, AccountDao accountDao) {
		super(ui);
		this.eventBus = ui.getFrontlineController().getEventBus();
		this.incomingPaymentDao = incomingPaymentDao;
		this.clientDao = clientDao;
		this.accountDao = accountDao;
		initDialog();
	}

//> SETUP METHODS
	private void initDialog() {
		this.dialogComponent = ui.loadComponentFromFile(DIALOG_XML_FILE, this);
		initDeviceList();
	}

	private void initDeviceList() {
		combo = getDeviceList();
		setUpSMSService(); 
	}

	private void setUpSMSService() {
		for(SmsService s : ui.getFrontlineController().getSmsServiceManager().getAll()) {
			ui.add(combo, getComboChoice(s));
		}
	}

//> UI HELPER METHODS
	private Object getDeviceList() {
		return find("cbDevices");
	}

	private Object find(String componentName) {
		return ui.find(this.dialogComponent, componentName);
	}

	private Object getComboChoice(SmsService s) {
		return ui.createComboboxChoice(s.getServiceName(), s);
	}

	public Object getDialog() {
		return this.dialogComponent;
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
				SmsService smsService = ui.getAttachedObject(ui.getSelectedItem(getDeviceList()), SmsService.class);
				MpesaPaymentService mpesaPaymentService = new MpesaPersonalService();
				mpesaPaymentService.setPin(pin);
				mpesaPaymentService.setSmsService((SmsModem) smsService);

				mpesaPaymentService.setClientDao(clientDao);
				mpesaPaymentService.setIncomingPaymentDao(incomingPaymentDao);	
				mpesaPaymentService.setAccountDao(accountDao);
				
				eventBus.registerObserver(mpesaPaymentService);
				ui.alert("Created payment service: " + smsService +" With PIN: " + pin +" Verify PIN: " + vPin + " And CService: " + smsService);
				
				ui.remove(this.dialogComponent);
			} else {
				ui.alert("The Pins are invalid or do not match");
				// TODO set focus on pin 1 field
			}
		}else{
			ui.alert("Please select a connected device.");
			// TODO set focus on combo
		}
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
	
	public void assertMaxLength(Object component, int maxLength) {
		String text = ui.getText(component);
		if(text.length() > maxLength) {
			ui.setText(component, text.substring(0, maxLength));
		}
	}
}
