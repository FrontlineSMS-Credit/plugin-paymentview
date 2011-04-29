package org.creditsms.plugins.paymentview.utils;

import org.apache.commons.lang.StringUtils;

public final class StringUtil {
	StringUtil() {

	}

	private static final Object SPACE = " ";

	public static String toCamelCase(String str){
		if (str != null){
			String[] strings = str.split(" ");
			str = "";
			for(int i=0; i < strings.length; i++){
				strings[i] = strings[i].toLowerCase();
				if(i != 0){
					strings[i] = StringUtils.capitalize(strings[i]);
				}
				str += strings[i];
			}
			return str;			
		}
		return null;
	}
	
	public static String getReadableFieldName(String fieldName) {
		String str = StringUtils.capitalize(fieldName);
		String[] strArr = StringUtils.splitByCharacterTypeCamelCase(str);
		StringBuilder str2 = new StringBuilder(2);
		for (String s : strArr) {
			str2.append(s);
			str2.append(SPACE);
		}
		return str2.toString().trim();
	}

	public static String getFormFieldName(String fieldName) {
		return fieldName;
	}

	public static String getFieldFromFormField(String formFieldName) {
		return StringUtils.EMPTY;
	}

	public static String getFormLabel(String fieldName) {
		return getReadableFieldName(fieldName) + ":";
	}
}
