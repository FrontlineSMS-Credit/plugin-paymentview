package net.frontlinesms.plugins.payment.settings.ui;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewTing implements PluginSettingsController {
	private final UiGeneratorController ui;
	private final PaymentServiceSettingsHandler serviceSettingsHandler;
	private final String title;
	
	public PaymentViewTing(UiGeneratorController ui,
			PaymentServiceSettingsDao dao, String title, String icon) {
		serviceSettingsHandler = new PaymentServiceSettingsHandler(ui, dao, title, icon);
		
		this.ui = ui;
		this.title = title;
	}

	public void addSubSettingsNodes(Object rootSettingsNode) {
		ui.add(rootSettingsNode, ui.createNode("Subsertting node", serviceSettingsHandler));
	}

	public UiSettingsSectionHandler getHandlerForSection(String section) {
		return null;
	}

	public UiSettingsSectionHandler getRootPanelHandler() {
		return new PaymentViewSettingsRootPanelHandler(ui);
	}

	public String getTitle() {
		return title;
	}

	public Object getRootNode() {
		Object node = ui.createNode(getTitle(), getRootPanelHandler());
		addSubSettingsNodes(node);
		return node;
	}
}
