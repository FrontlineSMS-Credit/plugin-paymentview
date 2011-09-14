package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;

import java.util.Date;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;

public class EditIncomingPaymentDialogHandler extends BaseDialog{
//> CONSTANTS
	private static final String COMPONENT_TEXT_NAME = "fldName";
	private static final String COMPONENT_TEXT_PHONE_NUMBER = "fldPhoneNumber";
	private static final String COMPONENT_TEXT_AMOUNT = "fldAmount";
	private static final String COMPONENT_TEXT_DATE = "fldDate";
	private static final String COMPONENT_TEXT_PAYMENT_ID = "fldPaymentId";
	private static final String COMPONENT_TEXT_NOTES = "fldNotes";
	private static String XML_EDIT_INCOMING = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgEditIncomingPayment.xml";

//>DAOs
	private IncomingPaymentDao incomingPaymentDao;
	
//> UI FIELDS
	private Object fieldName;
	private Object fieldPhoneNumber;
	private Object fieldAmount;
	private Object fieldDate;

//UI HELPERS	
	private IncomingPayment incomingPayment;
	private Object fieldPaymentId;
	private Object fieldNotes;


	
	public EditIncomingPaymentDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
			IncomingPayment incomingPayment) {
		super(ui);
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.incomingPayment = incomingPayment;
		init();
		refresh();
	}


	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_EDIT_INCOMING, this);
		fieldName = ui.find(dialogComponent, COMPONENT_TEXT_NAME);
		fieldPhoneNumber = ui.find(dialogComponent, COMPONENT_TEXT_PHONE_NUMBER);
		fieldAmount = ui.find(dialogComponent, COMPONENT_TEXT_AMOUNT);
		fieldDate = ui.find(dialogComponent, COMPONENT_TEXT_DATE);
		fieldPaymentId = ui.find(dialogComponent, COMPONENT_TEXT_PAYMENT_ID);
		fieldNotes = ui.find(dialogComponent, COMPONENT_TEXT_NOTES);

	}

	@Override
	public void refresh() {
			ui.setText(fieldName, this.incomingPayment.getPaymentBy());
			ui.setText(fieldPhoneNumber, this.incomingPayment.getPhoneNumber());
			ui.setText(fieldAmount, this.incomingPayment.getAmountPaid().toPlainString());
			ui.setText(fieldDate, InternationalisationUtils.getDatetimeFormat().format(new Date(incomingPayment.getTimePaid())));
			ui.setText(fieldPaymentId, this.incomingPayment.getPaymentId());
			ui.setText(fieldNotes, this.incomingPayment.getNotes());
	}

	public void saveIncomingPayment() throws DuplicateKeyException {
			incomingPayment.setPaymentId(ui.getText(fieldPaymentId));
			incomingPayment.setNotes(ui.getText(fieldNotes));
			incomingPaymentDao.updateIncomingPayment(this.incomingPayment);
			removeDialog();
	}
	
	public void assertMaxLength(Object component, int maxLength) {
		String text = ui.getText(component);
		if(text.length() > maxLength) {
			ui.setText(component, text.substring(0, maxLength));
		}
	}


}
