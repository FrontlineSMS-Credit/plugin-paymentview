package org.creditsms.plugins.paymentview.ui.handler.client.dialogs;

import org.creditsms.plugins.paymentview.data.domain.CustomField;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class OtherFieldHandler implements ThinletUiEventHandler {
	private static final String XML_OTHER_FIELD = "/ui/plugins/paymentview/clients/dialogs/dlgOtherField.xml";
	private UiGeneratorController ui;
	private Object dialogComponent;
	
	private CustomFieldDao customDataDao = DummyData.INSTANCE.getCustomFieldDao();
	private CustomizeClientDBHandler dlgCustomizeClientDB;
	
	public OtherFieldHandler(UiGeneratorController ui, CustomizeClientDBHandler dlgCustomizeClientDB) {
		this.ui = ui;
		init();
		refresh();
		this.dlgCustomizeClientDB = dlgCustomizeClientDB;
	}

	public void refresh() {				
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_OTHER_FIELD, this);		
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
	
	/** Remove the dialog from view. */
	public void removeDialog() {
		this.removeDialog(this.dialogComponent);
	}

	/** Remove a dialog from view. */
	public void removeDialog(Object dialog) {
		this.ui.removeDialog(dialog);
	}

	public Object getDialog() {
		return this.dialogComponent;
	}
}
