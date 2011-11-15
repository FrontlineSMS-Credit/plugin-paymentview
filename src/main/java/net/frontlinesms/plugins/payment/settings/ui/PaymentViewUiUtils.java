package net.frontlinesms.plugins.payment.settings.ui;

import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.ui.UiGeneratorController;

public class PaymentViewUiUtils {
	public static void refreshServiceList(UiGeneratorController ui, PaymentServiceSettingsDao dao, Object list) {
		for(PersistableSettings s : dao.getServiceAccounts()) {
			String description = s.getServiceClass() + ": " + s.getId();
			Object listItem = ui.createListItem(description, s);
			ui.add(list, listItem);
		}
	}
}
