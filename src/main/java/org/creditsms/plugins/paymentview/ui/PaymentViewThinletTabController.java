/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright(C) - 2009, 2010
 */
package org.creditsms.plugins.paymentview.ui;

import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.PaymentViewPluginController;

/**
 * 
 * @author Emmanuel Kala
 *
 */
public class PaymentViewThinletTabController implements ThinletUiEventHandler {

//>	STATIC CONSTANTS
	
//> INSTANCE PROPERTIES
	/** Controller for the PaymentView plug-in */
	private final PaymentViewPluginController paymentViewController;
	/** Thinlet UI Controller */
	private final UiGeneratorController uiController;
	/** Thinlet tab component whose functionality is handled by this class */
	private Object paymentViewTab;

//> CONSTRUCTORS
	/**
	 * 
	 * @param paymentViewController value for {@link #paymentViewController}
	 * @param uiController value for {@linkplain #uiController}
	 */
	public PaymentViewThinletTabController(PaymentViewPluginController paymentViewController, UiGeneratorController uiController){
		this.paymentViewController = paymentViewController;
		this.uiController = uiController;
	}
	
	/**
	 * Mutator method for setting the value of {@link #paymentViewTab}
	 * This method should be called by in the {@link #paymentViewController} code that
	 * initializes the PaymentView UI
	 * 
	 * @param paymentViewTab value for {@link #paymentViewTab}
	 */
	public void setTabComponent(Object paymentViewTab){
		this.paymentViewTab = paymentViewTab;
	}
}
