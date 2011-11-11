package net.frontlinesms.payment.settings.ui;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.events.FrontlineEventNotification;
import net.frontlinesms.payment.service.PaymentService;
import net.frontlinesms.payment.service.PaymentServiceImplementationLoader;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.settings.BaseServiceSettingsHandler;

public class PaymentServiceSettingsHandler extends BaseServiceSettingsHandler<PaymentService> {
	public PaymentServiceSettingsHandler(UiGeneratorController controller, PaymentServiceSettingsDao dao) {
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

	@Override
	public String getIconMapLocation() {
		// TODO Auto-generated method stub
		return null;
	}
}
