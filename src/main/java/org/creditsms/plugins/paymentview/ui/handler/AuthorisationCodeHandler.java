package org.creditsms.plugins.paymentview.ui.handler;

import java.lang.reflect.Method;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.AuthorizationChecker;

public class AuthorisationCodeHandler extends BaseDialog{
	private static final String XML_ENTER_AUTHORIZATION_CODE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep3.xml";
	private Method authorizationAction;
	private ThinletUiEventHandler authorizationHandler;
	
	public AuthorisationCodeHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		super(ui);
	}
	
	public void showAuthorizationCodeDialog(String methodToBeCalled, ThinletUiEventHandler eventListener){
		dialogComponent = ui.loadComponentFromFile(XML_ENTER_AUTHORIZATION_CODE, this);
		try {
			setAuthorizationAction(eventListener.getClass().getMethod(methodToBeCalled));
			setAuthorizationHandler(eventListener);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		ui.add(dialogComponent);
	}
	
	private void setAuthorizationHandler(
			ThinletUiEventHandler authorizationEventListener) {
		this.authorizationHandler = authorizationEventListener;
	}

	private void setAuthorizationAction(Method authorizationAction) {
		this.authorizationAction = authorizationAction;
	}

 	public void authorize(String authCode, String verifyAuthCode) {
		if ((authCode.equals(verifyAuthCode)) && AuthorizationChecker.authenticate(authCode)) {
			if (authorizationAction != null) {
				try {
					authorizationAction.invoke(authorizationHandler);
				}  catch (Exception e) {
					throw new RuntimeException(e);
				} finally {
					ui.remove(dialogComponent);
				}
			} else {
				throw new RuntimeException("Null AuthorizationAction!");
			}
		} else {
			ui.alert("Invalid Entry! Enter the Authorization Code Again.");
		}
	}
 	
	public Object getDialog() {
		return dialogComponent;
	}

	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}

	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
	
	public Object find(String object) {
		return ui.find(this.dialogComponent, object);
	}
	
	public void showDialog() {
		ui.add(this.dialogComponent);
	}
}
