/*
 * FrontlineSMS:Credit - http://www.creditsms.org
 * Copyright (C) - 2009, 2010
 * 
 * This file is part of FrontlineSMS:Credit
 * 
 */
package org.creditsms.plugins.paymentview;

import java.lang.reflect.Method;

import net.frontlinesms.BuildProperties;
import net.frontlinesms.FrontlineSMS;
import net.frontlinesms.plugins.BasePluginController;
import net.frontlinesms.plugins.PluginControllerProperties;
import net.frontlinesms.plugins.PluginInitialisationException;
import net.frontlinesms.ui.ThinletUiEventHandler;
import net.frontlinesms.ui.UiGeneratorController;

import org.creditsms.plugins.paymentview.data.repository.AccountDao;
import org.creditsms.plugins.paymentview.data.repository.ClientDao;
import org.creditsms.plugins.paymentview.data.repository.CustomFieldDao;
import org.creditsms.plugins.paymentview.data.repository.CustomValueDao;
import org.creditsms.plugins.paymentview.data.repository.IncomingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.OutgoingPaymentDao;
import org.creditsms.plugins.paymentview.data.repository.ServiceItemDao;
import org.creditsms.plugins.paymentview.data.repository.TargetDao;
import org.creditsms.plugins.paymentview.ui.PaymentViewThinletTabController;
import org.springframework.context.ApplicationContext;

/**
 * This is the base class for the FrontlineSMS:Credit PaymentView plugin. The
 * PaymentView plugin is used to process payments transacted via the connected
 * mobile phone. Processing of the payments includes mining the incoming message
 * for specific information and pushing the same to an online/external database
 * or system such as Mifos - http://www.mifos.org
 * 
 * @author Emmanuel Kala
 * @author Ian Onesmus Mukewa <ian@credit.frontlinesms.com>
 */
@PluginControllerProperties(name = "Payment View", iconPath = "/icons/creditsms.png", i18nKey = "plugins.paymentview", springConfigLocation = "classpath:org/creditsms/plugins/paymentview/paymentview-spring-hibernate.xml", hibernateConfigPath = "classpath:org/creditsms/plugins/paymentview/paymentview.hibernate.cfg.xml")
public class PaymentViewPluginController extends BasePluginController
		implements ThinletUiEventHandler {

//> CONSTANTS
	/** Filename and path of the XML for the PaymentView tab */
	private static final String XML_ENTER_AUTHORIZATION_CODE = "/ui/plugins/paymentview/settings/dialogs/createnewpaymentsteps/dlgCreateNewAccountStep3.xml";

	private AccountDao accountDao;
	private ClientDao clientDao;
	private CustomValueDao customValueDao;
	private CustomFieldDao customFieldDao;
	private IncomingPaymentDao incomingPaymentDao;
	private OutgoingPaymentDao outgoingPaymentDao;
	private TargetDao targetDao;
	private ServiceItemDao serviceItemDao;
	
	private PaymentViewThinletTabController tabController;
	private UiGeneratorController ui;
	private Method authorizationAction;
	private ThinletUiEventHandler authorizationEventListener;

	/**
	 * @see net.frontlinesms.plugins.PluginController#deinit()
	 */
	public void deinit() {
		//tabController.deinit();
	}

//> CONFIG METHODS
	/**
	 * @see net.frontlinesms.plugins.PluginController#init(FrontlineSMS,
	 *      ApplicationContext)
	 */
	public void init(FrontlineSMS frontlineController,
			ApplicationContext applicationContext)
			throws PluginInitialisationException {
		// Initialize the DAO for the domain objects
		clientDao 			= (ClientDao) applicationContext.getBean("clientDao");
		accountDao 			= (AccountDao) applicationContext.getBean("accountDao");
		customValueDao 		= (CustomValueDao) applicationContext.getBean("customValueDao");
		customFieldDao 		= (CustomFieldDao) applicationContext.getBean("customFieldDao");
		incomingPaymentDao 	= (IncomingPaymentDao) applicationContext.getBean("incomingPaymentDao");
		outgoingPaymentDao 	= (OutgoingPaymentDao) applicationContext.getBean("outgoingPaymentDao");
		serviceItemDao 		= (ServiceItemDao) applicationContext.getBean("serviceItemDao");
		targetDao 			= (TargetDao) applicationContext.getBean("targetDao");
		
		// If not a production build, and database is empty, add test data
		if(BuildProperties.getInstance().isSnapshot() && clientDao.getClientCount()==0) {
			DemoData.createDemoData(applicationContext);
		}
	}

	/** @see net.frontlinesms.plugins.BasePluginController#initThinletTab(UiGeneratorController) */
	@Override
	public Object initThinletTab(UiGeneratorController ui) {
		tabController = new PaymentViewThinletTabController(this, ui);
		this.ui = ui;
		return tabController.getPaymentViewTab();
	}
	
//> AUTHORIZATION UTILITY ACTIONS
	public void showAuthorizationCodeDialog(String methodToBeCalled, ThinletUiEventHandler eventListener){
		Object dialogComponent = ui.loadComponentFromFile(XML_ENTER_AUTHORIZATION_CODE, this);
		try {
			setAuthorizationAction(eventListener.getClass().getMethod(methodToBeCalled));
			setAuthorizationEventListener(eventListener);
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
		ui.add(dialogComponent);
	}
	
	private void setAuthorizationEventListener(
			ThinletUiEventHandler authorizationEventListener) {
		this.authorizationEventListener = authorizationEventListener;
	}

	private void setAuthorizationAction(Method authorizationAction) {
		this.authorizationAction = authorizationAction;
	}

 	public void authorize(String authCode, String verifyAuthCode) {
//		try {
//			if ((authCode.equals(verifyAuthCode)) & AuthorizationChecker.authenticate(authCode)){
//				if (authorizationAction != null) {
			try {
				authorizationAction.invoke(authorizationEventListener);
			}  catch (Exception e) {
				throw new RuntimeException(e);
			}
//				}else{
//					throw new RuntimeException("Null AuthorizationAction!");
//				}
//			}else{
//				ui.alert("Invalid Entry! Enter the Authorization Code Again.");
//			}
//		} catch (UnsupportedEncodingException e) {
//			throw new RuntimeException(e);
//		}
	}
	
//> ACCESSORS
	public AccountDao getAccountDao() {
		return accountDao;
	}
	
	public IncomingPaymentDao getIncomingPaymentDao() {
		return this.incomingPaymentDao;
	}
	
	public OutgoingPaymentDao getOutgoingPaymentDao() {
		return outgoingPaymentDao;
	}
	
	public ClientDao getClientDao() {
		return clientDao;
	}
	
	public CustomFieldDao getCustomFieldDao() {
		return customFieldDao;
	}
	
	public CustomValueDao getCustomValueDao() {
		return customValueDao;
	}

	public TargetDao getTargetDao() {
		return targetDao;
	}

	public ServiceItemDao getServiceItemDao() {
		return serviceItemDao;
	}
	
	public UiGeneratorController getUiGeneratorController() {
		return ui;
	}
}
