package org.creditsms.plugins.paymentview.authorizationcode.ui;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BasePanelHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

import thinlet.Thinlet;

public class AuthorizationCodeHandler extends BasePanelHandler{
	
private static final String BTN_AUTHORIZE = "btnAuthorize";
private static final String XML_ENTER_AUTHORIZATION_CODE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep3.xml";

private final ThinletUiEventHandler eventListener;
private final String methodToBeCalled;
	
	public AuthorizationCodeHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, ThinletUiEventHandler eventListener, String methodToBeCalled){
		super(ui);
		this.eventListener = eventListener;
		this.methodToBeCalled = methodToBeCalled;
		init();
		
	}

	private void init() {
		this.loadPanel(XML_ENTER_AUTHORIZATION_CODE);
		((UiGeneratorController)ui).setMethod(ui.find(getPanelComponent(), BTN_AUTHORIZE), 
				Thinlet.ATTRIBUTE_ACTION, methodToBeCalled, 
				getPanelComponent(), eventListener);
		
		ui.add(getPanelComponent());
	}

	public void showDialog() {
		ui.add(this.getPanelComponent());
	}
	
	
}
