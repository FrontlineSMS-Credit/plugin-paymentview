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
	private List<Client> deletedClients;
	private ClientSelector clientSelector;
	
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
		clients = new ArrayList<Client>();
		deletedClients = new ArrayList<Client>();
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
			List<ResponseRecipient> tempResponseRecipientLst = this.responseRecipientDao.
			getResponseRecipientByThirdPartyResponseId(tpResponse.getId());
			
			for(ResponseRecipient respRecipient : tempResponseRecipientLst){
				clients.add(respRecipient.getClient());
				ui.add(this.clientTableComponent, getRow(respRecipient.getClient()));
			}
		}
	}

	/** Save auto reply details 
	 * @throws DuplicateKeyException */
	public void save(String message) throws DuplicateKeyException {
		ThirdPartyResponse thirdPartyResponse = new ThirdPartyResponse();
		thirdPartyResponse.setClient(this.client);
		thirdPartyResponse.setMessage(message);
		if(this.thirdPartyResponseDao.getThirdPartyResponseByClientId(this.client.getId())==null){
			this.thirdPartyResponseDao.saveThirdPartyResponse(thirdPartyResponse);
		} else {
			thirdPartyResponse = this.thirdPartyResponseDao.getThirdPartyResponseByClientId(
					this.client.getId());
			thirdPartyResponse.setMessage(message);
			this.thirdPartyResponseDao.updateThirdPartyResponse(thirdPartyResponse);
			
			//delete clients in DB
			for (Client deletedClient :deletedClients){
				ResponseRecipient deletedResponseRecipient = this.responseRecipientDao.
					getResponseRecipientByTpResponseAndRecipient(thirdPartyResponse.getId(), deletedClient);
				if (deletedResponseRecipient != null){
					this.responseRecipientDao.deleteResponseRecipient(deletedResponseRecipient);
				}
			}
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
	
	/** Delete selected Third Party SMS recipient */
	public void deleteThirdParty(){
		Object[] selectedThirdPartyRecipients = this.ui.getSelectedItems(clientTableComponent);
		if (selectedThirdPartyRecipients.length == 0){
			ui.infoMessage("Please select Third Party SMS recipient(s).");	
		} else {
			for (Object selectedThirdPartyRecipient : selectedThirdPartyRecipients) {
				Client attachedThirdPartyRecipient = ui.getAttachedObject(selectedThirdPartyRecipient, Client.class);
				clients.remove(attachedThirdPartyRecipient);
				deletedClients.add(attachedThirdPartyRecipient);
				this.receiver(clients);
			}
		}
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
	
	public void receiver(List<Client> clients){
		if (clientSelector != null){
			clientSelector.removeDialog();
		}

		ui.removeAll(clientTableComponent);
		addClients(clients);
		for(Client client : this.clients){
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
		clientSelector = new ClientSelector(ui, pluginController);
		clientSelector.setSelectionMethod("multiple");
		clientSelector.showClientSelectorDialog(this, "receiver", List.class);
	}
	
	public List<Client> getClients() {
		return clients;
	}

	public void addClients(List<Client> clients) {
		for (Client c : clients) {
			if (!this.clients.contains(c)) 
				this.clients.add(c);
		}
	}
}