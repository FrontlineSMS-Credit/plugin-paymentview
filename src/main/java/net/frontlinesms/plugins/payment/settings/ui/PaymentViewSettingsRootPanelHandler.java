package net.frontlinesms.plugins.payment.settings.ui;

import java.util.List;

import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewSettingsRootPanelHandler implements
		UiSettingsSectionHandler {
	private final UiGeneratorController ui;
	private final Object panel;
	
	public PaymentViewSettingsRootPanelHandler(UiGeneratorController ui) {
		this.ui = ui;
		panel = ui.createPanel("pnPvSettings");
		add(ui.createLabel("Here is a labl"));
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

	public String getTitle() {
		return "Title of the thing";
	}

	public Object getSectionNode() {
		return null;
	}
}
