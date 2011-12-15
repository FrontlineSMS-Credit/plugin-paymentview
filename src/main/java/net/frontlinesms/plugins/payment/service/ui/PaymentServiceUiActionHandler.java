package net.frontlinesms.plugins.payment.service.ui;

import net.frontlinesms.plugins.payment.service.PaymentService;
import net.frontlinesms.ui.ThinletUiEventHandler;

public interface PaymentServiceUiActionHandler extends ThinletUiEventHandler {
	/** @return <code>true</code> if {@link #getMenuItems()} will return any items, or <code>false</code> otherwise. */
	boolean hasMenuItems();
	/** @return list of menu items to display for the attached {@link PaymentService}, or <code>null</code> if there are none. */
	Object[] getMenuItems();
}
