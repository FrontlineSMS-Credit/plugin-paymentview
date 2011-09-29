package org.creditsms.plugins.paymentview.ui.handler.base;

import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.ui.handler.tabclients.ClientsTabHandler;

public class BaseClientSelector extends ClientsTabHandler {

	public BaseClientSelector(UiGeneratorController ui,
			PaymentViewPluginController pluginController) {
		super(ui, pluginController);
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