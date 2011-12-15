package net.frontlinesms.plugins.payment.ui;

import net.frontlinesms.ui.ThinletUiEventHandler;

public interface PaymentPluginTabHandler extends ThinletUiEventHandler {
	/** Non-thread-safe refresh of tab.  This will be used at tab initialisation. */
	void refresh();
	/** @return tab component this handler controls */
	Object getTab();
}
