package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.ResponseRecipient;
import org.creditsms.plugins.paymentview.data.domain.ThirdPartyResponse;
import org.creditsms.plugins.paymentview.data.repository.ResponseRecipientDao;
import org.creditsms.plugins.paymentview.data.repository.ThirdPartyResponseDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseActionDialog;

public class ThirdPartySMSDialogHandler extends BaseActionDialog {
	private static final String TBL_CLIENT_CONTACT_LIST = "tbl_clientContactList";
	private static final String XML_THIRD_PARTY_SMS_DIALOG = "/ui/plugins/paymentview/clients/dialogs/dlgThirdPartySMS.xml";
	PaymentViewPluginController pluginController;
	private Object clientTableComponent;
	public List<Client> clients;
	private ThirdPartyResponseDao thirdPartyResponseDao;
	private ResponseRecipientDao responseRecipientDao;
    private Client client;
    Object replyTextMessage;
    protected Object dialogComponent;
	
	public ThirdPartySMSDialogHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController, Client client) {
		super(ui);
		this.pluginController = pluginController;
		this.thirdPartyResponseDao = pluginController.getThirdPartyResponseDao();
		this.responseRecipientDao = pluginController.getResponseRecipientDao();
		this.client = client;
		init();
	}

	@Override
	public void init() {
		super.init();
		clientTableComponent = ui.find(this.getDialogComponent(), TBL_CLIENT_CONTACT_LIST);
		String message = "";
		ThirdPartyResponse tpResponse = new ThirdPartyResponse();
		if(this.thirdPartyResponseDao.getThirdPartyResponseByClientId(
				this.client.getId())==null){
		} else {
			tpResponse = this.thirdPartyResponseDao.getThirdPartyResponseByClientId(
					this.client.getId());
			message = tpResponse.getMessage();
			ui.setText(ui.find(this.getDialogComponent(), "replyContent"), message);
			
			ui.removeAll(clientTableComponent);
			List<Client> clients = new ArrayList<Client>();
			List<ResponseRecipient> tempResponseRecipientLst = this.responseRecipientDao.
			getResponseRecipientByThirdPartyResponseId(tpResponse.getId());
			
			for(ResponseRecipient respRecipient : tempResponseRecipientLst){
				clients.add(respRecipient.getClient());
				ui.add(this.clientTableComponent, getRow(respRecipient.getClient()));
			}
			setClients(clients);
		}
	}

	/** Save auto reply details 
	 * @throws DuplicateKeyException */
	public void save(String message) throws DuplicateKeyException {
		
		if(getClients() == null || getClients().size()==0){
			ui.alert("Please select the message recipient(s) to proceed.");
		} else {
			ThirdPartyResponse thirdPartyResponse = new ThirdPartyResponse();
			thirdPartyResponse.setClient(this.client);
			thirdPartyResponse.setMessage(message);
			if(this.thirdPartyResponseDao.getThirdPartyResponseByClientId(
					this.client.getId())==null){
				this.thirdPartyResponseDao.saveThirdPartyResponse(thirdPartyResponse);
			} else {
				thirdPartyResponse = this.thirdPartyResponseDao.getThirdPartyResponseByClientId(
						this.client.getId());
				thirdPartyResponse.setMessage(message);
				this.thirdPartyResponseDao.updateThirdPartyResponse(thirdPartyResponse);
			}

			for(int y =0; y<getClients().size(); y++) {
				ResponseRecipient responseRecipient = new ResponseRecipient();
				responseRecipient.setClient(getClients().get(y));
				responseRecipient.setThirdPartyResponse(thirdPartyResponse);
				
				boolean addNotResponseRecipient = false;
				
				List<ResponseRecipient> tempResponseRecipientLst = this.responseRecipientDao.
				getResponseRecipientByThirdPartyResponseId(thirdPartyResponse.getId());
				
				for (ResponseRecipient respRec: tempResponseRecipientLst) {
					if(responseRecipient.getClient().getPhoneNumber().equals(respRec.getClient().getPhoneNumber())){
						addNotResponseRecipient = true;
					}
				}
				if (!addNotResponseRecipient){
						this.responseRecipientDao.saveResponseRecipient(responseRecipient);
				}
			}
			this.removeDialog();			
		}
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