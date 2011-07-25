package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs.steps.createnewsettings;

import net.frontlinesms.messaging.sms.modem.SmsModem;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.payment.safaricom.MpesaPayBillService;
import net.frontlinesms.payment.safaricom.MpesaPaymentService;
import net.frontlinesms.payment.safaricom.MpesaPersonalService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class MobilePaymentServiceSettingsInitialisationDialog extends BaseDialog {
	private static final String XML_MOBILE_PAYMENT_SERVICE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep1.xml";
	private final PaymentViewPluginController pluginController;

	public MobilePaymentServiceSettingsInitialisationDialog(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
		this.pluginController = pluginController;
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
		for(SmsModem s : ui.getFrontlineController().getSmsServiceManager().getSmsModems()) {
			ui.add(cmbDevices, getComboChoice(s));
		}
	}	
	
	private Object getComboChoice(SmsModem s) {
		return ui.createComboboxChoice(s.getServiceName(), s);
	}
	
	private void setUpPaymentServices(Object cmbSelectPaymentService) {
		MpesaPaymentService mpesaPersonal = new MpesaPersonalService();
		Object comboboxChoice1 = ui.createComboboxChoice(mpesaPersonal.toString(), mpesaPersonal);
		
		MpesaPaymentService mpesaPaybill = new MpesaPayBillService();
		Object comboboxChoice2 = ui.createComboboxChoice(mpesaPaybill.toString(), mpesaPaybill);

		ui.add(cmbSelectPaymentService, comboboxChoice1);
		ui.add(cmbSelectPaymentService, comboboxChoice2);
	}

	public void next() {
		if (getPaymentService() == null) {
			ui.alert("Please Select a Payment Service to Use.");
		} else if(getModem() == null){
			ui.alert("No Device Selected.");
		} else {
			EnterPinDialog enterPinDialog = new EnterPinDialog(ui, pluginController, getPaymentService(), getModem());
			enterPinDialog.showDialog();
			cleanUp();
			removeDialog();
		}
	}
	
	private void cleanUp() {
		//Memory Leaks; Should the Payment Services be Singletons?
	}

//> ACCESSORS
	Class<? extends PaymentService> getPaymentService() {
		Object paymentServiceCombobox = getServiceCombobox();
		Object selectedItem = ui.getSelectedItem(paymentServiceCombobox);
		return ui.getAttachedObject(selectedItem, MpesaPaymentService.class).getClass();
	}

	private Object getServiceCombobox() {
		return ui.find("cmbSelectPaymentService");
	}

	public void setPaymentService(MpesaPaymentService paymentService) {
		Object serviceCombobox = getServiceCombobox();
		Object[] items = ui.getItems(serviceCombobox);
		int serviceIndex = -1;
		for(int i=0; i<items.length; ++i) {
			if(ui.getAttachedObject(items[i]).getClass().equals(paymentService.getClass())) {
				serviceIndex = i;
			}
		}
		ui.setSelectedIndex(serviceCombobox, serviceIndex);
	}
	
	SmsModem getModem() {
		return ui.getAttachedObject(ui.getSelectedItem(getModemCombobox()), SmsModem.class);
	}

	private Object getModemCombobox() {
		return ui.find(dialogComponent, "cmbDevices");
	}

	public void setModem(SmsModem modem) {
		int modemIndex = -1;
		Object[] items = ui.getItems(getModemCombobox());
		for (int i = 0; i < items.length; i++) {
			if(ui.getAttachedObject(items[i]).equals(modem)) {
				modemIndex = i;
			}
		}
		ui.setSelectedIndex(getModemCombobox(), modemIndex);
	}
}
