package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.BaseActionDialog;

public class ThirdPartySMSDialogHandler extends BaseActionDialog  {
		private static final String XML_THIRD_PARTY_SMS_DIALOG = "/ui/plugins/paymentview/clients/dialogs/dlgThirdPartySMS.xml";
		PaymentViewPluginController pluginController;
		
		public ThirdPartySMSDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController) {
			super(ui);
			init();
			this.pluginController = pluginController;
		}
		
		@Override
		public void init() {
			super.init();
			//ui.setText(ui.find(this.getDialogComponent(), "replyContent"), autoReplyProperties.getMessage);
		}
		
		/** Save auto reply details */
		public void save(String message) {
			//autoReplyProperties.setMessage(message);
			this.removeDialog();
		}
		
		/** Remove a dialog from view. */
		public void removeDialog(Object dialog) {
			this.ui.removeDialog(dialog);
		}

		public void addConstantToDialog(String text, Object object, String type) {
			addConstantToCommand(text, object, type);
		}

		@Override
		protected void handleRemoved() {
			
		}

		@Override
		protected String getLayoutFilePath() {
			return XML_THIRD_PARTY_SMS_DIALOG;
		}
		
		public Object getDialog() {
			return this.getDialogComponent();
		}
		
		public void recipientDialog(){
			ui.add(new SelectRecipientDialogHandler(ui, this.pluginController).getDialog());
		}
	}