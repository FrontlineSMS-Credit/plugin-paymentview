package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;

public class OtherFieldHandler implements ThinletUiEventHandler {
	private static final String XML_OTHER_FIELD = "/ui/plugins/paymentview/clients/dialogs/dlgOtherField.xml";
	private CustomFieldDao customFieldDao;

	private Object dialogComponent;
	private UiGeneratorController ui;
	private int c;

	public OtherFieldHandler(PaymentViewPluginController pluginController,
			CustomFieldDao customFieldDao,
			int c) {
		this.c = c;
		this.ui = pluginController.getUiGeneratorController();
		this.customFieldDao = customFieldDao;
		
		init();
		refresh();
	}

	public void createField(String fieldName) throws DuplicateKeyException {
		CustomField customField = new CustomField();
		customField.setReadableName(fieldName);
		customField.setActive(true);
		customField.setUsed(true);
		try {
			this.customFieldDao.saveCustomField(customField);
		} catch (DuplicateKeyException e) {
			List<CustomField> customFields = customFieldDao.getCustomFieldsByReadableName(fieldName);
			if (customFields.size() == 1){
				customField = customFields.get(0);
				customField.setUsed(true);
				this.customFieldDao.updateCustomField(customField);
			}
		}

		this.removeDialog();
		ui.infoMessage("You have succesfully created the field '" + fieldName + "'!");
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
		ui.setText(dialogComponent, "Field "+ ++c +" - Other Field");
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
