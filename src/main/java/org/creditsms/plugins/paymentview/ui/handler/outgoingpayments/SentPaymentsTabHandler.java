package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Random;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsImportHandler;

public class SentPaymentsTabHandler extends BaseTabHandler {
	private static final String COMPONENT_CLIENT_TABLE = "tbl_clients";
	private static final String COMPONENT_INCOMING_PAYMENTS_TABLE = "tbl_clients";
	private static final String TABBED_PANE_MAIN = "tabbedPaneMain";
	private static final String XML_SENTPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/sentpayments.xml";
	private AccountDao accountDao;
	private ClientDao clientDao;
	SimpleDateFormat df = new SimpleDateFormat("dd.MM.yyyy");
	private NumberFormat formatter = new DecimalFormat("#,000.00");
	private OutgoingPaymentDao outgoingPaymentDao;
	//
	// For Dummy Purposes
	Random random = new Random();
	//

	private Object sentPaymentsTab;

	SimpleDateFormat tf = new SimpleDateFormat("hh:mm:ss");

	public SentPaymentsTabHandler(UiGeneratorController ui,
			ClientDao clientDao, OutgoingPaymentDao outgoingPaymentDao,
			AccountDao accountDao) {
		super(ui);
		this.clientDao = clientDao;
		this.accountDao = accountDao;
		this.outgoingPaymentDao = outgoingPaymentDao;
		init();
	}

	private Object createRow(OutgoingPayment o) {
		Client c = clientDao.getAllClients().get(
				random.nextInt(clientDao.getAllClients().size()));
		Object row = ui.createTableRow();
		ui.add(row,
				ui.createTableCell(c.getFirstName() + " " + c.getOtherName()));
		ui.add(row, ui.createTableCell(o.getPhoneNumber()));
		ui.add(row, ui.createTableCell(formatter.format(o.getAmountPaid())));
		ui.add(row, ui.createTableCell(df.format(o.getTimePaid())));
		ui.add(row, ui.createTableCell(tf.format(o.getTimePaid())));
		ui.add(row, ui.createTableCell(Long.toString(o.getAccount()
				.getAccountNumber())));
		ui.add(row, ui.createTableCell(o.getNotes()));
		return row;
	}

	public void exportPayments() {
		new OutgoingPaymentsExportHandler(ui, outgoingPaymentDao).showWizard();
		this.refresh();
	}

	public void importPayments() {
		new OutgoingPaymentsImportHandler(ui, accountDao).showWizard();
		this.refresh();
	}

	@Override
	protected Object initialiseTab() {
		sentPaymentsTab = ui.loadComponentFromFile(XML_SENTPAYMENTS_TAB, this);
		return sentPaymentsTab;
	}

	// > PRIVATE HELPER METHODS
	private void populateOutgoingPaymentsTable() {
		Object table = find(COMPONENT_INCOMING_PAYMENTS_TABLE);
		List<OutgoingPayment> outgoingPayments = outgoingPaymentDao
				.getAllOutgoingPayments();
		for (OutgoingPayment o : outgoingPayments) {
			ui.add(table, createRow(o));
		}
	}

	@Override
	public void refresh() {
		populateOutgoingPaymentsTable();
	}
}
