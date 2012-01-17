package net.frontlinesms.plugins.payment.settings.ui;

import java.util.List;

import net.frontlinesms.plugins.PluginController;
import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewSettingsRootPanelHandler implements
		UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String LAYOUT_XML = "/ui/plugins/payment/settings/pnAuthCode.xml";
	private final UiGeneratorController ui;
	private final Object panel;

	public PaymentViewSettingsRootPanelHandler(UiGeneratorController ui) {
		this.ui = ui;
		this.panel = ui.loadComponentFromFile(LAYOUT_XML, this);
	}
	
	private void add(Object component) {
		ui.add(panel, component);
	}

	public Object getPanel() {
		return panel;
	}

	public void deinit() {}

	public void save() {}

	public List<FrontlineValidationMessage> validateFields() {
		return null;
	}

	public void setAuthCode() {
//		ui.alert("Yeah!!");
		new ChangeAuthorizationCodeDialog(ui).showDialog();
	}
	
	public String getTitle() {
		return "Title of the thing";
	}

	public Object getSectionNode() {
		return null;
	}
}
