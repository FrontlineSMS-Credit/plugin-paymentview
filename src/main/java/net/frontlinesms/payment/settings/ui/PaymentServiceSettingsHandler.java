package net.frontlinesms.payment.settings.ui;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.payment.service.PaymentService;
import net.frontlinesms.payment.service.PaymentServiceImplementationLoader;
import net.frontlinesms.serviceconfig.ui.BaseServiceSettingsHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class PaymentServiceSettingsHandler extends BaseServiceSettingsHandler<PaymentService> {
	public PaymentServiceSettingsHandler(UiGeneratorController controller, PaymentServiceSettingsDao dao) {
		super(controller, new PaymentServiceImplementationLoader().getAll(), dao);
	}

	@Override
	public void refreshAccounts(Object accountList) {
		PaymentViewUiUtils.refreshServiceList(controller, (PaymentServiceSettingsDao) settingsDao, accountList);
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
