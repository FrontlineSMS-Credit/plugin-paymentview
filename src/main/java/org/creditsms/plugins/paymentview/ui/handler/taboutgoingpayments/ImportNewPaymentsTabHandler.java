package org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments;

import java.util.List;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.plugins.payment.service.PaymentServiceException;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.OutgoingPayment;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.OutgoingPaymentsImportHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SchedulePaymentAuthDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.taboutgoingpayments.dialogs.SelectPaymentServiceDialogHandler;

/**
 * FIXME 
 * @author Roy
 */
public class ImportNewPaymentsTabHandler extends BaseTabHandler {
	private static final String XML_IMPORT_NEW_PAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/innertabs/importnewpayments.xml";
	private static final String COMPONENT_NEW_PAYMENTS_TABLE = "tbl_new_payments";
	private static final String BTN_SEND_NEW_PAYMENT = "btn_sendPaymentToSelection";
	private AccountDao accountDao;
	private ClientDao clientDao;
	private Object newPaymentsTableComponent;
	private Object schedulePaymentAuthDialog;
	private Object newPaymentsTab;
	private static final String TAB_IMPORTNEWPAYMENTS = "tab_importNewOutgoingPayments";
	private PaymentViewPluginController pluginController;
	private Object importPaymentsTab;
	private OutgoingPaymentDao outgoingPaymentDao;
	private PaymentService paymentService;

	private List<OutgoingPayment> outgoingPayments;

	public ImportNewPaymentsTabHandler(UiGeneratorController ui, Object tabOutgoingPayments, PaymentViewPluginController pluginController) {
		super(ui, false);
		accountDao = pluginController.getAccountDao();
		clientDao = pluginController.getClientDao();
		this.pluginController = pluginController;
		outgoingPaymentDao = pluginController.getOutgoingPaymentDao();
		importPaymentsTab = ui.find(tabOutgoingPayments, TAB_IMPORTNEWPAYMENTS);
		init();
	}
	
	@Override
	protected Object initialiseTab() {
		newPaymentsTab = ui.loadComponentFromFile(XML_IMPORT_NEW_PAYMENTS_TAB, this);
		newPaymentsTableComponent = ui.find(newPaymentsTab, COMPONENT_NEW_PAYMENTS_TABLE);
		this.ui.add(importPaymentsTab, newPaymentsTab);
		ui.setEnabled(ui.find(newPaymentsTab, BTN_SEND_NEW_PAYMENT), false);
		return newPaymentsTab;
	}
	
	private Object getClientName(String phoneNumber){
		Client clnt = clientDao.getClientByPhoneNumber(phoneNumber);
		String clientName = "";
		if(clnt != null){
			clientName = clnt.getFullName();
		}
		return clientName;
	}
	
 	private Object getRow(OutgoingPayment oP, int i) {
		Object row = ui.createTableRow(i);
		
		Object clientName = ui.createTableCell(getClientName(oP.getClient().getPhoneNumber()).toString());
		Object phoneNumber = ui.createTableCell(oP.getClient().getPhoneNumber());
		Object amountToPay = ui.createTableCell(oP.getAmountPaid().toString());
		Object paymentId = ui.createTableCell(oP.getPaymentId());
		Object notes = ui.createTableCell(oP.getNotes());
		
		ui.add(row, clientName);
		ui.add(row, phoneNumber);
		ui.add(row, amountToPay);
		ui.add(row, paymentId);
		ui.add(row, notes);
		return row;
	}

	public void updateNewPayments(List<OutgoingPayment> newPaymentsLst) {
		ui.removeAll(newPaymentsTableComponent);
		setOutgoingPaymentsLst(newPaymentsLst);
		if (newPaymentsLst != null){
			int q = 0;
			for(OutgoingPayment o:newPaymentsLst){
				q++;
				ui.add(newPaymentsTableComponent, getRow(o, q));
			}
			ui.setEnabled(ui.find(newPaymentsTab, BTN_SEND_NEW_PAYMENT), true);
		}
	}

	public void showImportWizard(String typeName) {
		OutgoingPaymentsImportHandler outgoingPaymentsImportHandler = new OutgoingPaymentsImportHandler(ui, accountDao, clientDao, this);
		outgoingPaymentsImportHandler.showWizard();
	}

	public void showSchedulePaymentAuthDialog() {
		schedulePaymentAuthDialog = new SchedulePaymentAuthDialogHandler(ui)
				.getDialog();
		ui.add(schedulePaymentAuthDialog);
	}
	
	public void SelectPaymentService() {
		if (!pluginController.getActiveServices().isEmpty()){
			SelectPaymentServiceDialogHandler selectPaymentService = new SelectPaymentServiceDialogHandler(ui, pluginController, this);
			selectPaymentService.showDialog();
		} else {
			ui.infoMessage("Please set up a mobile payment account in the setting tab.");
		}
	}
	
	public void showSendPaymentAuthDialog() {
		try {					
			new AuthorisationCodeHandler(ui).showAuthorizationCodeDialog(this, "sendPayment");
		} catch (NumberFormatException ex){
			ui.infoMessage("Please enter an amount");
		}
	}
 	
 	/** Sets the client field for this outgoing payment, creating the Client if necessary */
    private void updateClient(OutgoingPayment o) throws DuplicateKeyException {
    	Client temporaryClient = o.getClient();
    	Client dbClient = clientDao.getClientByPhoneNumber(temporaryClient.getPhoneNumber());
    	if(dbClient != null) {
    		o.setClient(dbClient);
    	} else {
    		clientDao.saveClient(temporaryClient);
    	}
    }
    
	public void sendPayment() throws PaymentServiceException {
		try {
			List<OutgoingPayment> newPaymentsLst = outgoingPayments;
			if (newPaymentsLst != null) {
				ui.setEnabled(ui.find(newPaymentsTab, BTN_SEND_NEW_PAYMENT), false);
				
				for(OutgoingPayment o : newPaymentsLst) {
					updateClient(o);
					o.setStatus(OutgoingPayment.Status.CREATED);
					o.setConfirmationCode("");
					o.setPayBillPayment(false);
					outgoingPaymentDao.saveOutgoingPayment(o);
					try {
						paymentService.makePayment(o);
					} catch(Exception ex) {
						log.warn("An exception occurred while trying to make outgoing payment " + o, ex);
					}
					outgoingPaymentDao.updateOutgoingPayment(o);
				}
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}
	
	public void cancelNewPayments(){
		ui.removeAll(newPaymentsTableComponent);
		ui.setEnabled(ui.find(newPaymentsTab, BTN_SEND_NEW_PAYMENT), false);
	}

	public void setOutgoingPaymentsLst(List<OutgoingPayment> outgoingPayments) {
		this.outgoingPayments = outgoingPayments;
	}


	public void setPaymentService(PaymentService paymentService) {
		this.paymentService = paymentService;
	}
	
	@Override
	public void refresh() {
		// TODO Auto-generated method stub
	}
}
