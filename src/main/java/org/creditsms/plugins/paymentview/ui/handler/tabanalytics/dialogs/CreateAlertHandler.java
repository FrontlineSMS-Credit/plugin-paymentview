package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.userhomepropeties.analytics.CreateAlertProperties;
import org.creditsms.plugins.paymentview.utils.PaymentPluginConstants;

public class CreateAlertHandler implements ThinletUiEventHandler {
	private static final String XML_CREATE_ALERT = "/ui/plugins/paymentview/analytics/dialogs/dlgCreateAlert.xml";

	private static final String CONFIRM_DIALOG = "confirmDialog";
	private static final String COMPLETESTGT_CHECKBOX_COMPONENT = "chckCompletesTgt";
	private static final String MISSEDDEADLINE_CHECKBOX_COMPONENT = "chckMissesDeadline";
	private static final String TWOWEEKSNOPAY_CHECKBOX_COMPONENT = "chckTwksWithoutPay";
	private static final String AMONTHWITHNOPAY_CHECKBOX_COMPONENT = "chckAmnthWithoutPay";
	private static final String MEETSHALFTGT_CHECKBOX_COMPONENT = "chckMeetsHalf";
	private Object compPanelFields;
	private Object dialogComponent;
	private CreateAlertProperties createAlertProperties = CreateAlertProperties.getInstance();
	private UiGeneratorController ui;
	private static final String STATUS_LABEL_COMPONENT = "status";
	private static final String ICON_STATUS_TRUE = "/icons/led_green.png";
	private static final String DISABLE_ALERT = "ON";
	private static final String ENABLE_ALERT = "OFF";
	private static final String ICON_STATUS_FALSE = "/icons/led_red.png";
	private Object status_label;
	private Object chckCompletesTgt;
	private Object chckMissesDeadline;
	private Object chckTwksWithoutPay;
	private Object chckAmnthWithoutPay;
	private Object chckMeetsHalf;
	
	public void tryToggleAutoReply(){
//		if (!createAlertProperties.isAlertOn()){
//			ui.showConfirmationDialog("toggleAlertOn", this, PaymentPluginConstants.ANALYTICS_ALERT);
//		}else{
			toggleAlertOn();
		//}
	}
	
	public void toggleAlertOn() {
		createAlertProperties.toggleAlert();
		setUpAlertUI();
		ui.removeDialog(ui.find(CONFIRM_DIALOG));
	}
	
	private void setUpAlertUI() {
		ui.setIcon(status_label, createAlertProperties.isAlertOn() ? ICON_STATUS_TRUE : ICON_STATUS_FALSE);
		ui.setText(status_label, (createAlertProperties.isAlertOn() ? DISABLE_ALERT : ENABLE_ALERT));
		
		ui.setSelected(chckCompletesTgt, createAlertProperties.getCompletesTgt());
		ui.setSelected(chckMissesDeadline, createAlertProperties.getMissesDeadline());
		ui.setSelected(chckTwksWithoutPay, createAlertProperties.getTwksWithoutPay());
		ui.setSelected(chckAmnthWithoutPay, createAlertProperties.getA_mnthWithoutPay());
		ui.setSelected(chckMeetsHalf, createAlertProperties.getMeetsHalfTgt());
	}

	public CreateAlertHandler(UiGeneratorController ui) {
		this.ui = ui;
		init();
		refresh();
	}

	public void addField() {
		Object label = ui.createLabel("Field");
		ui.add(compPanelFields, label);
		Object txtfield = ui.createTextfield("", "");
		ui.add(compPanelFields, txtfield);
		this.refresh();
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CREATE_ALERT, this);
		status_label = ui.find(dialogComponent, STATUS_LABEL_COMPONENT);
		chckCompletesTgt = ui.find(dialogComponent, COMPLETESTGT_CHECKBOX_COMPONENT);
		chckMissesDeadline = ui.find(dialogComponent, MISSEDDEADLINE_CHECKBOX_COMPONENT);
		chckTwksWithoutPay = ui.find(dialogComponent, TWOWEEKSNOPAY_CHECKBOX_COMPONENT);
		chckAmnthWithoutPay = ui.find(dialogComponent, AMONTHWITHNOPAY_CHECKBOX_COMPONENT);
		chckMeetsHalf = ui.find(dialogComponent, MEETSHALFTGT_CHECKBOX_COMPONENT);
	}

	private void refresh() {
		setUpAlertUI();
	}

	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}

	public void removeField() {
		// TODO Auto-generated method stub
	}
	
	public void updateChckCompletesTgt(boolean selected) {
		createAlertProperties.setCompletesTgt(selected);
	}
	public void updateChckMissesDeadline(boolean selected) {
		createAlertProperties.setMissesDeadline(selected);
	}
	public void updateChckTwksWithoutPay(boolean selected) {
		createAlertProperties.setTwksWithoutPay(selected);
	}
	public void updateChckAmnthWithoutPay(boolean selected) {
		createAlertProperties.setA_mnthWithoutPay(selected);
	}
	public void updateChckMeetsHalf(boolean selected) {
		createAlertProperties.setMeetsHalfTgt(selected);
	}
}
