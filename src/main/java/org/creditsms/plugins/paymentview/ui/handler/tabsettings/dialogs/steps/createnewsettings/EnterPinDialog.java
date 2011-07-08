package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class EnterPinDialog extends BaseDialog {
	private static final String DLG_VERIFICATION_CODE = "dlgVerificationCode";
	private static final int EXPECTED_PIN_LENGTH = 4;
	private static final String XML_ENTER_PIN = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep2.xml";
	private final PaymentViewPluginController pluginController;
	private final MpesaPaymentService paymentService;
	private final SmsModem modem;

	public EnterPinDialog(UiGeneratorController ui, PaymentViewPluginController pluginController, MpesaPaymentService paymentService, SmsModem modem) {
		super(ui);		
		this.pluginController = pluginController;
		this.paymentService = paymentService;
		this.modem = modem;

		init();
	}
	
	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_PIN,this);
	}
	
	public void previous() {
		MobilePaymentServiceSettingsInitialisationDialog initialisationDialog = new MobilePaymentServiceSettingsInitialisationDialog(ui, pluginController);
		initialisationDialog.setPaymentService(paymentService);
		initialisationDialog.setModem(modem);
		initialisationDialog.showDialog();
		removeDialog();
	}
	
	public void next() {
		setUpThePaymentService(ui.getText(ui.find("pin")), ui.getText(ui.find("vpin")));
	}

	public void setUpThePaymentService(final String pin, final String vPin) {			
		if(checkValidityOfPinFields(pin, vPin)){
			paymentService.setPin(pin);
			paymentService.setCService(modem.getCService());
			paymentService.initDaosAndServices(pluginController);
			
			removeDialog();
			new AuthorisationCodeHandler(ui, pluginController).showAuthorizationCodeDialog("create", this);
		} else {
			ui.alert("Invalid! Please Re-enter the PIN numbers again.");
		}
	}
	
	public void create() {
		ui.getFrontlineController().getEventBus().registerObserver(paymentService);
		pluginController.setPaymentService(paymentService);
		ui.alert("The Payment service has been created successfully!");
		removeDialog(ui.find(DLG_VERIFICATION_CODE));
	}

	private boolean checkValidityOfPinFields(String pin, String vPin){
		return pin.length() == EXPECTED_PIN_LENGTH && vPin.equals(pin);
	}
	
	
	public void assertMaxLength(Object component) {
		String text = ui.getText(component);
		if(text.length() > EXPECTED_PIN_LENGTH) {
			ui.setText(component, text.substring(0, EXPECTED_PIN_LENGTH - 1));
		}
	}
}
