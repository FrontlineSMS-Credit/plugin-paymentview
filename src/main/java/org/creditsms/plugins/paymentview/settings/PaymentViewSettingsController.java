package org.creditsms.plugins.paymentview.settings;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.i18n.InternationalisationUtils;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewSettingsController implements PluginSettingsController {
	private UiGeneratorController ui;
	private PaymentViewPluginController pluginController;
	private String icon;
	
	public PaymentViewSettingsController(PaymentViewPluginController pluginController,
			UiGeneratorController ui, String icon) {
		this.pluginController = pluginController;
		this.ui = ui;
		this.icon = icon;
	}

	public void addSubSettingsNodes(Object rootSettingsNode) {
		// TODO Add settings screen for each configured payment service
	}

	public UiSettingsSectionHandler getHandlerForSection(String section) {
		return null;
	}

	public UiSettingsSectionHandler getRootPanelHandler() {
		return new PaymentViewSettingsRootSectionHandler(this.ui, this.getTitle(), this.icon);
	}

	public String getTitle() {
		return this.pluginController.getName(InternationalisationUtils.getCurrentLocale());
	}

	public Object getRootNode() {
		PaymentViewSettingsRootSectionHandler rootHandler = new PaymentViewSettingsRootSectionHandler(this.ui, this.getTitle(), this.icon);
		return rootHandler.getSectionNode();
	}

}
