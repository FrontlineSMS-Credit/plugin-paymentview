package org.creditsms.plugins.paymentview.settings;

import java.util.List;

import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewSettingsRootSectionHandler
		extends BaseSectionHandler
		implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_ROOT = "/ui/plugins/payment/settings/pnRootSettings.xml";
	
	private String title;
	private String icon;

	public PaymentViewSettingsRootSectionHandler(UiGeneratorController ui, String title, String icon) {
		super(ui);
		this.title = title;
		this.icon = icon;
	}

	public void save() {
		// TODO Auto-generated method stub
	}

	public List<FrontlineValidationMessage> validateFields() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getTitle() {
		return this.title;
	}
	
	@Override
	public Object getSectionNode() {
		return createSectionNode(title, this, icon);
	}

	@Override
	protected void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_ROOT, this);
	}
	
//> UI EVENT METHODS
	public void selectionChanged(Object lsServices, Object pnButtons) {}
	
	public void configureService(Object lsServices) {}
	
	public void showNewServiceWizard() {
		PaymentServiceSettingsHandler serviceSettingsHandler = new PaymentServiceSettingsHandler(this.uiController);
		serviceSettingsHandler.showNewServiceWizard();
	}

	public void removeServices() {}
	
	public void showConfirmationDialog(String methodCall) {
		uiController.showConfirmationDialog(methodCall, this);
	}
}
