package org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs;

import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.ClientExportHandler;
import org.creditsms.plugins.paymentview.utils.PaymentType;

public class ExportByClientXticsStep2Handler extends ClientExportHandler {
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_EXPORT_WIZARD_FORM = "/ui/plugins/paymentview/export/dialogs/dlgExportByClientXtics1.xml";
	private static final String UI_FILE_OPTIONS_PANEL_CLIENT = "/ui/plugins/paymentview/importexport/pnClientDetails.xml";

	private String exportfilepath;
	private List<Client> selectedUsers;

	private PaymentType paymentType;

	public ExportByClientXticsStep2Handler(UiGeneratorController ui,
			ClientDao clientDao, CustomValueDao customValueDao,
			CustomFieldDao customFieldDao, final List<Client> selectedUsers,
			final PaymentType paymentType) {
		super(ui, clientDao, customFieldDao, customValueDao);
		this.selectedUsers = selectedUsers;		
		this.paymentType = paymentType;
	}
	
	@Override
	protected String getDialogFile() {
		return UI_FILE_EXPORT_WIZARD_FORM;
	}

	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CLIENT;
	}
	
	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_EXPORTING_SELECTED_CONTACTS;
	}

	public void handleDoExport() {
		super.handleDoExport(this.exportfilepath);
	}

	public void next(String exportfilepath) {
		this.exportfilepath = exportfilepath;
		uiController.add(new ExportByClientXticsStep3Handler(uiController,
				this, exportfilepath, selectedUsers).getDialog());
		removeDialog();
	}
}
