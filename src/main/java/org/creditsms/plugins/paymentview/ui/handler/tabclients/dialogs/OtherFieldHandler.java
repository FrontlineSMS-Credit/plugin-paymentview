package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;

public class OtherFieldHandler implements ThinletUiEventHandler {
	private static final String XML_OTHER_FIELD = "/ui/plugins/paymentview/clients/dialogs/dlgOtherField.xml";
	private CustomFieldDao customDataDao;
	private CustomValueDao customValueDao;

	private Object dialogComponent;
	private CustomizeClientDBHandler dlgCustomizeClientDB;
	private UiGeneratorController ui;

	public OtherFieldHandler(UiGeneratorController ui,
			CustomizeClientDBHandler dlgCustomizeClientDB,
			CustomFieldDao customDataDao, CustomValueDao customValueDao) {
		this.ui = ui;
		this.customDataDao = customDataDao;
		this.customValueDao = customValueDao;
		init();
		refresh();
		this.dlgCustomizeClientDB = dlgCustomizeClientDB;
	}

	public void createField(String fieldName) {
		CustomField customField = new CustomField(fieldName);
		try {
			this.customDataDao.saveCustomField(customField);
		} catch (DuplicateKeyException e) {
			throw new RuntimeException(e);
		}

		this.removeDialog();
		this.dlgCustomizeClientDB.addField(fieldName);
		this.dlgCustomizeClientDB.refresh();
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
