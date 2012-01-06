package org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.data.DuplicateKeyException;
import net.frontlinesms.data.events.DatabaseEntityNotification;
import net.frontlinesms.data.events.EntitySavedNotification;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.plugins.payment.ui.PaymentPluginTabHandler;
import net.frontlinesms.ui.HomeTabEventNotification;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.events.FrontlineUiUpdateJob;
import net.frontlinesms.ui.handler.BaseTabHandler;
import net.frontlinesms.ui.handler.ComponentPagingHandler;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;
import net.frontlinesms.ui.i18n.InternationalisationUtils;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.analytics.TargetAnalytics;
import org.creditsms.plugins.paymentview.data.domain.Account;
import org.creditsms.plugins.paymentview.data.domain.Client;
import org.creditsms.plugins.paymentview.data.domain.IncomingPayment;
import org.creditsms.plugins.paymentview.data.domain.LogMessage;
import org.creditsms.plugins.paymentview.data.domain.ResponseRecipient;
import org.creditsms.plugins.paymentview.data.domain.Target;
import org.creditsms.plugins.paymentview.data.domain.ThirdPartyResponse;
import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.LogMessageDao;
import org.creditsms.plugins.paymentview.data.repository.ResponseRecipientDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.data.repository.ThirdPartyResponseDao;
import org.creditsms.plugins.paymentview.ui.handler.AuthorisationCodeHandler;
import org.creditsms.plugins.paymentview.ui.handler.importexport.IncomingPaymentsExportHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs.ClientSelector;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs.AutoReplyPaymentsDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs.DistributeIncomingPaymentDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs.EditIncomingPaymentDialogHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabincomingpayments.dialogs.FormatterMarkerType;
import org.creditsms.plugins.paymentview.userhomepropeties.analytics.CreateAlertProperties;
import org.creditsms.plugins.paymentview.userhomepropeties.incomingpayments.AutoReplyProperties;
import org.creditsms.plugins.paymentview.utils.PaymentPluginConstants;
import org.creditsms.plugins.paymentview.utils.PaymentViewUtils;

public class IncomingPaymentsTabHandler extends BaseTabHandler implements
		PaymentPluginTabHandler, PagedComponentItemProvider {
	private static final String CONFIRM_DIALOG = "confirmDialog";
	private static final String INVALID_DATE = "Please enter a correct starting date.";
	private static final String ENABLE_AUTOREPLY = "OFF";
	private static final String TXT_END_DATE = "txt_endDate";
	private static final String TXT_START_DATE = "txt_startDate";
	private static final String DISABLE_AUTOREPLY = "ON";
	private static final String COMPONENT_INCOMING_PAYMENTS_TABLE = "tbl_clients";
	private static final String COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE = "pnl_clients";
	private static final String COMPONENT_PANEL_MTNS = "pnl_buttons";
	private static final String XML_INCOMING_PAYMENTS_TAB = "/ui/plugins/paymentview/incomingpayments/tabincomingpayments.xml";
	private static final String CONFIRM_DELETE_INCOMING = "message.confirm.delete.incoming";
	
	private AutoReplyProperties autoReplyProperties = AutoReplyProperties.getInstance();
	private CreateAlertProperties createAlertProperties = CreateAlertProperties.getInstance();
	
	private static final String ICON_STATUS_TRUE = "/icons/led_green.png";
	private static final String STATUS_LABEL_COMPONENT = "status";
	private static final String ICON_STATUS_FALSE = "/icons/led_red.png";
	private static final String BTN_DELETE_INCOMING_PAYMENT = "miDeleteIncoming";
	private Object status_label;
	
	protected IncomingPaymentDao incomingPaymentDao;
	private LogMessageDao logMessageDao;
	
	private Object incomingPaymentsTab;

	protected Object incomingPaymentsTableComponent;
	protected ComponentPagingHandler incomingPaymentsTablePager;
	private Object pnlIncomingPaymentsTableComponent;
	private Object pnlBtns;
	private PaymentViewPluginController pluginController;
	private Object dialogConfirmation;
	private Object fldStartDate;
	private Object fldEndDate;
	private Date startDate;
	private Date endDate;
	protected int totalItemCount = 0;
	private ClientDao clientDao;
	private FrontlineSMS frontlineController;
	private TargetAnalytics targetAnalytics;
	private AccountDao accountDao;
	private TargetDao targetDao;
	private ThirdPartyResponseDao thirdPartyResponseDao;
	private ResponseRecipientDao responseRecipientDao;
	private IncomingPayment parentIncomingPayment;
	private ClientSelector clientSelector;

	public IncomingPaymentsTabHandler(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui, true);
		this.incomingPaymentDao = pluginController.getIncomingPaymentDao();
		this.clientDao = pluginController.getClientDao();
		this.logMessageDao = pluginController.getLogMessageDao();
		this.pluginController = pluginController;
		this.frontlineController = ui.getFrontlineController();
		this.targetAnalytics = new TargetAnalytics();
		this.targetAnalytics.setIncomingPaymentDao(pluginController.getIncomingPaymentDao());
		this.targetDao = pluginController.getTargetDao();
		this.targetAnalytics.setTargetDao(targetDao);
		this.accountDao = pluginController.getAccountDao();
		this.thirdPartyResponseDao = pluginController.getThirdPartyResponseDao();
		this.responseRecipientDao = pluginController.getResponseRecipientDao();
		//ui.getPh
		init();
	}
	
	@Override
	protected Object initialiseTab() {
		incomingPaymentsTab = ui.loadComponentFromFile(getXMLFile(), this);
		fldStartDate = ui.find(incomingPaymentsTab, TXT_START_DATE);
		fldEndDate = ui.find(incomingPaymentsTab, TXT_END_DATE);
		status_label = ui.find(incomingPaymentsTab, STATUS_LABEL_COMPONENT);
		incomingPaymentsTableComponent = ui.find(incomingPaymentsTab, COMPONENT_INCOMING_PAYMENTS_TABLE);
		incomingPaymentsTablePager = new ComponentPagingHandler(ui, this, incomingPaymentsTableComponent);
		pnlBtns = ui.find(incomingPaymentsTab, COMPONENT_PANEL_MTNS);
		pnlIncomingPaymentsTableComponent = ui.find(incomingPaymentsTab, COMPONENT_PANEL_INCOMING_PAYMENTS_TABLE);
		this.ui.add(pnlIncomingPaymentsTableComponent, this.incomingPaymentsTablePager.getPanel());
		if(ui.find(pnlBtns, BTN_DELETE_INCOMING_PAYMENT)!=null){
			this.ui.setEnabled(ui.find(pnlBtns, BTN_DELETE_INCOMING_PAYMENT), false);
		}
		return incomingPaymentsTab;
	}
	
	public void tryToggleAutoReply(){
		if (!autoReplyProperties.isAutoReplyOn()){
			ui.showConfirmationDialog("toggleAutoReplyOn", this, PaymentPluginConstants.AUTO_REPLY_CONFIRMATION);
		}else{
			toggleAutoReplyOn();
		}
	}

	public void toggleAutoReplyOn() {
		autoReplyProperties.toggleAutoReply();
		setUpAutoReplyUI();
		ui.removeDialog(ui.find(CONFIRM_DIALOG));
	}
	
	private void setUpAutoReplyUI() {
		ui.setIcon(status_label, autoReplyProperties.isAutoReplyOn() ? ICON_STATUS_TRUE : ICON_STATUS_FALSE);
		ui.setText(status_label, (autoReplyProperties.isAutoReplyOn() ? DISABLE_AUTOREPLY : ENABLE_AUTOREPLY));
	}

	protected String getXMLFile() {
		return XML_INCOMING_PAYMENTS_TAB;
	}
	
	public Object getRow(IncomingPayment incomingPayment) {
		Object row = ui.createTableRow(incomingPayment);
		
		ui.add(row, ui.createTableCell(incomingPayment.getConfirmationCode()));
		ui.add(row, ui.createTableCell(clientDao.getClientByPhoneNumber(incomingPayment.getPhoneNumber()).getFullName()));
		ui.add(row, ui.createTableCell(incomingPayment.getPhoneNumber()));
		ui.add(row, ui.createTableCell(incomingPayment.getAmountPaid().toPlainString()));
		ui.add(row, ui.createTableCell(InternationalisationUtils.getDatetimeFormat().format(new Date(incomingPayment.getTimePaid()))));
		ui.add(row, ui.createTableCell(incomingPayment.getPaymentId()));
		ui.add(row, ui.createTableCell(incomingPayment.getNotes()));
		
		return row;
	}
	
	@Override
	public void refresh() {
		this.updateIncomingPaymentsList();
	}

	public void editIncoming() {
		Object[] selectedIncomings = this.ui.getSelectedItems(incomingPaymentsTableComponent);
		for (Object selectedIncoming : selectedIncomings) {
			IncomingPayment ip = (IncomingPayment) ui.getAttachedObject(selectedIncoming);
			ui.add(new EditIncomingPaymentDialogHandler(ui,pluginController,ip).getDialog());
		}
	}
	
//>PAGING METHODS
	protected PagedListDetails getIncomingPaymentsListDetails(int startIndex,
			int limit) {
		List<IncomingPayment> incomingPayments = new ArrayList<IncomingPayment>();
		incomingPayments = getIncomingPaymentsForUI(startIndex, limit);
		Object[] listItems = toThinletComponents(incomingPayments);

		return new PagedListDetails(totalItemCount, listItems);
	}

	protected List<IncomingPayment> getIncomingPaymentsForUI(int startIndex, int limit) {
		setUpAutoReplyUI();
		
		List<IncomingPayment> incomingPayments;
		String strStartDate = ui.getText(fldStartDate);
		String strEndDate = ui.getText(fldEndDate);
		
		if (!strStartDate.isEmpty()){
			try {
				startDate = InternationalisationUtils.getDateFormat().parse(strStartDate);
			} catch (ParseException e) {
				ui.infoMessage(INVALID_DATE);
				return Collections.emptyList();
			}
		}

		if (!strEndDate.isEmpty()){
			try {
				endDate = InternationalisationUtils.getDateFormat().parse(strEndDate);
			} catch (ParseException e) {
				ui.infoMessage("Please enter a correct ending date.");
				return Collections.emptyList();
			}
		}

		if (strStartDate.isEmpty() && strEndDate.isEmpty()) {
			totalItemCount = this.incomingPaymentDao.getActiveIncomingPaymentsCount();
			incomingPayments = this.incomingPaymentDao.getActiveIncomingPayments(startIndex, limit);
		} else {
			if (strStartDate.isEmpty() && endDate != null){
				totalItemCount = this.incomingPaymentDao.getIncomingPaymentsByEndDate(endDate).size();
				incomingPayments = this.incomingPaymentDao.getIncomingPaymentsByEndDate(endDate, startIndex, limit);
				
			} else {
				if (strEndDate.isEmpty() && startDate != null){
					totalItemCount = this.incomingPaymentDao.getIncomingPaymentsByStartDate(startDate).size();
					incomingPayments = this.incomingPaymentDao.getIncomingPaymentsByStartDate(startDate, startIndex, limit);
				} else {
					totalItemCount = this.incomingPaymentDao.getIncomingPaymentsByDateRange(startDate, endDate).size();
					incomingPayments = this.incomingPaymentDao.getIncomingPaymentsByDateRange(startDate, endDate, startIndex, limit);
				}
			}
		}
		return incomingPayments;

	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		if (list == this.incomingPaymentsTableComponent) {
			return getIncomingPaymentsListDetails(startIndex, limit);
		} else {
			throw new IllegalStateException();
		}
	}

	private Object[] toThinletComponents(List<IncomingPayment> incomingPayments) {
		Object[] components = new Object[incomingPayments.size()];
		for (int i = 0; i < components.length; i++) {
			IncomingPayment in = incomingPayments.get(i);
			components[i] = getRow(in);
		}
		return components;
	}

	public void updateIncomingPaymentsList() {
		this.incomingPaymentsTablePager.setCurrentPage(0);
		this.incomingPaymentsTablePager.refresh();
	}
	
	public void checkSelection() {
		Object[] selectedIncomings = this.ui.getSelectedItems(incomingPaymentsTableComponent);
		if (selectedIncomings.length == 0){
			this.ui.setEnabled(ui.find(pnlBtns, BTN_DELETE_INCOMING_PAYMENT), false);
		} else {
			this.ui.setEnabled(ui.find(pnlBtns, BTN_DELETE_INCOMING_PAYMENT), true);
		}	
	}
	
//> INCOMING PAYMENT DELETION
	public void deleteIncomingPayment() {
		Object[] selectedIncomings = this.ui.getSelectedItems(incomingPaymentsTableComponent);
		if (selectedIncomings.length == 0){
			ui.infoMessage("Please select incoming payment(s).");	
		} else {
			for (Object selectedIncoming : selectedIncomings) {
				IncomingPayment attachedIncoming = ui.getAttachedObject(selectedIncoming, IncomingPayment.class);
				attachedIncoming.setActive(false);
				incomingPaymentDao.updateIncomingPayment(attachedIncoming);
				logMessageDao.saveLogMessage(
						new LogMessage(LogMessage.LogLevel.INFO,
							   	"Delete Incoming Payment",
							   	attachedIncoming.toStringForLogs()));
			}
			ui.infoMessage("You have successfully deleted the selected incoming payment(s).");	
		}		
	}
	
	public Account getAccount(Client client){
		List<Account> activeNonGenericAccountsByClientId = accountDao
		.getActiveNonGenericAccountsByClientId(client.getId());
		if (!activeNonGenericAccountsByClientId.isEmpty()) {
			return activeNonGenericAccountsByClientId.get(0);
		} else {
			return accountDao.getGenericAccountsByClientId(client.getId());
		}
	}
	
	public void reassignForClient(List<Client> clients){
		if (clients.size()<=0){
			ui.alert("Please select a client to reassign.");
		} else {
			Client client = clients.get(0);//Its a single object list
			
			Object selectedItem = ui.getSelectedItem(incomingPaymentsTableComponent);
			IncomingPayment incomingPayment = ui.getAttachedObject(selectedItem, IncomingPayment.class);
			String tempPhoneNo = incomingPayment.getPhoneNumber();
			incomingPayment.setAccount(getAccount(client));
			incomingPayment.setPhoneNumber(client.getPhoneNumber());
			Target tgt = targetDao.getActiveTargetByAccount(getAccount(incomingPayment.getPhoneNumber()).getAccountNumber());
			incomingPayment.setTarget(tgt);
			
			incomingPaymentDao.updateIncomingPayment(incomingPayment);
			
			refresh();
			logMessageDao.saveLogMessage(
				new LogMessage(LogMessage.LogLevel.INFO,"Payment Reassigned to different client", 
						"Incoming Payment ["+incomingPayment.getConfirmationCode()+
						"] Reassigned from "+ tempPhoneNo  +" to different Client" + 
						incomingPayment.getPhoneNumber()
				)
			);
			clientSelector.removeDialog();
		}
	}
	
	public void postAuthCodeAction() {
		Object[] selectedItems = ui.getSelectedItems(incomingPaymentsTableComponent);
		if (selectedItems.length <= 0){
			ui.alert("Please select a payment to reassign.");
		}else if (selectedItems.length > 1){
			ui.alert("You can only select one payment at a time.");
		}else{
			List<Client> clients = new ArrayList<Client>(selectedItems.length);
			for (Object o : selectedItems) {
				IncomingPayment attachedObject = ui.getAttachedObject(o, IncomingPayment.class);
				clients.add(clientDao.getClientByPhoneNumber(attachedObject.getPhoneNumber()));
			}
			clientSelector = new ClientSelector(ui, pluginController);
			clientSelector.setExclusionList(clients);
			clientSelector.showClientSelectorDialog(this, "reassignForClient", List.class);
		}
	}
	
	public void reassignIncomingPayment() {
		new AuthorisationCodeHandler(ui).showAuthorizationCodeDialog(this, "postAuthCodeAction");
	}

	/*
	 * This function shows distribution dialog while selecting client distribution list
	 */
	public void distributeIncoming(List<Client> childrenClients){
		if (childrenClients.size() <= 0){
			ui.alert("Please select a client.");
		} else {
			List<Child> children = new ArrayList<Child>(childrenClients.size());
			for (Client c:childrenClients){
			children.add(new Child(c,new BigDecimal("0.00")));
			}
			
			new DistributeIncomingPaymentDialogHandler(ui, pluginController, parentIncomingPayment, children).showDialog();
			clientSelector.removeDialog();
		}
	}
	
	/*
	 * This function shows client list dialog while selecting an incoming payment
	 */
	public void disaggregateIncomingPayment(){
		Object[] selectedItems = ui.getSelectedItems(incomingPaymentsTableComponent);
		if (selectedItems.length <= 0){
			ui.alert("Please select a payment to reassign.");
		}else if (selectedItems.length > 1){
			ui.alert("You can only select one payment at a time.");
		}else{
			clientSelector = new ClientSelector(ui, pluginController);
			clientSelector.setExclusionList(new ArrayList<Client>(0));
			clientSelector.setSelectionMethod("multiple");
			for (Object o : selectedItems) {
				parentIncomingPayment = ui.getAttachedObject(o, IncomingPayment.class);
			}
			clientSelector.showClientSelectorDialog(this, "distributeIncoming", List.class);
		}
	}

	
	// > EXPORTS...
	public void exportIncomingPayments() {
		if(ui.find(pnlBtns, BTN_DELETE_INCOMING_PAYMENT)!=null){
			this.ui.setEnabled(ui.find(pnlBtns, BTN_DELETE_INCOMING_PAYMENT), false);
		}
		Object[] selectedItems = ui.getSelectedItems(incomingPaymentsTableComponent);
		if (selectedItems.length <= 0){
			exportIncomingPayments(getIncomingPaymentsForExport());
		}else{
			List<IncomingPayment> incomingPayments = new ArrayList<IncomingPayment>(selectedItems.length);
			for (Object o : selectedItems) {
				incomingPayments.add(ui.getAttachedObject(o, IncomingPayment.class));
			}
			exportIncomingPayments(incomingPayments);
		}
	}

	protected List<IncomingPayment> getIncomingPaymentsForExport() {
		List<IncomingPayment> incomingPayments;
		String strStartDate = ui.getText(fldStartDate);
		String strEndDate = ui.getText(fldEndDate);
		try {
			startDate = InternationalisationUtils.getDateFormat().parse(strStartDate);
		} catch (ParseException e) {
		}
		try {
			endDate = InternationalisationUtils.getDateFormat().parse(strEndDate);
		} catch (ParseException e) {
		}
			
		if (strStartDate.isEmpty() && strEndDate.isEmpty()) {
			incomingPayments = this.incomingPaymentDao.getActiveIncomingPayments();
		} else {
			if (strStartDate.isEmpty()){
				incomingPayments = this.incomingPaymentDao.getIncomingPaymentsByEndDate(endDate);
				
			} else {
				if (strEndDate.isEmpty()){
					incomingPayments = this.incomingPaymentDao.getIncomingPaymentsByStartDate(startDate);
				} else {
					incomingPayments = this.incomingPaymentDao.getIncomingPaymentsByDateRange(startDate, endDate);
				}
			}
		}
		return incomingPayments;
	}
	
	public void exportIncomingPayments(List<IncomingPayment> incomingPayments) {
		new IncomingPaymentsExportHandler(ui, pluginController, incomingPayments).showWizard();
		this.refresh();
	}
	
	public void showAuthCode() {
		ui.remove(dialogConfirmation);
		new AuthorisationCodeHandler(ui).showAuthorizationCodeDialog(this, "deleteIncomingPayment");
	}
	
	public final void showDeleteConfirmationDialog(String methodToBeCalled){
		dialogConfirmation = this.ui.showConfirmationDialog(methodToBeCalled, this, CONFIRM_DELETE_INCOMING);
	}
	
	public final void showAutoReplyDialog(){
		ui.add(new AutoReplyPaymentsDialogHandler(ui, pluginController).getDialog());
	}
	
	public void showDateSelecter(Object textField) {
		((UiGeneratorController) ui).showDateSelecter(textField);
	}
	
//> INCOMING PAYMENT NOTIFICATION...
	@SuppressWarnings("rawtypes")
	public void notify(final FrontlineEventNotification notification) {
		super.notify(notification);
		if (notification instanceof DatabaseEntityNotification) {
			final Object entity = ((DatabaseEntityNotification) notification).getDatabaseEntity();				
			if (entity instanceof IncomingPayment) {
				if (notification instanceof EntitySavedNotification) {
					new FrontlineUiUpdateJob() {
						public void run() {
							IncomingPayment incomingPayment = (IncomingPayment) entity;
							if (!incomingPayment.isChildPayment()){
								if(autoReplyProperties.isAutoReplyOn()){
									replyToPayment((IncomingPayment) entity);
								}
								replyToThirdParty((IncomingPayment) entity);
							}
							if(incomingPaymentDao.getActiveIncomingPaymentByClientId(incomingPayment.getAccount().getClient().getId()).size() > 0) {
								eventBus.notifyObservers(new HomeTabEventNotification(HomeTabEventNotification.Type.GREEN, "Payment received from new client: " + incomingPayment.getPaymentBy() + " " +incomingPayment.getAmountPaid()));	
							}	
						if(!incomingPayment.getAccount().isGenericAccount()) {
							Target target = incomingPayment.getTarget();
							target.setStatus(targetAnalytics.getStatus(target.getId()).toString());
							try {
								targetDao.updateTarget(target);
							} catch (DuplicateKeyException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
							if(incomingPayment.getTarget() != null) {
								if(createAlertProperties.isAlertOn()) {
									if(createAlertProperties.getCompletesTgt()) {
										if(targetAnalytics.getPercentageToGo(incomingPayment.getTarget().getId()).intValue()>= 100 
												&& targetAnalytics.getPreviousPercentageToGo(incomingPayment.getTarget().getId()).intValue()<100){
											eventBus.notifyObservers(new HomeTabEventNotification(HomeTabEventNotification.Type.GREEN, "Savings target completed: " + incomingPayment.getAccount().getClient().getFullName()));
										}
									}
									if(createAlertProperties.getMeetsHalfTgt()) {
										if(targetAnalytics.getPercentageToGo(incomingPayment.getTarget().getId()).intValue()>= 50 
												&& targetAnalytics.getPreviousPercentageToGo(incomingPayment.getTarget().getId()).intValue()<50) {
											eventBus.notifyObservers(new HomeTabEventNotification(HomeTabEventNotification.Type.GREEN, "Reached 50% of savings target : " + incomingPayment.getAccount().getClient().getFullName()));
										}
									}
								}
							}
							refresh();
					IncomingPaymentsTabHandler.this.refresh();
						}
					}.execute();
				} else threadSafeRefresh();
			} else if (entity instanceof LogMessage 
					&& notification instanceof EntitySavedNotification){
				LogMessage logMsg = (LogMessage) entity;
				if (logMsg.getLogTitle().equals("PIN ERROR")) {
					eventBus.notifyObservers(new HomeTabEventNotification(HomeTabEventNotification.Type.RED, logMsg.getLogContent()));
				} else if (logMsg.getLogTitle().equals("PAYMENT FAILED")) {
					eventBus.notifyObservers(new HomeTabEventNotification(HomeTabEventNotification.Type.RED, logMsg.getLogContent()));
				}
			}
		}
	}

	protected void replyToPayment(IncomingPayment incomingPayment) {
		String message = replaceFormats(incomingPayment, autoReplyProperties.getMessage());
		if (message != null){
			frontlineController.sendTextMessage(incomingPayment.getPhoneNumber(), message);
		}
	}

	private void replyToThirdParty(IncomingPayment incomingPayment) {
		ThirdPartyResponse  thirdPartyResponse = this.thirdPartyResponseDao.
			getThirdPartyResponseByClientId(incomingPayment.getAccount().getClient().getId());
		if (thirdPartyResponse != null){
			List<ResponseRecipient> responseRecipientLst = this.responseRecipientDao.
				getResponseRecipientByThirdPartyResponseId(thirdPartyResponse.getId());
			
				String thirdPartResponseMsg = replaceFormats(incomingPayment, thirdPartyResponse.getMessage());
				if (thirdPartResponseMsg != null){
					for (ResponseRecipient responseRes : responseRecipientLst) {
						frontlineController.sendTextMessage(responseRes.getClient().getPhoneNumber(), 
								thirdPartResponseMsg);
					}
				}
		}
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
	
	private String replaceFormats(IncomingPayment incomingPayment, String message) {
		String formed_message = "";
		FormatterMarkerType[] formatEnums = FormatterMarkerType.values();
		final Target tgt = targetDao.getActiveTargetByAccount(
			getAccount(incomingPayment.getPhoneNumber()).getAccountNumber()
		);
		
		if (tgt != null) {
			//targetAnalytics.computeAnalyticsIntervalDatesAndSavings(tgt.getId());
		}
			
			for (FormatterMarkerType fe : formatEnums) {
				if(message.contains(fe.getMarker())){
					switch (fe) {
				      case CLIENT_NAME:
				    	  formed_message = message.replace(fe.getMarker(), clientDao.getClientByPhoneNumber(incomingPayment.getPhoneNumber()).getFullName());
				    	  message = formed_message ;
				        break;
				      case AMOUNT_PAID:
				    	  formed_message = message.replace(fe.getMarker(), incomingPayment.getAmountPaid().toString());
				    	  message = formed_message ;
				        break;
				      case AMOUNT_REMAINING:
				    	  formed_message = message.replace(fe.getMarker(), (tgt == null ? "" : 
				    		  tgt.getTotalTargetCost().subtract(targetAnalytics.getAmountSaved(tgt.getId())).toString()));
				    	  message = formed_message ;
				        break;
				      case DATE_PAID:
				    	  formed_message = message.replace(fe.getMarker(), PaymentViewUtils.formatDate(incomingPayment.getTimePaid()));
				    	  message = formed_message ;
				        break;
				      case DAYS_REMAINING:
				    	  formed_message = message.replace(fe.getMarker(), (tgt == null ? "" : 
				    		  targetAnalytics.getDaysRemaining(tgt.getId()).toString()));
				    	  message = formed_message ;
				        break;
				      case MONTHLY_DUE:
				    	  formed_message = message.replace(fe.getMarker(), (tgt == null ? "" : targetAnalytics.getMonthlyAmountDue().toString()));
				    	  message = formed_message ;
				        break;
				      case END_MONTH_INTERVAL:
				    	  formed_message = message.replace(fe.getMarker(), (tgt == null ? "" : PaymentViewUtils.formatDate(targetAnalytics.getEndMonthInterval())));
				    	  message = formed_message ;
				        break;
				      case MONTHLY_SAVINGS:
				    	  formed_message = message.replace(fe.getMarker(), (tgt == null ? "" : targetAnalytics.getMonthlyAmountSaved().toString()));
				    	  message = formed_message ;
				        break;
				      case RECEPIENT_NAME:
				    	  formed_message = message.replace(fe.getMarker(), incomingPayment.getPaymentBy());
				    	  message = formed_message ;
				        break;
				      case TARGET_ENDDATE:
				    	  formed_message = message.replace(fe.getMarker(), (tgt == null ? "" : PaymentViewUtils.formatDate(tgt.getEndDate())));
				    	  message = formed_message ;
				        break;
				    }
				}
			}
			return message;
	}
	
	public class Child {
		private Client client;
		private BigDecimal amount;
		
		Child(Client client,BigDecimal amount){
			this.client = client;
			this.amount = amount;
		}
		
		public Client getClient(){
			return client;
		}
		public BigDecimal getAmount(){
			return amount;
		}
		public void setAmount(BigDecimal amount){
			this.amount = amount;
		}
		
	}
}
