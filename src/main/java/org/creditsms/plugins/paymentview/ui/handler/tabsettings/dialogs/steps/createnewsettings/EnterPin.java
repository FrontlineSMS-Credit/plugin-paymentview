package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.SettingsTabHandler;

public class EnterPin extends BaseDialog {
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
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_PIN,
				this);
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
				PaymentService paymentService = previousMobilePaymentService.getPaymentService();
				paymentService.setPin(pin);
				paymentService.setSmsService((SmsModem) previousMobilePaymentService.getSmsService());

				paymentService.setClientDao(pluginController.getClientDao());
				paymentService.setIncomingPaymentDao(pluginController.getIncomingPaymentDao());
				
				ui.getFrontlineController().getEventBus().registerObserver(paymentService);				
				removeDialog();
				new AuthorizationCode(ui, pluginController, this).showDialog();
			}else{
				ui.alert("Invalid! Please Re-enter the PIN numbers again.");
			}
	}
	
	PaymentService getPaymentService() {
		return previousMobilePaymentService.getPaymentService();
	}
	
	SettingsTabHandler getSettingsTabHandler() {
		return previousMobilePaymentService.getSettingsTabHandler();
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
