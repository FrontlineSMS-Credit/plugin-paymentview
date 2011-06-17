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
		
		ui.add(cmbSelectPaymentService, comboboxChoice1);
		ui.add(cmbSelectPaymentService, comboboxChoice2);
	}

	public void next(Object cmbSelectPaymentService, Object cmbDevices) {
		int paymentServiceSelectedIndex = ui.getSelectedIndex(cmbSelectPaymentService);
		int devicesSelectedIndex = ui.getSelectedIndex(cmbDevices);
		
		if (paymentServiceSelectedIndex < 0){
			ui.alert("Please Select a Payment Service to Use!");
		}else if (devicesSelectedIndex < 0){
			ui.alert("Please select a device!");
		}else{
			smsService = ui.getAttachedObject(ui.getItem(cmbDevices, devicesSelectedIndex), SmsService.class);
			if(smsService == null){
				ui.alert("No Device Found!");
			}
			paymentService = ui.getAttachedObject(ui.getItem(cmbSelectPaymentService, paymentServiceSelectedIndex), MpesaPaymentService.class);
			if (paymentService != null) {
				EnterPin accountType = new EnterPin(ui, pluginController, this);
				accountType.showDialog();
				cleanUp();
				removeDialog();
			}else{
				ui.alert("Null Payment Service!");
			}
		}
	}
	
	private void cleanUp() {
		//Memory Leaks; Should the Payment Services be Singletons
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
