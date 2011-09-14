package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseActionDialog;

public class ThirdPartySMSDialogHandler extends BaseActionDialog {
	private static final String TBL_CLIENT_CONTACT_LIST = "tbl_clientContactList";
	private static final String XML_THIRD_PARTY_SMS_DIALOG = "/ui/plugins/paymentview/clients/dialogs/dlgThirdPartySMS.xml";
	PaymentViewPluginController pluginController;
	private Object clientTableComponent;
	public List<Client> clients;

	public ThirdPartySMSDialogHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui);
		init();
		this.pluginController = pluginController;
	}

	@Override
	public void init() {
		super.init();
		clientTableComponent = ui.find(this.getDialogComponent(), TBL_CLIENT_CONTACT_LIST);
		// ui.setText(ui.find(this.getDialogComponent(), "replyContent"),
		// autoReplyProperties.getMessage);
	}

	/** Save auto reply details */
	public void save(String message) {
		// autoReplyProperties.setMessage(message);
		this.removeDialog();
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
	
	public void receiver(List<Client> clients){
		ui.removeAll(clientTableComponent);
		setClients(clients);
		for(Client client : clients){
			ui.add(this.clientTableComponent, getRow(client));
		}
	}
	
	protected Object getRow(Client client) {
		Object row = ui.createTableRow(client);

		ui.add(row, ui.createTableCell(client.getFullName()));
		ui.add(row, ui.createTableCell(client.getPhoneNumber()));

		return row;
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

	public Object getDialogComponent() {
		return super.getDialogComponent();
	}

	public void recipientDialog() {
		ClientSelector clientSelector = new ClientSelector(ui, pluginController);
		clientSelector.setSelectionMethod("multiple");
		clientSelector.showClientSelectorDialog(this, "receiver", List.class);
	}
	
	public List<Client> getClients() {
		return clients;
	}

	public void setClients(List<Client> clients) {
		this.clients = clients;
	}
}