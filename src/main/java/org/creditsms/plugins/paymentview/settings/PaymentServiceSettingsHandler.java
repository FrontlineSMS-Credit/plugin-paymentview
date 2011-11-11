package org.creditsms.plugins.paymentview.settings;

import java.util.Stack;

import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class PaymentServiceSettingsHandler implements ThinletUiEventHandler {
	private static final String UI_FILE_CHOOSE_CLASS = "/ui/plugins/payment/settings/dgChooseClass.xml";
	
	private final UiGeneratorController ui;
	/** The screens we've gone through in the wizard */
	private final Stack<Object> screens = new Stack<Object>();
	
	public PaymentServiceSettingsHandler(UiGeneratorController ui) {
		this.ui = ui;
	}

	public void showNewServiceWizard() {
		screens.push(ui.loadComponentFromFile(UI_FILE_CHOOSE_CLASS, this));
		Object classList = find("lsClasses");

		for (Class<? extends PaymentService> provider : new PaymentServiceImplementationLoader().getAll()) {
			Object item = ui.createListItem(provider.getSimpleName(), provider.getCanonicalName());
			String icon = null; // TODO add icons for different payment service providers
			if (icon != null) {
				ui.setIcon(item, icon);
			}
			ui.add(classList, item);
		}

		ui.add(getCurrent());
	}
	
//> UI EVENT METHODS
	public void configureNewService(Object classList) {}
	
	public void selectionChanged(Object classList, Object pnButtons) {}
	
	public void removeDialog() {
		ui.removeDialog(getCurrent());
	}
	
//> UI HELPER METHODS
	private Object find(String componentName) {
		return ui.find(getCurrent(), componentName);
	}
	
	private Object getCurrent() {
		return screens.peek();
	}
}
