package org.creditsms.plugins.paymentview.ui.handler.tabclients.dialogs;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.base.BaseClientTableHandler;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;

public class ClientSelector extends ClientsTabHandler {
	private static final String XML_CLIENT_SELECTOR_DIALOG_XML = "/ui/plugins/paymentview/clients/dialogs/dgClientSelector.xml";

	public ClientSelector(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui, pluginController);
	}
	
	@Override
	protected BaseClientTableHandler getClientTableHandler(
			UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		return new ClientSelectorTableHandler(ui, pluginController);
	}

	@Override
	protected String getXMLFile() {
		return XML_CLIENT_SELECTOR_DIALOG_XML;
	}
	
	public void removeDialog() {
		ui.remove(getTab());
	}
	
	public Object getDialog() {
		refresh();
		return getTab();
	}
	
	public void show() {
		ui.add(getDialog());
	}
}
