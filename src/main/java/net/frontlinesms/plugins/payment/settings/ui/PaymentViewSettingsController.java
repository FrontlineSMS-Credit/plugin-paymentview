package net.frontlinesms.plugins.payment.settings.ui;

import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

public class PaymentViewSettingsController implements PluginSettingsController {
	private final UiGeneratorController ui;
	private final PaymentServiceSettingsHandler serviceSettingsHandler;
	private final AuthCodeChangePanelHandler authCodeChangeHandler;
	private final String title;
	private final String iconPath;
	
	public PaymentViewSettingsController(UiGeneratorController ui,
			PaymentServiceSettingsDao dao, String title, String icon) {
		serviceSettingsHandler = new PaymentServiceSettingsHandler(ui, dao, title + " - Payment Services", icon);
		authCodeChangeHandler = new AuthCodeChangePanelHandler(ui);
		
		this.ui = ui;
		this.iconPath = icon;
		this.title = title;
	}

	public void addSubSettingsNodes(Object rootSettingsNode) {
		ui.add(rootSettingsNode, ui.createNode("Change Authorisation Code", authCodeChangeHandler));
		ui.add(rootSettingsNode, ui.createNode("Payment Services", serviceSettingsHandler));
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
		ui.setIcon(node, iconPath);
		addSubSettingsNodes(node);
		return node;
	}
}
