package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Id;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.EventObserver;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

public class CustomizeClientDBHandler implements ThinletUiEventHandler, EventObserver {
	private static final String LST_CUSTOM_FIELDS = "lstCustomFields";
	private static final String COMPONENT_PNL_FIELDS = "pnlFields";

	private static final String XML_CUSTOMIZE_CLIENT_DB = "/ui/plugins/paymentview/clients/dialogs/dlgCustomizeClient.xml";

	private int c;
	private Object compPanelFields;
	private CustomFieldDao customFieldDao;

	private Object dialogComponent;
	private UiGeneratorController ui;
	private ClientsTabHandler clientsTabHandler;
	private Object listCustomFields;
	private PaymentViewPluginController pluginController;

	public CustomizeClientDBHandler(PaymentViewPluginController pluginController,
			CustomFieldDao customFieldDao, ClientsTabHandler clientsTabHandler) {
		this.pluginController = pluginController;
		this.ui = pluginController.getUiGeneratorController();
		this.clientsTabHandler = clientsTabHandler;
		this.customFieldDao = customFieldDao;
		
		ui.getFrontlineController().getEventBus().registerObserver(this);

		init();
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CUSTOMIZE_CLIENT_DB, this);
		compPanelFields = ui.find(dialogComponent, COMPONENT_PNL_FIELDS);
		listCustomFields = ui.find(dialogComponent, LST_CUSTOM_FIELDS);

		c = 0;
		for (Field field : Client.class.getDeclaredFields()) {
			if (field.isAnnotationPresent(Column.class) && (!field.isAnnotationPresent(Id.class))) {
				addField(PaymentViewUtils.getReadableFieldName(field.getName()));
			}
		}
		
		refreshList();
	}

	public void addField(String name) {
		Object label = ui.createLabel("Field " + ++c);
		Object txtfield = ui.createTextfield("fld" + name, name);

		ui.setColspan(txtfield, 2);
		ui.setColumns(txtfield, 50);
		ui.setEditable(txtfield, false);

		ui.add(compPanelFields, label);
		ui.add(compPanelFields, txtfield);
	}
	
	private void addListItem(CustomField cf, int count) {
		Object txtfield = ui.createListItem("Field "+ count + " - "+ cf.getReadableName(), cf);
		ui.add(listCustomFields, txtfield);
	}

	public void refresh() {
		this.refreshList();
	}

	public void refreshList() {
		ui.removeAll(listCustomFields);
		for (CustomField cf : customFieldDao.getAllActiveUsedCustomFields()) {
			addListItem(cf, ++c);
		}
	}

	public void addNewField(Object fieldCombo) {
		CustomField cf = (CustomField) ui.getAttachedObject(ui
				.getSelectedItem(fieldCombo));
		if (cf != null) {
			int index = ui.getIndex(compPanelFields, fieldCombo);
			ui.remove(fieldCombo);
			Object txtField = ui.createTextfield(ui.getName(fieldCombo),
					cf.getReadableName());

			cf.setUsed(true);

			try {
				customFieldDao.updateCustomField(cf);
			} catch (DuplicateKeyException e) {
				new RuntimeException(e);
			}
			ui.add(compPanelFields, txtField, index);
			ui.setColspan(txtField, 2);
			ui.setColumns(txtField, 50);
			ui.setEditable(txtField, false);
		}
	}

	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
		clientsTabHandler.revalidateTable();
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}

	/** Add a dialog from view. */
	public void addDialog() {
		this.addDialog(this.dialogComponent);
	}

	/** Add a dialog from view. */
	public void addDialog(Object dialog) {
		this.ui.add(dialog);
	}

	public void showOtherFieldDialog() {
		ui.add(new OtherFieldHandler(pluginController, customFieldDao, this).getDialog());
	}

	public void notify(FrontlineEventNotification notification) {
		if (!(notification instanceof EntitySavedNotification)) {
			return;
		}

		Object entity = ((EntitySavedNotification) notification).getDatabaseEntity();
		if (entity instanceof CustomField) {
			this.refresh();
		}
	}
}
