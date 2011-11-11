package org.creditsms.plugins.paymentview.settings;

import java.util.List;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewSettingsRootSectionHandler
		extends BaseSectionHandler
		implements UiSettingsSectionHandler, ThinletUiEventHandler {
	private static final String UI_SECTION_ROOT = "/ui/plugins/payment/settings/pnRootSettings.xml";
	
	private final String title;
	private final String icon;
	private final PaymentServiceSettingsDao serviceDao;

	public PaymentViewSettingsRootSectionHandler(UiGeneratorController ui,
			PaymentServiceSettingsDao serviceDao, String title, String icon) {
		super(ui);
		this.serviceDao = serviceDao;
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
		Object list = find("lsPaymentServices");
		for(PersistableSettings s : serviceDao.getServiceAccounts()) {
			String description = s.getServiceClassName() + ": " + s.getId();
			Object listItem = uiController.createListItem(description, s);
			uiController.add(list, listItem);
		}
	}
	
//> UI EVENT METHODS
	public void selectionChanged(Object lsServices, Object pnButtons) {}
	
	public void configureService(Object lsServices) {
		new MagicalHandler(uiController, serviceDao).configureService(lsServices);
	}
	
	public void showNewServiceWizard() {
//		PaymentServiceSettingsHandler serviceSettingsHandler = new PaymentServiceSettingsHandler(this.uiController);
//		serviceSettingsHandler.showNewServiceWizard();
		new MagicalHandler(uiController, serviceDao).showNewServiceWizard();
	}

	public void removeServices() {}
	
	public void showConfirmationDialog(String methodCall) {
		uiController.showConfirmationDialog(methodCall, this);
	}
}
