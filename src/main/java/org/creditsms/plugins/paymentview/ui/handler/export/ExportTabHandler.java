package org.creditsms.plugins.paymentview.ui.handler.export;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;

public class ExportTabHandler extends BaseTabHandler{
	private static final String XML_EXPORT_TAB = "/ui/plugins/paymentview/export/tabexport.xml";
	private static final String TABBED_PANE_MAIN = "tabP_MainPane";
	
	private Object exportTab;
	private Object mainTabbedPane;
	private ExportClientsTabHandler clientsTab;
	private ExportClientHistoryTabHandler clientHistoryTab;
	private ExportPaymentsTabHandler paymentsTab;
	private ClientDao clientDao;
	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao; 	
		
	public ExportTabHandler(UiGeneratorController ui, ClientDao clientDao, IncomingPaymentDao incomingPaymentDao, OutgoingPaymentDao outgoingPaymentDao) {
		super(ui);
		this.clientDao = clientDao;
		this.incomingPaymentDao = incomingPaymentDao;
		this.outgoingPaymentDao = outgoingPaymentDao;
		
		init(); 		
	} 

	@Override
	public void refresh() {		
	}

	@Override
	protected Object initialiseTab() {
		exportTab = ui.loadComponentFromFile(XML_EXPORT_TAB, this);		
		mainTabbedPane = ui.find(exportTab, TABBED_PANE_MAIN); 
		
		clientsTab = new ExportClientsTabHandler(ui);   
		ui.add(mainTabbedPane, clientsTab.getTab()); 
		clientHistoryTab = new ExportClientHistoryTabHandler(ui);   
		ui.add(mainTabbedPane, clientHistoryTab.getTab()); 
		paymentsTab = new ExportPaymentsTabHandler(ui);   
		ui.add(mainTabbedPane, paymentsTab.getTab()); 
		
		return exportTab;
	}

	//> EVENTS...
	public void customizeClientDB(){		
	}
	
	public void addClient(){			
	}
	
	public void importClient(){		
	}	

}
