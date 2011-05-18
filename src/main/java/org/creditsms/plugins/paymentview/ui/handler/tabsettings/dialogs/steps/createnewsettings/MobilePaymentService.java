package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.messaging.sms.SmsService;
import net.frontlinesms.payment.safaricom.MpesaPayBillService;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.payment.safaricom.MpesaPersonalService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabsettings.SettingsTabHandler;

public class MobilePaymentService extends BaseDialog {
	private static final String XML_MOBILE_PAYMENT_SERVICE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep1.xml";
	private final PaymentViewPluginController pluginController;
	private MpesaPaymentService paymentService;
	private SmsService smsService;
	private final SettingsTabHandler settingsTabHandler;

	public MobilePaymentService(UiGeneratorController ui, PaymentViewPluginController pluginController, SettingsTabHandler settingsTabHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.settingsTabHandler = settingsTabHandler;
		init();
	}
	
	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_MOBILE_PAYMENT_SERVICE, this);
		Object cmbDevices = ui.find(dialogComponent,"cmbDevices");
		Object cmbSelectPaymentService = ui.find(dialogComponent,"cmbSelectPaymentService");
		setUpPaymentServices(cmbSelectPaymentService);
		setUpSmsService(cmbDevices); 
	}

	private void setUpSmsService(Object cmbDevices) {
		for(SmsService s : ui.getFrontlineController().getSmsServiceManager().getAll()) {
			ui.add(cmbDevices, getComboChoice(s));
		}
	}	
	
	private Object getComboChoice(SmsService s) {
		return ui.createComboboxChoice(s.getServiceName(), s);
	}
	
	private void setUpPaymentServices(Object cmbSelectPaymentService) {
		//TODO: We should think of having modules at this case;Some Metaprogramming in the house!!
		MpesaPaymentService mpesaPersonal = new MpesaPersonalService();
		Object comboboxChoice1 = ui.createComboboxChoice(mpesaPersonal.toString(), mpesaPersonal);
		
		MpesaPaymentService mpesaPayBill = new MpesaPayBillService();
		Object comboboxChoice2 = ui.createComboboxChoice(mpesaPayBill.toString(), mpesaPayBill);
		
		//SETUP; Dont Add to the EventBus as of yet... May Cause More harm; Memory leak 
		ui.add(cmbSelectPaymentService, comboboxChoice1);
		ui.add(cmbSelectPaymentService, comboboxChoice2);
	}

	public void next(Object cmbSelectPaymentService, Object cmbDevices) {
		int selectedIndex = ui.getSelectedIndex(cmbSelectPaymentService);
		if (selectedIndex < 0){
			ui.alert("Please Select a Payment Service to Use.");
		}else{
			smsService = ui.getAttachedObject(ui.getItem(cmbDevices, ui.getSelectedIndex(cmbDevices)), SmsService.class);
			if(smsService == null){
				throw new RuntimeException("No Device Selected.");
			}
			paymentService = ui.getAttachedObject(ui.getItem(cmbSelectPaymentService, selectedIndex), MpesaPaymentService.class);
			if (paymentService != null) {
				EnterPin accountType = new EnterPin(ui, pluginController, this);
				accountType.showDialog();
				cleanUp();
				removeDialog();
			}else{
				throw new RuntimeException("Null Payment Service.");
			}
		}
	}
	
	private void cleanUp() {
		//TODO: Clean up unused stuff; or just let the Garbage collector to it.
	}

//> ACCESSORS
	MpesaPaymentService getPaymentService() {
		return paymentService;
	}
	
	SmsService getSmsService() {
		return smsService;
	}
	
	SettingsTabHandler getSettingsTabHandler() {
		return settingsTabHandler;
	}
}
