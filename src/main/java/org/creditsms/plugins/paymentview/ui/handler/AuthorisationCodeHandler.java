package org.creditsms.plugins.paymentview.ui.handler;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;
import org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.AuthorizationChecker;

public class AuthorisationCodeHandler extends BaseDialog{
	private static final String XML_ENTER_AUTHORIZATION_CODE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep3.xml";
	private MethodInvoker methodinvoker;
	
	public AuthorisationCodeHandler(UiGeneratorController ui) {
		super(ui);
	}
	
	public void showAuthorizationCodeDialog(String methodToBeCalled, ThinletUiEventHandler eventListener){
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_AUTHORIZATION_CODE, this);
		try {
			methodinvoker = new MethodInvoker(eventListener, methodToBeCalled);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		ui.add(dialogComponent);
	}
	
 	public void authorize(String authCode, String verifyAuthCode) {
		if ((authCode.equals(verifyAuthCode)) && AuthorizationChecker.authenticate(authCode)) {
				try {
					methodinvoker.invoke();
				}  catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					removeDialog();
				}
		} else {
			ui.alert("Invalid Entry! Enter the Authorization Code Again.");
		}
	}
	
	@Override
	public void showDialog() {
		ui.add(this.dialogComponent);
	}
}
