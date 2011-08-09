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
	List<IncomingPayment> listedIncomingPayments = new ArrayList<IncomingPayment>();

	public IncomingPaymentsDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController,	List<Client> selectedClients) {
		super(ui, pluginController);
		this.selectedClients = selectedClients;
		this.refresh();
	}

	@Override
	protected List<IncomingPayment> getIncomingPaymentsForExport() {
		return listedIncomingPayments;
	}

	@Override
	protected String getXMLFile() {
		return XML_INCOMING_PAYMENTS_DIALOG;
	}

	// >PAGING METHODS
	protected PagedListDetails getIncomingPaymentsListDetails(int startIndex,
			int limit) {
		listedIncomingPayments.clear();
		return super.getIncomingPaymentsListDetails(startIndex, limit);
	}

	@Override
	protected List<IncomingPayment> getIncomingPaymentsForUI(int startIndex, int limit) {
		if (this.selectedClients.isEmpty()) {
			listedIncomingPayments = this.incomingPaymentDao.getActiveIncomingPayments(startIndex, limit);
		} else {
			for (Client client : selectedClients) {
				listedIncomingPayments.addAll(this.incomingPaymentDao.getActiveIncomingPaymentByClientId(client.getId()));
			}
			if (incomingPaymentsTablePager.getMaxItemsPerPage() < listedIncomingPayments.size()){
				if ( (startIndex+limit) < listedIncomingPayments.size()){
					return listedIncomingPayments.subList(startIndex, startIndex+limit);
				} else {
					return listedIncomingPayments.subList(startIndex, listedIncomingPayments.size());
				}
				
			} 

		}
		return listedIncomingPayments;
	}

	public Object getDialog() {
		return getTab();
	}
}
