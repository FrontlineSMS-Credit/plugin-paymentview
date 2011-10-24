package org.creditsms.plugins.paymentview.ui.handler.tabanalytics.dialogs;

import org.creditsms.plugins.paymentview.utils.PaymentPluginConstants;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import org.creditsms.plugins.paymentview.userhomepropeties.incomingpayments.AutoReplyProperties;

public class CreateAlertHandler implements ThinletUiEventHandler {
	private static final String XML_CREATE_ALERT = "/ui/plugins/paymentview/analytics/dialogs/dlgCreateAlert.xml";

	private static final String CONFIRM_DIALOG = "confirmDialog";
	private Object compPanelFields;
	private Object dialogComponent;
	private AutoReplyProperties autoReplyProperties = AutoReplyProperties.getInstance();
	private UiGeneratorController ui;
	private static final String STATUS_LABEL_COMPONENT = "status";
	private static final String ICON_STATUS_TRUE = "/icons/led_green.png";
	private static final String DISABLE_AUTOREPLY = "ON";
	private static final String ENABLE_AUTOREPLY = "OFF";
	private static final String ICON_STATUS_FALSE = "/icons/led_red.png";
	private Object status_label;
	
	public void tryToggleAutoReply(){
		if (!autoReplyProperties.isAutoReplyOn()){
			ui.showConfirmationDialog("toggleAutoReplyOn", this, PaymentPluginConstants.AUTO_REPLY_CONFIRMATION);
		}else{
			toggleAutoReplyOn();
		}
	}
	
	public void toggleAutoReplyOn() {
		autoReplyProperties.toggleAutoReply();
		setUpAutoReplyUI();
		ui.removeDialog(ui.find(CONFIRM_DIALOG));
	}
	
	private void setUpAutoReplyUI() {
		ui.setIcon(status_label, autoReplyProperties.isAutoReplyOn() ? ICON_STATUS_TRUE : ICON_STATUS_FALSE);
		ui.setText(status_label, (autoReplyProperties.isAutoReplyOn() ? DISABLE_AUTOREPLY : ENABLE_AUTOREPLY));
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
	}

	private void refresh() {
		setUpAutoReplyUI();
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

}
