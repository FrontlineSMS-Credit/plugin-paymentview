package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import java.lang.reflect.InvocationTargetException;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.apache.log4j.chainsaw.Main;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.creditsms.plugins.paymentview.ui.handler.MethodInvoker;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTableHandler;

public class ClientSelector extends BaseClientSelector {
	private static final String XML_CLIENT_SELECTOR_DIALOG_XML = "/ui/plugins/paymentview/clients/dialogs/dgClientSelector.xml";
	private MethodInvoker methodInvoker;
	
	public ClientSelector(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui, pluginController);
	}
	
	public void showClientSelectorDialog(ThinletUiEventHandler eventListener, String methodToBeCalled, Class<?>... args){
		try {
			methodInvoker = new MethodInvoker(eventListener, methodToBeCalled, args);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		show();
	}
	
	public void sendSelectedListOfClients() {
		try {
			methodInvoker.invoke(getClientTableHandler().getSelectedClients());
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		this.removeDialog();
	}
	
	@Override
	protected BaseClientTableHandler createClientTableHandler(
			UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		return new ClientSelectorTableHandler(ui, pluginController);
	}
	
	@Override
	protected String getXMLFile() {
		return XML_CLIENT_SELECTOR_DIALOG_XML;
	}
	
	public void setSelectionMethod(String selection) {
		clientTableHandler.setSelectionMethod(selection);
	}
}