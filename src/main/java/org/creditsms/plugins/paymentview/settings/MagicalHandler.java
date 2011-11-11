package org.creditsms.plugins.paymentview.settings;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.data.repository.ConfigurableServiceSettingsDao;
import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.PaymentService;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.settings.BaseServiceSettingsHandler;

public class MagicalHandler extends BaseServiceSettingsHandler<PaymentService> {
	public MagicalHandler(UiGeneratorController controller, PaymentServiceSettingsDao dao) {
		super(controller, new PaymentServiceImplementationLoader().getAll(), dao);
	}

	@Override
	public void refreshAccounts(Object accountList) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public FrontlineEventNotification createDeletedNotification(
			PaymentService service) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FrontlineEventNotification createSavedNotification(
			PaymentService service) {
		// TODO Auto-generated method stub
		return null;
	}
}
