package org.creditsms.plugins.paymentview.ui.handler.importexport;

import java.io.IOException;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.importexport.ExportDialogHandler;

import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;

public class IncomingPaymentsExportHandler extends ExportDialogHandler<IncomingPayment> {

	public IncomingPaymentsExportHandler(Class<IncomingPayment> exportClass,
			UiGeneratorController ui) {
		super(exportClass, ui);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected void doSpecialExport(String dataPath) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doSpecialExport(String dataPath, List<IncomingPayment> selected)
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
