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
	private final PaymentViewPluginController pluginController;
	private final PaymentServiceSettingsDao settingsDao;

	private Object tab;
	private Object panel;
	
	public WalletTabHander(UiGeneratorController ui, PaymentViewPluginController pluginController) {
		this.ui = ui;
		this.pluginController = pluginController;
		this.settingsDao = pluginController.getPaymentServiceSettingsDao();
		
		tab = Thinlet.create("tab");
		ui.setText(this.tab, "MWallets");
		
		panel = ui.createPanel("");
		ui.setColumns(panel, 1);
		ui.add(tab, panel);
		
		Object tableHeader = ui.createTableHeader();

		ui.add(tableHeader, createTableColumn("Active"));
		ui.add(tableHeader, createTableColumn("Name"));

		Object table = Thinlet.create(Thinlet.TABLE);
		ui.setWeight(table, 1, 1);
		ui.setName(table, SERVICE_TABLE);
		ui.add(table, tableHeader);
		add(table);
		
		add(ui.createButton("Refresh", "refresh", tab, this));
	}
	
	private Object createTableColumn(String name) {
		Object col = Thinlet.create(Thinlet.COLUMN);
		ui.setText(col, name);
		return col;
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
		return ui.createTableRow(s,
				/*active*/	pluginController.isActive(s)? "YES": "NO", // TODO i18n
				/*name:*/	s.getClass() + ":" + s.getId());
	}

	private void add(Object component) {
		ui.add(panel, component);
	}
	
	private Object find(String componentName) {
		return ui.find(panel, componentName);
	}
	
	private Object getServiceList() {
		return find(SERVICE_TABLE);
	}
}
