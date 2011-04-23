package org.creditsms.plugins.paymentview.ui.handler.client.dialogs;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;

public class EditClientHandler implements ThinletUiEventHandler {
	private static final String COMPONENT_LIST_ACCOUNTS = "fldAccounts";

	private static final String COMPONENT_TEXT_FIRST_NAME = "fldFirstName";
	private static final String COMPONENT_TEXT_OTHER_NAME = "fldOtherName";
	private static final String COMPONENT_TEXT_PHONE_NUMBER = "fldPhoneNumber";
	private static final String XML_EDIT_CLIENT = "/ui/plugins/paymentview/clients/dialogs/dlgEditClient.xml";

	private ClientDao clientDao;
	private Client clientObj;
	private Object dialogComponent;
	private boolean editMode;

	private Object fieldFirstName;
	private Object fieldListAccounts;
	private Object fieldOtherName;
	private Object fieldPhoneNumber;
	private UiGeneratorController ui;

	public EditClientHandler(UiGeneratorController ui) {
		this.ui = ui;
		this.editMode = false;
		init();
		refresh();
	}

	public EditClientHandler(UiGeneratorController ui, Client clientObj,
			ClientDao clientDao) {
		this.editMode = true;
		this.setClientObj(clientObj);
		this.clientDao = clientDao;
		this.ui = ui;
		init();
		refresh();
	}

	private Object createListItem(Account acc) {
		return ui.createListItem(acc.getAccountNumber(), acc,
				true);
	}

	/**
	 * @return the clientObj
	 */
	public Client getClientObj() {
		return clientObj;
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EDIT_CLIENT, this);

		fieldFirstName = ui.find(dialogComponent, COMPONENT_TEXT_FIRST_NAME);
		fieldPhoneNumber = ui
				.find(dialogComponent, COMPONENT_TEXT_PHONE_NUMBER);
		fieldOtherName = ui.find(dialogComponent, COMPONENT_TEXT_OTHER_NAME);
		fieldListAccounts = ui.find(dialogComponent, COMPONENT_LIST_ACCOUNTS);
	}

	public void refresh() {
		if (editMode) {
			ui.setText(fieldFirstName, this.getClientObj().getFirstName());
			ui.setText(this.fieldOtherName, this.getClientObj().getOtherName());
			ui.setText(fieldPhoneNumber, this.getClientObj().getPhoneNumber());

			for (Account acc : this.getClientObj().getAccounts()) {
				ui.add(fieldListAccounts, createListItem(acc));
			}
		}
	}

	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}

	public void saveClient() throws DuplicateKeyException {
		this.clientDao.saveClient(getClientObj());
	}

	/**
	 * @param clientObj
	 *            the clientObj to set
	 */
	public void setClientObj(Client clientObj) {
		this.clientObj = clientObj;
	}
}
