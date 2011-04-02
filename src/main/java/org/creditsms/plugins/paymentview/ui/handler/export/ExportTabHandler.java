package org.creditsms.plugins.paymentview.ui.handler.export;

import org.creditsms.plugins.paymentview.ui.handler.outgoingpayments.SentPaymentsTabHandler;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

public class ExportTabHandler extends BaseTabHandler{
	private static final String XML_EXPORT_TAB = "/ui/plugins/paymentview/export/tabexport.xml";
	private static final String TABBED_PANE_MAIN = "tabP_MainPane";
	
	private Object exportTab;
	private Object mainTabbedPane;
	private ExportClientsTabHandler clientsTab;
	private ExportClientHistoryTabHandler clientHistoryTab;
	private ExportPaymentsTabHandler paymentsTab; 
		
	public ExportTabHandler(UiGeneratorController ui) {
		super(ui);		
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
