package net.frontlinesms.plugins.payment.settings.ui;

import java.util.List;

import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class AuthCodeChangePanelHandler implements UiSettingsSectionHandler, ThinletUiEventHandler {
//> STATIC CONSTANTS
	private static final String LAYOUT_XML = "/ui/plugins/payment/settings/pnAuthCode.xml";
	
//> INSTANCE CONSTANTS
	private final UiGeneratorController ui;
	private final Object panel;
	
	AuthCodeChangePanelHandler(UiGeneratorController ui) {
		this.ui = ui;
		this.panel = ui.loadComponentFromFile(LAYOUT_XML, this);
	}

	public Object getPanel() {
		return panel;
	}

	public void setAuthCode() {
		new AuthCodeChangeDialogHandler(ui).showDialog();
	}

	public void deinit() {}
	public void save() {}
	public List<FrontlineValidationMessage> validateFields() { return null; }
	public String getTitle() {
		return "SOOOHOOOOOOSAOFOIJDF";
	}
	public Object getSectionNode() { return null;  }
}
