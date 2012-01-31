package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs;

import java.util.List;
import java.util.Date;

import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.LogMessageDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseDialog;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.IncomingPaymentsTabHandler.Child;
import org.creditsms.plugins.paymentview.utils.PaymentPluginConstants;

public class DistributeConfirmationDialogHandler extends BaseDialog{
//> CONSTANTS
	private static final String TBL_CHILDREN = "tbl_children";
	private static String XML_DISTRIBUTE_CONFIRMATION = "/ui/plugins/paymentview/incomingpayments/dialogs/dlgDistributeConfirmation.xml";

//>DAOs
	private IncomingPaymentDao incomingPaymentDao;
	private AccountDao accountDao;
	private TargetDao targetDao;
	private ClientDao clientDao;
	private LogMessageDao logMessageDao;
	
//UI HELPERS	
	private IncomingPayment parentIncomingPayment;
	private List<Child> children;
	private Object tblChildrenComponent;
	private PaymentViewPluginController pluginController;
	private Object dialogConfimDistributeIp;
	private TargetAnalytics targetAnalytics; 
	
	
	public DistributeConfirmationDialogHandler(UiGeneratorController ui, PaymentViewPluginController pluginController, 
			IncomingPayment parentIncomingPayment, List<Child> children,DistributeIncomingPaymentDialogHandler distributeIncomingPaymentDialogHandler) {
		super(ui);
		this.pluginController = pluginController;
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.accountDao = pluginController.getAccountDao();
		this.clientDao = pluginController.getClientDao();
		this.targetDao = pluginController.getTargetDao();
		this.logMessageDao = pluginController.getLogMessageDao();
		this.parentIncomingPayment = parentIncomingPayment;
		this.targetAnalytics = new TargetAnalytics();
		this.targetAnalytics.setIncomingPaymentDao(pluginController
				.getIncomingPaymentDao());
		this.targetAnalytics.setTargetDao(targetDao);
		
		this.children = children;
		init();
		refresh();
	}

	public void init() {
		dialogComponent = ui.loadComponentFromFile(XML_DISTRIBUTE_CONFIRMATION, this);
		tblChildrenComponent = ui.find(dialogComponent, TBL_CHILDREN);	
		for(Child child: children) {
			ui.add(this.tblChildrenComponent, getRow(child));
		}
	}
	
	protected Object getRow(Child child) {
		Object row = ui.createTableRow(child);
		ui.add(row, ui.createTableCell(child.getClient().getPhoneNumber()));
		ui.add(row, ui.createTableCell(child.getClient().getFullName()));
		ui.add(row, ui.createTableCell(child.getAmount().toPlainString()));
		return row;
	}
	
	Account getAccount(String phoneNumber) {
		Client client = clientDao.getClientByPhoneNumber(phoneNumber);
		if (client != null) {
			List<Account> activeNonGenericAccountsByClientId = accountDao.getActiveNonGenericAccountsByClientId(client.getId());
			if(!activeNonGenericAccountsByClientId.isEmpty()){
				return activeNonGenericAccountsByClientId.get(0);
			} else {
				return accountDao.getGenericAccountsByClientId(client.getId());
			}
		}
		return null;
	}

	public void create(){
		dialogConfimDistributeIp = ((UiGeneratorController) ui).showConfirmationDialog("createInvidualIncomingPayments", this, PaymentPluginConstants.CONFIRM_ACCEPT_DISTRIBUTE_IP);
	}
	
	public void createInvidualIncomingPayments(){
		ui.removeDialog(dialogConfimDistributeIp);
		
		Target tgtIn = parentIncomingPayment.getTarget();
		Target tgt = new Target();
		
		if (tgtIn != null) {
			Account account = accountDao.getAccountById(tgtIn.getAccount()
					.getAccountId());
			if (account.getActiveAccount()) {
				if (tgtIn.getCompletedDate()==null) {
					/*
					 * Still an active target
					 * 
					 * 1) Deactivate the incoming payment
					 */
					//update parent incoming payment;
					parentIncomingPayment.setActive(false);
					incomingPaymentDao.updateIncomingPayment(parentIncomingPayment);
					
					logMessageDao.saveLogMessage(new LogMessage(LogMessage.LogLevel.INFO,"Group incoming payment disaggregated.",
							parentIncomingPayment.toStringForLogs() + " Confimation Code: " + parentIncomingPayment.getConfirmationCode() ));
					//create individual incoming payments
					IncomingPayment individualIncomingPayment = new IncomingPayment();
					for (Child child: children){
						individualIncomingPayment = createIncomingPayment(child);
						tgt = individualIncomingPayment.getTarget();
						incomingPaymentDao.saveIncomingPayment(individualIncomingPayment);
						if (tgt != null) {
							updateTargetStatus(tgt);
						}
						logMessageDao.saveLogMessage(new LogMessage(LogMessage.LogLevel.INFO,"Individual incoming payment created.",
								child.getClient().getFullName() + " - " + child.getClient().getPhoneNumber() + " - " + child.getAmount() + " KES - "
								+ parentIncomingPayment.getConfirmationCode()));
					}
				} else {
					/*
					 * Completed target cannot be activated since another target
					 * has been created for the user
					 */
					ui.alert("This incoming payment belongs to a closed target.");
				}
			} else {
				if (tgtIn.getCompletedDate()!=null) {
					/*
					 * Completed target but can still be activated since no
					 * other target has been created for the user
					 * 
					 * 1) Deactivate the incoming payment
					 * 2) Activate the target and account 
					 * 3) Update the target
					 * status
					 */
					//update parent incoming payment;
					parentIncomingPayment.setActive(false);
					incomingPaymentDao.updateIncomingPayment(parentIncomingPayment);
					
					account.setActiveAccount(true);
					accountDao.updateAccount(account);
					tgtIn.setCompletedDate(null);
					try {
						targetDao.updateTarget(tgtIn);
					} catch (DuplicateKeyException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					logMessageDao.saveLogMessage(new LogMessage(LogMessage.LogLevel.INFO,"Group incoming payment disaggregated.",
							parentIncomingPayment.toStringForLogs() + " Confimation Code: " + parentIncomingPayment.getConfirmationCode() ));
					//create individual incoming payments
					IncomingPayment individualIncomingPayment = new IncomingPayment();
					for (Child child: children){
						individualIncomingPayment = createIncomingPayment(child);
						tgt = individualIncomingPayment.getTarget();
						incomingPaymentDao.saveIncomingPayment(individualIncomingPayment);
						if (tgt != null) {
							updateTargetStatus(tgt);
						}
						logMessageDao.saveLogMessage(new LogMessage(LogMessage.LogLevel.INFO,"Individual incoming payment created.",
								child.getClient().getFullName() + " - " + child.getClient().getPhoneNumber() + " - " + child.getAmount() + " KES - "
								+ parentIncomingPayment.getConfirmationCode()));
					}
				}
			}
		} else {
			//update parent incoming payment;
			parentIncomingPayment.setActive(false);
			incomingPaymentDao.updateIncomingPayment(parentIncomingPayment);

			logMessageDao.saveLogMessage(new LogMessage(LogMessage.LogLevel.INFO,"Group incoming payment disaggregated.",
					parentIncomingPayment.toStringForLogs() + " Confimation Code: " + parentIncomingPayment.getConfirmationCode() ));
			//create individual incoming payments
			IncomingPayment individualIncomingPayment = new IncomingPayment();
			for (Child child: children){
				individualIncomingPayment = createIncomingPayment(child);
				tgt = individualIncomingPayment.getTarget();
				incomingPaymentDao.saveIncomingPayment(individualIncomingPayment);
				if (tgt != null) {
					updateTargetStatus(tgt);
				}
				logMessageDao.saveLogMessage(new LogMessage(LogMessage.LogLevel.INFO,"Individual incoming payment created.",
						child.getClient().getFullName() + " - " + child.getClient().getPhoneNumber() + " - " + child.getAmount() + " KES - "
						+ parentIncomingPayment.getConfirmationCode()));
			}
		}
				
		this.removeDialog();
		ui.infoMessage("You have successfully distribute the incoming payment " + parentIncomingPayment.getConfirmationCode());
	}
	
	private IncomingPayment createIncomingPayment(Child child) {
		IncomingPayment individualIncomingPayment = new IncomingPayment();

		Account acc = getAccount(child.getClient().getPhoneNumber());
		individualIncomingPayment.setAccount(acc);
		individualIncomingPayment.setActive(true);
		individualIncomingPayment.setAmountPaid(child.getAmount());
		individualIncomingPayment.setChild(true);
		individualIncomingPayment.setConfirmationCode(parentIncomingPayment.getConfirmationCode());
		individualIncomingPayment.setPaymentBy(child.getClient().getFullName());
		individualIncomingPayment.setPhoneNumber(child.getClient().getPhoneNumber());
		individualIncomingPayment.setTarget(targetDao.getActiveTargetByAccount(acc.getAccountNumber()));		
		individualIncomingPayment.setTimePaid(new Date(parentIncomingPayment.getTimePaid()));

		return individualIncomingPayment;
	}
	
	private void updateTargetStatus(Target tgt) {
		Target activatedTgt = targetDao.getTargetById(tgt.getId());
		if (targetAnalytics.getStatus(activatedTgt.getId())
				.equals(TargetAnalytics.Status.PAID)) {
			Account account = accountDao.getAccountById(activatedTgt
					.getAccount().getAccountId());
			account.setActiveAccount(false);
			accountDao.updateAccount(account);

			activatedTgt.setCompletedDate(new Date());
			try {
				targetDao.updateTarget(activatedTgt);
			} catch (DuplicateKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public void previous(){
		new DistributeIncomingPaymentDialogHandler(ui, pluginController, parentIncomingPayment,children).showDialog();
		this.removeDialog();
	}
	
	@Override
	public void refresh() {
	}
	
	@Override
	public void showDialog() {
		ui.add(this.dialogComponent);
	}
}
