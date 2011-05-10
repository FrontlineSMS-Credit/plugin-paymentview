package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.domain.CustomValue;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;

public class EditClientHandler extends BaseDialog{
	private static final String COMPONENT_LIST_ACCOUNTS = "fldAccounts";

	private static final String COMPONENT_TEXT_FIRST_NAME = "fldFirstName";
	private static final String COMPONENT_TEXT_OTHER_NAME = "fldOtherName";
	private static final String COMPONENT_TEXT_PHONE_NUMBER = "fldPhoneNumber";
	private static final String XML_EDIT_CLIENT = "/ui/plugins/paymentview/clients/dialogs/dlgEditClient.xml";

	private ClientDao clientDao;
	private Client client;
	private Object dialogComponent;
	private boolean editMode;

	private Object fieldFirstName;
	private Object fieldListAccounts;
	private Object fieldOtherName;
	private Object fieldPhoneNumber;
	private UiGeneratorController ui;

	private ClientsTabHandler clientsTabHandler;

	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;

	private Object compPanelFields;
	private Map<CustomField, Object> customComponents;

	public EditClientHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, ClientsTabHandler clientsTabHandler) {
		super(ui);
		this.clientDao = pluginController.getClientDao();
		this.clientsTabHandler = clientsTabHandler;
		this.customValueDao = pluginController.getCustomValueDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		this.editMode = false;
		init();
		refresh();
	}

	public EditClientHandler(UiGeneratorController ui, Client client,
			PaymentViewPluginController pluginController, ClientsTabHandler clientsTabHandler) {
		super(ui);
		this.client = client;
		this.clientsTabHandler = clientsTabHandler;
		this.customValueDao = pluginController.getCustomValueDao();
		this.customFieldDao = pluginController.getCustomFieldDao();
		this.clientDao = pluginController.getClientDao();
		this.editMode = true;
		init();
		refresh();
	}

	private Object createListItem(Account acc) {
		return ui.createListItem(acc.getAccountNumber(), acc, true);
	}

	/** 
	 * @return the clientObj
	 */
	public Client getClientObj() { 
		return client;
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EDIT_CLIENT, this);
		compPanelFields = ui.find(dialogComponent, "pnlFields");

		fieldFirstName = ui.find(dialogComponent, COMPONENT_TEXT_FIRST_NAME);
		fieldPhoneNumber = ui
				.find(dialogComponent, COMPONENT_TEXT_PHONE_NUMBER);
		fieldOtherName = ui.find(dialogComponent, COMPONENT_TEXT_OTHER_NAME);
		fieldListAccounts = ui.find(dialogComponent, COMPONENT_LIST_ACCOUNTS);

		List<CustomField> allUsedCustomFields = customFieldDao
				.getAllActiveUsedCustomFields();

		customComponents = new HashMap<CustomField, Object>(
				allUsedCustomFields.size());

		for (CustomField cf : allUsedCustomFields) {
			addField(cf, cf.getCamelCaseName(), cf.getReadableName());
		}

	}

	public void addField(CustomField cf, String name, String readableName) {
		Object label = ui.createLabel(readableName);

		Object txtfield = ui.createTextfield(name, "");
		customComponents.put(cf, txtfield);

		ui.setColspan(txtfield, 2);
		ui.setColumns(txtfield, 50);

		ui.add(compPanelFields, label);
		ui.add(compPanelFields, txtfield);
	}

	public void refresh() {
		if (editMode) {
			ui.setText(fieldFirstName, this.getClientObj().getFirstName());
			ui.setText(fieldOtherName, this.getClientObj().getOtherName());
			ui.setText(fieldPhoneNumber, this.getClientObj().getPhoneNumber());

			List<CustomField> allCustomFields = this.customFieldDao
					.getAllActiveUsedCustomFields();
			List<CustomValue> allCustomValues = this.customValueDao
					.getCustomValuesByClientId(client.getId());

			if (!allCustomFields.isEmpty()) {
				for (CustomField cf : allCustomFields) {
					for (CustomValue cv : allCustomValues) {
						if (cv.getCustomField().equals(cf)) {
							ui.setText(customComponents.get(cf),
									cv.getStrValue());
						}
					}
				}
			}

			for (Account acc : this.getClientObj().getAccounts()) {
				ui.add(fieldListAccounts, createListItem(acc));
			}
		}
	}

	public void saveClient() {
		if (editMode) {
			try {
				this.client.setFirstName(ui.getText(fieldFirstName));
				this.client.setOtherName(ui.getText(fieldOtherName));
				this.client.setPhoneNumber(ui.getText(fieldPhoneNumber));
				this.clientDao.updateClient(this.client);

				List<CustomField> allCustomFields = this.customFieldDao
						.getAllActiveUsedCustomFields();

				if (!allCustomFields.isEmpty()) {
					for (CustomField cf : allCustomFields) {
						List<CustomValue> cvs = customValueDao
								.getCustomValuesByClientId(client.getId());
						CustomValue cv = null;

						for (CustomValue _cv : cvs) {
							if (_cv.getCustomField().equals(cf)) {
								cv = _cv;
							}

						}
						if (cv == null) {
							cv = new CustomValue(ui.getText(customComponents
									.get(cf)), cf, client);
							try {
								customValueDao.saveCustomValue(cv);
							} catch (DuplicateKeyException e) {
								throw new RuntimeException(e);
							}
						} else {
							cv.setStrValue(ui.getText(customComponents.get(cf)));

							try {
								customValueDao.updateCustomValue(cv);
							} catch (DuplicateKeyException e) {
								throw new RuntimeException(e);
							}
						}
					}
				}
			} catch (DuplicateKeyException e) {
				throw new RuntimeException(e);
			}
		} else {
			String fn = ui.getText(fieldFirstName);
			String on = ui.getText(fieldOtherName);
			String phone = ui.getText(fieldPhoneNumber);
			Client client = new Client(fn, on, phone);
			try {
				this.clientDao.saveClient(client);
			} catch (DuplicateKeyException e) {
				throw new RuntimeException(e);
			}

			List<CustomField> allUsedCustomFields = this.customFieldDao
					.getAllActiveUsedCustomFields();

			if (!allUsedCustomFields.isEmpty()) {
				for (CustomField cf : allUsedCustomFields) {
					CustomValue cv = new CustomValue(
							ui.getText(customComponents.get(cf)), cf, client);
					try {
						customValueDao.saveCustomValue(cv);
					} catch (DuplicateKeyException e) {
						throw new RuntimeException(e);
					}
				}
			}
		}
		removeDialog();
		clientsTabHandler.refresh();
	}
}
