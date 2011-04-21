/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.FrontlineUtils;
import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.apache.log4j.Logger;
import org.creditsms.plugins.paymentview.PaymentViewPluginController;

/**
 * 
 * @author Emmanuel Kala
 * @author Ian Mukewa
 */
public class PaymentViewThinletTabController extends
		BasePluginThinletTabController<PaymentViewPluginController> implements
		ThinletUiEventHandler, PagedComponentItemProvider {
	// > CONSTANTS
	private static final Logger LOG = FrontlineUtils
			.getLogger(PaymentViewThinletTabController.class);
	// > UI FILES
	private static final String UI_FILE_SEND_MONEY_DIALOG = "/ui/plugins/paymentview/dgSendMoneyForm.xml";

	private PaymentViewPluginController controller;

	// > UI COMPONENTS
	/** Thinlet tab component whose functionality is handled by this class */
	private Object paymentViewTab;

	/**
	 * 
	 * @param paymentViewController
	 *            value for {@link #controller}
	 * @param uiController
	 *            value for {@linkplain #ui}
	 */
	public PaymentViewThinletTabController(
			PaymentViewPluginController controller, UiGeneratorController ui) {
		super(controller, ui);

		this.controller = controller;
	}

	public PagedListDetails getListDetails(Object list, int startIndex,
			int limit) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Refreshes the tab display
	 */
	public void refresh() {
	}
}