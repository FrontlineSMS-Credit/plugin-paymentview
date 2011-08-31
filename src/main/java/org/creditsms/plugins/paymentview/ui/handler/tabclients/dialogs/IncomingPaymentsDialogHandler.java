package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.IncomingPaymentsTabHandler;

public class IncomingPaymentsDialogHandler extends IncomingPaymentsTabHandler {
	private static final String XML_INCOMING_PAYMENTS_DIALOG = "/ui/plugins/paymentview/clients/dialogs/dlgIncomingPayments.xml";
	private List<Client> selectedClients;

	public IncomingPaymentsDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController,	List<Client> selectedClients) {
		super(ui, pluginController);
		this.selectedClients = selectedClients;
	}

	@Override
	protected List<IncomingPayment> getIncomingPaymentsForExport() {
		if (this.selectedClients.isEmpty()) {
			return this.incomingPaymentDao.getActiveIncomingPayments();
		} else {
			List<IncomingPayment> listedIncomingPayments = new ArrayList<IncomingPayment>();
			for (Client client : selectedClients) {
				listedIncomingPayments.addAll(this.incomingPaymentDao.getActiveIncomingPaymentByClientId(client.getId()));
			}
			return listedIncomingPayments;
		}
	}

	@Override
	protected String getXMLFile() {
		return XML_INCOMING_PAYMENTS_DIALOG;
	}

	// >PAGING METHODS
	protected PagedListDetails getIncomingPaymentsListDetails(int startIndex,
			int limit) {
		return super.getIncomingPaymentsListDetails(startIndex, limit);
	}

	@Override
	protected List<IncomingPayment> getIncomingPaymentsForUI(int startIndex, int limit) {
		if (this.selectedClients.isEmpty()) {
			totalItemCount = this.incomingPaymentDao.getActiveIncomingPayments().size();
			return this.incomingPaymentDao.getActiveIncomingPayments(startIndex, limit);
		} else {
			List<IncomingPayment> listedIncomingPayments = new ArrayList<IncomingPayment>();
			for (Client client : selectedClients) {
				listedIncomingPayments.addAll(this.incomingPaymentDao.getActiveIncomingPaymentByClientId(client.getId()));
			}
			totalItemCount = listedIncomingPayments.size();
			if (incomingPaymentsTablePager.getMaxItemsPerPage() < listedIncomingPayments.size()){
				if ( (startIndex+limit) < listedIncomingPayments.size()){
					return listedIncomingPayments.subList(startIndex, startIndex+limit);
				} else {
					return listedIncomingPayments.subList(startIndex, listedIncomingPayments.size());
				}
			} 
			return listedIncomingPayments;
		}
	}

	public Object getDialog() {
		return getTab();
	}
}
