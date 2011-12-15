package org.creditsms.plugins.paymentview.ui;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;
import org.creditsms.plugins.paymentview.data.repository.PaymentServiceSettingsDao;

import thinlet.Thinlet;

import net.frontlinesms.data.domain.PersistableSettings;
import net.frontlinesms.plugins.payment.ui.PaymentPluginTabHandler;
import net.frontlinesms.ui.UiGeneratorController;

public class WalletTabHander implements PaymentPluginTabHandler {
	private static final String SERVICE_TABLE = "tbServices";
	
	private final UiGeneratorController ui;
	private PaymentServiceSettingsDao settingsDao;
	
	private Object tab;
	
	public WalletTabHander(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		this.ui = ui;
		this.settingsDao = pluginController.getPaymentServiceSettingsDao();
		
		this.tab = Thinlet.create("tab");
		ui.setText(this.tab, "MWallets");
		
		Object table = Thinlet.create(Thinlet.TABLE);
		Object tableHeader = Thinlet.create("header");
		Object col1 = Thinlet.create(Thinlet.COLUMN);
		ui.setText(col1, "Wallet");
		ui.add(tableHeader, col1);
		ui.add(table, tableHeader);
		ui.setName(table, SERVICE_TABLE);
		add(table);
	}

	public void refresh() {
		Object list = getServiceList();
		ui.removeAll(list);
		for(PersistableSettings s : settingsDao.getServiceAccounts()) {
			ui.add(list, createRow(s));
		}
	}

	public Object getTab() {
		return tab;
	}

	private Object createRow(PersistableSettings s) {
		return ui.createTableRow(s, s.getClass() + ":" + s.getId());
	}

	private void add(Object component) {
		ui.add(tab, component);
	}
	
	private Object find(String componentName) {
		return ui.find(tab, componentName);
	}
	
	private Object getServiceList() {
		return find(SERVICE_TABLE);
	}
}
