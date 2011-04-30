package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.lang.reflect.Field;

import javax.persistence.Column;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;
import org.creditsms.plugins.paymentview.utils.StringUtil;

public class CustomizeClientDBHandler implements ThinletUiEventHandler {
	private static final String ENTER_NEW_FIELD = "Enter New Field";
	private static final String COMPONENT_PARENT_PNL_FIELDS = "parentPnlFields";
	private static final String COMPONENT_PNL_FIELDS = "pnlFields";

	private static final String XML_CUSTOMIZE_CLIENT_DB = "/ui/plugins/paymentview/clients/dialogs/dlgCustomizeClient.xml";

	private int c;
	private Object compPanelFields;
	private Object compParentPanelFields;
	private CustomFieldDao customFieldDao;

	private Object dialogComponent;
	private UiGeneratorController ui;
	private Object combobox;
	private ClientsTabHandler clientsTabHandler;

	public CustomizeClientDBHandler(UiGeneratorController ui,
			CustomFieldDao customFieldDao, ClientsTabHandler clientsTabHandler) {
		this.ui = ui;
		this.clientsTabHandler = clientsTabHandler;
		this.customFieldDao = customFieldDao;

		init();
		// refresh();
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
	}

	private void init() {
		dialogComponent = ui.loadComponentFromFile(XML_CUSTOMIZE_CLIENT_DB,
				this);
		compPanelFields = ui.find(dialogComponent, COMPONENT_PNL_FIELDS);
		compParentPanelFields = ui.find(dialogComponent,
				COMPONENT_PARENT_PNL_FIELDS);

		c = 0;
		for (Field field : Client.class.getDeclaredFields()) {
			if (field.isAnnotationPresent(Column.class)) {
				addField(StringUtil.getReadableFieldName(field.getName()));
			}
		}

		for (CustomField cf : customFieldDao.getAllActiveUsedCustomFields()) {
			if (cf.isActive() & cf.isUsed()) {
				addField(cf.getReadableName());
			}
		}
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

	public void addComboFieldChooser() {
		Object label = ui.createLabel("Field " + ++c);
		String fieldName = "fld" + c;
		Object cmbfield = this.combobox = ui.createCombobox(fieldName,
				"Select Field Name");

		this.refreshChoices(cmbfield);

		ui.setColspan(cmbfield, 2);
		ui.setColumns(cmbfield, 50);

		ui.add(compPanelFields, label);
		ui.add(compPanelFields, cmbfield);
		ui.setAction(cmbfield, "addNewField(" + fieldName + ")",
				compPanelFields, this);

		this.refresh();

	}

	public void refresh() {
		ui.remove(compPanelFields);
		int index = ui.getIndex(this.dialogComponent, compParentPanelFields);
		ui.remove(compParentPanelFields);

		ui.add(compParentPanelFields, compPanelFields);
		ui.add(this.dialogComponent, compParentPanelFields, index);

		// Rectangle pnlFieldsbounds = ui.getBounds(compPanelFields);
		//
		// Rectangle bounds = ui.getBounds(this.dialogComponent);
		// ui.repaint(this.dialogComponent, bounds.x, bounds.y,
		// bounds.width + bounds.x + pnlFieldsbounds.width, bounds.height +
		// bounds.y + pnlFieldsbounds.height);

	}

	public void refreshChoices() {
		refreshChoices(this.combobox);
	}

	public void refreshChoices(Object cmbfield) {
		ui.removeAll(cmbfield);

		for (CustomField cf : customFieldDao.getAllActiveUnusedCustomFields()) {
			if (cf.isActive() & !cf.isUsed()) {
				Object cmbchoice = ui.createComboboxChoice(
						cf.getReadableName(), cf);
				ui.add(cmbfield, cmbchoice);
			}
		}

		Object cmbchoice = ui.createComboboxChoice(ENTER_NEW_FIELD, null);
		ui.add(cmbfield, cmbchoice);
	}

	public void addNewField(Object fieldCombo) {
		if (ui.getText(fieldCombo).equals(ENTER_NEW_FIELD)) {
			showOtherFieldDialog(fieldCombo);
		} else {
			int index = ui.getIndex(compPanelFields, fieldCombo);
			ui.remove(fieldCombo);
			CustomField cf = (CustomField) ui.getAttachedObject(ui
					.getSelectedItem(fieldCombo));
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

	public void showOtherFieldDialog(Object comboBox) {
		this.combobox = comboBox;
		ui.add(new OtherFieldHandler(ui, customFieldDao, this, comboBox)
				.getDialog());
	}

	void setSelectedItemOnCombo(Object customField) {
		ui.setSelectedIndex(this.combobox,
				ui.getIndex(this.combobox, customField));
	}
}
