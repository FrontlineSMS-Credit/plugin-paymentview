package org.creditsms.plugins.paymentview.utils;

import net.frontlinesms.FrontlineUtils;

import org.apache.log4j.Logger;

public class PvUtils {
	public PvUtils() {
		// TODO Auto-generated constructor stub
	}

	public static Logger getLogger(Class<? extends Object> clazz) {
		return FrontlineUtils.getLogger(clazz);
	}
}
