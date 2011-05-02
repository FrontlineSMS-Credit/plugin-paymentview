package org.creditsms.plugins.paymentview.ui.handler.tabexport.dialogs;

import java.util.List;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.utils.PaymentType;

public class ExportByClientXticsStep1Handler implements ThinletUiEventHandler {
	private static final String COMPONENT_RAD_INCOMING_PAY = "radIncomingPay";
	private static final String COMPONENT_RAD_OUTGOING_PAY = "radOutgoingPay";

	private static final String XML_EXPORT_BY_CLIENT_XTICS = "/ui/plugins/paymentview/export/dialogs/dlgExportByClientXtics.xml";

	private ClientDao clientDao;
	private Object dialogComponent;

	private UiGeneratorController ui;
	private List<Client> selectedUsers;
	private PaymentType paymentType;
	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;

	public ExportByClientXticsStep1Handler(UiGeneratorController ui,
			ClientDao clientDao, CustomFieldDao customFieldDao,
			CustomValueDao customValueDao, List<Client> selectedUsers) {
		this.ui = ui;
		this.clientDao = clientDao;
		this.customFieldDao = customFieldDao;
		this.customValueDao = customValueDao;
		this.selectedUsers = selectedUsers;
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
		new ExportByClientXticsStep2Handler(ui, clientDao, customValueDao, customFieldDao,
				selectedUsers, paymentType).showWizard();
		removeDialog();
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
}
