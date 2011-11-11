package org.creditsms.plugins.paymentview.settings;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewSettingsController implements PluginSettingsController {
	private final UiGeneratorController ui;
	private final PaymentServiceSettingsDao dao;
	private final String title;
	private final String icon;
	
	public PaymentViewSettingsController(PaymentServiceSettingsDao dao,
			String title,
			UiGeneratorController ui, String icon) {
		this.ui = ui;
		this.icon = icon;
		this.title = title;
		this.dao = dao;
	}

	public void addSubSettingsNodes(Object rootSettingsNode) {
		// TODO Add settings screen for each configured payment service
	}

	public UiSettingsSectionHandler getHandlerForSection(String section) {
		return null;
	}

	public UiSettingsSectionHandler getRootPanelHandler() {
		return new PaymentViewSettingsRootSectionHandler(this.ui, dao,
				this.title, this.icon);
	}

	public String getTitle() {
		return title;
	}

	public Object getRootNode() {
		return getRootPanelHandler().getSectionNode();
	}

}
