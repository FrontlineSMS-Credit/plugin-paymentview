package org.creditsms.plugins.paymentview.ui.handler.importexport;

import java.io.IOException;
import java.util.List;

import org.creditsms.plugins.paymentview.data.domain.Payment;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.importexport.ExportDialogHandler;

public class PaymentsExportHandler extends ExportDialogHandler<Payment> {

	public PaymentsExportHandler(Class<Payment> exportClass,
			UiGeneratorController ui) {
		super(exportClass, ui);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doSpecialExport(String dataPath) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doSpecialExport(String dataPath, List<Payment> selected)
			throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected String getWizardTitleI18nKey() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String getOptionsFilePath() {
		// TODO Auto-generated method stub
		return null;
	}

}
