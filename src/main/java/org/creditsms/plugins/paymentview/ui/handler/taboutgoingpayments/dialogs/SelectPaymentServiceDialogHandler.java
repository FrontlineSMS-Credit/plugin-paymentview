package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs;

import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.ImportNewPaymentsTabHandler;

public class SelectPaymentServiceDialogHandler extends BaseDialog{
	private static final String XML_SELECT_PAYMENT_SERVICE = "/ui/plugins/paymentview/outgoingpayments/dialogs/dlgPaymentServiceLst.xml";
	private static final String COMPONENT_CMB_OP_MOBILE_PAYMENT_SYSTEM = "cmbPaymentServices";
	private Object cmbOpMobilePaymentSystem;
	private PaymentViewPluginController pluginController;
	private PaymentService paymentService;
	private final ImportNewPaymentsTabHandler importNewPaymentHndlr;
	
	public SelectPaymentServiceDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, ImportNewPaymentsTabHandler importNewPaymentHndlr) {
		super(ui);
		this.pluginController = pluginController;
		this.importNewPaymentHndlr = importNewPaymentHndlr;
		initialise();
	}
	
	protected void initialise() {
		dialogComponent = ui.loadComponentFromFile(XML_SELECT_PAYMENT_SERVICE, this);
		cmbOpMobilePaymentSystem = ui.find(dialogComponent, COMPONENT_CMB_OP_MOBILE_PAYMENT_SYSTEM);
		setupPaymentServices();
	}
	
	private void setupPaymentServices() {
		for (PaymentService pService : pluginController.getPaymentServices()) {
			if(pService.isOutgoingPaymentEnabled()) {
				String serviceDescription = pService.toString() + " : " 
						+ pService.getSettings().getId();
				ui.add(cmbOpMobilePaymentSystem, ui.createComboboxChoice(serviceDescription, pService));
			}
		}
	}
	
 	public void authorize() {
		Object selectedPaymentServiceItem = ui.getSelectedItem(cmbOpMobilePaymentSystem);
		if (selectedPaymentServiceItem != null){
			paymentService = ui.getAttachedObject(selectedPaymentServiceItem, PaymentService.class);
			importNewPaymentHndlr.setPaymentService(paymentService);
			importNewPaymentHndlr.showSendPaymentAuthDialog();
			this.removeDialog();
		} else {
			ui.infoMessage("Please select a payment service.");
		}
	}
	
	@Override
	public void showDialog() {
		ui.add(this.dialogComponent);
	}
	
	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}
}
