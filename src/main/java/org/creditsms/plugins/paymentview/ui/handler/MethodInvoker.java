package org.creditsms.plugins.paymentview.ui.handler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import net.frontlinesms.ui.ThinletUiEventHandler;

public class MethodInvoker {
	private Method actionMethod;
	private ThinletUiEventHandler actionHandler;

	public MethodInvoker(ThinletUiEventHandler eventListener, String methodToBeCalled, Class<?>... args) throws SecurityException, NoSuchMethodException {
		actionMethod = eventListener.getClass().getMethod(methodToBeCalled, args);
		actionHandler = eventListener;
	}
	
	public void invoke(Object... args) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		actionMethod.invoke(actionHandler, args);
	}

	public void invoke() throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {
		invoke(actionHandler);
	}
	
}
