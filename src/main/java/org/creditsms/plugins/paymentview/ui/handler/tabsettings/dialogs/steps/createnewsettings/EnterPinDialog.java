package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.events.EventBus;
import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.event.PaymentServiceStartedNotification;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;
import org.creditsms.plugins.paymentview.userhomepropeties.payment.service.PaymentServiceProperties;


public class EnterPinDialog extends BaseDialog {
	private static final String DLG_VERIFICATION_CODE = "dlgVerificationCode";
	private static final int EXPECTED_PIN_LENGTH = 4;
	private static final String XML_ENTER_PIN = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep2.xml";
	private final PaymentViewPluginController pluginController;
	private final MpesaPaymentService paymentService;
	private final SmsModem modem;
	private Object pin;
	private Object vpin;
	private Class<? extends PaymentService> paymentServiceCls;

	private PaymentServiceProperties paymentSettingsPropPin = PaymentServiceProperties.getInstance();

	
	private PaymentService initPaymentService(Class<?> paymentService) {
		if(paymentService != null){
			try {
				return (PaymentService) Class.forName(paymentService.getName()).newInstance();
			} catch (Exception ex) {
			}
		}
		return null;
	}
	
	public EnterPinDialog(UiGeneratorController ui, PaymentViewPluginController pluginController,Class<? extends PaymentService> paymentService, SmsModem modem) {
		super(ui);		
		this.paymentServiceCls = paymentService;
		this.pluginController = pluginController;
		this.paymentService = (MpesaPaymentService) initPaymentService(paymentService);
		this.modem = modem;

		init();
	}
	
	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_PIN,this);
		pin = ui.find(dialogComponent, "pin");
		vpin = ui.find(dialogComponent, "vpin");
		
	}
	
	public void next() {
		setUpThePaymentService(ui.getText(pin), ui.getText(vpin));
	}

	public void setUpThePaymentService(final String pin, final String vPin) {			
		if(checkValidityOfPinFields(pin, vPin)){
			paymentService.setPin(pin);
			paymentService.setCService(modem.getCService());
			paymentService.initDaosAndServices(pluginController);
			persistPaymentService(pin);
			removeDialog();
			new AuthorisationCodeHandler(ui).showAuthorizationCodeDialog(this, "create");
		} else {
			ui.alert("Invalid! Please Re-enter the PIN numbers again.");
		}
	}
	
	private void persistPaymentService(String pin){
		String modemSerial = this.modem.getSerial().toString();
		paymentSettingsPropPin.setPaymentServiceClass(this.paymentServiceCls);
		paymentSettingsPropPin.setSmsModem(modemSerial);
		paymentSettingsPropPin.setPin(pin);
		paymentSettingsPropPin.saveToDisk();
	}
	
	public void create() {
		EventBus eventBus = ui.getFrontlineController().getEventBus();
		eventBus.registerObserver(paymentService);
		
		//then
		pluginController.setPaymentService(paymentService);
		//then
		eventBus.notifyObservers(new PaymentServiceStartedNotification(paymentService));
		
		ui.alert("The Payment service has been created successfully!");
		removeDialog(ui.find(DLG_VERIFICATION_CODE));
	}

	private boolean checkValidityOfPinFields(String pin, String vPin){
		return pin.length() == EXPECTED_PIN_LENGTH && vPin.equals(pin);
	}
	
	
	public void assertMaxLength(Object component) {
		String text = ui.getText(component);
		if(text.length() > EXPECTED_PIN_LENGTH) {
			ui.setText(component, text.substring(0, EXPECTED_PIN_LENGTH));
		}
	}
}
