package org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs;

import java.util.ArrayList;
import java.util.List;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.utils.PaymentType;

public class ExportByClientXticsStep1Handler implements ThinletUiEventHandler {
	private static final String CMB_PAST_WEEK = "cmbPastWeek";
	private static final String CMB_ALL = "cmbAll";
	private static final String CMB_TODAY = "cmbToday";
	private static final String PNL_DATE_RANGE = "pnlDateRange";
	private static final String CMB_DATE_RANGE = "cmbDateRange";
	private static final String COMPONENT_RAD_INCOMING_PAY = "radIncomingPay";
	private static final String COMPONENT_RAD_OUTGOING_PAY = "radOutgoingPay";

	private static final String XML_EXPORT_BY_CLIENT_XTICS = "/ui/plugins/paymentview/export/dialogs/dlgExportByClientXtics.xml";

	private ClientDao clientDao;
	private Object dialogComponent;

	private UiGeneratorController ui;
	private List<Client> selectedClients;
	private PaymentType paymentType = null;
	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;
	private Object pnlDateRange;
	private List<Object> payments;
	private DateOption dateOption = null;
	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao;

	public ExportByClientXticsStep1Handler(UiGeneratorController ui,
			ClientDao clientDao, CustomFieldDao customFieldDao,
			CustomValueDao customValueDao, List<Client> selectedUsers,
			OutgoingPaymentDao outgoingPaymentDao,
			IncomingPaymentDao incomingPaymentDao) {
		this.ui = ui;
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.customValueDao = customValueDao;
		this.selectedClients = selectedUsers;
		this.incomingPaymentDao = incomingPaymentDao;
		this.outgoingPaymentDao = outgoingPaymentDao;
		init();
		refresh();
	}

	/**
	 * @return the customizeClientDialog
	 */
	public Object getDialog() {
		return dialogComponent;
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EXPORT_BY_CLIENT_XTICS,
				this);
	}

	public void next() {
		getListOfPayments();
		new ExportByClientXticsStep2Handler(this).showWizard();
		removeDialog();
	}

	private void getListOfPayments() {
		//FIXME: look at how to get the real Ranges for these items
		if (this.dateOption != null & paymentType != null) {
			if (this.payments == null) {
				this.payments = new ArrayList<Object>(5);
			}
			switch (dateOption) {
			case ALL:
				switch (paymentType) {
				case INCOMING:
					for (Client client : selectedClients) {
						payments.addAll(incomingPaymentDao.getIncomingPaymentByClientId(client.getId()));
					}
				case OUTGOING:
					for (Client client : selectedClients) {
						payments.addAll(outgoingPaymentDao.getOutgoingPaymentsByClientId(client.getId()));
					}
				}
			case TODAY:
				switch (paymentType) {
				case INCOMING:
					for (Client client : selectedClients) {
						payments.addAll(incomingPaymentDao.getIncomingPaymentByClientId(client.getId()));
					}
				case OUTGOING:
					for (Client client : selectedClients) {
						payments.addAll(outgoingPaymentDao.getOutgoingPaymentsByClientId(client.getId()));
					}
				}
			case PAST_WEEK:
				switch (paymentType) {
				case INCOMING:
					for (Client client : selectedClients) {
						payments.addAll(incomingPaymentDao.getIncomingPaymentByClientId(client.getId()));
					}
				case OUTGOING:
					for (Client client : selectedClients) {
						payments.addAll(outgoingPaymentDao.getOutgoingPaymentsByClientId(client.getId()));
					}
				}
			case DATE_RANGE:
				switch (paymentType) {
				case INCOMING:
					for (Client client : selectedClients) {
						payments.addAll(incomingPaymentDao.getIncomingPaymentByClientId(client.getId()));
					}
				case OUTGOING:
					for (Client client : selectedClients) {
						payments.addAll(outgoingPaymentDao.getOutgoingPaymentsByClientId(client.getId()));
					}
				}
			}
		}
	}

	public void paymentTypeChanged(Object radiobutton) {
		if (ui.getName(radiobutton).equals(COMPONENT_RAD_INCOMING_PAY)
				& ui.isSelected(radiobutton)) {
			paymentType = PaymentType.INCOMING;
		} else if (ui.getName(radiobutton).equals(COMPONENT_RAD_OUTGOING_PAY)
				& ui.isSelected(radiobutton)) {
			paymentType = PaymentType.OUTGOING;
		}
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

	public void showDateSelecter(Object textField) {
		this.ui.showDateSelecter(textField);
	}

	enum DateOption {
		ALL, PAST_WEEK, DATE_RANGE, TODAY;
	}

	public void enable(Object checkbox) {
		String name = ui.getName(checkbox);
		if (pnlDateRange == null) {
			pnlDateRange = ui.find(this.dialogComponent, PNL_DATE_RANGE);
		}
		if (name.equals(CMB_DATE_RANGE)) {
			dateOption = DateOption.DATE_RANGE;
			ui.setEnabledRecursively(pnlDateRange, true);
		} else if (name.equals(CMB_TODAY)) {
			dateOption = DateOption.TODAY;
			ui.setEnabledRecursively(pnlDateRange, false);
		} else if (name.equals(CMB_ALL)) {
			dateOption = DateOption.ALL;
			ui.setEnabledRecursively(pnlDateRange, false);
		} else if (name.equals(CMB_PAST_WEEK)) {
			dateOption = DateOption.PAST_WEEK;
			ui.setEnabledRecursively(pnlDateRange, false);
		}
	}

	// > GETTERS AND SETTERS
	public ClientDao getClientDao() {
		return clientDao;
	}

	public void setClientDao(ClientDao clientDao) {
		this.clientDao = clientDao;
	}

	public UiGeneratorController getUi() {
		return ui;
	}

	public void setUi(UiGeneratorController ui) {
		this.ui = ui;
	}

	public List<Client> getSelectedUsers() {
		return selectedClients;
	}

	public void setSelectedUsers(List<Client> selectedUsers) {
		this.selectedClients = selectedUsers;
	}

	public PaymentType getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(PaymentType paymentType) {
		this.paymentType = paymentType;
	}

	public CustomValueDao getCustomValueDao() {
		return customValueDao;
	}

	public void setCustomValueDao(CustomValueDao customValueDao) {
		this.customValueDao = customValueDao;
	}

	public CustomFieldDao getCustomFieldDao() {
		return customFieldDao;
	}

	public void setCustomFieldDao(CustomFieldDao customFieldDao) {
		this.customFieldDao = customFieldDao;
	}
}
