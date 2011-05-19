package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.SettingsTabHandler;

public class EnterPin extends BaseDialog {
	private static final int MAX_LENGTH = 4;
	private static final String XML_ENTER_PIN = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep2.xml";
	private final PaymentViewPluginController pluginController;
	private MobilePaymentService previousMobilePaymentService;

	public EnterPin(UiGeneratorController ui, PaymentViewPluginController pluginController,
			MobilePaymentService previousMobilePaymentService){
		super(ui);		
		this.pluginController = pluginController;

		init();
		this.previousMobilePaymentService = previousMobilePaymentService;
	}
	
	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_PIN,this);
	}
	
	public void previous() {
		previousMobilePaymentService.showDialog();
		removeDialog();
	}
	
	public void next(final String Pin, final String VPin) {
		setUpThePaymentService(Pin, VPin);
	}

	public void setUpThePaymentService(final String pin, final String vPin) {			
			if(checkValidityOfPinFields(pin, vPin)){
				MpesaPaymentService paymentService = previousMobilePaymentService.getPaymentService();
				paymentService.setPin(pin);
				paymentService.setSmsService((SmsModem) previousMobilePaymentService.getSmsService());

				paymentService.setClientDao(pluginController.getClientDao());
				paymentService.setAccountDao(pluginController.getAccountDao());
				paymentService.setIncomingPaymentDao(pluginController.getIncomingPaymentDao());
				
				removeDialog();
				new AuthorizationCode(ui, pluginController, this).showDialog();
			}else{
				ui.alert("Invalid! Please Re-enter the PIN numbers again.");
			}
	}
	
	MpesaPaymentService getPaymentService() {
		return previousMobilePaymentService.getPaymentService();
	}
	
	SettingsTabHandler getSettingsTabHandler() {
		return previousMobilePaymentService.getSettingsTabHandler();
	}
	
	private boolean checkValidityOfPinFields(String pin, String vPin){
		if(pin.isEmpty() || pin.length() < MAX_LENGTH){
			return false;
		}else if(vPin.isEmpty() || vPin.length() < MAX_LENGTH){
			return false;
		}else if(pin.length() == vPin.length() & vPin.length() == MAX_LENGTH){
			if(vPin.equals(pin)){
				return true;
			}else{
				return false;
			}
		}
		return false;
	}
	
	
	public void assertMaxLength(Object component) {
		String text = ui.getText(component);
		if(!text.isEmpty() & text.length() > MAX_LENGTH) {
			ui.setText(component, text.substring(0, MAX_LENGTH - 1));
		}
	}
}
