package net.frontlinesms.payment.settings.ui;

import java.util.List;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.plugins.PluginSettingsController;
import net.frontlinesms.settings.BaseSectionHandler;
import net.frontlinesms.settings.FrontlineValidationMessage;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.settings.UiSettingsSectionHandler;

public class PaymentViewSettingsController
		extends BaseSectionHandler
		implements UiSettingsSectionHandler, ThinletUiEventHandler, PluginSettingsController {
	private static final String UI_SECTION_ROOT = "/ui/plugins/payment/settings/pnRootSettings.xml";
	
	private final PaymentServiceSettingsDao dao;
	private final String title;
	private final String icon;
	
	public PaymentViewSettingsController(PaymentServiceSettingsDao dao,
			String title,
			UiGeneratorController ui, String icon) {
		super(ui);
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
		return this;
	}

	public String getTitle() {
		return title;
	}
	
	public List<FrontlineValidationMessage> validateFields() {
		// TODO Auto-generated method stub
		return null;
	}
	
	public void save() {
		// TODO Auto-generated method stub
	}

	public Object getRootNode() {
		return getSectionNode();
	}
	
	@Override
	public Object getSectionNode() {
		return createSectionNode(title, this, icon);
	}

	@Override
	protected void init() {
		this.panel = uiController.loadComponentFromFile(UI_SECTION_ROOT, this);
		Object list = find("lsPaymentServices");
		for(PersistableSettings s : dao.getServiceAccounts()) {
			String description = s.getServiceClassName() + ": " + s.getId();
			Object listItem = uiController.createListItem(description, s);
			uiController.add(list, listItem);
		}
	}
	
//> UI EVENT METHODS
	public void selectionChanged(Object lsServices, Object pnButtons) {}
	
	public void configureService(Object lsServices) {
		new PaymentServiceSettingsHandler(uiController, dao).configureService(lsServices);
	}
	
	public void showNewServiceWizard() {
		new PaymentServiceSettingsHandler(uiController, dao).showNewServiceWizard();
	}

	public void removeServices() {}
	
	public void showConfirmationDialog(String methodCall) {
		uiController.showConfirmationDialog(methodCall, this);
	}
}
