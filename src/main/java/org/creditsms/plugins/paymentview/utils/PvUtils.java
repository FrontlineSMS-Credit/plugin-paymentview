package org.creditsms.plugins.paymentview.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import net.frontlinesms.FrontlineUtils;

import org.apache.log4j.Logger;

public class PvUtils {
	private static final String DATETIME_PATTERN = "d/M/yy hh:mm a";
	private static final SimpleDateFormat SDF = new SimpleDateFormat(DATETIME_PATTERN);
	
	public static Logger getLogger(Class<? extends Object> clazz) {
		return FrontlineUtils.getLogger(clazz);
	}
	
	public static String formatDate(Date datetime){
		return SDF.format(datetime);
	}
	
	public static Date parseDate(String strDatetime) throws ParseException{
		return SDF.parse(strDatetime);
	}

	public static String formatDate(Long timePaid) {
		return formatDate(new Date(timePaid));
	}
}
