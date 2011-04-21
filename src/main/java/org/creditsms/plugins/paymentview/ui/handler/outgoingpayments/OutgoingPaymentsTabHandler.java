package org.creditsms.plugins.paymentview.ui.handler.outgoingpayments;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

public class OutgoingPaymentsTabHandler extends BaseTabHandler{

	private static final String XML_OUTGOINGPAYMENTS_TAB = "/ui/plugins/paymentview/outgoingpayments/taboutgoingpayments.xml";
	private static final String TABBED_PANE_MAIN = "tabbedPaneMain";
	
	private Object outgoingPaymentsTab;
	private SentPaymentsTabHandler sentPaymentsTab;
	private SendNewPaymentsTabHandler sendNewPaymentsTab;
	private ImportNewPaymentsTabHandler importNewPaymentsTab;
	private SelectFromClientsTabHandler selectFromClientsTab;
	private Object mainTabbedPane;
	
	private OutgoingPaymentDao outgoingPaymentDao;
	
	 
	public OutgoingPaymentsTabHandler(UiGeneratorController ui, OutgoingPaymentDao outgoingPaymentDao) {
		super(ui);		
		this.outgoingPaymentDao = outgoingPaymentDao;
		init();
	}

	@Override
	public void refresh() {
		sentPaymentsTab.refresh();
		sendNewPaymentsTab.refresh();
		importNewPaymentsTab.refresh();
		selectFromClientsTab.refresh();
	}

	@Override
	protected Object initialiseTab() {
		outgoingPaymentsTab = ui.loadComponentFromFile(XML_OUTGOINGPAYMENTS_TAB, this);
		
		mainTabbedPane = ui.find(outgoingPaymentsTab, TABBED_PANE_MAIN); 
		
		sentPaymentsTab = new SentPaymentsTabHandler(ui);
		ui.add(mainTabbedPane, sentPaymentsTab.getTab());
		
		sendNewPaymentsTab = new SendNewPaymentsTabHandler(ui); 
		ui.add(mainTabbedPane, sendNewPaymentsTab.getTab());
		
		importNewPaymentsTab = new ImportNewPaymentsTabHandler(ui); 
		ui.add(mainTabbedPane, importNewPaymentsTab.getTab());
		
		selectFromClientsTab = new SelectFromClientsTabHandler(ui);
		selectFromClientsTab.refresh();
		ui.add(mainTabbedPane, selectFromClientsTab.getTab()); 
		
		return outgoingPaymentsTab;
	}
	
}