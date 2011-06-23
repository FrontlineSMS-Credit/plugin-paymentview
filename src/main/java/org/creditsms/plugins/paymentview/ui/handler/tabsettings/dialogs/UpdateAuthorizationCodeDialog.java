package org.creditsms.plugins.paymentview.ui.handler.tabsettings.dialogs;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.authorizationcode.AuthorizationChecker;
import org.creditsms.plugins.paymentview.authorizationcode.AuthorizationProperties;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;

public class UpdateAuthorizationCodeDialog extends BaseDialog{
	private static final String DIALOG_UPDATE_AUTHCODE_XML = "/ui/plugins/paymentview/settings/dialogs/dlgUpdateAuthSettings.xml";

	public UpdateAuthorizationCodeDialog(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
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
					AuthorizationProperties.getInstance().
					setAuthCode(AuthorizationChecker.getHash(AuthorizationChecker.ITERATION_NUMBER, newcode, AuthorizationChecker.getSalt()));
					
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
