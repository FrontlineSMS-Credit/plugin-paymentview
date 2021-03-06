package net.frontlinesms.plugins.payment.settings.ui;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;
import org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.AuthorizationChecker;
import org.creditsms.plugins.paymentview.userhomepropeties.authorizationcode.AuthorizationProperties;

public class AuthCodeChangeDialogHandler extends BaseDialog {
	private static final String DIALOG_UPDATE_AUTHCODE_XML = "/ui/plugins/payment/settings/dlgChangeAuthSettings.xml";

	public AuthCodeChangeDialogHandler(UiGeneratorController ui) {
		super(ui);
		init();
	}

	private void init() {
	   this.dialogComponent = ui.loadComponentFromFile(DIALOG_UPDATE_AUTHCODE_XML, this);
	}

	public void updateAuthCode(String existing, String newcode, String verifynew) throws UnsupportedEncodingException, NoSuchAlgorithmException {
		//Check existing code
		if (AuthorizationChecker.authenticate(existing)){
			if (newcode.length()>0 || verifynew.length()>0){
				if (newcode.equals(verifynew)){
					AuthorizationProperties authProperties = AuthorizationProperties.getInstance();
					authProperties.setAuthCode(newcode);
					authProperties.saveToDisk();

					ui.infoMessage("Authorization code has been successfully updated.");
					ui.remove(dialogComponent);
				} else {
					ui.infoMessage("The new and verify authorization codes do not match.");
				}	
			} else {
				ui.infoMessage("The authorisation code cannot be empty.");
			}
		} else {
			ui.infoMessage("Wrong existing code. Please re-enter the existing code.");
		}
	}
}