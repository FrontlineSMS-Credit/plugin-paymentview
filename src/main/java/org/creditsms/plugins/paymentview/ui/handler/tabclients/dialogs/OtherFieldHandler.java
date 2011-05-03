package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;

public class OtherFieldHandler implements ThinletUiEventHandler {
	private static final String XML_OTHER_FIELD = "/ui/plugins/paymentview/clients/dialogs/dlgOtherField.xml";
	private CustomFieldDao customFieldDao;

	private Object dialogComponent;
	private UiGeneratorController ui;
	private CustomizeClientDBHandler customizeClientDBHandler;

	public OtherFieldHandler(UiGeneratorController ui,
			CustomFieldDao customFieldDao,
			CustomizeClientDBHandler customizeClientDBHandler, Object comboBox) {
		this.ui = ui;
		this.customFieldDao = customFieldDao;
		this.customizeClientDBHandler = customizeClientDBHandler;
		init();
		refresh();
	}

	public void createField(String fieldName) {
		CustomField customField = new CustomField();
		customField.setReadableName(fieldName);
		customField.setActive(true);
		try {
			this.customFieldDao.saveCustomField(customField);
		} catch (DuplicateKeyException e) {
			throw new RuntimeException(e);
		}

		this.removeDialog();
		this.customizeClientDBHandler.refreshChoices();
		customizeClientDBHandler.setSelectedItemOnCombo(customField);
		ui.infoMessage("You have succesfully created the field '" + fieldName
				+ "'!");
	}

	public static String camelCase(String string) {
		String result = "";
		for (int i = 0; i < string.length(); i++) {
			String next = string.substring(i, i + 1);
			if (i == 0) {
				result += next.toUpperCase();
			} else {
				result += next.toLowerCase();
			}
		}
		return result;
	}

	public Object getDialog() {
		return this.dialogComponent;
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_OTHER_FIELD, this);
	}

	public void refresh() {
	}

	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}
}
