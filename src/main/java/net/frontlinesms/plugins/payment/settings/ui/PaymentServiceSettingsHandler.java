package net.frontlinesms.plugins.payment.settings.ui;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.plugins.payment.service.PaymentServiceImplementationLoader;
import net.frontlinesms.serviceconfig.ui.BaseServiceSettingsHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class PaymentServiceSettingsHandler extends BaseServiceSettingsHandler<PaymentService> {
	public PaymentServiceSettingsHandler(UiGeneratorController controller, PaymentServiceSettingsDao dao,
			String title, String icon) {
		super(controller, dao, new PaymentServiceImplementationLoader().getAll(), title, icon);
	}
	
	@Override
	public Class<PaymentService> getServiceSupertype() {
		return PaymentService.class;
	}

	@Override
	public String getIconMapLocation() {
		return null;
	}
}
