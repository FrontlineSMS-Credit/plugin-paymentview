/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.plugins.BasePluginThinletTabController;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;
import net.frontlinesms.ui.handler.PagedComponentItemProvider;
import net.frontlinesms.ui.handler.PagedListDetails;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

/**
 * 
 * @author Emmanuel Kala
 * @author Ian Mukewa
 */
public class PaymentViewThinletTabController extends
		BasePluginThinletTabController<PaymentViewPluginController> implements
		ThinletUiEventHandler, PagedComponentItemProvider {

	private PaymentViewPluginController controller;

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