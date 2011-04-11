package org.creditsms.plugins.paymentview.ui.handler.importexport;

import java.io.File;
import java.io.IOException;
import java.util.List;

import net.frontlinesms.csv.CsvRowFormat;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.importexport.ExportDialogHandler;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.csv.CsvUtils;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.dummy.DummyData;
import org.creditsms.plugins.paymentview.data.importexport.CsvExporter;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;

public class ClientExportHandler extends ExportDialogHandler<Client> {
	/** I18n Text Key: TODO document */
	private static final String MESSAGE_EXPORTING_SELECTED_CONTACTS = "plugins.paymentview.message.exporting.selected.client";
	private static final String UI_FILE_OPTIONS_PANEL_CLIENT = "/ui/plugins/paymentview/importexport/pnClientDetails.xml";
	private static final String COMPONENT_CB_FIRSTNAME = "cbFirstame";
	private static final String COMPONENT_CB_OTHERNAME = "cbOtherName";
	private static final String COMPONENT_ACCOUNTS = "cbAccounts";
	private static final String COMPONENT_CB_PHONE = "cbPhone";
	 
	private ClientDao clientDao;
	
	public ClientExportHandler(UiGeneratorController ui) {
		super(Client.class, ui);
		clientDao = DummyData.INSTANCE.getClientDao();
	}

	@Override
	protected String getWizardTitleI18nKey() {
		return MESSAGE_EXPORTING_SELECTED_CONTACTS;
	}
	
	@Override
	protected String getOptionsFilePath() {
		return UI_FILE_OPTIONS_PANEL_CLIENT;
	}
	
	@Override
	public void doSpecialExport(String dataPath) throws IOException {
		log.debug("Exporting all contacts..");
		exportClients(this.clientDao.getAllClients(), dataPath);
	}

	@Override
	public void doSpecialExport(String dataPath, List<Client> selected) throws IOException {
		exportClients(selected, dataPath);
	}
	
	/**
	 * Export the supplied contacts using settings set in {@link #wizardDialog}.
	 * @param clients The contacts to export
	 * @param filename The file to export the contacts to
	 * @throws IOException 
	 */
	private void exportClients(List<Client> clients, String filename) throws IOException { 
		CsvRowFormat rowFormat = getRowFormatForClient();
		
		if (!rowFormat.hasMarkers()) {
			uiController.alert(InternationalisationUtils.getI18nString(MESSAGE_NO_FIELD_SELECTED));
			log.trace("EXIT");
			return;
		}
		
		log.debug("Row Format [" + rowFormat + "]");
		
		CsvExporter.exportClients(new File(filename), clients, rowFormat);
		uiController.setStatus(InternationalisationUtils.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
		this.uiController.infoMessage(InternationalisationUtils.getI18nString(MESSAGE_EXPORT_TASK_SUCCESSFUL));
	}
	
	protected CsvRowFormat getRowFormatForClient() { 
		CsvRowFormat rowFormat = new CsvRowFormat();
		addMarker(rowFormat, CsvUtils.MARKER_CLIENT_FIRST_NAME, COMPONENT_CB_FIRSTNAME);
		addMarker(rowFormat, CsvUtils.MARKER_CLIENT_OTHER_NAME, COMPONENT_CB_OTHERNAME);
		addMarker(rowFormat, CsvUtils.MARKER_CLIENT_ACCOUNTS, COMPONENT_ACCOUNTS);
		addMarker(rowFormat, CsvUtils.MARKER_CONTACT_PHONE, COMPONENT_CB_PHONE); 
		return rowFormat;
	}
}
