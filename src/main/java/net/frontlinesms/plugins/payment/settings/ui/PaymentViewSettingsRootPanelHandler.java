package net.frontlinesms.plugins.payment.settings.ui;

import java.util.List;

import thinlet.Thinlet;

import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewSettingsRootPanelHandler implements
		UiSettingsSectionHandler, ThinletUiEventHandler {
	private final Object panel;

	public PaymentViewSettingsRootPanelHandler(UiGeneratorController ui) {
		this.panel = Thinlet.create(Thinlet.PANEL);
		ui.add(panel, ui.createLabel("Expand the tree to edit preferences for PaymentView."));
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
