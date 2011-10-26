package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;

import java.util.Date;
import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;

public class DistributeIncomingPaymentDialogHandler extends BaseDialog{
//> CONSTANTS
	private static final String COMPONENT_TEXT_PARENT_NAME = "fldName";
	private static final String COMPONENT_TEXT_PARENT_PHONE_NUMBER = "fldPhoneNumber";
	private static final String COMPONENT_TEXT_PARENT_AMOUNT = "fldAmount";
	private static String XML_DISTRIBUTE_INCOMING = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgDistributeIncomingPayment.xml";

//>DAOs
	private IncomingPaymentDao incomingPaymentDao;
	
//> UI FIELDS
	private Object fieldName;
	private Object fieldPhoneNumber;
	private Object fieldAmount;
	private Object fieldDate;

//UI HELPERS	
	private IncomingPayment parentIncomingPayment;
	private Object fieldPaymentId;
	private Object fieldNotes;
	private List<Client> children;
	private ClientDao clientDao;


	
	public DistributeIncomingPaymentDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
			IncomingPayment parentIncomingPayment, List<Client> children) {
		super(ui);
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.clientDao = pluginController.getClientDao();
		this.parentIncomingPayment = parentIncomingPayment;
		this.children = children;
		init();
		refresh();
	}


	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_DISTRIBUTE_INCOMING, this);
		fieldName = ui.find(dialogComponent, COMPONENT_TEXT_PARENT_NAME);
		fieldPhoneNumber = ui.find(dialogComponent, COMPONENT_TEXT_PARENT_PHONE_NUMBER);
		fieldAmount = ui.find(dialogComponent, COMPONENT_TEXT_PARENT_AMOUNT);
//		fieldDate = ui.find(dialogComponent, COMPONENT_TEXT_DATE);
//		fieldPaymentId = ui.find(dialogComponent, COMPONENT_TEXT_PAYMENT_ID);
//		fieldNotes = ui.find(dialogComponent, COMPONENT_TEXT_NOTES);

	}

	@Override
	public void refresh() {
			ui.setText(fieldName, clientDao.getClientByPhoneNumber(this.parentIncomingPayment.getPhoneNumber()).getFullName());			
			ui.setText(fieldPhoneNumber, this.parentIncomingPayment.getPhoneNumber());
			ui.setText(fieldPhoneNumber, this.parentIncomingPayment.getPhoneNumber());
			ui.setText(fieldAmount, this.parentIncomingPayment.getAmountPaid().toPlainString());
			
			
//			ui.setText(fieldDate, InternationalisationUtils.getDatetimeFormat().format(new Date(incomingPayment.getTimePaid())));
//			ui.setText(fieldPaymentId, this.incomingPayment.getPaymentId());
//			ui.setText(fieldNotes, this.incomingPayment.getNotes());
	}

	public void next() throws DuplicateKeyException {
//			parentIncomingPayment.setPaymentId(ui.getText(fieldPaymentId));
//			parentIncomingPayment.setNotes(ui.getText(fieldNotes));
//			incomingPaymentDao.updateIncomingPayment(this.parentIncomingPayment);
//			removeDialog();
	}
	
	@Override
	public void showDialog() {
		ui.add(this.dialogComponent);
	}


}
