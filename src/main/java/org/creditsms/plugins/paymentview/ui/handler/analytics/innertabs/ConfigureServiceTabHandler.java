package org.creditsms.plugins.paymentview.ui.handler.analytics.innertabs;

import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.BaseTabHandler;

import org.creditsms.plugins.paymentview.ui.handler.analytics.dialogs.CreateNewTargetHandler;

public class ConfigureServiceTabHandler extends BaseTabHandler {
	private static final String TAB_CONFIGURE_SERVICE = "tab_configureService";
	private static final String XML_CONFIGURE_SERVICE = "/ui/plugins/paymentview/analytics/configureservice/configurelayaway.xml";

	private Object configureServiceTab;

	public ConfigureServiceTabHandler(UiGeneratorController ui,
			Object tabAnalytics) {
		super(ui);
		configureServiceTab = ui.find(tabAnalytics, TAB_CONFIGURE_SERVICE);
		this.init();
	}

	public void createNew() {
		ui.add(new CreateNewTargetHandler((UiGeneratorController) ui)
				.getDialog());
	}

	public void deinit() {
	}

	@Override
	protected Object initialiseTab() {
		ui.add(configureServiceTab,
				ui.loadComponentFromFile(XML_CONFIGURE_SERVICE, this));
		return configureServiceTab;
	}

	public void refresh() {

	}

}
